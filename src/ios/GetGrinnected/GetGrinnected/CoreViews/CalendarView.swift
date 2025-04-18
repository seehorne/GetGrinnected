//
//  CalendarView.swift
//  GetGrinnected
//
//  Created by Ellie Seehorn on 3/4/25.
//  Edited by Budhil Thijm on 04/18/25.

import Foundation
import SwiftUI

struct CalendarView: View {
    @State private var isShowingEventModal = false
    @State private var selectedDate = Date()
    
    var body: some View {
        GeometryReader{proxy in
            let safeAreaTop = proxy.safeAreaInsets.top
            ScrollView(.vertical, showsIndicators: false){
                VStack(){
                    Header(safeAreaTop, title: "Calendar", searchBarOn: true)
                    
                    VStack(spacing: 16) {
                        
                        WeekView(selectedDate: $selectedDate)
                            .padding(.bottom, 4)
                        
                        EventListView()
                    }
                    .padding(.top)
                    
                }
                
            }
            .edgesIgnoringSafeArea(.top)
            
            
            
            
        }//geometry reader
    }
}




struct TimelineView: View {
    let hourSpacing: CGFloat
    //maybe don't need!
    var body: some View {
        VStack(alignment: .trailing, spacing: hourSpacing) {
            ForEach(0..<25) { hour in
                TimelineHourView(hour: hour)
            }
        }
        .padding(.bottom, 40)
    }
}//timeline view

struct TimelineHourView: View {
    let hour: Int
    
    private var timeString: String {
        String(format: "%d:00", hour == 0 ? 12 : (hour > 12 ? hour - 12 : hour))
    }
    
    private var amPm: String {
        hour < 12 ? "AM" : "PM"
    }
    
    var body: some View {
        VStack(alignment: .trailing) {
            Text(timeString)
            Text(amPm)
        }
        .font(.footnote)
        .fontWeight(.semibold)
        .foregroundColor(.appTextPrimary)
        .frame(height: 30)
        .id("hour-\(hour)")
    }
}//timeline hour view


struct CurrentTimeIndicator: View {
    @State private var currentTime = Date()
    let timer = Timer.publish(every: 60, on: .main, in: .common).autoconnect()
    
    private var timeString: String {
        let formatter = DateFormatter()
        formatter.dateFormat = "h:mm"
        return formatter.string(from: currentTime)
    }
    
    let hourSpacing: CGFloat
    private let timelineHourHeight: CGFloat = 30
    
    private var indicatorOffset: CGFloat {
        let calendar = Calendar.current
        let components = calendar.dateComponents([.hour, .minute], from: currentTime)
        let hour = CGFloat(components.hour ?? 0)
        let minute = CGFloat(components.minute ?? 0)
        
        return (hour + minute / 60) * (hourSpacing + timelineHourHeight)
    }
    
    var body: some View {
        HStack {
            ZStack(alignment: .top) {
                Rectangle()
                    .fill(Color.appBackground)
                    .frame(width: 4)
                    .cornerRadius(2)
                
                Rectangle()
                    .fill(Color.colorRed)
                    .frame(width: 4, height: indicatorOffset + 10)
                    .cornerRadius(2)
                
                ZStack {
                    RoundedRectangle(cornerRadius: 4)
                        .fill(.colorRed)
                        .frame(width: 35, height: 20)
                    
                    Text(timeString)
                        .font(.caption2)
                        .fontWeight(.semibold)
                        .foregroundColor(.appTextPrimary)
                }
                .offset(y: indicatorOffset)
            }
        }
        .onReceive(timer) { _ in
            currentTime = Date()
        }
    }
}//determine current time



#Preview("Light Mode") {
    CalendarView()
        .preferredColorScheme(.light)
}


#Preview("Dark Mode") {
    CalendarView()
        .preferredColorScheme(.dark)
}


#Preview {
    CalendarView()
}
