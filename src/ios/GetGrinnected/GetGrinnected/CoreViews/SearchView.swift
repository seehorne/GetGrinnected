//
//  SearchView.swift
//  GetGrinnected
//
//  Created by Budhil Thijm on 4/28/25.
//

import SwiftUI
import SwiftData

struct SearchView: View {
    // the parent model used for updating our event list
    //model context, containing how we save to cache
    @Environment(\.modelContext) private var modelContext
    
    //only initialized value that is a query; sort our events by start time, and make it queryable by @query
    @Query(sort: \EventModel.startTime) var events: [EventModel] //an array of eventmodels
    
    //parentview that observes values if change
    @StateObject var parentView = EventListParentViewModel()
    @State private var selectedEvent: Int?
    @State private var isLoading = false //set loading states
    @State private var refreshTimer: Timer? //a timer to count when we refresh
    
    
    //Sorting and Filtering parameters
    @State private var sortOrder = SortOrder.time
    @State private var filterType = FilterType.name
    @State private var filter = ""
    
    
    var body: some View{
        GeometryReader{proxy in
            let safeAreaTop = proxy.safeAreaInsets.top
            VStack{ //contains pickers
                //searchbar
                SearchBar(inputText: $filter, safeAreaTop: safeAreaTop)
                
                //header elements
                HStack{
                    //creating a selector of sort order
                    Picker("Sort Order", selection: $sortOrder){
                        //for all cases of our possible sort orders
                        ForEach(SortOrder.allCases) { sortOrder in
                            Text("Sort by \(sortOrder)")
                        }
                    }
                    .padding(.horizontal) //horizontal padding, so the sortorder dropdown isn't right on the leftside of the screen
                    
                    Spacer()
                    
                    //filtering list
                    Picker("Search by by", selection: $filterType) {
                        ForEach(FilterType.allCases) { filterType in
                            Text("Search By by \(filterType)")
                        }
                    }
                    .padding(.horizontal) //horizontal padding
                    
                }//header elements
                
                ScrollView(.vertical, showsIndicators: true){
                    EventList(parentView: parentView, selectedEvent: -1, sortOrder: sortOrder, filterType: filterType, filter: filter, filterToday: false)
                    
                }//scroll view
                .padding(.horizontal)
                
            }//navigation stack
            .edgesIgnoringSafeArea(.top)
        }//geometry reader
        
    }//body
}
