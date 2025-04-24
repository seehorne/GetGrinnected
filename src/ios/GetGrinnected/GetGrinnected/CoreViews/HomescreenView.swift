//
//  HomescreenView.swift
//  GetGrinnected
//
//  Created by Ellie Seehorn on 3/4/25.
//

import Foundation
import SwiftUI

struct HomescreenView: View {
    // the parent model used for updating our event list
    @StateObject private var viewModel = EventListParentViewModel()
    @State private var searchText = ""
    
    var body: some View {
        GeometryReader{proxy in
            let safeAreaTop = proxy.safeAreaInsets.top
            VStack(){
                // Header is outside of scrollable so it does not move
                Header(safeAreaTop, title: "Home")
                
                ScrollView(.vertical, showsIndicators: false){
                    
                    //content
                    VStack {
                        //Place Event Picker at top
                        HStack {
                            // a picker for date
                            DatePicker(
                                "Currently viewing date: ",
                                selection: $viewModel.viewedDate,
                                in: Date.now...Date.distantFuture,
                                displayedComponents: .date
                            )
                            .labelsHidden()
                            .padding()
                            
                            // a picker for tags
                            Picker("Tags", selection: $viewModel.selectedTags){
                                ForEach(EventTags.allCases, id: \.self) { tag in
                                    Text(tag.rawValue)
                                }
                            }
                            .padding()
                        } //HStack
                        
                        //Main Event List View
                        EventList(selectedEvent: -1, parentView: viewModel, searchString: searchText)
                            .searchable(text: $searchText)
                        
                        Spacer() // KEEPS DATE, TAGS, AND EVENTS AT TOP
                        
                    } //Scroll view
                    .frame(minHeight: proxy.size.height)//height
                    
                }
            } //VStack
            .edgesIgnoringSafeArea(.top)
//https://stackoverflow.com/questions/67873845/why-my-custom-views-try-to-take-all-the-available-vertical-space-in-vstack
            
        }//GeometryReaderh
        // if the viewedDate changes update timeSpan
        .onChange(of: viewModel.viewedDate) { oldValue, newValue in
            // update start and end of time span when you are viewing a different day
            viewModel.timeSpan.start = newValue
            viewModel.timeSpan.end = newValue.startOfNextDay
        }
    } //body
} //HomescreenView

#Preview {
    HomescreenView()
}
