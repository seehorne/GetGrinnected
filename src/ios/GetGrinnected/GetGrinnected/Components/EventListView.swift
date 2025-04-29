//
//  EventListView.swift
//  GetGrinnected
//
//  Created by Budhil Thijm on 4/29/25.
//

import SwiftUI
import SwiftData

struct EventListView: View {
    @Binding var isLoading: Bool
    @Query(sort: \EventModel.startTime) private var events: [EventModel] //an array of eventmodels
    @State var selectedEvent: Int? //An integer to represent which event we select
    @State private var refreshTimer: Timer? //a timer to count when we refresh
    
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
//            //fetch events on initial appear
//            Task {
//                await EventList.fetchEvents()
//            }
//            
//            //refresh on timer, every 5 minutes for now!
//            refreshTimer = Timer.scheduledTimer(withTimeInterval: 300, repeats: true){ _ in
//                Task {
//                    await EventList.fetchEvents()
//                }
//            }
                
        }
        .onDisappear {
            //if view disappears, invalidate timers, so it doesn't refresh every time!
            refreshTimer?.invalidate()
            refreshTimer = nil
        }
    }//body
}
