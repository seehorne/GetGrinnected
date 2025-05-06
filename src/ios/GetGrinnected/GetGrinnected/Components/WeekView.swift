//
//  WeekView.swift
//
//  Created by Samnang Aing on 2024-11-15.
//  Edited by Budhil Thijm on 2025.04.18

import SwiftUI

/**
 These components are taken from the following tutorial
 https://www.youtube.com/watch?v=RvTPVFFzYvg&ab_channel=DesignCode
 
 Edited according to our needs
 
 */


///Week row view takes a day view and places it into a week,
///changes based on the base date
struct WeekRowView: View {
    //base date as input
    let baseDate: Date
    
    //selected date to show which one is selectable, inputting into dayview
    @Binding var selectedDate: Date
    
    //set a calendar
    private let calendar = Calendar.current
    
    
    //set every week date.
    //making a [Date] (array of dates)
    private var datesForWeek: [Date] {
        (0..<7).compactMap { index in
            //for every i until 7..
            //add a the next day (byAdding adds the next day, so we add 1
            calendar.date(byAdding: .day, value: index, to: baseDate)
        }
    }
    
    //Body main view
    var body: some View {
        //Spacing for horizontal stack
        HStack(spacing: 8) {
            //for every date in the week, since 1970..
            ForEach(datesForWeek, id: \.timeIntervalSince1970) { date in
                //we set the single day view according to the day..
                DayView(
                    date: date,//this is how we go forward in days
                    isSelected: calendar.isDate(date, inSameDayAs: selectedDate)
                )
                .onTapGesture {//on tap, we change the selected date to that day that is selected (HERE IS WHERE THE TAP LOGIC IS)
                    selectedDate = date
                }//tap gesture
            }//for every
        }//Hstack
    }//body
}//WeekRowView

//WeekView
struct WeekView: View {
    //The selected date
    @Binding var selectedDate: Date
    //Show or not to show date picker
    @State private var showDatePicker = false
    //The week offset changes the base date!
    @State private var weekOffset = 0
    
    //Set calendar to today's calendar
    private let calendar = Calendar.current
    
    //week count at an arbitrary 301 weeks
    private let weekCount = 301
    // Set the center to 150.
    private let centerIndex = 150
    
    //start week
    private var currentWeekStart: Date {
        //setting start week as todays's!
        calendar.date(from: calendar.dateComponents([.yearForWeekOfYear, .weekOfYear], from: Date()))!
        //from the Date (current day), get the year for week of year components
    }
    
    //get day for week
    private func getDateForWeek(_ offset: Int) -> Date {
        //based on the currentweek start, we can get the date of a week
        calendar.date(byAdding: .weekOfYear, value: offset, to: currentWeekStart)!
    }
    
    //date displayed week
    private func isDateInDisplayedWeek(_ date: Date, for offset: Int) -> Bool {
        //get the displayed week based on the offset
        let displayedWeek = getDateForWeek(offset)
        //returns the date
        return calendar.isDate(date, equalTo: displayedWeek, toGranularity: .weekOfYear)
    }
    
