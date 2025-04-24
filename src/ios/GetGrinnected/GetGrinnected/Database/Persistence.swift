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
final class EventModel {
//    @Attribute
//    var lastUpdated: Date?//tell when the events were last updated so not updated too often
    var id: Int
    var name: String?
    //var description: String
    var date: String
    var location: String?
    var organizations: [String]
    var startTime: Date?
    var endTime: Date?
    var tags: [String]
    var imageURL: String?
    
    // Constructor that converts from your DTO
    init(from dto: EventDTO) {
        self.id = dto.eventid ?? 0
        self.name = dto.event_name ?? "Unnamed Event"
        self.date = dto.event_date ?? "Undated Event"
       // self.description = dto.event_description
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
        self.imageURL = dto.event_image!
        
    }//initialize from an eventDTO
    
    // Empty initializer required by SwiftData
    init() {
        self.id = 0
        self.name = ""
        self.location = ""
        self.organizations = []
        self.tags = []
        self.startTime = Date()
        self.endTime = Date()
        self.tags = []
        self.imageURL = ""
        self.date = ""
    }//empty initializer, required by SwiftData
    
}//Model
