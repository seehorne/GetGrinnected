//
//  CalendarView.swift
//  Homepage
//
//  Created by Samnang Aing on 2024-11-15.
//


import SwiftUI

struct DayView: View {
    let date: Date
    let isSelected: Bool
    
    private let calendar = Calendar.current
    
    private var weekdayString: String {
        let formatter = DateFormatter()
        formatter.dateFormat = "EEE"
        return formatter.string(from: date).uppercased()
    }
    
    private var dayString: String {
        let formatter = DateFormatter()
        formatter.dateFormat = "d"
        return formatter.string(from: date)
    }
    
    private var isToday: Bool {
        calendar.isDateInToday(date)
    }
    
    private var isWeekend: Bool {
        let weekday = calendar.component(.weekday, from: date)
        return weekday == 1 || weekday == 7
    }
    
    private var textColor: Color {
        if isSelected {
            return .white
        }
        if isToday {
            return .colorRed
        }
        return isWeekend ? .appTextPrimary : .appTextSecondary
    }
    
    private var borderColor: Color {
        if isSelected {
            return .colorRed
        }
        return isToday ? .colorRed : .appBorder
    }
    
    var body: some View {
        VStack(spacing: 8) {
            Text(weekdayString)
                .font(.caption)
                .fontWeight(isSelected ? .semibold : .regular)
            
            Text(dayString)
                .font(.title3)
                .fontWeight(isSelected ? .bold : .semibold)
        }
        .frame(maxWidth: .infinity)
        .foregroundColor(textColor)
        .padding(.vertical, 8)
        .background(
            ZStack {
                Color(isSelected ? .colorRed : .appContainer)
                RoundedRectangle(cornerRadius: 10)
                    .stroke(borderColor, lineWidth: isToday ? 2 : 1)
            }
        )
        .cornerRadius(10)
    }
}
