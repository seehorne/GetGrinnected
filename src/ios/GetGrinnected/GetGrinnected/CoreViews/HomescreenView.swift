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
    @State private var selectedTags = EventTags.any
    
    var body: some View {
        GeometryReader{proxy in
            let safeAreaTop = proxy.safeAreaInsets.top
            ScrollView(.vertical, showsIndicators: false){
                VStack(){
                    Header(safeAreaTop, title: "Home", searchBarOn: true)
                    
                    
                    //content
                    VStack {
                        //Place Event Picker at top
                        HStack {
                            // a picker for date
                            DatePicker(
                                "Currently viewing date: ",
                                selection: $viewedDate,
                                in: Date.now...lastDate,
                                displayedComponents: .date
                            )
                            .labelsHidden()
                            .padding()
                        
                            // a picker for tags
                            Picker("Tags", selection: $selectedTags){
                                ForEach(EventTags.allCases, id: \.self) { tag in
                                    Text(tag.rawValue)
                                }
                            }
                            .padding()
                        } //HStack
                    
                        //Main Event List View
                        EventListView(timeSpan: DateInterval(start: viewedDate, end: viewedDate.addingTimeInterval(86400)))

                    } //VStack
                    .frame(minHeight: proxy.size.height)//height
                            
                }
            }//Scroll view
            .edgesIgnoringSafeArea(.top)
//https://stackoverflow.com/questions/67873845/why-my-custom-views-try-to-take-all-the-available-vertical-space-in-vstack
            
        }//GeometryReaderh
    } //body
} //HomescreenView

#Preview {
    HomescreenView()
}
