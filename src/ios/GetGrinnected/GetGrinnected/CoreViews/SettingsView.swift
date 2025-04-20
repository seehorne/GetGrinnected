//
//  ProfileView.swift
//  GetGrinnected
//
//  Created by Ellie Seehorn on 3/4/25.
//

import Foundation
import SwiftUI

struct SettingsView: View {
    // the evironment color scheme
    @Environment(\.colorScheme) private var userColorScheme
    
    // our users username
    @State private var username: String = "user123"
    
    // current view color scheme
    @State private var viewColorScheme: ColorScheme = .light
    // boolean that says if we are on light mode or not
    @State private var lightModeOn: Bool = true
    
    var body: some View {
        
        GeometryReader{proxy in
            let safeAreaTop = proxy.safeAreaInsets.top
            ScrollView(.vertical, showsIndicators: false){
                VStack(){
                    Header(safeAreaTop, title: "Settings", searchBarOn: true)
                    
                    
                    //content
                    VStack {
                        InputView(text: $username, title: "Change Username", placeholder: "Username")
                            .padding()
                        
                        // switch for light/dark mode
                        Toggle(lightModeOn ? "Light Mode" : "Dark Mode", systemImage: lightModeOn ? "lightswitch.on" : "lightswitch.off", isOn: $lightModeOn)
                            .padding()
                            .tint(.colorRed)
                            .frame(maxWidth: 200)
                    } //VStack
                    .padding()
                            
                }//vstack
            }//Scroll view
            .edgesIgnoringSafeArea(.top)
            
        }//GeometryReader
        .preferredColorScheme(viewColorScheme)
        // run switchAppearance when the view is shown
        .onAppear {
            switchAppearance()
        }
        // changes the viewColorScheme when lightModeOn is changed
        .onChange(of: lightModeOn){oldValue, newValue in
            if newValue == true {
                viewColorScheme = .light
            } else {
                viewColorScheme = .dark
            }
        } //onChange
    } //body
    
    /*
     Changes the lightModeOn boolean based on what the current viewColorScheme is
     */
    func switchAppearance() {
        viewColorScheme = userColorScheme
        
        // check if we are in light mode and change boolean to match
        if viewColorScheme == .light {
            lightModeOn = true
        } else {
            lightModeOn = false
        }
    } //switchAppearance
} //ProfileView

#Preview {
    SettingsView()
}
