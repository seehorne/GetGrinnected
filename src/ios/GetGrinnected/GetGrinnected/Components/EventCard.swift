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
 A view type is a struct that can create a view with the constructor of the variables. In this specific one, there's only "event" as an input
 a func viewbuilder does the exact same thing, the syntax is different.
 The difference is object-oriented (struct structName: View) versus functional (@Viewbuilder \n func functionName(){})
 */
struct EventCard: View {
    @Environment(\.modelContext) private var modelContext
    
    //Event is the struct we defined in "Event" file, stores all information of JSON
    let event: EventModel
    let isExpanded: Bool//The single card does not need a @binding or @state tag
    //simply based on this value, the expansion will show and not.
    let userProfile = UserProfile()
    
    let description: String //description used later
    //let deepLink: URL //for now unneeded
    
    init(event: EventModel, isExpanded: Bool) {
        self.event = event
        self.isExpanded = isExpanded
        //self.deepLink = URL(string: "GetGrinnected://item/\(event.id)")!
        self.description = (
        """
        Event: \(event.name)
        Location: \(event.location ?? "N/A")
        Organization: By \(event.organizations.joined(separator: ", "))
        Time: \(event.startTimeString ?? "N/A")
        Link: https://github.com/seehorne/GetGrinnected
        """//eventually add a link to the app instead of a link to the getGrinnected github!
        )
    }

    
    //body is how this is rendered
    
