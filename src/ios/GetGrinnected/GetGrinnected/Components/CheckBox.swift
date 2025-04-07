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
 */
struct CheckBoxOption {
    var name: String
    var isChecked: Bool
}

struct CheckBox: View {
    @State var items: [CheckBoxOption]
    
    var body: some View {
        VStack {
            // Display each check box item
            ForEach($items, id: \.name) { $item in
                HStack {
                    Image(systemName: item.isChecked ? "checkmark.square.fill" : "square")
                        .foregroundColor(item.isChecked ? Color.green : Color.gray)
                        .onTapGesture { // if image is tapped, toggle checkmark
                            item.isChecked.toggle()
                        }
                    
                    Text(item.name)
                        .onTapGesture { // if text is tapped, toggle checkmark
                            item.isChecked.toggle()
                        }
                } //HStack
            } // ForEach
        } //VStack
    } //body
} // CheckBox

#Preview {
    CheckBox(items: [CheckBoxOption(name: "Not checked", isChecked: false),
                     CheckBoxOption(name: "Checked", isChecked: true)])
}
