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
    @State var selectedTags = EventTags.any
    
    var body: some View {
        NavigationStack {
            VStack {
                HStack {
                    // a picker for date
                    DatePicker("Currently viewing date: ", selection: $viewedDate, in: Date.now...lastDate, displayedComponents: .date)
                        .labelsHidden()
                    
                    // a picker for tags
                    Picker("Tags", selection: $selectedTags)
                    {
                        ForEach(EventTags.allCases, id: \.self) { tag in
                            Text(tag.rawValue)
                        }
                    }
                } //HStack
                
                List(0..<100) { i in
                    Text("Row \(i)")}
            } //VStack
            
            .navigationTitle("Home")
            .toolbarBackground(Color.red, for: .navigationBar)
            .toolbarBackground(.visible, for: .navigationBar)
            .toolbar{
                // logo on toolbar. copied from budhil
                Image("white_circle")
                    .resizable()
                    .scaledToFill()
                    .frame(width: 75, height: 75)
                    .padding(.vertical, 32)
                    .overlay(
                        Image("gglogo_nt")
                            .resizable()
                            .scaledToFill()
                            .frame(width: 75, height: 75)
                            .padding(.vertical, 32)
                    )
            } // toolbar
        } //NavigationStack
    } //body
} //HomescreenView

#Preview {
    HomescreenView()
}
