//
//  EventModel.swift
//  GetGrinnected
//
//  Created by Budhil Thijm on 4/23/25.
//



import SwiftData
import Foundation


/**
 File for persistent states in our Application.
 Scraped from API, converted through this class.
 
 
 @Model attribute shows that this is what is going to be saved
 */
@Model
final class EventModel: Hashable { // hashable for uniqueness
    @Attribute(.unique) var id: Int //attribute that is unique to prevent duplicates..
    var name: String //required!, shared a default value
    var descr: String?
    var date: String?
    var location: String?
    var startTime: Date?
    var endTime: Date?
    var rsvp: Int?
    var all_day: Int?
    var event_private: Int?
    var repeats: Int?
    var imageURL: String?
    var is_draft: Int?

    
    // external storage of tags, this is what we are using arraytransformer on
    @Attribute(.externalStorage)
    var tags: [String] = []
    
    // external storage of organizations, this is what we are using arraytransformer on
    @Attribute(.externalStorage)
    var organizations: [String] = []
    
    // non-eventDTO attributes, will be noted here, and pre-populated
    var favorited: Bool = false
    var lastUpdated: Date = Date() //set last updated date to today.

    // UI-only flags (not persisted in SwiftData)
    @Transient var notified: Bool = false

    //initialization from a DTO
    init(from dto: EventDTO) {
        //if they exist, set it as that dto attribute, if not, set it to empty
        self.id = dto.eventid ?? 0
        self.name = dto.event_name ?? ""
        
        // optional type values do not need to be initialized with ?? in case that field is empty in the DTO
        self.date = dto.event_date
        self.descr = dto.event_description
        self.location = dto.event_location
        self.tags = dto.tags ?? []
        self.organizations = dto.organizations ?? []

        //creating a formatter (coudld use the extension that we wrote..
        let formatter = DateFormatter()
        formatter.locale = Locale(identifier: "en_US_POSIX")
        formatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"

        self.startTime = dto.event_start_time.flatMap { formatter.date(from: $0) }
        self.endTime = dto.event_end_time.flatMap { formatter.date(from: $0) }

        self.imageURL = dto.event_image
        self.rsvp = dto.rsvp
        self.all_day = dto.event_all_day
        self.event_private = dto.event_private
        self.repeats = dto.repeats
        self.is_draft = dto.is_draft
        self.favorited = false
        self.lastUpdated = Date()
//        self.isSelected = false
        self.notified = false
    }

    // Required by SwiftData
    // a basic initializer for any non-optional attributes
    init() {
        self.id = 0
        self.name = ""
//        self.isSelected = false
        self.notified = false
    }
    
    //hasher function that only takes into account the id
    func hash(into hasher: inout Hasher) {
        hasher.combine(id)
    }
    //a function that compares, (helps arraytransformer)
    static func ==(lhs: EventModel, rhs: EventModel) -> Bool {
        return lhs.id == rhs.id
    }
}
