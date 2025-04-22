//
//  CheckBoxPicker.swift
//  GetGrinnected
//
//  Created by Michael Paulin on 4/6/25.
//

import SwiftUI

/*
 CheckBoxOption is the structure that holds the name and status of check box option.
 
 name: A string that holds the displayed name of the option
 isChecked: A boolean that is true if the option is checked. Otherwise, false.
 uiCompOne: The component that is displayed when the check box is checked.
 uiCompTwo: The component that is displayed when the check box is not checked.
 */
struct CheckBoxOption {
    var name: String?
    var isChecked: Bool
    var uiCompOne: String
    var uiCompTwo: String
}

struct CheckBox: View {
    
    @State var items: [CheckBoxOption]
    var body: some View {
        VStack {
            // Display each check box item
            ForEach($items, id: \.name) { $item in
                HStack {
                    Image(systemName: item.isChecked ? item.uiCompOne : item.uiCompTwo)
                        .foregroundColor(item.isChecked ? .colorBlue : Color(.gray))
                        .onTapGesture { // if image is tapped, toggle checkmark
                            item.isChecked.toggle()
                        }
                    
                    if(item.name != nil) {
                        Text(item.name!)
                            .onTapGesture { // if text is tapped, toggle checkmark
                                item.isChecked.toggle()
                            } //Text
                    } //if
                } //HStack
            } // ForEach
        } //VStack
    } //body
} // CheckBox

#Preview {
    CheckBox(items: [CheckBoxOption(name: "Not checked", isChecked: false, uiCompOne: "checkmark.square.fill", uiCompTwo: "square"),
                     CheckBoxOption(name: "Checked", isChecked: true, uiCompOne: "checkmark.square.fill", uiCompTwo: "square" )])
}
