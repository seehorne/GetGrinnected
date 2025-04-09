//
//  EventData.swift
//  GetGrinnected
//
//  Created by Budhil Thijm on 4/8/25.
//
import SwiftUI

/**
 Guides for decoding:
 - https://codewithchris.com/codable/
 
 Class for making API call, converting JSON to Event Struct, to be turned into a UI component in EventCards.swift
 
 As of 2025.04.08, no API call. just a string declaration
 
 */


class EventData{
    //initialize array of events
    var events = [Event]()
    //our API link
    let urlString = "https://node16049-csc324--spring2025.us.reclaim.cloud/"

    
    static func fetchData(urlString: String) async throws -> String {
        let url = URL(string: urlString)!
        
        do {
            let (data, _) = try await URLSession.shared.data(from: url)
            return String(data: data, encoding: .utf8)!
        } catch {
            print("Data fetching error: \(error)")
            return "Error: \(error.localizedDescription)"
        }
    }//fetchData
    
    static func decode(json: String) -> String{
        /// Then convert it to Data so we can decode it using
        /// JSONDecoder
        
        guard let data = json.data(using: .utf8) else {
            print("Failed to convert JSON string to data")
            return "Error: JSON string conversion failed"
        }
        /// This is the JSONDecoder instance we'll use to
        /// convert the JSON string into the model struct
        let decoder = JSONDecoder()
        
        do {
            // Attempt to decode the JSON data into an Event object
            let myEventData = try decoder.decode(Event.self, from: data)
            print("Successfully decoded event: \(myEventData.event_name)")
            return myEventData.event_name
        } catch {
            // Enhanced error handling for better debugging
            print("Decoding error: \(error)")
            return "Error: \(error.localizedDescription)"
        }
    }//decode
    
    static func parseEvents(json: String) -> [Event] {

        
        // Convert string to data
        guard let data = json.data(using: .utf8) else {
            print("Failed to convert JSON string to data")
            return []
        }
        let decoder = JSONDecoder()
        
        do {
            // Decode to Events struct which contains an array of Event
            let events = try decoder.decode([Event].self, from: data)
            print("Successfully decoded \(events.count) events")
            return events
        } catch{
            // Enhanced error handling
            print("Decoding error: \(error)")
            
            if let decodingError = error as? DecodingError {
                switch decodingError {
                case .keyNotFound(let key, let context):
                    print("Missing key: \(key.stringValue) - \(context.debugDescription)")
                    
                case .typeMismatch(let type, let context):
                    print("Type mismatch: expected \(type) - \(context.debugDescription)")
                    
                case .valueNotFound(let type, let context):
                    print("Value not found: expected \(type) - \(context.debugDescription)")
                    
                case .dataCorrupted(let context):
                    print("Data corrupted: \(context.debugDescription)")
                    
                @unknown default:
                    print("Unknown decoding error")
                }
            }
            return []
        }
        
    }

    
}

struct SampleView: View {
    var body: some View {
//        let printString = EventData.decode(json: """
//            {
//              "eventid": 28273,
//              "event_name": "SGA Concert",
//              "event_description": "No description available",
//              "event_location": "Main Hall Gardner Lounge",
//              "organizations": [
//                "Sga Concerts"
//              ],
//              "rsvp": 0,
//              "event_date": "April 9",
//              "event_time": "7 p.m. - 10 p.m.",
//              "event_all_day": 0,
//              "event_start_time": "2025-04-10T00:00:00.000Z",
//              "event_end_time": "2025-04-10T03:00:00.000Z",
//              "tags": [
//                "Music",
//                "Student Activity",
//                "Alumni",
//                "Faculty &amp; Staff",
//                "General Public",
//                "Prospective Students",
//                "Student Families",
//                "Students"
//              ],
//              "event_private": 0,
//              "repeats": 0,
//              "event_image": null,
//              "is_draft": 0
//            }
//""")
        
        
    let  myjson = "[{\"eventid\":28273,\"event_name\":\"SGA Concert\",\"event_description\":\"No description available\",\"event_location\":\"Main Hall Gardner Lounge\",\"organizations\":[\"Sga Concerts\"],\"rsvp\":0,\"event_date\":\"April 9\",\"event_time\":\"7 p.m. - 10 p.m.\",\"event_all_day\":0,\"event_start_time\":\"2025-04-10T00:00:00.000Z\",\"event_end_time\":\"2025-04-10T03:00:00.000Z\",\"tags\":[\"Music\",\"Student Activity\",\"Alumni\",\"Faculty &amp; Staff\",\"General Public\",\"Prospective Students\",\"Student Families\",\"Students\"],\"event_private\":0,\"repeats\":0,\"event_image\":null,\"is_draft\":0},{\"eventid\":30810,\"event_name\":\"Concerts\",\"event_description\":\"\\n  Tabling for Starcleaner Reunion\\n\",\"event_location\":\"Rosenfield Center 1st Floor Lobby - Table 4\",\"organizations\":[\"Sga Concerts\"],\"rsvp\":0,\"event_date\":\"April 8\",\"event_time\":\"11 a.m. - 1 p.m.\",\"event_all_day\":0,\"event_start_time\":\"2025-04-08T16:00:00.000Z\",\"event_end_time\":\"2025-04-08T18:00:00.000Z\",\"tags\":[\"Music\",\"Student Activity\",\"Students\"],\"event_private\":0,\"repeats\":0,\"event_image\":null,\"is_draft\":0}]"
        let myEvents = EventData.parseEvents(json: myjson)
        

        Text("myEvents: \(myEvents)")
        Text("Name: \(myEvents[0].event_name)")
    }
}


#Preview {
    SampleView()
}


