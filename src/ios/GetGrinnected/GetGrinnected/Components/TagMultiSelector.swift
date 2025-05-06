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
    // Whether or not the dropdown is expanded or not
    @State private var isExpanded: Bool = false
    // The tags we have selected
    @State var selectedTags: Set<EventTags> = []
    
    var body: some View {
        Menu (title) {
            // make a row for a clear button
            TagMultiSelectorRow(tag: "Clear",
                                isSelected: false,
                                showCheck: false) {
                // remove tags from selected tags
                selectedTags.removeAll()
            } //TagMultiSelectorRow
            
            //look at each tag
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
        } //Menu
        .buttonStyle(.bordered)
        .foregroundColor(.appTextPrimary)
        .font(.title3)
        .border(.border)
        .menuActionDismissBehavior(.disabled) // make the menu not close when you tap something
    } //body
} //TagMultiSelector

struct TagMultiSelectorRow: View {
    // The text displayed on the row
    let tag: String
    // If the tag is selected or not
    let isSelected: Bool
    // If the check box is shown or not
    let showCheck: Bool
    // The action that the row button will make
    let action: () -> Void
    
    var body: some View {
        Button(action: action) {
            HStack {
                // if we are showing the checkbox
                if showCheck {
                    Image(systemName: isSelected ? "checkmark.square.fill" : "square")
                } //if
                Text(tag)
            } //HStack
        } //Button
    } //body
} //TagMultiSelectorRow

#Preview {
    TagMultiSelector(title: "Tags")
    
    Spacer()
}
