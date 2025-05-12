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
        content.title = "Event \(event.name) begins in 15 minutes!"
        content.subtitle = "\(event.organizations.isEmpty ? "" : "By \(event.organizations.joined(separator: ", "))")"
        content.body = """
        Come by \(event.location!) at \(event.startTimeString!)
        """
        content.sound = UNNotificationSound.default
        /**
         TODO: change badge number, instead of 1, create app storage or universal variable that can store the badge number and add to it.
         */
        content.badge = 1 //the red circle that appears on app
        /**
         To remove the badge when opening the app, underneath the main view..
         .onAppear{
            UIApplication.shared.applicationIconBadgeNumber = 0
         }
         */
        
        //Three kinds of triggers: Timer interval, calendar, and locative.. seen in video
        /**
         trigger sends notification in "timeInterval"'s value
         */
        
        
        //set calendar
        let calendar = Calendar.current
        //take date from event
        let newDate = calendar.date(byAdding: .minute, value: -15, to: event.startTime!)
        let components = calendar.dateComponents([ .minute, .hour, .day, .month, .year], from: newDate! as Date)
         
        //calendar
        let trigger = UNCalendarNotificationTrigger(dateMatching: components, repeats: false)
        
        //location
        
        /**
         identifier to remove the notification
         */
        //ID changer
        let request = UNNotificationRequest(identifier: event.id.codingKey.stringValue, content: content, trigger: trigger)
        
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
        //get all pending notifications
        UNUserNotificationCenter.current().getPendingNotificationRequests { (notificationRequests) in
            //and ids..
           var identifiers: [String] = []
            //if the notification id matches..
           for notification:UNNotificationRequest in notificationRequests {
               //remove notification if the event has been removed
               //if matches..
               if notification.identifier == event.id.codingKey.stringValue {
                  identifiers.append(notification.identifier)
               }
           }
            //remove it!
            UNUserNotificationCenter.current().removePendingNotificationRequests(withIdentifiers: identifiers)
        }
    }
}
