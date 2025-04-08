//
//  ProfileView.swift
//  GetGrinnected
//
//  Created by Ellie Seehorn on 3/4/25.
//

import Foundation
import SwiftUI

struct ProfileView: View {
    @State private var email = ""
    
    var body: some View {
        NavigationStack {
            Header(title: "Profile") // add header to view
            
            VStack {
                Image(systemName:"person.crop.circle.badge.plus")
                    .imageScale(.large)
                    .font(.system(size: 100))
                    .foregroundStyle(.blue, .gray)
                
                InputView(text: $email, title: "Change Email", placeholder: "Enter your Grinnell Email")
            } //VStack
            
            .padding()
        } //NavigationStack
    } //body
} //ProfileView

#Preview {
    ProfileView()
}
