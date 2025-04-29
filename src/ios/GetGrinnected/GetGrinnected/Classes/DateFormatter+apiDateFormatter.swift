//
//  DateFormatter+apiDateFormatter.swift
//  GetGrinnected
//
//  Created by Budhil Thijm on 4/25/25.
//
import Foundation

// Add to a utility file, can be used instead of creating a new formatter every time
extension DateFormatter {
    static let apiDateFormatter: DateFormatter = {
        let formatter = DateFormatter()
        formatter.locale = Locale(identifier: "en_US_POSIX")
        formatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
        return formatter
    }()
}

//// Then use it like this:
//if let startTimeString = dto.event_start_time {
//    self.startTime = DateFormatter.apiDateFormatter.date(from: startTimeString)
//}
