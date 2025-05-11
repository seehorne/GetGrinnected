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
    // The parent view where we store the selected events
    @ObservedObject var parentView: EventListParentViewModel
    
    var body: some View {
        Menu () {
            if !parentView.possibleTags.isEmpty {
                // make a row for a clear button
                TagMultiSelectorRow(tag: "Clear",
                                    isSelected: false,
                                    showCheck: false) {
                    // remove tags from selected tags
                    parentView.selectedTags.removeAll()
                } //TagMultiSelectorRow
                
                //look at each tag
                ForEach(Array(parentView.possibleTags).sorted(), id: \.self) { tag in
                    // make a row for the current tag that is checked if selected or not
                    TagMultiSelectorRow(tag: tag,
                                        isSelected: parentView.selectedTags.contains(tag),
                                        showCheck: true) {
                        // add or remove tag if tapped
                        if parentView.selectedTags.contains(tag) {
                            parentView.selectedTags.remove(tag)
                        } else {
                            parentView.selectedTags.insert(tag)
                        }
                    } //TagMultiSelectorRow
                } //ForEach
            } else {
                Text("No tags found")
            }
        } label: {
            Label(title, systemImage: "chevron.down")
                .padding(.all, 3)
                .font(.subheadline)
                .foregroundColor(.white)
                .background(Color.appContainer.opacity(0.75))
                .cornerRadius(10)
                .overlay(
                    RoundedRectangle(cornerRadius: 10)
                        .stroke(Color.appContainer, lineWidth: 1)
                )
        }
        .foregroundColor(.appTextPrimary)
        .font(.title3)
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
    var parentView = EventListParentViewModel()
    parentView.possibleTags = ["test 1", "test 2", "test 3"]
    
    return VStack {
        TagMultiSelector(title: "Tags", parentView: parentView)
        Spacer()
    }
}
