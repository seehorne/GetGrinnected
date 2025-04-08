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
            VStack {
                Image(systemName:"person.crop.circle.badge.plus")
                    .imageScale(.large)
                    .font(.system(size: 100))
                    .foregroundStyle(.blue, .gray)
                
                InputView(text: $email, title: "Change Email", placeholder: "Enter your Grinnell Email")
            } //VStack
            
            .padding()
            .navigationTitle("Profile")
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
} //ProfileView

#Preview {
    ProfileView()
}
