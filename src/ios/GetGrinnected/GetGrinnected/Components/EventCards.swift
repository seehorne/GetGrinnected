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
    //Until EventData is complete, we cannot use this form
    var myData: EventData
    
    
    init(myData: EventData){
        self.myData = myData
    }
    
    
    //Example initialization
    init?(){
        
        self.myData = EventData(fromJSON: """
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
        self.myData.title = "Aikido"
        self.myData.date = "April 4"
        self.myData.time = "5:30 p.m. - 6:30 p.m."
        self.myData.startTimeISO = "2025-04-04T17:30:00-05:00"
        self.myData.endTimeISO = "2025-04-04T18:30:00-05:00"
        self.myData.allDay = false
        self.myData.location = "BRAC P103 - Multipurpose Dance Studio"
        self.myData.description = "\n  Aikido Practice: Aikido is a Japanese martial art dedicated to resolving conflict as peacefully as possible. It uses unified body motion, rather than upper body strength, to throw, lock, or pin attackers. We are affiliated with the United States Aikido Federation, and rank earned at Grinnell is transferrable to other dojos in our organization. Beginners are welcome to start at any time (just wear comfortable clothes).\n"
        self.myData.audience = ["Alumni","Faculty &amp; Staff","General Public","Prospective Students","Student Families","Students"]
        self.myData.organization = "Aikido"
        self.myData.tags = []
        self.myData.id = 23325
        self.myData.notify = false
        self.myData.favorited = false
        
        
    }//tester initializer
    
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
                HStack(alignment: .center){
                    VStack{
                        Text(myData.title)// once EventData is ready, myData.title is the correct call
                            .foregroundStyle(Color(.textPrimary))
                            .fontWeight(.semibold)
                            //to be determined later
                        Text(myData.organization) // once ready myData.organization
                            .foregroundStyle(Color(.textPrimary))
                            .fontWeight(.light)
                        
                    }//left side
                    VStack(alignment: .trailing){
                        Text(myData.date)//once EventData is ready, myData.date
                            .foregroundStyle(Color(.textSecondary))
                            .fontWeight(.light)
                        Text(myData.location)// once EventData is ready, myData.location
                            .foregroundStyle(Color(.textSecondary))
                            .fontWeight(.light)
                    } //right side
                    VStack {
                        //currently does not work!
                        Button("", systemImage: myData.notify ? "bell.fill" : "bell") { myData.notify.toggle() }
                            .buttonStyle(.bordered)
                            .padding(0)
                        Button("", systemImage: myData.favorited ? "heart.fill" : "heart") { myData.favorited.toggle()}
                            .buttonStyle(.bordered)
                            .padding(0)
                    }
                    
                    
                }//Hstack (outer)
            }//title group box
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

        EventCards()
    }
}
