//
//  InputField.swift
//  GetGrinnected
//
//  Created by Budhil Thijm on 4/1/25.
//

import SwiftUI
struct InputView: View {
    @Binding var text: String
    let title: String
    let placeholder: String
    var isSecureField = false
    var body: some View {
        VStack (alignment: .leading, spacing: 12) {
            Text(title)
                .foregroundColor (Color(.darkGray))
                .fontWeight(.semibold)
                .font(.footnote)
            if isSecureField {
                SecureField(placeholder, text: $text)
                    .font(.callout)
                    .autocapitalization(.none)
            } else {
                TextField(placeholder, text: $text)
                    .font(.callout)
                    .autocapitalization(.none)
            }
            Divider ()
        }
    }
}
struct InputView_Previews: PreviewProvider {
    static var previews: some View {
        InputView(text: .constant(" "), title: "Email Address", placeholder:
                    "name@grinnell.com")
    }
}
