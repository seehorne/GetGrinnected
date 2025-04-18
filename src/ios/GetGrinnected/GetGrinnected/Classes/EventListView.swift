//
//  EventListView.swift
//  GetGrinnected
//
//  Created by Budhil Thijm & Michael Paulin on 4/8/25.
//

import SwiftUI


/**
 Links to look at:
 https://khush7068.medium.com/building-a-swiftui-app-with-mvvm-and-async-await-for-networking-ef777b2bf7e8
 
 https://www.reddit.com/r/SwiftUI/comments/10e8lu6/swiftui_networking_using_mvvm_pattern/
 
 */
//EventViewModel: Find a way to View our Events
class EventViewModel: ObservableObject {
    @Published var events: [Event] = [] //Store Events from our API
    @Published var isLoading = false //check if loading
    @Published var errorMessage: String? = nil //showing the error message
     
    //HERE IS OUR API link
    private let urlString = "https://node16049-csc324--spring2025.us.reclaim.cloud/events"
    
    
    //Function Fetch Events
    /**
     Returns nothing:
     once called, shows isloading ot be true, and resets error message
     */
    func fetchEvents() {
        //reset error message, set loading to be true
        isLoading = true
        errorMessage = nil
        
        //in a task: do-catch outline..
        Task {
            do {
                //get our json string using EventData's fetch-data
                let jsonString = try await EventData.fetchData(urlString: urlString)
                //print out string for testing
                //print("String: \(jsonString)")
                
                //parse events into fetchedEvents from that string
                let fetchedEvents = EventData.parseEvents(json: jsonString)
                
                // Update on main thread since we're changing published properties
                await MainActor.run {
                    //set events and now that we are done, set isLoading to false
                    self.events = fetchedEvents
                    self.isLoading = false
                }//await
            } catch {
                //if error..
                await MainActor.run {
                    //show error
                    self.errorMessage = "Failed to load events: \(error.localizedDescription)"
                    self.isLoading = false
                }
            }// do-catch
        }//Task
    }//fetchEvents
    
    
}

struct EventListView: View {
    @StateObject private var viewModel = EventViewModel()
    
    var body: some View {
        VStack{
            ForEach(viewModel.events, id: \.eventid) { event in
                EventCard(event: event)
                
            }
        
            
            if viewModel.isLoading {
                ProgressView()
            }
            
            if let error = viewModel.errorMessage {
                VStack {
                    Text(error)
                        .foregroundColor(.red)
                    Button("Retry") {
                        viewModel.fetchEvents()
                    }
                    .padding()
                    .background(Color.blue)
                    .foregroundColor(.white)
                    .cornerRadius(8)
                }
            }
                
        }//For all events
        .onAppear(){
            viewModel.fetchEvents()
        }
    }
}

// For preview purposes (assuming Event struct exists)
struct EventListView_Previews: PreviewProvider {
    static var previews: some View {
        EventListView()
    }
}
