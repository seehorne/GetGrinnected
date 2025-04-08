//
//  HomescreenView.swift
//  GetGrinnected
//
//  Created by Ellie Seehorn on 3/4/25.
//

import Foundation
import SwiftUI

struct HomescreenView: View {
    // the date is currently being viewed. default is today
    @State private var viewedDate = Date.now
    // the furthest date in the future we can see. default is 2 weeks
    @State private var lastDate = Date.now.addingTimeInterval(86400 * 13)
    // the tags selected to filter by. default is any
    @State var selectedTags = EventTags.any
    // the events we have
    // @State var events: [EventData]
    
    var body: some View {
        NavigationStack {
            Header(title: "Home") // add header to view
            
            Spacer() // add some space under header
            
            VStack {
                HStack {
                    // a picker for date
                    DatePicker("Currently viewing date: ", selection: $viewedDate, in: Date.now...lastDate, displayedComponents: .date)
                        .labelsHidden()
                    
                    // a picker for tags
                    Picker("Tags", selection: $selectedTags)
                    {
                        ForEach(EventTags.allCases, id: \.self) { tag in
                            Text(tag.rawValue)
                        }
                    }
                } //HStack
                
                List {
                    EventCards(title: "Aikido", date: "April 4", startTimeISO: "2025-04-04T17:30:00-05:00", endTimeISO: "2025-04-04T18:30:00-05:00", allDay: false, location: "BRAC P103 - Multipurpose Dance Studio", description: "\n  Aikido Practice: Aikido is a Japanese martial art dedicated to resolving conflict as peacefully as possible. It uses unified body motion, rather than upper body strength, to throw, lock, or pin attackers. We are affiliated with the United States Aikido Federation, and rank earned at Grinnell is transferrable to other dojos in our organization. Beginners are welcome to start at any time (just wear comfortable clothes).\n", organization: "Aikido", tags: [], id: 23325)
                    EventCards(title: "Aikido", date: "April 4", startTimeISO: "2025-04-04T17:30:00-05:00", endTimeISO: "2025-04-04T18:30:00-05:00", allDay: false, location: "BRAC P103 - Multipurpose Dance Studio", description: "\n  Aikido Practice: Aikido is a Japanese martial art dedicated to resolving conflict as peacefully as possible. It uses unified body motion, rather than upper body strength, to throw, lock, or pin attackers. We are affiliated with the United States Aikido Federation, and rank earned at Grinnell is transferrable to other dojos in our organization. Beginners are welcome to start at any time (just wear comfortable clothes).\n", organization: "Aikido", tags: [], id: 23325)
                }
            } //VStack
        } //NavigationStack
    } //body
} //HomescreenView

#Preview {
    HomescreenView()
}
