//
//  EventCards.swift
//  GetGrinnected
//
//  Created by Budhil Thijm on 4/6/25.
//
// Some other resources I used:
// https://www.createwithswift.com/swiftui-reference-groupbox-view-in-swiftui-3/

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
    var title: String
    var date: String
    var startTimeISO: String
    var endTimeISO: String
    var allDay: Bool
    var location: String
    var description: String
    var audience: [String]?
    var organization: String
    var tags: [String]
    var id: Int
    //Until EventData is complete, we cannot use this form
//    var myData: EventData
    
    
    /** For logic of favorites and notifications*/
    //The specific logic needs to be figured out as of 2025.04.06, 11:50 pm, however
    //this is likely going to have to be apart of the "eventData" data structure as opposed to this file!
//    @Binding var favorited: Bool = false
//    @Binding var notify: Bool = false
    var body: some View {
        
        /**
            For each event cards, there will be two views: the "compressed view," and the "hover" (or in this case, "open", after tapping the "compressed view")
         */
        VStack (alignment: .leading, spacing: 12) {
            //location of title
            GroupBox {
                HStack{
                    VStack{
                        Text(title)// once EventData is ready, myData.title is the correct call
                            .foregroundStyle(Color(.textPrimary))
                            .fontWeight(.semibold)
                            //to be determined later
                        Text(organization) // once ready myData.organization
                            .foregroundStyle(Color(.textPrimary))
                            .fontWeight(.light)
                        
                    } //left side
                    VStack{
                        Text(date)//once EventData is ready, myData.date
                            .foregroundStyle(Color(.textSecondary))
                            .fontWeight(.light)
                            .frame(alignment: .leading)
                        Text(location)// once EventData is ready, myData.location
                            .foregroundStyle(Color(.textSecondary))
                            .fontWeight(.light)
                            .frame(alignment: .leading)
                    } //right side
//                    HStack{
//                        Button{
//                            notify.toggle()
//                        } label: {
//                            Image(systemName: notify ? "bell.fill" : "bell")
//                        }//notifications bell
//                        
//                        Button{
//                            favorited.toggle()
//                        } label:{
//                            Image(systemName: notify ? "heart.fill" : "heart")
//                        }//favorites button
//                        
//                        
//                    }
                    
                }
            } //title group box
            .foregroundStyle(Color(.container))
            Divider ()
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
        
        //Must be changed once event cards are ready
        EventCards(title: "Aikido", date: "April 4", startTimeISO: "2025-04-04T17:30:00-05:00", endTimeISO: "2025-04-04T18:30:00-05:00", allDay: false, location: "BRAC P103 - Multipurpose Dance Studio", description: "\n  Aikido Practice: Aikido is a Japanese martial art dedicated to resolving conflict as peacefully as possible. It uses unified body motion, rather than upper body strength, to throw, lock, or pin attackers. We are affiliated with the United States Aikido Federation, and rank earned at Grinnell is transferrable to other dojos in our organization. Beginners are welcome to start at any time (just wear comfortable clothes).\n", organization: "Aikido", tags: [], id: 23325)
        //EventCards(myData: )
    }
}
