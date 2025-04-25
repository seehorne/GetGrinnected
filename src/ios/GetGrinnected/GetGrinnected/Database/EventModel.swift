//
//  EventModel.swift
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
final class EventModel: Hashable {
    var id: Int
    var name: String?
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

    @Attribute(.transformable(by: ArrayTransformer.self))
    var tags: [String]

    @Attribute(.transformable(by: ArrayTransformer.self))
    var organizations: [String]

    var favorited: Bool
    var lastUpdated: Date

    // UI-only flags (not persisted in SwiftData)
    @Transient var notified: Bool = false
    @Transient var isSelected: Bool = false

    init(from dto: EventDTO) {
        self.id = dto.eventid ?? 0
        self.name = dto.event_name
        self.date = dto.event_date
        self.descr = dto.event_description
        self.location = dto.event_location
        self.tags = dto.tags ?? []
        self.organizations = dto.organizations ?? []

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
    }

    // Required by SwiftData
    init() {
        self.id = 0
        self.name = nil
        self.descr = nil
        self.date = nil
        self.location = nil
        self.startTime = nil
        self.endTime = nil
        self.rsvp = nil
        self.all_day = nil
        self.event_private = nil
        self.repeats = nil
        self.imageURL = nil
        self.is_draft = nil
        self.tags = []
        self.organizations = []
        self.favorited = false
        self.lastUpdated = Date()
    }

    func hash(into hasher: inout Hasher) {
        hasher.combine(id)
    }

    static func ==(lhs: EventModel, rhs: EventModel) -> Bool {
        return lhs.id == rhs.id
    }
}
