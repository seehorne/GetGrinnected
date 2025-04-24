//
//  EventViewModel.swift
//  GetGrinnected
//
//  Created by Budhil Thijm on 4/22/25.
//
import SwiftUI

//EventViewModel: Find a way to View our Events
class EventViewModel: ObservableObject {
    @Published var events: [Event] = []
    @Published var viewedEvents: [Event] = []
    @Published var isLoading = false
    @Published var errorMessage: String? = nil
        
    private let urlString = "https://node16049-csc324--spring2025.us.reclaim.cloud/events"
    private let refreshInterval: TimeInterval = 3600 // Refresh from API every hour
    
    
    // Key for storing last refresh timestamp
    private let lastRefreshKey = "lastEventAPIRefresh"
        
    //taken from CLAUDAI
    func loadEvents() async {
        await MainActor.run {
            self.isLoading = true
            self.errorMessage = nil
        }
            
        // First load from local database (fast)
        await loadFromLocalDatabase()
            
        // Check if we need to refresh from API
        if shouldRefreshFromAPI() {
            await refreshFromAPI()
        } else {
            await MainActor.run {
                self.isLoading = false
            }
        }
    }
    
    //from CLAUDAI
    private func loadFromLocalDatabase() async {
        // Load events from local database
        let dbEvents = DatabaseManager.shared.fetchAllEvents()
           
        await MainActor.run {
            if !dbEvents.isEmpty {
                self.events = dbEvents
                sortEventsByStartTime()
                print("Loaded \(dbEvents.count) events from local database")
            }
        }
    }
    
    private func shouldRefreshFromAPI() -> Bool {
        // Check when we last refreshed
        if let lastRefresh = UserDefaults.standard.object(forKey: lastRefreshKey) as? Date {
            let elapsed = Date().timeIntervalSince(lastRefresh)
            return elapsed > refreshInterval
        }
        // If never refreshed, then we should refresh
        return true
    }
       
    func refreshFromAPI() async {
        do {
            let jsonString = try await EventData.fetchData(urlString: urlString)
            let fetchedEvents = EventData.parseEvents(json: jsonString)
               
            // Save to database
            DatabaseManager.shared.saveEvents(fetchedEvents)
               
            // Update last refresh time
            UserDefaults.standard.set(Date(), forKey: lastRefreshKey)
               
            await MainActor.run {
                self.events = fetchedEvents
                sortEventsByStartTime()
                self.isLoading = false
                print("Refreshed \(fetchedEvents.count) events from API")
            }
        } catch {
            await MainActor.run {
                // If we already have events from the database, just show a warning
                if !self.events.isEmpty {
                    print("Could not refresh from API, using cached data")
                } else {
                    self.errorMessage = "Failed to load events: \(error.localizedDescription)"
                }
                self.isLoading = false
            }
        }
    }
       
    // Force refresh from API (can be called by pull-to-refresh)
    func forceRefresh() async {
        await MainActor.run {
            self.isLoading = true
        }
        await refreshFromAPI()
    }
    
    //end claude's help
    
    // This is the main function to call when you need events
    func fetchEvents() async {
        await MainActor.run {
            self.isLoading = true
            self.errorMessage = nil
        }
            
        // First try to load from local database
        let dbEvents = DatabaseManager.shared.fetchAllEvents()
            
        if !dbEvents.isEmpty {
            // We have cached data, use it immediately
            await MainActor.run {
                self.events = dbEvents
                sortEventsByStartTime()
                self.isLoading = false
                print("Loaded \(dbEvents.count) events from local database")
            }
                
            // Check if we need to refresh in background
            if shouldRefreshFromAPI() {
                // Refresh in background without showing loading indicator
                Task {
                    await refreshFromAPIInBackground()
                }
            }
        } else {
            // No cached data, we must fetch from API
            await refreshFromAPI()
        }
    }//fetchEvents
    
    
    // Refresh from API in background without showing loading indicator
    private func refreshFromAPIInBackground() async {
        do {
            let jsonString = try await EventData.fetchData(urlString: urlString)
            let fetchedEvents = EventData.parseEvents(json: jsonString)
               
            // Save to database
            DatabaseManager.shared.saveEvents(fetchedEvents)
               
            // Update last refresh time
            UserDefaults.standard.set(Date(), forKey: lastRefreshKey)
               
            await MainActor.run {
                self.events = fetchedEvents
                sortEventsByStartTime()
                print(
                    "Background refreshed \(fetchedEvents.count) events from API"
                )
            }
        } catch {
            print("Background refresh failed: \(error.localizedDescription)")
        }
    }
       
   
    /*
     Sorts events by ascending start time.
     */
    func sortEventsByStartTime() {
        events.sort {
            event1,
            event2 in
            // check the events' start times are not nil
            if(
                event1.useful_event_start_time != nil && event2.useful_event_start_time != nil
            ){
                // check if event1 starts before or at same time as event2
                if (
                    event1.useful_event_start_time! <= event2.useful_event_start_time!
                ) {
                    return true
                } //if
            } //if
            return false
        } //sort
    }
   
    /*
     Filters the events array to only events in the time span and stores them in viewed events.
    
     timeSpan: An interval of dates that you want to see events for.
     */
    func filterEventsByDateInterval(timeSpan: DateInterval) {
        // filter based on if an event in events is inside the timeSpan
        viewedEvents = events.filter { event in
            // check that the event has a start and end time
            if event.useful_event_start_time != nil && event.useful_event_end_time != nil {
                // check current events start or end is in the timeSpan
                if timeSpan
                    .contains(event.useful_event_start_time!) || timeSpan
                    .contains(event.useful_event_end_time!) {
                    return true
                } //if
            } //if
            return false
        } //filter
    }
}
