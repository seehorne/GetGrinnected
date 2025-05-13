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
    
    
    //for date picker
    @State private var startDate = Date()
    @State private var endDate = Date()
    
    var body: some View{
        GeometryReader{proxy in
            let safeAreaTop = proxy.safeAreaInsets.top
            VStack{ //contains pickers
                //searchbar
                Header(safeAreaTop: safeAreaTop, title: "Search", searchBarOn: false)
                
                NavigationView {
                    //header elements
                    VStack{
                        //input for searching
                        /**
                         Search by picker..
                         */
                        Picker("Search by", selection: $filterType) {
                            ForEach(FilterType.allCases) { filterType in
                                Text("Search by \(filterType)")
                            }
                        }
                        .pickerStyle(.segmented)
                        .padding(.horizontal) //horizontal padding
                        
                        //input view for searching
                        Text("Search here:")
                            .foregroundStyle(.textSecondary)
                            .padding(.horizontal)
                            .padding(.top)
                        TextField("Search by \(filterType.rawValue.capitalized)", text: $filter)
                            .font(.callout)
                            .autocapitalization(.none)
                            .padding()//for general text to be padded in the background
                            .padding(.horizontal)//for the text field to not at the edge
                            .background(
                                RoundedRectangle(cornerRadius: 20, style: .circular)
                                    .fill(.gray.opacity(0.175))//opacity to be similar to swift native
                                    .padding(.horizontal) //for the rectangle to be not at the edge
                            )
                        
                        /**
                         Date pickers
                         */
                        Text("Select Date")
                            .foregroundStyle(.textSecondary)
                            .padding(.horizontal)
                            .padding(.top)
                        DatePicker(
                            "Start Date",
                            selection: $startDate,
                             displayedComponents: [.date, .hourAndMinute]
                        )
                        .padding()
                        .onChange(of: startDate) {
                            // Update parentView timeSpan start
                            parentView.timeSpan.start = startDate
                            
                            // Ensure end date is not before new start date
                            if endDate < startDate {
                                endDate = startDate
                            }
                        }//on change
                        
                        //set date end-range
                        DatePicker(
                            "End Date",
                            selection: $endDate,
                            //24 * 60 * 60  = 24 hours, 60 minutes, 60 seconds
                            in:  startDate.addingTimeInterval(24 * 60 * 60)...,
                            displayedComponents: [.date, .hourAndMinute]
                        )
                        .padding()
                        .onChange(of: endDate) {
                            // Update parentView timeSpan end
                            parentView.timeSpan.end = endDate
                        }//onchange
                        
                        

                        NavigationLink(destination:  SearchResults(parentView: parentView, selectedEvent: selectedEvent, isLoading: isLoading, refreshTimer: refreshTimer, sortOrder: $sortOrder, filterType: $filterType, filter: $filter)) {
                            HStack(){
                                Text("\(Image(systemName: "magnifyingglass")) Search")
                                    .font(.headline)
                                    .foregroundStyle(Color.white)
                                    .padding()
                                    .background(
                                        RoundedRectangle(cornerRadius: 8)
                                            .fill(Color.appBlue)
                                            .overlay(
                                                RoundedRectangle(cornerRadius: 8)
                                                    .stroke(Color.appBorder, lineWidth: 1)
                                            )
                                    )//nice background for searching
                            }
                        } //link to search, search through here
                        Spacer()
                    }
                }//navigation view
                
            } //navigation stack
            .edgesIgnoringSafeArea(.top)
        }//geometry reader
        
    }//body
}

struct SearchResults: View {
    
    //parentview that observes values if change
    @ObservedObject var parentView: EventListParentViewModel
    @State private var selectedEvent: Int?
    @State private var isLoading = false //set loading states
    @State private var refreshTimer: Timer? //a timer to count when we refresh
    
    
    //Sorting and Filtering parameters
    @Binding private var sortOrder: SortOrder
    @Binding private var filterType: FilterType
    @Binding private var filter: String
    
    init(parentView: EventListParentViewModel, selectedEvent: Int? = nil, isLoading: Bool = false, refreshTimer: Timer? = nil, sortOrder: Binding<SortOrder>, filterType: Binding<FilterType>, filter: Binding<String>) {
        self.parentView = parentView
        self.selectedEvent = selectedEvent
        self.isLoading = isLoading
        self.refreshTimer = refreshTimer
        self._sortOrder = sortOrder
        self._filterType = filterType
        self._filter = filter
    }
    
    var body: some View {
        VStack{
            //main view
            ScrollView(.vertical, showsIndicators: true){
                //selectors
                HStack{
                    //creating a selector of sort order
                    Picker("Sort by", selection: $sortOrder) {
                        ForEach(SortOrder.allCases) { sortOrder in
                            Text("Sort by \(sortOrder.rawValue.capitalized)")
                        }
                    }
                    .pickerStyle(.segmented)
                    
                    TagMultiSelector(title: "Select Tags", parentView: parentView)
                    
                }//header elements
                .padding()
                
                //eventlist
                EventList(parentView: parentView, selectedEvent: -1, sortOrder: sortOrder, filterType: filterType, filter: filter, filterByDate: true)
                
            }//scroll view
            .onAppear{
                // refresh the events so we remove old ones and refresh the tags
                DispatchQueue.main.asyncAfter(deadline: .now() + 0.5) {
                    parentView.forceRefresh() //add a small delay to ensure eventlist is fully initialized
                }
            }
            .padding(.horizontal)
        }
        
    }
}
