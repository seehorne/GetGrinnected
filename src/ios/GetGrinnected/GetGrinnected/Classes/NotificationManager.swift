//
//  NotificationManager.swift
//  GetGrinnected
//
//  Created by Budhil Thijm on 5/11/25.
//

/**
 Taken in part from this youtube video: https://www.youtube.com/watch?v=mG9BVAs8AIo&ab_channel=SwiftfulThinking
 */

import UserNotifications

class NotificationManager{
    
    //to be used when calling functions of >>
    static let instance = NotificationManager()
    
    /**
     call this function to request authorization to send notificatinos to user
     */
    func requestAuthorization(){
        let options: UNAuthorizationOptions = [.alert, .badge, .sound]
        //The popup on iphoen that asks you to "allow notifications"
        UNUserNotificationCenter.current().requestAuthorization(options: options) { (success, error) in
            if let error = error{
                print("Error requesting authorization: \(error)")
                return
            } else {
                print("Successfully requested authorization")
            }
        }
    }
    
    /**
     this function will take an
     EventModel
     and take all of the values to create a notification that has all this information
     * start time
     * event name
     * event location
     * organization
     
     call this function when a "notificatoin" is added, to add it
     */
    func scheduleNotification(event: EventModel){
        /**
         TODO: if optional name is empty, remove it from the body of the nofication..
         */
        //determine content of notificatinos
        let content = UNMutableNotificationContent()
        //if name is unnamed, do not write it down!
        content.title = event.name != "Unnamed Event" ? "\(event.name) is today!" : "Event is today!"
        //check if organizations are empty, if so, don't write anything as subtitle
        content.subtitle = "\(event.organizations.isEmpty ? "" : "By \(event.organizations.joined(separator: ", "))")"
        //not force unwrapping location or string if it doesn't exist
        let eventTime: String = {
            if let startTime = event.startTimeString {//lastly, check if ONLY the time is not nil.
                return startTime
            } else if let date = event.date {//if the MOST beautiful string is not available, utilize the simple date (April 18th, for example), instead.
                                                    //there may be a possibility that this event.date still stores information of the hour (where startimestring stores all that information), and minutes, but it's simply not showing because it's set to nil for some reason. lookinto this further after.
                return "all day on \(date)" // Consider formatting this if needed
            } else {
                return ""
            }
        }()
        
        let locationText = event.location?.isEmpty == false ? " \(event.location!)" : ""
        content.body = "Come by\(locationText) — it's happening \(eventTime)"
        content.sound = UNNotificationSound.default
        /**
         TODO: change badge number, instead of 1, create app storage or universal variable that can store the badge number and add to it.
         */
        
        //get the current badge number, add one to current badge number!
        let currentBadge = UserDefaults.standard.integer(forKey: "badgeCount")
        let newBadge = currentBadge + 1
        UserDefaults.standard.set(newBadge, forKey: "badgeCount")
        content.badge = NSNumber(value: newBadge)//the red circle that appears on app
        /**
         To remove the badge when opening the app, underneath the main view..
         .onAppear{
            UIApplication.shared.applicationIconBadgeNumber = 0
         }
         */
        
        //Three kinds of triggers: Timer interval, calendar, and locative.. seen in video
        //set calendar
        let calendar = Calendar.current
        
        ///handle dates correctly:
        ///if starttime precise to the hour/minute exists, use that as the start time, minus 15 minutes to send notification
        ///if the date exists (and not starttime which is precise to hour/minute), use the day, and will schedule notification for the morning of the event
        ///if neither exists, do not schedule notification, as we cannot know what day the event is.
        
        var triggerDateComponents: DateComponents?

        if let startTime = event.startTime {
            // Standard behavior: Notify 15 mins before event
            if let newDate = calendar.date(byAdding: .minute, value: -15, to: startTime) {
                triggerDateComponents = calendar.dateComponents([.minute, .hour, .day, .month, .year], from: newDate)
            }
        } else if let eventDate = event.date {
            // No start time? Schedule for 9:00 AM on the event's date
            let dateFormatter = DateFormatter()
            dateFormatter.dateFormat = "yyyy-MM-dd"
            if let parsedDate = dateFormatter.date(from: eventDate) {
                // Schedule for 9:00 AM that day
                var components = calendar.dateComponents([.day, .month, .year], from: parsedDate)
                components.hour = 9
                components.minute = 0
                triggerDateComponents = components
            } else {
                print("Could not parse event.date: \(eventDate)")
                return
            }
        } else {
            print("Error: Event has no valid time or date.")
            return
        }
        
        guard let components = triggerDateComponents else {
            print("Failed to create trigger date components.")
            return
        }

        let trigger = UNCalendarNotificationTrigger(dateMatching: components, repeats: false)
        
        /**
         identifier to remove the notification when needed
         */
        //ID changer
        let identifier = event.id.codingKey.stringValue
        let request = UNNotificationRequest(identifier: identifier, content: content, trigger: trigger)
        
        //add request based on our request..
        UNUserNotificationCenter.current().add(request) { (error) in
            //error handling
            if let error = error{
                print("Error scheduling notification: \(error)")
                return
            } else {
                print("Successfully scheduled notification")
            }
        }
        
    }
    
    //cancel notifiaction through removing them!
    func cancelReminderWithIdentifier(event: EventModel) {
        let identifier = event.id.codingKey.stringValue
        //cancel only current one
        UNUserNotificationCenter.current().removePendingNotificationRequests(withIdentifiers: [identifier])
    }
}
