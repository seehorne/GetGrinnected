//
//  DateExtension.swift
//  GetGrinnected
//
//  Created by Michael Paulin on 4/21/25.
//

import Foundation

extension Date {
    /*
     Tells you when the start of the next day is.
     Referenced from https://stackoverflow.com/questions/48046204/milliseconds-until-midnight-local-time-in-swift
     */
    var startOfNextDay: Date {
        Calendar.current.nextDate(after: self, matching: DateComponents(hour: 0, minute: 0), matchingPolicy: .nextTimePreservingSmallerComponents)!
    }
    
    /*
     Tells you when the number of milliseconds until midnight.
     Referenced from https://stackoverflow.com/questions/48046204/milliseconds-until-midnight-local-time-in-swift
     */
    var millisecondsUntilTheNextDay: TimeInterval {
        startOfNextDay.timeIntervalSince(self) * 1000
    }
}
