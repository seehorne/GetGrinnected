//
//  GetGrinnectedApp.swift
//  GetGrinnected
//
//  Created by Ellie Seehorn on 3/4/25.
//

import SwiftUI
import SwiftData

@main
struct GetGrinnectedApp: App {
    //container for persistent states
    var sharedModelContainer: ModelContainer = {
           let schema = Schema([EventModel.self]) //removing profile for now , UserProfile.self
           let modelConfiguration = ModelConfiguration(schema: schema, isStoredInMemoryOnly: false)
           
           do {
               return try ModelContainer(for: schema, configurations: [modelConfiguration])
           } catch {
               fatalError("Could not create ModelContainer: \(error)")
           }
       }()
    
    //initialize array of events
    var events = [EventDTO]()
    //our API link
    let urlString = "https://node16049-csc324--spring2025.us.reclaim.cloud/"
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
        .modelContainer(sharedModelContainer)//set container to model container
    }
}
