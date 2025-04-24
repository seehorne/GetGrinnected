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
struct EventListView: View {
    @StateObject private var viewModel = EventViewModel()
    //binding to the value we input into event card
    //@State to show that this will be passed into the
    @State var selectedEvent: Int? //An integer to represent which event we select
    // the parent model used for updating our event list
    @ObservedObject var parentView: EventListParentViewModel
    
    
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
