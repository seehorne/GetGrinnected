////
////  EventCards.swift
////  GetGrinnected
////
////  Created by Budhil Thijm on 4/6/25.
////
//// Some other resources I used:
//// https://www.createwithswift.com/swiftui-reference-groupbox-view-in-swiftui-3/
//
////todo: With data scraped from event Data, create event UI
//
///**
// Example Event JSON:
// {"Title":"Aikido",
// "Date":"April 4",
// "Time":"5:30 p.m. - 6:30 p.m.",
// "StartTimeISO":"2025-04-04T17:30:00-05:00",
// "EndTimeISO":"2025-04-04T18:30:00-05:00",
// "AllDay?":false,
// "Location":"BRAC P103 - Multipurpose Dance Studio",
// "Description":"\n  Aikido Practice: Aikido is a Japanese martial art dedicated to resolving conflict as peacefully as possible. It uses unified body motion, rather than upper body strength, to throw, lock, or pin attackers. We are affiliated with the United States Aikido Federation, and rank earned at Grinnell is transferrable to other dojos in our organization. Beginners are welcome to start at any time (just wear comfortable clothes).\n",
// "Audience":["Alumni","Faculty &amp; Staff","General Public","Prospective Students","Student Families","Students"],
// "Org":"Aikido",
// "Tags":null,
// "ID":23325},
//
// Some other resources I used:
// https://www.createwithswift.com/swiftui-reference-groupbox-view-in-swiftui-3/
// For ui width!
// https://stackoverflow.com/questions/69633018/how-do-i-extend-the-width-of-my-button-component-in-my-swiftui-view

//todo: With data scraped from event Data, create event UI

/**
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
import SwiftUI

struct EventCards: View {
    //Until EventData is complete, we cannot use this form
    var myData: Event
    
    
    init(myData: Event){
        self.myData = myData
    }
    
    /** For logic of favorites and notifications*/
    var body: some View {
        
        /**
            For each event cards, there will be two views: the "compressed view," and the "hover" (or in this case, "open", after tapping the "compressed view")
         */
        VStack (alignment: .leading, spacing: 12) {
            //location of title
            GroupBox {
                HStack(alignment: .center){
                    VStack(alignment: .leading) {
                        Text("\(myData.event_name) - \(myData.organizations)")
                            .font(.headline)
                            .foregroundStyle(.textPrimary)
                        Text(myData.event_location)
                            .foregroundStyle(.textPrimary)
                        Text("\(myData.event_date) • \(myData.event_time)")
                            .font(.caption)
                            .foregroundStyle(.textPrimary)
                    }
                    .padding(.vertical, 4)
                    VStack {
                        CheckBox(
                            items: [CheckBoxOption(
                                name: "-",
                                isChecked: false,
                                uiCompOne: "heart.fill",
                                uiCompTwo: "heart")]
                        )//checkbox
                        CheckBox(items: [CheckBoxOption(name: "-", isChecked: false, uiCompOne: "bell.fill", uiCompTwo: "bell")])
                    }//Vstack
                    .padding(.vertical, 4)
                    .frame(alignment: .trailing)
                    
                    
                }//Hstack (outer)
                .foregroundStyle(Color(.white))
                .frame(width: UIScreen.main.bounds.width - 90.0)
                /**
                 source: https://stackoverflow.com/questions/69633018/how-do-i-extend-the-width-of-my-button-component-in-my-swiftui-view
                 */
            }//title group box
            .foregroundStyle(Color(.container))
        }//Vstack
        
    }//body
    

}

/**
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
struct EventCards_Previews: PreviewProvider {
    static var previews: some View {
        let  myjson = "[{\"eventid\":28273,\"event_name\":\"SGA Concert\",\"event_description\":\"No description available\",\"event_location\":\"Main Hall Gardner Lounge\",\"organizations\":[\"Sga Concerts\"],\"rsvp\":0,\"event_date\":\"April 9\",\"event_time\":\"7 p.m. - 10 p.m.\",\"event_all_day\":0,\"event_start_time\":\"2025-04-10T00:00:00.000Z\",\"event_end_time\":\"2025-04-10T03:00:00.000Z\",\"tags\":[\"Music\",\"Student Activity\",\"Alumni\",\"Faculty &amp; Staff\",\"General Public\",\"Prospective Students\",\"Student Families\",\"Students\"],\"event_private\":0,\"repeats\":0,\"event_image\":null,\"is_draft\":0},{\"eventid\":30810,\"event_name\":\"Concerts\",\"event_description\":\"\\n  Tabling for Starcleaner Reunion\\n\",\"event_location\":\"Rosenfield Center 1st Floor Lobby - Table 4\",\"organizations\":[\"Sga Concerts\"],\"rsvp\":0,\"event_date\":\"April 8\",\"event_time\":\"11 a.m. - 1 p.m.\",\"event_all_day\":0,\"event_start_time\":\"2025-04-08T16:00:00.000Z\",\"event_end_time\":\"2025-04-08T18:00:00.000Z\",\"tags\":[\"Music\",\"Student Activity\",\"Students\"],\"event_private\":0,\"repeats\":0,\"event_image\":null,\"is_draft\":0}]"
            var myEvents = EventData.parseEvents(json: myjson)
        EventCards(myData: myEvents[0])
    }
}
