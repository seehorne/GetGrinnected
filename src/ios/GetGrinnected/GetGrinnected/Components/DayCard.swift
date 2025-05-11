//
//  CalendarView.swift
//  Homepage
//
//  Created by Samnang Aing on 2024-11-15.
//


import SwiftUI

/**
 Day View
 */
struct DayView: View {
    let date: Date // input a date
    let isSelected: Bool //boolean
    
    //set current calendar with calendar library
    //do get current date can simply run Date()
    private let calendar = Calendar.current //current is the current calendar, and day.
    
    //Set the weekday string through formatting the date.
    private var weekdayString: String {
        let formatter = DateFormatter()
        //takes the first three letters of each day
        formatter.dateFormat = "EEE"
        //return the date uppercased so the day looks ince.
        return formatter.string(from: date).uppercased()
    }
    
    
    //Day String
    private var dayString: String {
        //initialize formatter
        let formatter = DateFormatter()
        //One letter for the date will suffice
        //it will still contain the whole day
        //eg: March 15th --> 15
        formatter.dateFormat = "d"
        //Return the formatted date.
        return formatter.string(from: date)
    }
    
    //check if today
    private var isToday: Bool {
        //equal to whether or not the day is today.
        calendar.isDateInToday(date)
    }
    
    //same for weekend.
    private var isWeekend: Bool {
        let weekday = calendar.component(.weekday, from: date)
        //weekdays are 1 and 7.
        return weekday == 1 || weekday == 7
    }//weekend
    
    //text color.
    //This is what determines the color of the day card.
    private var textColor: Color {
        //if selected, we can make the text white
        if isSelected {
            return .white
        }
        //if the day is today, we make the color red
        if isToday {
            return .appLightBlue
        }
        //then if the color is weekend, we make the color
        // our primary color, and if not secondary.
        return .appTextPrimary
    }//color
    
    //Change border color according to if it's selected, or if the day card is today
    private var borderColor: Color {
        //if selected, return red
        if isSelected {
            return .appLightBlue
        }
        
        //if today, the app border will be black as opposed to our app's red color.
        return isToday ? .colorBlue : .appBorder
    }
    
    //Main body view
    var body: some View {
        //Vstack for the weekday and the day, spacing of 8 between two eleemnts
        VStack(spacing: 8) {
            //weekday string
            Text(weekdayString)
                .font(.caption)
                .fontWeight(isSelected ? .semibold : .regular) // if selected, make it bold! (this is an alternate way to make it bold as opposed to making a variable)
            
            //day string will be bold
            Text(dayString)
                .font(.title3) //make the day string somewhat big with .title3
                .fontWeight(isSelected ? .bold : .semibold) //make it bold
        }
        .frame(maxWidth: .infinity) //make each of the cards take up the most space it possibly can.
        .foregroundColor(textColor) //set the text color according to our above-defined values
        .padding(.vertical, 8) //padding all around 8.
        .background( //set background color
            ZStack {
                Color(isSelected ? .colorBlue : .appContainer) //if selected, set background to red
                RoundedRectangle(cornerRadius: 10) //create the background as a rectangle that matches the card
                    .stroke(borderColor, lineWidth: isToday ? 2 : 1) //according to the color.
            }
        )
        .cornerRadius(10) //set the corner radius equal to the rectangle
    }//body view
}// day card

#Preview{
    //set date as today.
    let date = Date()

    //add a date for every week
    let date1 = Calendar.current.date(byAdding: .day, value: 1, to: date)!
    let date2 = Calendar.current.date(byAdding: .day, value: 2, to: date)!
    let date3 = Calendar.current.date(byAdding: .day, value: 3, to: date)!
    let date4 = Calendar.current.date(byAdding: .day, value: 4, to: date)!
    let date5 = Calendar.current.date(byAdding: .day, value: 5, to: date)!
    let date6 = Calendar.current.date(byAdding: .day, value: 6, to: date)!
    
    //today's views
    HStack{
        //do a day view for every day
        DayView(date: date, isSelected: true)
        DayView(date: date1, isSelected: false)
        DayView(date: date2, isSelected: false)
        DayView(date: date3, isSelected: false)
        DayView(date: date4, isSelected: false)
        DayView(date: date5, isSelected: false)
        DayView(date: date6, isSelected: false)
    }//Hstack for horiontal viewing
    

}//preview
