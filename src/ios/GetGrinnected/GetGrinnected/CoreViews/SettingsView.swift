//
//  SettingsView.swift
//  GetGrinnected
//
//  Created by Michael Paulin on 4/20/25.
//

import Foundation
import SwiftUI

struct SettingsView: View {
    // store standard font size of 20 in app storage
    @AppStorage("FontSize") private var fontSize: Double = 15.0
    
    // access the core data to see the color scheme the device is set to
    @Environment(\.colorScheme) private var userColorScheme
    
    // boolean to keep track if we are logged in
    @Binding var isLoggedIn: Bool
    // our users username
    @State private var username: String = "user123"
    
    @StateObject private var userProfile = UserProfile()
    
    // current view color scheme
    @State private var viewColorScheme: ColorScheme = .light
    // boolean that says if we are on light mode or not
    @State private var lightModeOn: Bool = true
    @State private var basicInput: String = ""
    
    var body: some View {
        
        GeometryReader{proxy in
            let safeAreaTop = proxy.safeAreaInsets.top
            VStack(){
                // Header is outside of scrollable so it does not move
                Header(inputText: $basicInput, safeAreaTop: safeAreaTop, title: "Settings", searchBarOn: false)
                
                ScrollView(.vertical, showsIndicators: false){
                    
                    //content
                    VStack {
                        HStack {
                            // spacer to right align button
                            Spacer()
                            
                            // Logout button
                            Button(action: {
                                // we are logging out
                                isLoggedIn = false
                            }) {
                                Text("Logout")
                                    .foregroundColor(.border)
                                Image(systemName: "rectangle.portrait.and.arrow.right")
                                    .imageScale(.large)
                            } //Button
                            .navigationDestination(isPresented: $isLoggedIn) {
                                ContentView()
                            }
                        }
                        
                        // change username field
                        InputView(text: $username, title: "Change Username", placeholder: "Username")
                            .padding()
                            .background( //background color as app container
                                ZStack {
                                    Color.appContainer
                                        .opacity(0.75)
                                    RoundedRectangle(cornerRadius: 8)
                                        .stroke(Color.appBorder, lineWidth: 1)
                                }
                            )//background
                            .cornerRadius(8)
                        
                        // switch for light/dark mode
                        Toggle(lightModeOn ? "Light Mode" : "Dark Mode", systemImage: lightModeOn ? "lightswitch.on" : "lightswitch.off", isOn: $lightModeOn)
                            .padding()
                            .background( //background color as app container
                                ZStack {
                                    Color.appContainer
                                        .opacity(0.75)
                                    RoundedRectangle(cornerRadius: 8)
                                        .stroke(Color.appBorder, lineWidth: 1)
                                }
                            )//background
                            .cornerRadius(8)
                        
                        VStack{
                            Text("Font Size")
                                .padding(.top)
                            
                            // slider for text size
                            Slider(
                                value: $fontSize,
                                in: 10...45,
                                // has 5 discrete steps instead of a continous slider
                                step: 5
                            ) {} minimumValueLabel: {
                                Text("Aa")
                                    .font(.system(size: 10))
                            } maximumValueLabel: {
                                Text("Aa")
                                    .font(.system(size: 45))
                            }
                            .padding()
                            
                        }
                        .background( //background color as app container
                            ZStack {
                                Color.appContainer
                                    .opacity(0.75)
                                RoundedRectangle(cornerRadius: 8)
                                    .stroke(Color.appBorder, lineWidth: 1)
                            }
                        )//background
                        .cornerRadius(8)
                    } //VStack
                    .padding()
                    
                }   //vstack
            }   //Scroll view
            .edgesIgnoringSafeArea(.top)
            .foregroundColor(.border)
            .tint(.border)
            
        }//GeometryReader
        .preferredColorScheme(viewColorScheme)
        // run switchAppearance when the view is shown
        .onAppear {
            switchAppearance()
        }
        // changes the viewColorScheme when lightModeOn is changed
        .onChange(of: lightModeOn){ oldValue, newValue in
            if newValue == true {
                viewColorScheme = .light
            } else {
                viewColorScheme = .dark
            }
        } //onChange
    } //body
    
    /*
     Changes the lightModeOn boolean based on what the current viewColorScheme is
     Referenced from https://www.youtube.com/watch?v=JCCImOLui5E
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
    SettingsView(isLoggedIn: .constant(true))
}
