//
//  TagMultiSelector.swift
//  GetGrinnected
//
//  Created by Michael Paulin on 4/24/25.
//

import SwiftUI

struct TagMultiSelector: View {
    // The title of the dropdown menu
    @State var title: String
    // The parent view the drop down is in
    @ObservedObject var parentView: EventListParentViewModel
    // Whether or not the dropdown is expanded or not
    @State private var isExpanded: Bool = false
    
    @State var selectedTags: Set<EventTags> = []
    
    var body: some View {
        VStack {
            Button(action: {
                // toggle if it is expanded
                withAnimation {
                    isExpanded.toggle()
                }
            }) {
                // show the title and a chevron the changes when expanded
                Text(title)
                    .foregroundColor(.appTextPrimary)
                Image(systemName: isExpanded ? "chevron.down" : "chevron.right")
                    .foregroundColor(.appTextSecondary)
            }
            
            // display checkbox buttons when expanded
            if isExpanded {
                ScrollView(showsIndicators: true) {
                    VStack(spacing: 0) {
                        // make a clear button that unselects all tags
                        TagMultiSelectorRow(tag: "Clear",
                                            isSelected: false,
                                            showCheck: false) {
                            // remove all tags if selected
                            selectedTags.removeAll()
                        } //TagMultiSelectorRow
                        
                        // look at each tag
                        ForEach(EventTags.allCases, id: \.self) { tag in
                            // make a row for the current tag that is checked if selected or not
                            TagMultiSelectorRow(tag: tag.rawValue,
                                                isSelected: selectedTags.contains(tag),
                                                showCheck: true) {
                                // add or remove tag if tapped
                                if selectedTags.contains(tag) {
                                    selectedTags.remove(tag)
                                } else {
                                    selectedTags.insert(tag)
                                }
                            } //TagMultiSelectorRow
                        } //ForEach
                    } //VStack
                }
            } //if
        } //VStack
        .background( // apply a rounded background
            RoundedRectangle(cornerRadius: 4) // apply curvature
                .stroke(.border, lineWidth: 1) // apply border and color and a thickness of 1
        ) //background
    } //body
} //TagMultiSelector

struct TagMultiSelectorRow: View {
    let tag: String
    let isSelected: Bool
    let showCheck: Bool
    let toggleSelection: () -> Void
    
    var body: some View {
        Button(action: toggleSelection) {
            HStack {
                Text(tag)
                    .padding()
                if showCheck {
                    Image(systemName: isSelected ? "checkmark.square.fill" : "square")
                        .foregroundStyle(.border)
                        .padding(.trailing)
                        .imageScale(.large)
                } //if
            } //HStack
        } //Button
        .buttonStyle(PlainButtonStyle())
        .overlay(Divider(), alignment: .bottom)
    } //body
} //TagMultiSelectorRow

#Preview {
    TagMultiSelector(title: "Tags", parentView: EventListParentViewModel())
}
