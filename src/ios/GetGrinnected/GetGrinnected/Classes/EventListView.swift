//
//  EventListView.swift
//  GetGrinnected
//
//  Created by Budhil Thijm & Michael Paulin on 4/8/25.
//

import SwiftUI


/**
 Links to look at:
 https://khush7068.medium.com/building-a-swiftui-app-with-mvvm-and-async-await-for-networking-ef777b2bf7e8
 
 https://www.reddit.com/r/SwiftUI/comments/10e8lu6/swiftui_networking_using_mvvm_pattern/
 
 */
//EventViewModel: Find a way to View our Events
class EventViewModel: ObservableObject {
    @Published var events: [Event] = [] //Store Events from our API
    @Published var isLoading = false //check if loading
    @Published var errorMessage: String? = nil //showing the error message
     
    //HERE IS OUR API link
    private let urlString = "https://node16049-csc324--spring2025.us.reclaim.cloud/events"
    
    
    //Function Fetch Events
    /**
     Returns nothing:
     once called, shows isloading ot be true, and resets error message
     */
    func fetchEvents() {
        //reset error message, set loading to be true
        isLoading = true
        errorMessage = nil
        
        //in a task: do-catch outline..
        Task {
            do {
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
        }//Task
    }//fetchEvents
    
    
}

struct EventListView: View {
    @StateObject private var viewModel = EventViewModel()
    //binding to the value we input into event card
    //@State to show that this will be passed into the
    @State var selectedEvent: Int? //An integer to represent which event we select
    // The span of time we want to see the events in
    @State var timeSpan: DateInterval
    // The tags we want the events we see to have
    @State var tags: Set<EventTags>?
    
    
    var body: some View {
        VStack{
            ForEach(viewModel.events, id: \.eventid) { event in
                // check current event is in the timeSpan
                if event.useful_event_start_time != nil && timeSpan.contains(event.useful_event_start_time!) {
                    // make an event card for current event
                    EventCard(event: event, isExpanded: (event.eventid! == selectedEvent))
                        .onTapGesture {//change expansion to ture to make card larger
                            //add animation to the tap gesture!
                            withAnimation(.easeInOut) { // this animation should be changed: The best way to change this may include changing the structure of our Event Card
                                //HERE WE ARE NOT CHECKING FOR OPTIONAL
                                //however, ALL events DO have an id
                                //if have an error, check here, though
                                // near-impossible. some errors, but
                                if(selectedEvent == event.eventid!){
                                    //if you select the same event, it unselects it
                                    selectedEvent = -1
                                } else{
                                    //if you select something else, you select a new id
                                    selectedEvent = event.eventid!
                                }
                            }
                        }//tap gesture
                } //if
            }
        
            
            if viewModel.isLoading {
                ProgressView()
            }
            
            if let error = viewModel.errorMessage {
                VStack {
                    Text(error)
                        .foregroundColor(.red)
                    Button("Retry") {
                        viewModel.fetchEvents()
                    }
                    .padding()
                    .background(Color.blue)
                    .foregroundColor(.white)
                    .cornerRadius(8)
                }
            }
                
        } //For all events
        .onAppear(){
            viewModel.fetchEvents()
        }
    }
}

// For preview purposes (assuming Event struct exists)
struct EventListView_Previews: PreviewProvider {
    static var previews: some View {
        EventListView(selectedEvent: -1, timeSpan: DateInterval(start: Date.now, end: Date.now.addingTimeInterval(86400)))
    }
}
