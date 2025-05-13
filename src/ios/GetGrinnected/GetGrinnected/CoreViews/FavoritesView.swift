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
    @State private var sortOrder = SortOrder.time
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
                        //selectors and tags
                        HStack{
                            //creating a selector of sort order
                            Picker("Sort by", selection: $sortOrder) {
                                ForEach(SortOrder.allCases) { sortOrder in
                                    Text("Sort by \(sortOrder.rawValue.capitalized)")
                                }
                            }
                            .pickerStyle(.segmented)
                            
                            TagMultiSelector(title: "Tags", parentView: parentView)
                            
                            
                            
                        }//header elements
                        .padding()
                        
                        //event list view for all the events (may have to pass in some arguments according to the day
                        EventList(parentView: parentView, selectedEvent: -1, sortOrder: sortOrder, filterType: filterType, filter: "", filterByDate: true)
                        
                    }//vstack
                }
                .onAppear{
                    // refresh the events so we remove old ones and refresh the tags
                    DispatchQueue.main.asyncAfter(deadline: .now() + 0.5) {
                        parentView.forceRefresh() //add a small delay to ensure eventlist is fully initialized
                    }
                }
            }
            .edgesIgnoringSafeArea(.top)
            
        }//GeometryReader
    }
}

#Preview {
    FavoritesView()
}
