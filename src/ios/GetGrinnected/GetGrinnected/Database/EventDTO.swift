//
//  EventDTO.swift
//  GetGrinnected
//
//  Created by Budhil Thijm on 4/8/25.
//

import Foundation
import SwiftData

/**
 This struct is meant to decode the information from a JSON file with the example (commented on the bottom of this file)
 
 The codable protocol allows for encoding and decoding (and this one specifically for JSONs, especially the codingkey.
 This is used to take the JSON string from the API and deconstruct many events, in the form of [Event] to deconstruct the entire JSON
 */
struct EventDTO: Codable {
    //? type is an optional type. If scraping finds a null value, the value may remain null.
    var eventid: Int?
    var event_name: String?
    var event_description: String?
    var event_location: String?
    var organizations: [String]?
    var rsvp: Int?
    var event_date: String?
    var event_time: String?
    var event_all_day: Int?
    var event_start_time: String?
    var useful_event_start_time: Date? {
        guard let timeString = event_start_time else { return nil }
        let dateFormatter = DateFormatter()
        dateFormatter.locale = Locale(identifier: "en_US_POSIX")
        dateFormatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
        return dateFormatter.date(from: timeString)
    } // referenced from https://stackoverflow.com/questions/36861732/convert-string-to-date-in-swift
    var event_end_time: String?
    var useful_event_end_time: Date? {
        guard let timeString = event_end_time else { return nil }
        let dateFormatter = DateFormatter()
        dateFormatter.locale = Locale(identifier: "en_US_POSIX")
        dateFormatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
        return dateFormatter.date(from: timeString)
    }
    var tags: [String]?
    var event_private: Int?
    var repeats: Int?
    var event_image: String?
    var is_draft: Int?
    
    
    /**
     A new feature in swift allows us to match the event names exactly as we see fit.
     So, if in the JSON there's a title of an event, You can match that here.
     I don't necessarily have to have all the fields filled out below in this CodingKeys struct, however it's helpful. 
     */
    enum CodingKeys: String, CodingKey {
        case eventid = "eventid"
        case event_name = "event_name"
        case event_description = "event_description"
        case event_location = "event_location"
        case organizations = "organizations"
        case rsvp = "rsvp"
        case event_date = "event_date"
        case event_time = "event_time"
        case event_all_day = "event_all_day"
        case event_start_time = "event_start_time"
        case event_end_time = "event_end_time"
        case tags = "tags"
        case event_private = "event_private"
        case repeats = "repeats"
        case event_image = "event_image"
        case is_draft = "is_draft"
    }
    
}

//Example Event in JSON

/**
 
 [
   {
     "eventid": 28273,
     "event_name": "SGA Concert",
     "event_description": "No description available",
     "event_location": "Main Hall Gardner Lounge",
     "organizations": [
       "Sga Concerts"
     ],
     "rsvp": 0,
     "event_date": "April 9",
     "event_time": "7 p.m. - 10 p.m.",
     "event_all_day": 0,
     "event_start_time": "2025-04-10T00:00:00.000Z",
     "event_end_time": "2025-04-10T03:00:00.000Z",
     "tags": [
       "Music",
       "Student Activity",
       "Alumni",
       "Faculty &amp; Staff",
       "General Public",
       "Prospective Students",
       "Student Families",
       "Students"
     ],
     "event_private": 0,
     "repeats": 0,
     "event_image": null,
     "is_draft": 0
   },
   {
     "eventid": 30810,
     "event_name": "Concerts",
     "event_description": "\n  Tabling for Starcleaner Reunion\n",
     "event_location": "Rosenfield Center 1st Floor Lobby - Table 4",
     "organizations": [
       "Sga Concerts"
     ],
     "rsvp": 0,
     "event_date": "April 8",
     "event_time": "11 a.m. - 1 p.m.",
     "event_all_day": 0,
     "event_start_time": "2025-04-08T16:00:00.000Z",
     "event_end_time": "2025-04-08T18:00:00.000Z",
     "tags": [
       "Music",
       "Student Activity",
       "Students"
     ],
     "event_private": 0,
     "repeats": 0,
     "event_image": null,
     "is_draft": 0
   }
 ]
 
 */
