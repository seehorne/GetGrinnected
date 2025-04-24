//
//  Persistence.swift
//  GetGrinnected
//
//  Created by Budhil Thijm on 4/23/25.
//


/**
 File for persistent states in our Application.
 Scraped from API, converted through this class.
 
 */

import SwiftData
import Foundation

@Model
final class EventModel: Identifiable {
//    @Attribute
//    var lastUpdated: Date?//tell when the events were last updated so not updated too often
    var id: Int
    var name: String
    var descr: String?
    var date: String?
    var location: String?
    var organizations: [String]?
    var startTime: Date?
    var endTime: Date?
    
    var rsvp: Int?
    var all_day: Int?
    var usefulStartTime: Date?
    var usefulEndTime: Date?
    var tags: [String]?
    var event_private: Int?
    var repeats: Int?
    var imageURL: String?
    var is_draft: Int?
    
    // Constructor that converts from your DTO
    init(from dto: EventDTO) {
        self.id = dto.eventid ?? 0
        self.name = dto.event_name ?? "Unnamed Event"
        self.date = dto.event_date ?? "Undated Event"
        self.descr = dto.event_description ?? "No Description"
        self.location = dto.event_location
        self.organizations = dto.organizations ?? []
        
        // Parse dates using the same formatter you're using
        let dateFormatter = DateFormatter()
        dateFormatter.locale = Locale(identifier: "en_US_POSIX")
        dateFormatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
        
        if let startTimeString = dto.event_start_time {
            self.startTime = dateFormatter.date(from: startTimeString)!
        } else {
            self.startTime = Date() // default to today
        }
        
        if let endTimeString = dto.event_end_time {
            self.endTime = dateFormatter.date(from: endTimeString)!
        } else {
            self.endTime = Date() // default to today
        }
        
        self.tags = dto.tags ?? []
        self.imageURL = dto.event_image ?? ""
        self.rsvp = dto.rsvp ?? 0
        self.all_day = dto.event_all_day ?? 0
        self.usefulStartTime = dto.useful_event_start_time ?? Date()
        self.usefulEndTime = dto.useful_event_end_time ?? Date()
        self.tags = dto.tags ?? []
        self.event_private = dto.event_private ?? 0
        self.repeats = dto.repeats ?? 0
        self.imageURL = dto.event_image ?? ""
        self.is_draft = dto.is_draft ?? 0
        
    }//initialize from an eventDTO
    
    // Empty initializer required by SwiftData
    init() {
        self.id = 0
        self.name = ""
        self.location = ""
        self.descr = ""
        self.organizations = []
        self.tags = []
        self.startTime = Date()
        self.endTime = Date()
        self.tags = []
        self.imageURL = ""
        self.date = ""
        self.rsvp = 0
        self.all_day = 0
        self.usefulStartTime = Date()
        self.usefulEndTime = Date()
        self.tags = []
        self.event_private = 0
        self.repeats = 0
        self.imageURL = ""
        self.is_draft = 0
        
        
        
        
        
    }//empty initializer, required by SwiftData
    
}//Model
