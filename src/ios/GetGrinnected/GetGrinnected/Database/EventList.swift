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
    
//    
//    init(selectedEvent: Int?, parentView: EventListParentViewModel){
//        self.selectedEvent = selectedEvent
//        self._parentView = ObservedObject(wrappedValue: parentView)
//        
//        let timeSpanStart = parentView.timeSpan.start
//        let timeSpanEnd = parentView.timeSpan.end
//        
//        let predicate = #Predicate<EventModel> { event in
//            if let startTime = event.startTime {
//                return startTime >= timeSpanStart && startTime <= timeSpanEnd
//            } else {
//                return false
//            }
//        }//search
//        
//        _events = Query(filter: predicate, sort: \EventModel.startTime)
//        
//        _viewModel = StateObject(wrappedValue: EventViewModel(modelContext: modelContext))
//        
//    }
    
    
    //the initializer for the eventlist is the sorting function!
    
    init(selectedEvent: Int?, parentView: EventListParentViewModel, sortOrder: [SortDescriptor<EventModel>] = [], searchString: String){
        self.selectedEvent = selectedEvent
        self._parentView = ObservedObject(wrappedValue: parentView)
        
        //checking predicate of searching
        //        let predicate = searchString.isEmpty ? nil: #Predicate<EventModel> {
        //            //we are unwrapping it, though maybe it's important to check whether or not name exists
        //            $0.name!.localizedStandardContains(searchString) ||
        //            //same for organizations
        //            $0.organizations!.contains(where: { $0.localizedStandardContains(searchString) })
        //        }
        
        
        
        //apply sort order, default if no title or organization provided
        let finalSortOrder = sortOrder.isEmpty ? [SortDescriptor(\EventModel.name)] : sortOrder
        
        //initialize events according to those sorting specificatinos
        _events = Query(filter: #Predicate {
            if searchString.isEmpty {
                return true
            } else {
                return $0.name.localizedStandardContains(searchString)
            }
        }, sort: finalSortOrder)
    }//init
    
    
    var body: some View {
            VStack{
                ForEach(events) { event in
                    EventCard(event: event, isExpanded: (event.id == selectedEvent))
                        .onTapGesture {
                            withAnimation(.easeInOut) {
                                if selectedEvent == event.id {
                                    selectedEvent = -1
                                } else {
                                    selectedEvent = event.id
                                }
                            }
                        }//tap each event, and it sets id = to that event
                    
                }//foreach
                
                //placeholder if no events
                if events.isEmpty {
                    Text("No Events in this time period")
                        .padding()
                }
                
            }//vstack
            .onChange(of: parentView.timeSpan) {oldValue, newValue in
                //change predicate of searching
                
            }
            .onAppear{
                
                //fetch events on appear
                Task {
                    await fetchEvents()
                }
                
            }
        
//        .searchable(text: $searchString, placement: .automatic, prompt: "Search..")
            
            
//        if let error = viewModel.errorMessage {
//            VStack {
//                Text(error)
//                    .foregroundColor(.red)
//                Button("Retry") {
//                    Task {
//                        await viewModel.fetchEvents()
//                    }
//                }
//                .padding()
//                .background(Color.blue)
//                .foregroundColor(.white)
//                .cornerRadius(8)
//            }
//        }//error cehcking
        
    }//body
    
    
    
    /// Fetching events
   
   //Function Fetch Events
   /**
    Returns nothing:
    once called, shows isloading ot be true, and resets error message
    */
   private func fetchEvents() async {
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
}
