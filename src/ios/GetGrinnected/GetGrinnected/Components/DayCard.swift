//
//  DayCard.swift
//  GetGrinnected
//
//  Created by Budhil Thijm on 4/16/25.
//

import SwiftUI

struct dayCard: View{
    let date: Date
    let isSelected: Bool
    
    private var weekDayString: String{
        let formatter = DateFormatter()
        formatter.dateFormat = "EEE"
        return formatter.string(from: date).uppercased()
    }//deciding the day label (M-s/su)
    
    private var dayString: String {
        let formatter = DateFormatter()
        formatter.dateFormat = "dd"
        return formatter.string(from: date)
    }//Deciding the label M, T, W, T, F, S, SU
    
    var body: some View{
        VStack(spacing: 8){
            Text(weekDayString)
                .font(.caption)
                .fontWeight(isSelected ? .semibold : .regular)
            
            Text(dayString)
                .font(.caption)
                .fontWeight(isSelected ? .semibold : .regular)
        }
    }
}

#Preview{
    
}
