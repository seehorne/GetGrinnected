//
//  EventViewModel.swift
//  GetGrinnected
//
//  Created by Budhil Thijm on 4/22/25.
//
import SwiftUI

//EventViewModel: Find a way to View our Events
class EventViewModel: ObservableObject {
   @Published var events: [Event] = [] //Store Events from our API
   @Published var viewedEvents: [Event] = [] //Store events that are viewed based on filters
   @Published var isLoading = false //check if loading
   @Published var errorMessage: String? = nil //showing the error message
    
   //HERE IS OUR API link
   private let urlString = "https://node16049-csc324--spring2025.us.reclaim.cloud/events"
   
   
   //Function Fetch Events
   /**
    Returns nothing:
    once called, shows isloading ot be true, and resets error message
    */
   func fetchEvents() async {
       // do-catch outline..
       do {
           // Update on main thread since we're changing published properties
           await MainActor.run {
           //reset error message, set loading to be true
               self.isLoading = true
               self.errorMessage = nil
           } //await
           
           //get our json string using EventData's fetch-data
           let jsonString = try await EventData.fetchData(urlString: urlString)
           //print out string for testing
           //print("String: \(jsonString)")
           
           //parse events into fetchedEvents from that string
           let fetchedEvents = EventData.parseEvents(json: jsonString)
           
           // Update on main thread since we're changing published properties
           await MainActor.run {
               //set events and now that we are done, set isLoading to false
               self.events = fetchedEvents
               // sort the events by start time
               sortEventsByStartTime()
               
               self.isLoading = false
           }//await
       } catch {
           //if error..
           await MainActor.run {
               //show error
               self.errorMessage = "Failed to load events: \(error.localizedDescription)"
               self.isLoading = false
           }
       }// do-catch
   }//fetchEvents
   
   /*
    Sorts events by ascending start time.
    */
   func sortEventsByStartTime() {
       events.sort { event1, event2 in
           // check the events' start times are not nil
           if(event1.useful_event_start_time != nil && event2.useful_event_start_time != nil){
               // check if event1 starts before or at same time as event2
               if (event1.useful_event_start_time! <= event2.useful_event_start_time!) {
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
               if timeSpan.contains(event.useful_event_start_time!) || timeSpan.contains(event.useful_event_end_time!) {
                   return true
               } //if
           } //if
           return false
       } //filter
   }
    
    func filterEventsBySelectedTags(selectedTags: Set<EventTags>) {
        // check if there are any tags
        if !selectedTags.isEmpty {
            // filter based on if an event has a selected tag
            viewedEvents = events.filter { event in
                // check the event has tags
                if event.useful_tags != nil && !event.useful_tags!.isEmpty {
                    // check if the event has any of the selected tags
                    if selectedTags.isSubset(of: event.useful_tags!) {
                        return true
                    }
                } //if
                return false
            } //filter
        } //if
    }
}
