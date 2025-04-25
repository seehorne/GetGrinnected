//
//  EventList.swift
//  GetGrinnected
//
//  Created by Budhil Thijm on 4/24/25.
//

import SwiftData
import SwiftUI

struct EventList: View {
    //model context!
    @Environment(\.modelContext) private var modelContext
    //only initialized value that is a query
    @Query(sort: \EventModel.startTime) private var events: [EventModel]
    
    //parentview that observes values if change
    @ObservedObject var parentView: EventListParentViewModel
    
    //selected event
    @State var selectedEvent: Int? //An integer to represent which event we select
    @State private var refreshTimer: Timer?
    @State private var isLoading = false //set loading states
    
    
    //the initializer for the eventlist is the sorting function!
    
    init(
        selectedEvent: Int?,
        parentView: EventListParentViewModel,
        sortOrder: [SortDescriptor<EventModel>] = [],
        searchString: String,
        filterToday: Bool = false,
        showFavorites: Bool = false
    ){
        self.selectedEvent = selectedEvent
        self._parentView = ObservedObject(wrappedValue: parentView)
        
        let timeSpanStart = parentView.timeSpan.start
        let timeSpanEnd = parentView.timeSpan.end
        
        //apply sort order, default if no title or organization provided
        let finalSortOrder = sortOrder.isEmpty ? [SortDescriptor(
            \EventModel.name
        )] : sortOrder
        
        //debugging purposes
        print("EventList initialized with timespan: \(timeSpanStart) to \(timeSpanEnd), filterToday: \(filterToday)")
        
        //initialize events according to those sorting specificatinos
        _events = Query(
            filter: buildFilterPredicate(
                timeSpanStart: timeSpanStart,
                timeSpanEnd: timeSpanEnd,
                filterToday: filterToday,
                searchString: searchString,
                showFavorites: showFavorites
            ),
            sort: finalSortOrder,
            animation: .default
        )//sort by name default animation
    } //init
    
    
    private func buildFilterPredicate(
        timeSpanStart: Date,
        timeSpanEnd: Date,
        filterToday: Bool,
        searchString: String,
        showFavorites: Bool
    ) -> Predicate<EventModel> {
        return #Predicate<EventModel> { event in
            // Time filter condition
            return (
                filterToday
                ? event.startTime
                    .flatMap {
                        $0 >= timeSpanStart && $0 <= timeSpanEnd
                    } ?? false //check the dates, otherwise false (not in that date)
                : true) //if not filtering for today, return true
            &&
            (
                searchString.isEmpty || event.name.localizedStandardContains(searchString)
            )
            &&
            (showFavorites ? event.favorited : true)
        }
    }
    
    var body: some View {
        VStack{
            if events.isEmpty{
                Text("No Events in this time period")
                    .padding()
                
            }
            
            
            if isLoading {
                //progress View
                ProgressView("Loading Events...")
                    .padding()
            } else {
                ForEach(events, id: \.id) { event in
                    //checks if favorited page
                    EventCard(event: event, isExpanded: (event.id == selectedEvent))
                        .onTapGesture {
                            withAnimation(.easeInOut) {
                                //select event
                                if (event.id == selectedEvent){
                                    selectedEvent = -1
                                } else {
                                    selectedEvent = event.id
                                }
                            }
                        }//tap each event, and it sets id = to that event
                    
                }//foreach
            }//if loading, else, fetch.
            
        }//vstack
        .onAppear{
            //fetch events on appear
            Task {
                await fetchEvents()
            }
            
            //refrehs on timer
            refreshTimer = Timer.scheduledTimer(withTimeInterval: 300, repeats: true){ _ in
                Task {
                    await fetchEvents()
                }
            }
                
        }
        .onDisappear {
            //if view disappears, invalidate timers
            refreshTimer?.invalidate()
            refreshTimer = nil
        }
        .toolbar {
            //force refresh
            Button("Refresh"){
                parentView.forceRefresh() //only changes last change to nil
                removeDuplicates() //remove duplicates
                Task{ await fetchEvents()}
            }
            .disabled(isLoading) //cannot use while events are loading
        }
    }//body
    
    
    
    /// Fetching events
    
    //Function Fetch Events
    /**
     Returns nothing:
     once called, shows isloading ot be true, and resets error message
     */
    private func fetchEvents() async {
        //determine if we should fetch
        let shouldFetch = parentView.lastFetched == nil || Date().timeIntervalSince(parentView.lastFetched!) >= parentView.cacheExpiration || parentView.forceRefreshRequested
        
        //use cache data
        if !shouldFetch{
            print("using cached data")
            return
        }
        
        //if loading, set to true
        await MainActor.run {
            isLoading = true
        }
        
        //remove duplicates
        await MainActor.run {
            removeDuplicates()
        }
        
        print("Fetching data from API, updating cache")
        
        // Update on main thread since we're changing published properties
        let url = "https://node16049-csc324--spring2025.us.reclaim.cloud/events"
            
        //decode JSON into DTOs
        guard let jsonString = try? await EventData.fetchData(urlString: url) else {
            print("Failed to fetch data")
            await MainActor
                .run {
                    isLoading = false
                } //set to false if you failed to fetch data
            return
        }//guard let
            
            
        //parse events
        let eventDTOs = EventData.parseEvents(json: jsonString)
            
            
        // Update on main thread since we're changing published properties
        await MainActor.run {
            //add all existing ids to events
            var processedEventIds = Set<Int>()
                
            //process each event from API
            for dto in eventDTOs {
                //checks if that ID exists
                guard let dtoID = dto.eventid, dtoID > 0 else { continue }
                    
                    
                //if the events can fetch the descriptor from the modelcontext, and if they exist..
                if processedEventIds.contains(dtoID){
                    continue
                }//If contains, continue, if not..
                processedEventIds
                    .insert(dtoID)//add it to the list of processed IDs
                    
                // Check if this event already exists in the database
                let existingEvent = events.first(where: { $0.id == dtoID })
                    
                if let existing = existingEvent{
                    //update all values if already existing!
                    existing.name = dto.event_name ?? existing.name
                    existing.imageURL = dto.event_image ?? existing.imageURL
                    existing.location = dto.event_location ?? existing.location
                        
                    //handle arrays carefully
                    if let newOrgs = dto.organizations{
                        existing.organizations = newOrgs
                    }
                    if let newTags = dto.tags{
                        existing.tags = newTags
                    }
                        
                    //update dates
                    if let startTimeString = dto.event_start_time{
                        let dateFormatter = DateFormatter()
                        dateFormatter.locale = Locale(
                            identifier: "en_US_POSIX"
                        )
                        dateFormatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
                        existing.startTime = dateFormatter
                            .date(
                                from: startTimeString
                            ) ?? existing.startTime
                    }//setting starttime
                        
                    if let endTimeString = dto.event_end_time {
                        let dateFormatter = DateFormatter()
                        dateFormatter.locale = Locale(identifier: "en_US_POSIX")
                        dateFormatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
                        existing.endTime = dateFormatter
                            .date(from: endTimeString) ?? existing.endTime
                    }
                        
                    //change url
                    existing.imageURL = dto.event_image ?? existing.imageURL
                    existing.lastUpdated = Date() //update last-updated date
                    //print("Update existing event: \(existing.name) (ID: \(existing.id))")
                } else {
                    //create new event
                    let newEvent = EventModel(from: dto)
                        
                    //add a new persistent event model to our local database utilizing swiftdata
                    modelContext.insert(newEvent)
                    //print("added new event: \(eventModel.name) (ID: \(eventModel.id))")//
                } //if there isn't an event with that ID, create one.
            } //if there is an event with that ID
                
                
            //cleanup old Events (older than a week) from events
            let weekAgo = Calendar.current.date(
                byAdding: .day,
                value: -7,
                to: Date()
            )!
                
            //delete all values older than a week
            let oldPredicate = #Predicate<EventModel> {
                $0.lastUpdated < weekAgo
            } //defining predicate
            try? modelContext
                .delete(model: EventModel.self, where: oldPredicate)
                
            // a little inefficient, however..
            removeDuplicates()
                
            //save context after adding all events
            try? modelContext.save()
                
            // update timestamps
            parentView.lastFetched = Date()
            parentView.forceRefreshRequested = false
                
            //turn off loading state
            isLoading = false
            print("API fetch completed at \(Date())")
        } // await MainActor
        
        
        
        await MainActor.run {
            print("Fetch complete. Found \(events.count) events for timespan \(parentView.timeSpan.start) to \(parentView.timeSpan.end)")
        }
    } //fetchEvents
    
    
    private func removeDuplicates() {
        // Group events by ID and find duplicates
        let eventsByID = Dictionary(grouping: events, by: { $0.id })
        
        for (_, duplicates) in eventsByID where duplicates.count > 1 {
            // Sort by last updated
            let sortedDuplicates = duplicates.sorted {
                $0.lastUpdated > $1.lastUpdated
            }
            
            // Keep the most recently updated one and delete the rest
            for duplicate in sortedDuplicates.dropFirst() {
                // The issue is here - before deleting, make sure we're not
                // trying to access or modify the tags property
                modelContext.delete(duplicate)
            }
        }
        
        // Try to save the context and handle potential errors
        do {
            try modelContext.save()
        } catch {
            print("Error saving context after removing duplicates: \(error)")
        }
    }
    
    
}//EventList


