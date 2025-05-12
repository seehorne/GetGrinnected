//
//  SettingsView.swift
//  GetGrinnected
//
//  Created by Michael Paulin on 4/20/25.
//

import Foundation
import SwiftUI
import Combine

struct SettingsView: View {
    // store standard font size of 20 in app storage
    @AppStorage("FontSize") private var fontSize: Double = 15.0
    
    // access the core data to see the color scheme the device is set to
    @Environment(\.colorScheme) private var userColorScheme
    
    // boolean to keep track if we are logged in
    @Binding var isLoggedIn: Bool
    // our users username
    @State private var username: String = "user123"
    // our users email
    @State private var email: String = "email@email.com"
    
    @StateObject private var userProfile = UserProfile()
    
    // current view color scheme
    @State private var viewColorScheme: ColorScheme = .light
    // boolean that says if we are on light mode or not
    @State private var lightModeOn: Bool = true
    @State private var basicInput: String = ""
    //@State private var userProfile = UserProfile()
    @State private var loggedOut: Bool = false
    
    @State var isProfileSelected = false
    @State var isAppearanceSelected = false
    @State var isAccessibilitySelected = false
    @State var isAboutSelected = false
    
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
                                userProfile.updateLoginState(isLoggedIn: false)
                                loggedOut = true
                            }) {
                                Text("Logout")
                                    .foregroundColor(.border)
                                Image(systemName: "rectangle.portrait.and.arrow.right")
                                    .imageScale(.large)
                            } //Button
                            .navigationDestination(isPresented: $loggedOut) {
                                ContentView()
                            }
                        }
                        
                        //content
                        VStack {
                            profileEditing()
                            
                            // switch for light/dark mode
                            appearance()
                            
                            //fontsize
                            accessibility()
                            
                            //about section
                            about()
                            
                            
                        } //about
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
        }
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
    } //switchAppearance\
    
    
    //because we are using this in main, we are to use "some View" and not "any View"

    /**
     Profile editing function
     to hold ui components of changing username and email. this is useful to make it expandable.
     */
    func profileEditing() -> some View {
        VStack {
            HStack {
                Text("Profile")
                Image(systemName: isProfileSelected ? "chevron.down" : "chevron.left")
            }
            .font(.headline)
            .padding()
            
            if isProfileSelected {
                // change username field
                HStack {
                    Text("Username:")
                    
                    Spacer()
                    
                    TextField("Username", text: $username)
                }
                .padding(.horizontal)
                .padding(.bottom)
                .transition(.asymmetric(
                    insertion: .move(edge: .top),
                    removal: .move(edge: .top)
                ))//transition so that it pulls down instead of appearing out of nowhere
//                    .transition(.move(edge: .top)) //alternative transition
                
                // change email field
                HStack {
                    Text("Email:")
                    
                    Spacer()
                    
                    TextField("Email", text: $email)
                }
                .padding(.horizontal)
                .padding(.bottom)
                .transition(.asymmetric(
                    insertion: .move(edge: .top),
                    removal: .move(edge: .top)
                ))//transition so that it pulls down instead of appearing out of nowhere
//                    .transition(.move(edge: .top)) //alternative transition
                
                // username change
                Button(action: {
                    print(username)
                    // set the username that has been typed
                    userProfile.setUsername(username)
                    print("username submitted")
                    print(username)
                }) {
                    Text("Submit username change")
                        .foregroundColor(.border)
                    Image(systemName: "square.and.arrow.up")
                        .imageScale(.large)
                } //Button
                .padding(.horizontal)
                .padding(.bottom)
                .transition(.asymmetric(
                    insertion: .move(edge: .top),
                    removal: .move(edge: .top)
                ))//transition so that it pulls down instead of appearing out of nowhere
//                    .transition(.move(edge: .top)) //alternative transition
            }
        }
        .frame(maxWidth: .infinity)
        .background(
            RoundedRectangle(cornerRadius: 8)
                .fill(Color.appContainer.opacity(0.75))
                .overlay(
                    RoundedRectangle(cornerRadius: 8)
                        .stroke(Color.appBorder, lineWidth: 1)
                )
        )
        .clipShape(RoundedRectangle(cornerRadius: 8))
        .onTapGesture {
            withAnimation(.easeInOut){
                isProfileSelected.toggle()
            }
        }
    }//username change
    
    /**
     appearance()
     change lightdark
     */
    func appearance() -> some View {
        Toggle(lightModeOn ? "Light Mode" : "Dark Mode", systemImage: lightModeOn ? "lightswitch.on" : "lightswitch.off", isOn: $lightModeOn)
            .padding()
            .background(
                RoundedRectangle(cornerRadius: 8)
                    .fill(Color.appContainer.opacity(0.75))
                    .overlay(
                        RoundedRectangle(cornerRadius: 8)
                            .stroke(Color.appBorder, lineWidth: 1)
                    )
            )
            .clipShape(RoundedRectangle(cornerRadius: 8))
    }//lightdark
    
    /**
     accessibility()
     */
    func accessibility() -> some View{
        VStack {
                HStack{
                    Text("Accessibility")
                    Image(systemName: isAccessibilitySelected ? "chevron.down" : "chevron.left")
                }
                .font(.headline)
                .padding(.top)
                .padding(.bottom)

                if isAccessibilitySelected {
                    VStack {
                        // slider for text size
                        Slider(
                            value: $fontSize,
                            in: 9...48,
                            step: 3
                        ) {} minimumValueLabel: {
                            Text("Aa")
                                .font(.system(size: 9))
                        } maximumValueLabel: {
                            Text("Aa")
                                .font(.system(size: 45))
                        }
                        .padding()
                    }
                    .transition(.asymmetric(
                        insertion: .move(edge: .top),
                        removal: .move(edge: .top)
                    ))//transition so that it pulls down instead of appearing out of nowhere
//                    .transition(.move(edge: .top)) //alternative transition
                }
            }
            .frame(maxWidth: .infinity)
            .background(
                RoundedRectangle(cornerRadius: 8)
                    .fill(Color.appContainer.opacity(0.75))
                    .overlay(
                        RoundedRectangle(cornerRadius: 8)
                            .stroke(Color.appBorder, lineWidth: 1)
                    )
            )
            .clipShape(RoundedRectangle(cornerRadius: 8))
            .onTapGesture {
                withAnimation(.easeInOut){
                    isAccessibilitySelected.toggle()
                }
            }
    }
    
    /**
     section()
     */
    func about() -> some View {
        VStack{
            HStack {
                Text("About")
                Image(systemName: isAboutSelected ? "chevron.down" : "chevron.left")
            }
                .font(.headline)
                .padding(.top)
                .padding(.bottom)
            
            
            if(isAboutSelected){
                VStack{
                    Text("Get Grinnected was developed by Grinnelians, for Grinnellians, with the goal of creating an intuitive and accessible platform to stay informed abotu campus events.")
                    Text("We have a discord, so for those interested in expanding the toolbox offered to Grinnellians, please join! Here are our socials and github: ")
                    HStack{
                        Link(destination: URL(string: "https://discord.gg/e4PrM4RyEr")!) {
                            Image("discord_logo")
                                .resizable()
                                .frame(width: 50, height: 50)
                        }
                        
                        // GitHub Link
                        Link(destination: URL(string: "https://github.com/seehorne/GetGrinnected")!) {
                            Image("github_logo")
                                .resizable()
                                .frame(width: 50, height: 50)
                        }
                        
                    }//hstack
                    .padding()
                }//vstack
                .transition(.asymmetric(
                    insertion: .move(edge: .top),
                    removal: .move(edge: .top)
                ))
                .padding(.horizontal)
            }//selection
            
            if(isAboutSelected){
                VStack{
                    Text("Acknowledgements")
                        .font(.title2)
                        .padding(.bottom)
                    
                    Text("Logo Design")
                        .font(.headline)
                    Text("Rei Yamada")
                        .padding(.bottom)
                    
                    Text("Testing and Stakeholder Feedback")
                        .font(.headline)
                    Text("Yuina Iseki")
                    Text("Lily Freeman")
                    Text("Regan Stambaugh")
                        .padding(.bottom)
                        
                    Text("Development Team")
                        .font(.headline)
                    Text("Ellie Seehorn '25")
                    Text("Michael Paulin '25")
                    Text("Almond Heil '25")
                    Text("Budhil Thijm '25")
                    Text("Ethan Hughes '25")
                    Text("Anthony Schwindt '25")
                        .padding(.bottom)
                    
                    Text("Faculty Instructor")
                        .font(.headline)
                    Text("Professor Leah Pearlmutter")
                        .padding(.bottom)
                }
                .transition(.asymmetric(
                    insertion: .move(edge: .top),
                    removal: .move(edge: .top)
                ))//transition
                
            }//seleciton
        }//vstack
        .frame(maxWidth:.infinity)
        .background(
            RoundedRectangle(cornerRadius: 8)
                .fill(Color.appContainer.opacity(0.75))
                .overlay(
                    RoundedRectangle(cornerRadius: 8)
                        .stroke(Color.appBorder, lineWidth: 1)
                )
        ) //background
        .clipShape(RoundedRectangle(cornerRadius: 8))
        .onTapGesture {
            withAnimation(.easeInOut){
                isAboutSelected.toggle()
            }
        }
        
    }
    
} //ProfileView

#Preview {
    SettingsView(isLoggedIn: .constant(true))
}


