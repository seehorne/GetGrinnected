//
//  CalendarView.swift
//  GetGrinnected
//
//  Created by Ellie Seehorn on 3/4/25.
//  Edited by Budhil Thijm on 04/18/25.

import Foundation
import SwiftUI

//Calendar view
struct CalendarView: View {
    // the parent model used for updating our event list
    @StateObject private var viewModel = EventListParentViewModel()
    @State private var searchText = ""
    
    //Main view body
    var body: some View {
        //geomtry view to have header
        GeometryReader{proxy in
            //no safe area so the header goes all the way to the top
            //vstack of header and events
            NavigationStack{
                VStack(){
                    Spacer()
                    
                    //vertical scroll view to see more events
                    ScrollView(.vertical, showsIndicators: false){
                        //have search bar on here.
                        
                        
                        //vstack to have some spacing between header and main compoments
                        VStack(spacing: 16) {
                            
                            WeekView(selectedDate: $viewModel.viewedDate)
                                .padding(.bottom, 4)
                                .padding(.top, 120)
                            
                            //event list view for all the events (may have to pass in some arguments according to the day
                            EventList(selectedEvent: -1, parentView: viewModel, searchString: searchText, filterToday: searchText.isEmpty)
                        }
                        .padding(.top)//padding on top
                        
                    }
                    .searchable(text: $searchText, placement: .toolbar)
                    
                }//scroll view
                .edgesIgnoringSafeArea(.top)
            }
            .navigationTitle("Calendar")
            .headerProminence(.increased)
            
        }//geometry reader
        // if the viewedDate changes update timeSpan
        .onChange(of: viewModel.viewedDate) { oldValue, newValue in
            // update start and end of time span when you are viewing a different day
            viewModel.timeSpan.start = newValue
            viewModel.timeSpan.end = newValue.startOfNextDay
        }
    }//main body view
}





#Preview() {
    CalendarView()
}
