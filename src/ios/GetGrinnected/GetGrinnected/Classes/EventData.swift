//
//  eventData.swift
//  GetGrinnected
//
//  Created by Budhil Thijm on 4/1/25.
//

/**
 
 
 AS OF 2025.04.06, THIS SCRAPING DOESN'T WORK FOR SURE
 
 HOW TO GET THE ACTUAL API CALLS?
 
 
 WHAT ARE THE NAMES OF THE EXACT VARIABLES?
 
 
 THIS IS ALMOST THERE BUT NOT QUITE
 
 
 
 */

//https://www.hackingwithswift.com/read/7/3/parsing-json-using-the-codable-protocol 
import Foundation
import SwiftUI
/**
 JSON format:
 
 Example Event JSON:
 {"Title":"Aikido",
 "Date":"April 4",
 "Time":"5:30 p.m. - 6:30 p.m.",
 "StartTimeISO":"2025-04-04T17:30:00-05:00",
 "EndTimeISO":"2025-04-04T18:30:00-05:00",
 "AllDay?":false,
 "Location":"BRAC P103 - Multipurpose Dance Studio",
 "Description":"\n  Aikido Practice: Aikido is a Japanese martial art dedicated to resolving conflict as peacefully as possible. It uses unified body motion, rather than upper body strength, to throw, lock, or pin attackers. We are affiliated with the United States Aikido Federation, and rank earned at Grinnell is transferrable to other dojos in our organization. Beginners are welcome to start at any time (just wear comfortable clothes).\n",
 "Audience":["Alumni","Faculty &amp; Staff","General Public","Prospective Students","Student Families","Students"],
 "Org":"Aikido",
 "Tags":null,
 "ID":23325},

  
 "Tags" and "Audience" are same format!
 */
 
class EventData: ObservableObject, Identifiable {
    // Properties mirroring the JSON
    @Published var title: String = ""
    @Published var date: String = ""
    @Published var startTimeISO: String = ""
    @Published var endTimeISO: String = ""
    @Published var allDay: Bool = false
    @Published var location: String = ""
    @Published var description: String? = nil
    @Published var audience: [String]? = nil
    @Published var organization: String = ""
    @Published var tags: [String]? = nil
    @Published var id: Int = 0
    
    /**
     Non-scraped data
     */
    @Published var notify: Bool = false
    @Published var favorited: Bool = false
    
    // Helper struct for decoding JSON
    private struct EventDataDTO: Decodable {
        var title: String
        var date: String
        var startTimeISO: String
        var endTimeISO: String
        var allDay: Bool
        var location: String
        var description: String?
        var audience: [String]?
        var organization: String
        var tags: [String]?
        var id: Int
        
        enum CodingKeys: String, CodingKey {
            case title = "Title"
            case date = "Date"             // Maps struct property "date" to JSON key "Date"
            case startTimeISO = "StartTimeISO"
            case endTimeISO = "EndTimeISO"
            case allDay = "AllDay?"
            case location = "Location"
            case description = "Description"
            case audience = "Audience"
            case organization = "Org"      // Maps struct property "organization" to JSON key "Org"
            case tags = "Tags"
            case id = "ID"
        }
    }
    
    //default
    init(){
        
    }
    
    //full initializer
    init(title: String,
        date: String,
        startTimeISO: String,
        endTimeISO: String,
        allDay: Bool,
        location: String,
        description: String?,
        audience: [String]?,
        organization: String,
        tags: [String]?,
        id: Int){
        self.title = title
        self.date = date
        self.startTimeISO = startTimeISO
        self.endTimeISO = endTimeISO
        self.allDay = allDay
        self.location = location
        self.description = description
        self.audience = audience
        self.organization = organization
        self.tags = tags
        self.id = id
    }
    
    
    // Initialize from JSON string
    convenience init(fromJSON jsonString: String) {
        self.init()
        guard let jsonData = jsonString.data(using: .utf8) else {
            print("Failed to convert string to data")
            //No longer returning nil, as this is no longer a init? type
            return //nil
        }
            
        do {
            //initialize decoder
            let decoder = JSONDecoder()
            let dto = try decoder.decode(EventDataDTO.self, from: jsonData)
                
            // Transfer values from DTO to published properties
            self.title = dto.title
            self.date = dto.date
            self.startTimeISO = dto.startTimeISO
            self.endTimeISO = dto.endTimeISO
            self.allDay = dto.allDay
            self.location = dto.location
            self.description = dto.description
            self.audience = dto.audience
            self.organization = dto.organization
            self.tags = dto.tags
            self.id = dto.id
            
            //non-scraped
            self.notify = false
            self.favorited = false
        } catch {
            print("Decoding error: \(error)")
            //no longer  afailable initializer
            //return nil
        }//do-catch
    }//initialize with a string input
        
    
    // function to parse JSON string into EventData
    static func fromJSON(_ jsonString: String) -> EventData? {
        return EventData(fromJSON: jsonString)
    }
        
    // function to parse an array of JSON objects, does the same as initializer
    static func arrayFromJSON(_ jsonString: String) -> [EventData]? {
        //convert string data
        guard let jsonData = jsonString.data(using: .utf8) else {
            print("Failed to convert string to data")
            return nil
        }
        
        do {
            //initialize decoder
            let decoder = JSONDecoder()
            let dtos = try decoder.decode([EventDataDTO].self, from: jsonData)
                
            //initialize the event-Data decoding using the initializer
            return dtos.map { dto in
                EventData(
                    title: dto.title,
                    date: dto.date,
                    startTimeISO: dto.startTimeISO,
                    endTimeISO: dto.endTimeISO,
                    allDay: dto.allDay,
                    location: dto.location,
                    description: dto.description,
                    audience: dto.audience,
                    organization: dto.organization,
                    tags: dto.tags,
                    id: dto.id
                )
            }
        } catch {
            //print error if an error is found
            print("Array decoding error: \(error)")
            return nil
        }
    }
}


//API call --> ALL events
//for a page, note current filters!
//filter out events that are not included
//turn each of remaining events into eventCards UI components
//

struct myView: View {
    var body: some View{
        // this is the proper input form for EventData
        let data = EventData(fromJSON: """
            {"Title":"Aikido",
            "Date":"April 4",
            "Time":"5:30 p.m. - 6:30 p.m.",
            "StartTimeISO":"2025-04-04T17:30:00-05:00",
            "EndTimeISO":"2025-04-04T18:30:00-05:00",
            "AllDay?":false,
            "Location":"BRAC P103 - Multipurpose Dance Studio",
            "Description":"\n  Aikido Practice: Aikido is a Japanese martial art dedicated to resolving conflict as peacefully as possible. It uses unified body motion, rather than upper body strength, to throw, lock, or pin attackers. We are affiliated with the United States Aikido Federation, and rank earned at Grinnell is transferrable to other dojos in our organization. Beginners are welcome to start at any time (just wear comfortable clothes).\n",
            "Audience":["Alumni","Faculty &amp; Staff","General Public","Prospective Students","Student Families","Students"],
            "Org":"Aikido",
            "Tags":null,
            "ID":23325}
        """)
        //testing whether or not the ouptuts work!
        //let printSt = if(data.title.compare("Aikido").rawValue == 0) {"Correct!"} else {"Incorrect!"}
//        Text(printSt)
        var title = if(data.title.compare("Aikido").rawValue == 0) {"Correct!"} else {"Incorrect!"}
        var location = if(data.location.compare("Aikido").rawValue == 0) {"Correct!"} else {"Incorrect!"}
        Text(title)
        Text(location)
    }
    
}

#Preview {
    myView()
}
