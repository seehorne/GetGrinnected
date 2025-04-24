//
//  EventViewModel.swift
//  GetGrinnected
//
//  Created by Budhil Thijm on 4/22/25.
//
import SwiftUI
import SwiftData

/**
 This is where the main changes to making a local database utilizing SwiftData is going to be.
 This file will take events from the API, and store it in our SwiftData..
 
 */
//EventViewModel: Find a way to View our Events
class EventViewModel: ObservableObject {
    //set the model context, containing model EventsDTO and the model userprofile
    private let modelContext: ModelContext
    
    init(modelContext: ModelContext) {
        self.modelContext = modelContext
    }
    
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
           let url = "https://node16049-csc324--spring2025.us.reclaim.cloud/events"
           
           //decode JSON into DTOs
           let jsonString = try await EventData.fetchData(urlString: url)
           let eventDTOs = EventData.parseEvents(json: jsonString)
           
           
           // Update on main thread since we're changing published properties
           await MainActor.run {
               for dto in eventDTOs {
                   //add predicate while checking if that dto already exists
                   let predicate = #Predicate<EventModel> { $0.id == dto.eventid ?? 0}
                   //get descriptor
                   let descriptor = FetchDescriptor<EventModel>(predicate: predicate)
                   
                   //if the events can fetch the descriptor from the modelcontext, and if they exist..
                   if let existingEvents = try? modelContext.fetch(descriptor),  !existingEvents.isEmpty {
                       //update the names
                       let existingEvent = existingEvents[0]
                       //update all values if already existing!
                       existingEvent.name = dto.event_name ?? existingEvent.name
                       existingEvent.imageURL = dto.event_image ?? existingEvent.imageURL
                       existingEvent.location = dto.event_location ?? existingEvent.location
                       existingEvent.organizations = dto.organizations ?? existingEvent.organizations
                       existingEvent.tags = dto.tags ?? existingEvent.tags
                   } else {
                       //create new event
                       let eventModel = EventModel(from: dto)
                       //add a new persistent event model to our local database utilizing swiftdata
                       modelContext.insert(eventModel)
                   }
               }
               //save context after adding all events
               try? modelContext.save()
               
           }//await
       } catch {
           //if error..
           await MainActor.run {
               //show error
               print("Failed to fetch events: \(error.localizedDescription)")
           }
       }// do-catch
   }//fetchEvents
   
    
    //need to update sorting events by date
    
   /*
    Sorts events by ascending start time.
    */
//   func sortEventsByStartTime() {
//       events.sort { event1, event2 in
//           // check the events' start times are not nil
//           if(event1.useful_event_start_time != nil && event2.useful_event_start_time != nil){
//               // check if event1 starts before or at same time as event2
//               if (event1.useful_event_start_time! <= event2.useful_event_start_time!) {
//                   return true
//               } //if
//           } //if
//           return false
//       } //sort
//   }
//   
//   /*
//    Filters the events array to only events in the time span and stores them in viewed events.
//    
//    timeSpan: An interval of dates that you want to see events for.
//    */
//   func filterEventsByDateInterval(timeSpan: DateInterval) {
//       // filter based on if an event in events is inside the timeSpan
//       viewedEvents = events.filter { event in
//           // check that the event has a start and end time
//           if event.useful_event_start_time != nil && event.useful_event_end_time != nil {
//               // check current events start or end is in the timeSpan
//               if timeSpan.contains(event.useful_event_start_time!) || timeSpan.contains(event.useful_event_end_time!) {
//                   return true
//               } //if
//           } //if
//           return false
//       } //filter
//   }
}
