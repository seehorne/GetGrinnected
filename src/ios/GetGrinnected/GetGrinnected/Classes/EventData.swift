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



/**
 End to end  implementation file!
 
 This is where we connect to the API to get the JSONS. The JSONS are then taken and turned into an array of [Event]
 This allows us to use the EventCards functionality to transform our event struct into a UI component from the scraped data!
 */
class EventData{
    
    /**
     fetchData Function, intakes the URL (of our API), and releases a string of all the JSON.
     may be useful for other APIs.
     */
    static func fetchData(urlString: String) async throws -> String {
        //check if input can be converted into URL
        let url = URL(string: urlString)!
        
        do {
            //then, waits for the information from the data to come in.
            let (data, _) = try await URLSession.shared.data(from: url)
            //returns the string, encoded into .utf8, and I am not sure what the ! does but should be a simple google search..
            return String(data: data, encoding: .utf8)!
        } catch {
            //if there's an error in getting this data, then we throw out the error!
            print("Data fetching error: \(error)")
            return "Error: \(error.localizedDescription)"
        }
    }//fetchData
    
    /**
     Decodes a string and turns it into a single event.
     This is used in the parseEvents function to parse multiple events
     */
    static func decode(json: String) -> String{
        // Then convert it to Data so we can decode it using
        // JSONDecoder
        
        guard let data = json.data(using: .utf8) else {
            print("Failed to convert JSON string to data")
            return "Error: JSON string conversion failed"
        }
        // This is the JSONDecoder instance we'll use to
        // convert the JSON string into the model struct
        let decoder = JSONDecoder()
        
        do {
            // Attempt to decode the JSON data into an Event object
            let myEventData = try decoder.decode(EventDTO.self, from: data)
            if(myEventData.event_name != nil){
                print("Successfully decoded event: \(myEventData.event_name!)")
            }
            return myEventData.event_name!
        } catch {
            // Enhanced error handling for better debugging
            print("Decoding error: \(error)")
            return "Error: \(error.localizedDescription)"
        }
    }//decode
    
    /**
     parseEvents function
     parses all events in the string, and returns an array of Events.
     This can be then turned into UI components using the EventCards function
     */
    static func parseEvents(json: String) -> [EventDTO] {
        
        // Convert string to data
        guard let data = json.data(using: .utf8) else {
            print("Failed to convert JSON string to data")
            return []
        }
        let decoder = JSONDecoder()
        
        do {
            // Decode to Events struct which contains an array of Event
            let events = try decoder.decode([EventDTO].self, from: data)
            print("Successfully decoded \(events.count) events")
            return events
        } catch{
            // Enhanced error handling
            print("Decoding error: \(error)")
            
            //more specific Error handling.
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
        }//do/catch
        
    }//parseEvents

    
}

struct SampleView: View {
    var body: some View {
        
//testing code from before..
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
        
        //current code. The JSON MUST be in this format.
        //luckily, if we take data from the API directly, new line characters are marked as such and won't have any formatting issues.
    let  myjson = "[{\"eventid\":28273,\"event_name\":\"SGA Concert\",\"event_description\":\"No description available\",\"event_location\":\"Main Hall Gardner Lounge\",\"organizations\":[\"Sga Concerts\"],\"rsvp\":0,\"event_date\":\"April 9\",\"event_time\":\"7 p.m. - 10 p.m.\",\"event_all_day\":0,\"event_start_time\":\"2025-04-10T00:00:00.000Z\",\"event_end_time\":\"2025-04-10T03:00:00.000Z\",\"tags\":[\"Music\",\"Student Activity\",\"Alumni\",\"Faculty &amp; Staff\",\"General Public\",\"Prospective Students\",\"Student Families\",\"Students\"],\"event_private\":0,\"repeats\":0,\"event_image\":null,\"is_draft\":0},{\"eventid\":30810,\"event_name\":\"Concerts\",\"event_description\":\"\\n  Tabling for Starcleaner Reunion\\n\",\"event_location\":\"Rosenfield Center 1st Floor Lobby - Table 4\",\"organizations\":[\"Sga Concerts\"],\"rsvp\":0,\"event_date\":\"April 8\",\"event_time\":\"11 a.m. - 1 p.m.\",\"event_all_day\":0,\"event_start_time\":\"2025-04-08T16:00:00.000Z\",\"event_end_time\":\"2025-04-08T18:00:00.000Z\",\"tags\":[\"Music\",\"Student Activity\",\"Students\"],\"event_private\":0,\"repeats\":0,\"event_image\":null,\"is_draft\":0}]"
        
        //set my events
        let myEvents = EventData.parseEvents(json: myjson)
        
        //see if we have successfully taken the event information
        Text("myEvents: \(myEvents)")
        //check before printing
        if(myEvents[0].event_name != nil){
            Text("Name: \(myEvents[0].event_name!)")
        }
    }
}


#Preview {
    SampleView()
}


