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
        
        //register the array transformer for EventDTO (from API) into EventModel (to cache)
        // for tags [String] and organizations [String]
        ArrayTransformer.register()
        
        
        do {
            //attempt to create container for cache
            print("Attempting to create container…")
            
            //schema is what kind of information is stored in the model, for now it's just eventmodel, not the profile or organizations (for now)
            let schema = Schema([
                            EventModel.self
                        ])
            //for now, ,
//            UserModel.self //, is notincluded
            
            //model configuration setup, with schema, and..
            let config = ModelConfiguration(
                         schema: schema,
                         isStoredInMemoryOnly: false, //Not stored in memory only means data transfers from open and closing of app
                         allowsSave: true // allows save of data
                     )
            //create container with the model container (configuration and schema
            container = try ModelContainer(for: schema, configurations: [config])
            
            //print if success
            print("Container created successfully!")
        } catch {
            //if fail, print an accurate error message
            print("ModelContainer init failed: \(error.localizedDescription)")
            
            //send out error message using fatalerror
            fatalError("Failed to create ModelContainer: \(error)")
        }
    }//initialize model container
    
    //initialize array of eventDTO (to store JSON from API
    var events = [EventDTO]()
    
    //our API link
    let urlString = "https://node16049-csc324--spring2025.us.reclaim.cloud/"
    
    //body of main view
    var body: some Scene {
        
        //content view contains the signup signin vievw.
        WindowGroup {
            ContentView()
        }
        .modelContainer(container)//set container to model container
    }
}
