//
//  TagPicker.swift
//  GetGrinnected
//
//  Created by Michael Paulin on 4/24/25.
//

import SwiftUI

struct TagPicker: View {
    // The title of the dropdown menu
    @State var title: String
    // The parent view the drop down is in
    @ObservedObject var parentView: EventListParentViewModel
    // Whether or not the dropdown is expanded or not
    @State private var isExpanded: Bool = false
    
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
                VStack {
                    ForEach(EventTags.allCases, id: \.self) { tag in
                        Button(action: {
                            // TODO
                            // Change parentview selected tags to contain or not contain tag
                        }) {
                            // make a checkbox for current item
                            CheckBox(item: CheckBoxOption(name: tag.rawValue, isChecked: false, uiCompOne: "checkmark.square.fill", uiCompTwo: "square", fillColor: .border))
                        }
                    }
                }
            }
        }
    }
}

#Preview {
    TagPicker(title: "test", parentView: EventListParentViewModel())
}
