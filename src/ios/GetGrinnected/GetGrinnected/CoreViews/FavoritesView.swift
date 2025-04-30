//
//  FavoritesView.swift
//  GetGrinnected
//
//  Created by Ellie Seehorn on 3/4/25.
//

import SwiftData
import SwiftUI


struct FavoritesView: View {
    // the parent model used for updating our event list
    @StateObject private var viewModel = EventListParentViewModel()
    
    //only initialized value that is a query; sort our events by start time, and make it queryable by @query
    @Query(filter: #Predicate<EventModel>{ event in event.favorited }, sort: \EventModel.startTime) var events: [EventModel] //an array of eventmodels
    
    //parentview that observes values if change
    @StateObject var parentView = EventListParentViewModel()
    @State private var isLoading: Bool = false
    @State private var refreshTimer: Timer? //a timer to count when we refresh
    
    //Sorting and Filtering parameters
    @State private var sortOrder = SortOrder.eventTime
    @State private var filterType = FilterType.name
    @State private var filter = ""
    @State private var selectedEvent: Int = -1
    
    var body: some View {
        GeometryReader{proxy in
            let safeAreaTop = proxy.safeAreaInsets.top
            VStack(){
                // Header is outside of scrollable so it does not move
                Header(safeAreaTop: safeAreaTop, title: "Favorites", searchBarOn: false)
                ScrollView(.vertical, showsIndicators: false){
                //content of eventlist, show favorites true!, no search string
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
                }
                //TODO: set tags for filtering
            }
            .edgesIgnoringSafeArea(.top)
            
        }//GeometryReader
    }
}

#Preview {
    FavoritesView()
}
