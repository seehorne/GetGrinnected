//
//  TesterFile.swift
//  GetGrinnected
//
//  Created by Budhil Thijm on 4/6/25.
//

import Foundation
import SwiftUI

/**
 This file is used for whatever UI components you would like to see how they look.
 For example, right now, I (Budhil) am using this to test out my Event Cards themes.
 I'll simply call the EventCards(parameters, parameters.., parameters) and see how it looks based on that!
 In order to see this used, simply, call "TesterView()" in the "GetGrinnectedApp.swift" file!
 */

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
struct TesterView: View {
    var body: some View {
        // this is the long-input form of event card
        EventCards(title: "Aikido", date: "April 4", startTimeISO: "2025-04-04T17:30:00-05:00", endTimeISO: "2025-04-04T18:30:00-05:00", allDay: false, location: "BRAC P103 - Multipurpose Dance Studio", description: "\n  Aikido Practice: Aikido is a Japanese martial art dedicated to resolving conflict as peacefully as possible. It uses unified body motion, rather than upper body strength, to throw, lock, or pin attackers. We are affiliated with the United States Aikido Federation, and rank earned at Grinnell is transferrable to other dojos in our organization. Beginners are welcome to start at any time (just wear comfortable clothes).\n", organization: "Aikido", tags: [], id: 23325)
    }//body
}


#Preview {
    TesterView()
}
