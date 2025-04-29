//
//  EventList.swift
//  GetGrinnected
//
//  Created by Budhil Thijm on 4/24/25.
//

import SwiftData //swift data is how we cache information
import SwiftUI


/**
 EventList takes EventDTO, converts into cached EventModel. If event already exists in cache, updates information.
 Searching, filtering and forcerefreshing is all in this file.
 */
struct EventList: View {
    //model context, containing how we save to cache
    @Environment(\.modelContext) private var modelContext
    
    //only initialized value that is a query; sort our events by start time, and make it queryable by @query
    @Query(sort: \EventModel.startTime) private var events: [EventModel] //an array of eventmodels
    
    //parentview that observes values if change
    @ObservedObject var parentView: EventListParentViewModel
    
    @State var selectedEvent: Int? //An integer to represent which event we select
    @State private var refreshTimer: Timer? //a timer to count when we refresh
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
        //set selected event to selected event
        self.selectedEvent = selectedEvent
        self._parentView = ObservedObject(wrappedValue: parentView) //set parent view to an observed object of parent view
        
        
        //apply sort order, default if no title or organization provided
        let finalSortOrder = sortOrder.isEmpty ? [SortDescriptor(
            \EventModel.startTime //sort by time
        )] : sortOrder
        
        //set timespans to explain what view is showing (debugging purposes)
        let timeSpanStart = parentView.timeSpan.start
        let timeSpanEnd = parentView.timeSpan.end
        
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
    //space so it's easier to distinguish different components of this view
    
    
    
    
    
    
    /**
     Buildpredicate is used to input into the query. This is so that the query is able to run efficiently
     otherwise the compiler freaks out.
     */
    private func buildFilterPredicate(
        timeSpanStart: Date,
        timeSpanEnd: Date,
        filterToday: Bool,
        searchString: String,
        showFavorites: Bool
    ) -> Predicate<EventModel> {
        return #Predicate<EventModel> { event in
            // Time filter condition
            return (filterToday //filter today by the start time and end time
                    ? event.startTime.flatMap {$0 >= timeSpanStart && $0 <= timeSpanEnd} ?? false //check the dates, otherwise false (not in that date)
                    : true) //if not filtering for today, return true
            &&
            // search filtering: if it's empty, simply write true (so all events are present), or whatever event name contains that search
            (searchString.isEmpty || event.name.localizedStandardContains(searchString))
            &&
            //if the event is favorited, show the event. ShowFavorites is only on for the favorites page
            (showFavorites ? event.favorited : true)
        }
    }
    
    //main event body view
    var body: some View {
        VStack{
            //if empty show that no events are in that time period
            if events.isEmpty{
                //a more helpful empty message
                Text("No Events under those parameters...")
                    .padding()
                    .foregroundStyle(.textSecondary)
                
            }
            
            //helpful loading page
            if isLoading {
                //progress View
                ProgressView("Loading Events...")
                    .padding()
            } else {
                //for every event, based on ID
                ForEach(events, id: \.id) { event in
                    //add an event card of that event that..
                    EventCard(event: event, isExpanded: (event.id == selectedEvent))//selects based on if the selected event is the same as the event ID
                        .onTapGesture { //on tap..
                            withAnimation(.easeInOut) { //changes selectedEvent to this event, (and thus expands it)
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
            //fetch events on initial appear
            Task {
                await fetchEvents()
            }
            
            //refresh on timer, every 5 minutes for now!
            refreshTimer = Timer.scheduledTimer(withTimeInterval: 300, repeats: true){ _ in
                Task {
                    await fetchEvents()
                }
            }
                
        }
        .onDisappear {
            //if view disappears, invalidate timers, so it doesn't refresh every time!
            refreshTimer?.invalidate()
            refreshTimer = nil
        }
    }//body
    
    
    
    /// Fetching events
    
    //Function Fetch Events
    /**
     Returns nothing:
     once called, shows isloading ot be true, and resets error message
     */
    private func fetchEvents() async {
        //determine if we should fetch based on last fetched time, time interval (date selected), and if a force refresh was requested
        let shouldFetch = parentView.lastFetched == nil || Date().timeIntervalSince(parentView.lastFetched!) >= parentView.cacheExpiration || parentView.forceRefreshRequested
        
        //use cache data
        if !shouldFetch{
            print("using cached data")
            return //return to escape from the fetched events function
        }
        
        //if loading, set to true, to show loading view
        await MainActor.run {
            isLoading = true
        }
        
        //remove duplicates in the main actor (not sure why)
        await MainActor.run {
            removeDuplicates()
        }
        
        //logging for debugging
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
                processedEventIds.insert(dtoID)//add it to the list of processed IDs
                    
                // Check if this event already exists in the database
                let existingEvent = events.first(where: { $0.id == dtoID })
                    
                if let existing = existingEvent{
                    //update all values if already existing!
                    existing.name = dto.event_name ?? existing.name
                    existing.imageURL = dto.event_image ?? existing.imageURL
                    existing.location = dto.event_location ?? existing.location
                    
                    //handle saving data, so no overwriting
                    print("favorited \(existing.name)\(existing.favorited)")
                    existing.favorited = existing.favorited
                        
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
        
        
        //note that fetch is complete
        await MainActor.run {
            print("Fetch complete. Found \(events.count) events for timespan \(parentView.timeSpan.start) to \(parentView.timeSpan.end)")
        }
    } //fetchEvents
    
    
    /**
     Remove duplicates to remove any duplicate events clogging up the cache!
     */
    private func removeDuplicates() {
        // Group events by ID and find duplicates
        let eventsByID = Dictionary(grouping: events, by: { $0.id })
        
        //for every duplicate, where the event matches the id and there are more than 1 of that event..
        for (_, duplicates) in eventsByID where duplicates.count > 1 {
            // Sort by last updated
            let sortedDuplicates = duplicates.sorted {
                $0.lastUpdated > $1.lastUpdated
            }
            
            // Keep the most recently updated one and delete the rest
            for duplicate in sortedDuplicates.dropFirst() {
                // The issue is here - before deleting, make sure we're not
                // trying to access or modify the tags property
                //delete that duplicate
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


