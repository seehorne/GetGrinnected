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
enum SortOrder: String, Identifiable, CaseIterable {
    case eventName, eventTime
    var id: Self { self}
}

enum FilterType: String, Identifiable, CaseIterable {
    case name, favorites //different sorting options, for now removing organization and tag
    var id: Self { self } //identifiable protocol
}

struct EventList: View {
    //model context, containing how we save to cache
    @Environment(\.modelContext) private var modelContext
    
    //only initialized value that is a query; sort our events by start time, and make it queryable by @query
    @Query var events: [EventModel] //an array of eventmodels
    @State var isLoading: Bool = false
    
    
    //parentview that observes values if change
    @ObservedObject var parentView: EventListParentViewModel
    
    
    //Sorting and Filtering parameters
    @Binding private var sortOrder: SortOrder
    @Binding private var filterType: FilterType
    @Binding private var filter: String
    
    
    // https://www.youtube.com/watch?v=ASnbOSMv1iw&ab_channel=StewartLynch
    init(parentView: EventListParentViewModel, sortOrder: Binding<SortOrder>, filterType: Binding<FilterType>, filter: Binding<String>){
        self.parentView = parentView
        self._filterType = filterType
        self._sortOrder = sortOrder
        self._filter = filter
    }
    
    
    var body: some View {
        EventListView(selectedEvent: -1, sortOrder: $sortOrder, filterType: $filterType, filter: $filter)
    }
    
    
    //update events, save to cache
    func updateEvents() async {
        let EventDTOs = await fetchEvents(parentView: parentView, events: events, isLoading: $isLoading)
        await DTOtoCache(parentView: parentView, events: events, eventDTOs: EventDTOs, isLoading: $isLoading)
    }
    
    /**
     Returns nothing:
     once called, shows isloading ot be true, and resets error message
    Takes JSON from API and converts into DTO
     */
    func fetchEvents(parentView: EventListParentViewModel,events: [EventModel], isLoading: Binding<Bool>) async -> [EventDTO] {
        //determine if we should fetch based on last fetched time, time interval (date selected), and if a force refresh was requested
        let shouldFetch = parentView.lastFetched == nil || Date().timeIntervalSince(parentView.lastFetched!) >= parentView.cacheExpiration || parentView.forceRefreshRequested
        
        //use cache data
        if !shouldFetch{
            print("No need to Fetch")
            return []//return to escape from the fetched events function
        }
        
        //if loading, set to true, to show loading view
        await MainActor.run {
            isLoading.wrappedValue = true
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
                    isLoading.wrappedValue = false
                } //set to false if you failed to fetch data
            return []
        }//guard let
            
        //parse events
        let eventDTOs = EventData.parseEvents(json: jsonString)
        
        //note that fetch is complete
        await MainActor.run {
            print("Fetch complete. Found \(events.count) events for timespan \(parentView.timeSpan.start) to \(parentView.timeSpan.end)")
        }
        
        //return all DTOs for another function to save to cache
        return eventDTOs
    } //fetchEvents
    
    /**
        FetchEvents now only returns DTOs, while DTOtoCache takes thoes values
     and  stores them in the cache
     */
    func DTOtoCache(parentView: EventListParentViewModel, events: [EventModel], eventDTOs: [EventDTO], isLoading: Binding<Bool>) async {
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
                
                
            //cleanup old Events (older than two weeks) from events
            let weekAgo = Calendar.current.date(
                byAdding: .day,
                value: -14,
                to: Date()
            )!
                
            //delete all values older than two weeks
            let oldPredicate = #Predicate<EventModel> {
                $0.lastUpdated < weekAgo
            } //defining predicate
            
            try? modelContext
                .delete(model: EventModel.self, where: oldPredicate)
                
            // a little inefficient, however..
            removeDuplicatesFromCache(events: events)
                
            //save context after adding all events
            try? modelContext.save()
                
            // update timestamps
            parentView.lastFetched = Date()
            parentView.forceRefreshRequested = false
                
            //turn off loading state
            isLoading.wrappedValue = false
            print("DTO Caching completed at \(Date())")
        } // await MainActor
        
        
    }
    
    /**
     Remove duplicates to remove any duplicate events clogging up the cache!
     */
    func removeDuplicatesFromCache(events: [EventModel]) {
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


