//
//  CalendarView.swift
//  GetGrinnected
//
//  Created by Ellie Seehorn on 3/4/25.
//  Edited by Budhil Thijm on 04/18/25.

import Foundation
import SwiftUI

//Calendar view
struct HomeView: View {
    // the parent model used for updating our event list
    @StateObject private var parentView = EventListParentViewModel()
    @State private var searchText = ""
    @State private var isLoading = true
    // The type of filter we are using on the event list
    @State private var filterType: FilterType = .name
    @State private var sortOrder: SortOrder = .time
    
    //Main view body
    var body: some View {
        //geomtry view to have header
        GeometryReader{proxy in
            let safeAreaTop = proxy.safeAreaInsets.top
            
            //vstack of header and events
            NavigationStack{
                VStack(){
                    // Header is outside of scrollable so it does not move
                    Header(inputText: $searchText, safeAreaTop: safeAreaTop, title: "Home", searchBarOn: false)
                        .accessibilityHeading(.h1)
                        .accessibilityLabel("Home screen")
                    
                    //vertical scroll view to see more events
                    ScrollView(.vertical, showsIndicators: false){
                        //vstack to have some spacing between header and main compoments
                        VStack(spacing: 16) {
                            WeekView(selectedDate: $parentView.viewedDate)
                                .padding(.bottom, 4)
                            
                            HStack{
                                //sort by picker
                                Picker("Sort by", selection: $sortOrder) {
                                    ForEach(SortOrder.allCases) { sortOrder in
                                        Text("Sort by \(sortOrder.rawValue.capitalized)")
                                    }
                                }
                                .accessibilityLabel("Sort events by")
                                .accessibilityHint("Sorts events by \(sortOrder)")
                                .pickerStyle(.segmented)
                                
                                //tag selector
                                TagMultiSelector(title: "Tags", parentView: parentView)
                            }
                            .padding(.horizontal)
                            
                            //event list view for all the events (may have to pass in some arguments according to the day
                            EventList(parentView: parentView, selectedEvent: -1, sortOrder: sortOrder, filterType: filterType, filter: searchText, filterByDate: searchText.isEmpty)
                        }
                        .padding(.top)//padding on top
                    }
                    .refreshable {
                        //force refresh through the button!
                        parentView.forceRefresh() //only changes last change to nil
                    }
                } //scroll view
                .edgesIgnoringSafeArea(.top)
            }
            .navigationTitle("Calendar")
            .headerProminence(.increased)
            
        }//geometry reader
        .onAppear{
            //set initial date of the calendar.
            parentView.timeSpan.start = parentView.viewedDate
            parentView.timeSpan.end = parentView.viewedDate.startOfNextDay
            
            DispatchQueue.main.asyncAfter(deadline: .now() + 0.5) {
                parentView.forceRefresh() //add a small delay to ensure eventlist is fully initialized
            }
            
        }
        // if the viewedDate changes update timeSpan
        .onChange(of: parentView.viewedDate) { oldValue, newValue in
            // update start and end of time span when you are viewing a different day
            parentView.timeSpan.start = newValue
            parentView.timeSpan.end = newValue.startOfNextDay
        }
        .refreshable {
            parentView.forceRefresh()
        }
    }//main body view
}

#Preview() {
    HomeView()
}
