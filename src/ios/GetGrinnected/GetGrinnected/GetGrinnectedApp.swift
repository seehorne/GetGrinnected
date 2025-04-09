//
//  GetGrinnectedApp.swift
//  GetGrinnected
//
//  Created by Ellie Seehorn on 3/4/25.
//

import SwiftUI
@main
struct GetGrinnectedApp: App {
    //initialize array of events
    var events = [Event]()
    //our API link
    let urlString = "https://node16049-csc324--spring2025.us.reclaim.cloud/"
    
    var body: some Scene {
        WindowGroup {
            ContentView()
            //TesterView()
        }
    }
}
