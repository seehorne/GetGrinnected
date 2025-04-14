////
////  EventCards.swift
////  GetGrinnected
////
////  Created by Budhil Thijm on 4/6/25.
////
//// Some other resources I used:
//// https://www.createwithswift.com/swiftui-reference-groupbox-view-in-swiftui-3/
//
//
//
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

/**
 What is the View Type? Why does this need to be a view type
 Versus a Func type
 */
struct EventCard: View {
    
    //Event is the struct we defined in "Event" file, stores all information of JSON
    var event: Event
    
    
    //body is how this is rendered
    
    var body: some View{
        
        /**
            For each event cards, there will be two views: the "compressed view," and the "expanded" (or in this case, "open", after tapping the "compressed view")
         */
        
        //Vstack = vertical stack
        VStack (alignment: .leading, spacing: 12) {
            //location of title
            //Creates the box
            GroupBox { // the box that surrounds
                
                //horizontal stack
                //filled with two VStacks that are horizontally aligned
                HStack(alignment: .center){ //for contents
                    
                    //Vstack contains the text of the event.
                    VStack(alignment: .leading) {
                        
                        //Check if null
                        if(event.event_name != nil){
                            
                            //because it's optional type, we have to use ! to
                            //tell the code that we are SURE it exists
                            //putting variable into strings is "\(variable)"
                            Text("\(event.event_name!)")
                                .font(.headline) //determining font (make it big!)
                                .foregroundStyle(.textPrimary)//this color is defined in assets
                        }//event name
                        
                        //Check if null
                        if(!event.organizations!.isEmpty) {
                            //join the array with a ", "
                            Text("\(event.organizations!.joined(separator: ", "))")
                                .font(.subheadline)
                                .foregroundStyle(.textPrimary)
                            //alternatives for colors
                                //.foregroundStyle(Color("textPrimary"))
                                // .foregroundStyle(Color.textPrimary)
                        }//organizations
                        
                        //Check if null
                        if(event.event_location != nil){
                            Text(event.event_location!)
                                .foregroundStyle(.textPrimary)
                        }
                        
                        //create a text bullet point inbetween the date and time
                        if((event.event_date != nil) && (event.event_time != nil)){
                            Text("\(event.event_date!) • \(event.event_time!)")
                                .font(.caption)
                                .foregroundStyle(.textPrimary)
                        }
                            
                    }//Vstack text
                    .lineLimit(1, reservesSpace: true) //card wont change size thx to this.
                    .padding(.vertical, 4)
                    
                    
                    VStack {
                        //component that can be reusable
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
                .frame(width: UIScreen.main.bounds.width - 90)//the whole event card, WONT reach the edge of the screen
                /**
                 source: https://stackoverflow.com/questions/69633018/how-do-i-extend-the-width-of-my-button-component-in-my-swiftui-view
                 */
            }//title group box
            .foregroundStyle(Color(.container))
        }//Vstack
    }
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

/**
 Explain!
 Preview type doesn't run when you run emulator
 */

//the preview is to test specific components
struct EventCards_Previews: PreviewProvider {
    static var previews: some View {
        let  myjson = "[{\"eventid\":28273,\"event_name\":\"SGA Concert\",\"event_description\":\"No description available\",\"event_location\":\"Main Hall Gardner Lounge\",\"organizations\":[\"Sga Concerts\"],\"rsvp\":0,\"event_date\":\"April 9\",\"event_time\":\"7 p.m. - 10 p.m.\",\"event_all_day\":0,\"event_start_time\":\"2025-04-10T00:00:00.000Z\",\"event_end_time\":\"2025-04-10T03:00:00.000Z\",\"tags\":[\"Music\",\"Student Activity\",\"Alumni\",\"Faculty &amp; Staff\",\"General Public\",\"Prospective Students\",\"Student Families\",\"Students\"],\"event_private\":0,\"repeats\":0,\"event_image\":null,\"is_draft\":0},{\"eventid\":30810,\"event_name\":\"Concerts\",\"event_description\":\"\\n  Tabling for Starcleaner Reunion\\n\",\"event_location\":\"Rosenfield Center 1st Floor Lobby - Table 4\",\"organizations\":[\"Sga Concerts\"],\"rsvp\":0,\"event_date\":\"April 8\",\"event_time\":\"11 a.m. - 1 p.m.\",\"event_all_day\":0,\"event_start_time\":\"2025-04-08T16:00:00.000Z\",\"event_end_time\":\"2025-04-08T18:00:00.000Z\",\"tags\":[\"Music\",\"Student Activity\",\"Students\"],\"event_private\":0,\"repeats\":0,\"event_image\":null,\"is_draft\":0}]"
            let myEvents = EventData.parseEvents(json: myjson)
        EventCard(event: myEvents[0])
    }
}
