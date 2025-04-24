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
    @StateObject private var viewModel = ParentViewModel()
    
    //Main view body
    var body: some View {
        //geomtry view to have header
        GeometryReader{proxy in
            //no safe area so the header goes all the way to the top
            let safeAreaTop = proxy.safeAreaInsets.top
            //vstack of header and events
            VStack(){
                // Header is outside of scrollable so it does not move
                Header(safeAreaTop, title: "Calendar", searchBarOn: true)
                
                //vertical scroll view to see more events
                ScrollView(.vertical, showsIndicators: false){
                    //have search bar on here.
                    
                    
                    //vstack to have some spacing between header and main compoments
                    VStack(spacing: 16) {
                        
                        WeekView(selectedDate: $viewModel.viewedDate)
                            .padding(.bottom, 4)
                        
                        //event list view for all the events (may have to pass in some arguments according to the day
                        EventListView(parentView: viewModel)
                    }
                    .padding(.top)//padding on top
                    
                }
                
            }//scroll view
            .edgesIgnoringSafeArea(.top)
            
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
