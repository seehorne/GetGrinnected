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
    //Show selected date
    @State private var selectedDate = Date()
    
    //Main view body
    var body: some View {
        //geomtry view to have header
        GeometryReader{proxy in
            //no safe area so the header goes all the way to the top
            let safeAreaTop = proxy.safeAreaInsets.top
            //vertical scroll view to see mroe events
            ScrollView(.vertical, showsIndicators: false){
                //vstack of header and events
                VStack(){
                    //have search bar on here.
                    Header(safeAreaTop, title: "Calendar", searchBarOn: true)
                    
                    //vstack to have some spacing between header and main compoments
                    VStack(spacing: 16) {
                        
                        WeekView(selectedDate: $selectedDate)
                            .padding(.bottom, 4)
                        
                        //event list view for all the events (may have to pass in some arguments according to the day
                        EventListView(timeSpan: DateInterval(start: selectedDate, end: selectedDate.startOfNextDay))
                    }
                    .padding(.top)//padding on top
                    
                }
                
            }//scroll view
            .edgesIgnoringSafeArea(.top)
            
        }//geometry reader
    }//main body view
}





#Preview("Light Mode") {
    CalendarView()
        .preferredColorScheme(.light)
}


#Preview("Dark Mode") {
    CalendarView()
        .preferredColorScheme(.dark)
}
