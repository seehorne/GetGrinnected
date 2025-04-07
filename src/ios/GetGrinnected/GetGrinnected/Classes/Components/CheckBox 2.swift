//
//  CheckBoxPicker.swift
//  GetGrinnected
//
//  Created by Michael Paulin on 4/6/25.
//

import SwiftUI

struct CheckBoxOption {
    var name: String
    var isChecked: Bool
}

struct CheckBox: View {
    @State var items: [CheckBoxOption]
    
    var body: some View {
        VStack {
            ForEach($items, id: \.name) { $item in
                HStack {
                    Image(systemName: item.isChecked ? "checkmark.circle.fill" : "circle")
                        .foregroundColor(item.isChecked ? Color.green : Color.gray)
                        .onTapGesture {
                            item.isChecked.toggle()
                        }
                    Text(item.name)
                }
            }
        }
    }
}

#Preview {
    CheckBox(items: [CheckBoxOption(name: "Not checked", isChecked: false),
                     CheckBoxOption(name: "Checked", isChecked: true)])
}
