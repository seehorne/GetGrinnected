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
}

struct EventListView: View {
    @StateObject private var viewModel = EventViewModel()
    //binding to the value we input into event card
    //@State to show that this will be passed into the
    @State var selectedEvent: Int? //An integer to represent which event we select
    // the parent model used for updating our event list
    @ObservedObject var parentView: EventListParentViewModel
    // The tags we want the events we see to have
    // @Binding var tags: Set<EventTags>?
    
    
    var body: some View {
        VStack{
            ForEach(viewModel.viewedEvents, id: \.eventid) { event in
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
                    } //tap gesture
            }
        
            
            if viewModel.isLoading {
                ProgressView()
            }
            
            if let error = viewModel.errorMessage {
                VStack {
                    Text(error)
                        .foregroundColor(.red)
                    Button("Retry") {
                        // TODO: fix retry button
                        //viewModel.fetchEvents()
                    }
                    .padding()
                    .background(Color.blue)
                    .foregroundColor(.white)
                    .cornerRadius(8)
                }
            }
                
        } //For all events
        .onAppear(){
            // background process
            Task {
                // re-fetch events
                await viewModel.fetchEvents()
                // filter events to time span
                viewModel.filterEventsByDateInterval(timeSpan: parentView.timeSpan)
            }
        }
        // if the timeSpan changes filter events again
        .onChange(of: parentView.timeSpan) { oldValue, newValue in
            // check we have events
            if(!viewModel.events.isEmpty) {
                // filter events to time span
                viewModel.filterEventsByDateInterval(timeSpan: parentView.timeSpan)
            }
        }
    }
}


#Preview {
    EventListView(selectedEvent: -1, parentView: EventListParentViewModel())
}