    //month year string function returns the string of the date
    private func monthYearString(for date: Date) -> String {
        //set a date formatter
        let formatter = DateFormatter()
        //set the format
        formatter.dateFormat = "MMMM yyyy"
        
        // If we're in the displayed week, use selected date
        if isDateInDisplayedWeek(selectedDate, for: weekOffset) {
            //return the string
            return formatter.string(from: selectedDate)
        }
        
        //toherwise just return the date.
        return formatter.string(from: date)
    }
    
    
    //main view
    var body: some View {
        //Vstack for vertical stacking
        VStack(alignment: .leading, spacing: 16) {
            //horizontal
            HStack(spacing: 16) {
                //the week
                Text("\(monthYearString(for: getDateForWeek(weekOffset)))")
                    .font(.title3)
                    .fontWeight(.bold)
                    .foregroundColor(.appTextPrimary)
                
                // pulldown for the week
                Image(systemName: "chevron.down")
                    .foregroundColor(.appTextSecondary)
                    //.frame(width: 40, height: 40)
                    .imageScale(.large)
                
                //a spacer for aesthetics
                Spacer()
                
                //Button that moves thew eek back to today
                Button(action: {
                    //animates the week so it goes back to t
                    withAnimation {
                        //week offset to 0 to make the date today
                        weekOffset = 0
                        selectedDate = Date()
                    }//animation
                }) {
                    
                    /**
                     The UI visuals of the button
                     */
                    //HStack
                    HStack(spacing: 4) {
                        //calendar image
                        Image(systemName: "calendar")
                            .font(.system(size: 12))
                        //A text that says today so it visually indicates you can jump to today
                        Text("Today")
                            .font(.subheadline)
                    }
                    .padding(8)//padding
                    .background( //background color as app container
                        ZStack {
                            Color.appContainer
                            RoundedRectangle(cornerRadius: 8)
                                .stroke(Color.appBorder, lineWidth: 1)
                        }
                    )//bacgkground
                    .foregroundColor(.appTextPrimary)
                    .cornerRadius(8)
                }
                // COMBINE MULTIPLE EFFECT
                .opacity(isDateInDisplayedWeek(Date(), for: weekOffset) ? 0 : 1)// If we are in currentw eek, will change color
                .offset(y: isDateInDisplayedWeek(Date(), for: weekOffset) ? 20 : 0) // will move offscreen be present we are in current week
                .scaleEffect(isDateInDisplayedWeek(Date(), for: weekOffset) ? 0.9 : 1)
                .animation(.easeInOut(duration: 0.3), value: isDateInDisplayedWeek(Date(), for: weekOffset)) //animation to show up and disappear
            }
            .padding(.leading, 8)
            .onTapGesture { //when you tap, will change date picker
                showDatePicker = true
            }
            .overlay { //overlay over nothign because we are not sure if this date picker will exist or not
                if showDatePicker {
                    DatePicker( //if it does exist, input these
                        "Test",
                        selection: $selectedDate,
                        displayedComponents: .date
                    )
                    .labelsHidden()
                    .datePickerStyle(.compact)
                    .accentColor(.blue)
                    .onChange(of: selectedDate) { _, _ in
                        showDatePicker = false
                    }
                    .blendMode(.destinationOver)
                }
            }
            
            /**
             THE ACTUAL WEEK VIEW
             */
            //within the Vstack, this is the lower row that contains all the days
            TabView(selection: $weekOffset) { //a tab view with the week offset
                ForEach(-centerIndex..<centerIndex, id: \.self) { offset in //Before and after the offset..
                    VStack {//add another week row view
                        WeekRowView(
                            baseDate: getDateForWeek(offset), //set day based on the offset
                            selectedDate: $selectedDate //show the selected date, adjusts based on offset.
                        )
                        .padding(.horizontal, 4)// adds padding
                    }
                    .tag(offset)
                }
            }
            .tabViewStyle(.page(indexDisplayMode: .never)) //
            .frame(height: 70) //set the frame to 70
        }
        .padding(.horizontal) //horizontal padding
        .onChange(of: selectedDate) { _, newDate in //when you change the week..
            //set new start of week
            let startOfNewDateWeek = calendar.date(from: calendar.dateComponents([.yearForWeekOfYear, .weekOfYear], from: newDate))!
            
            //if we are not in the current week..
            if startOfNewDateWeek != getDateForWeek(weekOffset) {
                //get the new offset
                let newOffset = calendar.dateComponents([.weekOfYear], from: currentWeekStart, to: startOfNewDateWeek).weekOfYear ?? 0
                
                withAnimation {
                    weekOffset = newOffset
                }
            }
        }//on change
    }//vstack
}//Weekview

#Preview {
    @Previewable @StateObject var viewModel = EventListParentViewModel()
    
    WeekView(selectedDate: $viewModel.viewedDate)
}
