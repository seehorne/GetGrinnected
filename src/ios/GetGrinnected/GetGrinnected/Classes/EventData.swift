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
    
    // convert to URL and then convert with "parse"
    func getData() {
        //safely convert to url
        if let url = URL(string: urlString) {
            if let data = try? Data(contentsOf: url) {
                parse(json: data)
            }
        }//if
    }
    
    
    
    //parse function taken from the tutorial linked at the top
    func parse(json: Data) {
        let decoder = JSONDecoder()
        if let jsonEvents = try? decoder.decode (Events.self, from: json) {
            events = jsonEvents.data
        }
    }
    
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
                
            // Provide more detail about the specific decoding error
            if let decodingError = error as? DecodingError {
                switch decodingError {
                case .keyNotFound(let key, let context):
                    print(
                        "Missing key: \(key.stringValue) - \(context.debugDescription)"
                    )
                    return "Error: Missing key '\(key.stringValue)'"
                        
                case .typeMismatch(let type, let context):
                    print(
                        "Type mismatch: expected \(type) - \(context.debugDescription)"
                    )
                    return "Error: Type mismatch for '\(context.codingPath.last?.stringValue ?? "unknown field")'"
                        
                case .valueNotFound(let type, let context):
                    print(
                        "Value not found: expected \(type) - \(context.debugDescription)"
                    )
                    return "Error: Missing value for '\(context.codingPath.last?.stringValue ?? "unknown field")'"
                        
                case .dataCorrupted(let context):
                    print("Data corrupted: \(context.debugDescription)")
                    return "Error: JSON data is corrupted"
                        
                @unknown default:
                    print("Unknown decoding error")
                    return "Error: Unknown decoding issue"
                }
            }
                
            return "Error: \(error.localizedDescription)"
        }
    }

    
}

struct SampleView: View {
    var body: some View {
        let printString = EventData.decode(json: """
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
            }
        """)
        Text(printString)
    }
}


#Preview {
    SampleView()
}