    var body: some View{
        
        var accessibilityLabelText: String {
            var label = "\(event.name)"
            if let location = event.location {
                label += ", at \(location)"
            }
            if let time = event.startTimeString {
                label += ", at \(time)"
            }
            if isExpanded, let descr = event.descr {
                label += ". Description: \(descr)"
            }
            return label
        }
        
        /**
            For each event cards, there will be two views: the "compressed view," and the "expanded" (or in this case, "open", after tapping the "compressed view")
         */
        
        //Vstack = vertical stack
        VStack (alignment: .leading, spacing: 12) {
            //location of title
            //Creates the box
            ZStack {
                //groupbox tutorial:
                /**
                 https://www.youtube.com/watch?v=NvE3SaGGurQ&ab_channel=SeanAllen
                 */
                GroupBox { // the box that surrounds
                    //filled with two VStacks that are horizontally aligned
                    HStack(alignment: .center){ //for contents
                        
                        //Vstack contains the text of the event.
                        VStack(alignment: .leading) {
                            //event name, check if null
                            if(event.name != "Unnamed Event"){
                                //because it's optional type, we have to use ! to
                                //tell the code that we are SURE it exists
                                //putting variable into strings is "\(variable)"
                                Text("\(event.name)")
                                    .font(.headline) //determining font (make it big!)
                                    .foregroundStyle(.textPrimary)//this color is defined in assets
                                    .frame(alignment: .leading)//specifically adding leading alignment to get
                            }//event name
                            
                            
                            //Check if null
                            if(!event.organizations.isEmpty) {
                                //join the array with a ", "
                                Text("By \(event.organizations.joined(separator: ", "))")
                                    .font(.subheadline)
                                    .foregroundStyle(.textPrimary)
                                //alternatives for colors
                                    //.foregroundStyle(Color("textPrimary"))
                                    // .foregroundStyle(Color.textPrimary)
                            }//organizations
                            
                            //Check if null
                            if(event.location != nil){
                                Text(event.location!)
                                    .foregroundStyle(.textPrimary)
                                    .font(.caption)
                            }
                            
                            // Formatted time string
                            if(event.startTimeString != nil){ //lastly, check if ONLY the time is not nil.
                                Text("\(event.startTimeString!)")
                                    .font(.caption)
                                    .foregroundStyle(.textPrimary)
                            } else if (event.date != nil) { //if the MOST beautiful string is not available, utilize the simple date (April 18th, for example), instead.
                                //there may be a possibility that this event.date still stores information of the hour (where startimestring stores all that information), and minutes, but it's simply not showing because it's set to nil for some reason. lookinto this further after.
                                Text("\(event.date!)")
                                    .font(.caption)
                                    .foregroundStyle(.textPrimary)
                                
                            }
                            
                            
                            //Add description if our event is expanded
                            if (isExpanded && event.descr != nil){
                                Text("\(event.descr!)")
                                    .font(.caption) //determining font (make it big!)
                                    .foregroundStyle(.textPrimary)//this color is defined in assets
                                    .frame(alignment: .leading)//specifically adding leading alignment to get
                                    .lineLimit(1000, reservesSpace: false)
                            }
                          //Add event tags, checking both if expanded and event tags
                            if(isExpanded && !event.tags.isEmpty) {
                                //join the array with a ", "
                                Text("Tags: \(event.tags.joined(separator: ", "))")
                                    .font(.caption2)
                                    .foregroundStyle(.textPrimary)
                                    .lineLimit(1000, reservesSpace: false)
                                //alternatives for colors
                                    //.foregroundStyle(Color("textPrimary"))
                                    // .foregroundStyle(Color.textPrimary)
                            }//organizations
                            
                            
                                
                        }//Vstack text
                        //if expanded, set line limit to max, and don't reserve space!
                        .lineLimit(1, reservesSpace: true) //card wont change size thx to this.
                        .padding(.leading, 8) //adding space before
                        
                        Spacer()//adding a spacer between the two Vstacks, takes up maximum space
                        
                        
                        VStack {
                            ShareLink(
                                item: "Check out this event!\n",
                                subject: Text("Join me at this event!"),
                                message: Text("\(self.description)"),
                                preview: SharePreview("Check this out!", image: Image(systemName: "square.and.arrow.up"))
                            ) {
                                Image(systemName: "square.and.arrow.up")
                                    .foregroundColor(.border)
                            }//can utilize
                            .accessibilityLabel("Share event")
                            .accessibilityHint("Shares event information via available apps.")
                            .padding(.vertical, 2)//adding space after
                            
                            //component that can be reusable
                            Button(action: {
                                event.favorited.toggle()
                                event.lastUpdated = Date() // mark as modified
                                Task{
                                    print("Trying to favorite save")
                                    userProfile.getUserFavoritedEvents(context: modelContext)
                                    print("Just got all the existing favorites")
                                    let favorites = userProfile.fetchFavoritedEventIDs(from: modelContext)
                                    userProfile.setUserFavoritedEvents(events: favorites)
                                    //now, an up to date list of the notified events
                                    userProfile.getUserFavoritedEvents(context: modelContext)
                                    try? modelContext.save()
                                }
                                //todo: save this back to the cache with the get call
                            }) {
                                Image(systemName: event.favorited ? "heart.fill" : "heart")
                                    .foregroundColor(.border)
                                    .imageScale(.large)
                            }
                            .accessibilityLabel("Favorite")
                            .accessibilityValue(event.favorited ? "\(event.name) is favorited"  : "\(event.name) is not favorited")
                            .accessibilityAddTraits(.isButton)
                            .accessibilityAddTraits(event.favorited ? .isSelected : [])
                            .padding(.vertical, 2)//adding space after
                            
                            Button(action: {
                                event.notified.toggle()
                                Task{
                                    print("trying to notify save")
                                    userProfile.getUserNotifiedEvents(context: modelContext)
                                    let notifs = userProfile.fetchNotifiedEventIDs(from: modelContext)
                                    userProfile.setUserNotifiedEvents(events: notifs)
                                    userProfile.getUserNotifiedEvents(context: modelContext)
                                    
                                    //set notifications
                                    if(event.notified){
                                        NotificationManager.instance.requestAuthorization()
                                        NotificationManager.instance.scheduleNotification(event: self.event)
                                    } else {
                                        NotificationManager.instance.scheduleNotification(event: self.event)
                                    }
                                    try? modelContext.save()
                                }
                            }) {
                                Image(systemName: event.notified ? "bell.fill" : "bell")
                                    .foregroundColor(.border)
                                    .imageScale(.large)
                            }
                            .accessibilityLabel("Notifications")
                            .accessibilityValue(event.notified ? "Notifications for \(event.name) on" : "Notifications for \(event.name) off")
                            .accessibilityAddTraits(.isButton)
                            .accessibilityAddTraits(event.notified ? .isSelected : [])
                        } //Vstack
                        .padding(.vertical, 2)//adding space after
                        .frame(alignment: .trailing)
                        
                        
                    }//Hstack (outer)
                    .foregroundStyle(Color(.white))
                    .frame(width: UIScreen.main.bounds.width - 90)//the whole event card, WONT reach the edge of the screen
                    /**
                     source: https://stackoverflow.com/questions/69633018/how-do-i-extend-the-width-of-my-button-component-in-my-swiftui-view
                     */
                    .accessibilityElement(children: .contain)
                    .accessibilityLabel(accessibilityLabelText)
                    .accessibilityHint("Double-tap for more details or actions.")
                }
                .foregroundStyle(Color(.container))
                .cornerRadius(20)//make the corner radius small enough
                .overlay( /// apply a rounded border
                    ///even if you use .border, it makes the border pointed.. so this is a bad part about swiftui
                    RoundedRectangle(cornerRadius: 20)// apply curvature
                        .stroke(.border, lineWidth: 1) //apply border and color and a thickness of 1
                        .opacity(0.75) //change opacity of the the border to 0.75
                )
                    
            }
            
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


/**
Commented preview out because eventcard now uses an EventDTO, which is not the current format of the parsed events.
An active use of the eventcards is in eventlist!
 
 Keeping this here so that it can be fixed. 
 */
//the preview is to test specific components
//struct EventCards_Previews: PreviewProvider {
//    static var previews: some View {
//        let  myjson = "[{\"eventid\":28273,\"event_name\":\"SGA Concert\",\"event_description\":\"No description available\",\"event_location\":\"Main Hall Gardner Lounge\",\"organizations\":[\"Sga Concerts\"],\"rsvp\":0,\"event_date\":\"April 9\",\"event_time\":\"7 p.m. - 10 p.m.\",\"event_all_day\":0,\"event_start_time\":\"2025-04-10T00:00:00.000Z\",\"event_end_time\":\"2025-04-10T03:00:00.000Z\",\"tags\":[\"Music\",\"Student Activity\",\"Alumni\",\"Faculty &amp; Staff\",\"General Public\",\"Prospective Students\",\"Student Families\",\"Students\"],\"event_private\":0,\"repeats\":0,\"event_image\":null,\"is_draft\":0},{\"eventid\":30810,\"event_name\":\"Concerts\",\"event_description\":\"\\n  Tabling for Starcleaner Reunion\\n\",\"event_location\":\"Rosenfield Center 1st Floor Lobby - Table 4\",\"organizations\":[\"Sga Concerts\"],\"rsvp\":0,\"event_date\":\"April 8\",\"event_time\":\"11 a.m. - 1 p.m.\",\"event_all_day\":0,\"event_start_time\":\"2025-04-08T16:00:00.000Z\",\"event_end_time\":\"2025-04-08T18:00:00.000Z\",\"tags\":[\"Music\",\"Student Activity\",\"Students\"],\"event_private\":0,\"repeats\":0,\"event_image\":null,\"is_draft\":0}]"
//            let myEvents = EventData.parseEvents(json: myjson)
//        
//        List{
//            EventCard(event: myEvents[0], isExpanded: false)
//            EventCard(event: myEvents[0], isExpanded: true)
//        }
//    }
//}

