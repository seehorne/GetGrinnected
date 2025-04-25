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
    let container: ModelContainer
    
    init() {
        
        ArrayTransformer.register()
        do {
            print("Attempting to create container…")
            container = try ModelContainer(
                for: EventModel.self,
                configurations: ModelConfiguration(isStoredInMemoryOnly: false)
            )
            print("Container created successfully!")
        } catch {
            print("❌ ModelContainer init failed: \(error.localizedDescription)")
            fatalError("Failed to create ModelContainer: \(error)")
        }
    }
    
    //initialize array of events
    var events = [EventDTO]()
    //our API link
    let urlString = "https://node16049-csc324--spring2025.us.reclaim.cloud/"
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
        .modelContainer(container)//set container to model container
    }
}
