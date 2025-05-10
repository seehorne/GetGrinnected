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
    //@State private var userProfile = UserProfile()
    @State private var loggedOut: Bool = false
    
    @State var isFontSizeSelected = false
    @State var isSectionSelected = false
    @State var isAckSelected = false
    
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
                                //isLoggedIn = false
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
                        
                        InputView(text: $username, title: "Change Username", placeholder: "Username")
                            .padding()
                        
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
                            
                            //change username
                            changeUsername()
                            
                            // switch for light/dark mode
                            toggleLightDark()
                            
                            //fontsize
                            fontSizeSelector(selection: isFontSizeSelected)
                                .onTapGesture {
                                    withAnimation(.easeInOut){
                                        isFontSizeSelected.toggle()
                                    }
                                }
                            
                            //about section
                            about(selection: isSectionSelected)
                                .onTapGesture {
                                    withAnimation(.easeInOut){
                                        isSectionSelected.toggle()
                                    }
                                }
                            
                            //acknolwedgements
                            acknowledgements(selection: isAckSelected)
                                .onTapGesture {
                                    withAnimation(.easeInOut){
                                        isAckSelected.toggle()
                                    }
                                }
                            
                            
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
     Changeusername function
     to hold ui components of changing username. this is useful to make it expandable.
     */
    func changeUsername() -> some View {
        // change username field
        InputView(text: $username, title: "Change Username", placeholder: "Username")
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
    }//username change
    
    /**
     toggleLightDark()
     change lightdark
     */
    func toggleLightDark() -> some View {
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
     fontSizeSelector()
     */
    func fontSizeSelector(selection: Bool) -> some View{
        VStack {
                Text("Font Size")
                    .font(.headline)
                    .padding(.top)
                    .padding(.bottom)

                if selection {
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
    }
    
    /**
     section()
     */
    func about(selection: Bool) -> some View {
        VStack{
            Text("About")
                .font(.headline)
                .padding(.top)
            
            if(selection){
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
                }//vstack
                .transition(.asymmetric(
                    insertion: .move(edge: .top),
                    removal: .move(edge: .top)
                ))
            }//selection
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
        
    }
    
    /**
     acknowledgements()
     */
    func acknowledgements(selection: Bool) -> some View {
        VStack{
            Text("Acknowledgements")
                .padding(.top)
                .font(.headline)
            
            
            if(selection){
                VStack{
                    Text("Logo Design")
                        .font(.headline)
                    Text("Rei Yamada")
                    
                    Text("Testing and Stakeholder Feedback")
                        .font(.headline)
                    Text("Yuina Iseki")
                    Text("Lily Freeman")
                    Text("Regan Stambaugh")
                        
                    Text("Development Team")
                        .font(.headline)
                    Text("Ellie Seahorn '25")
                    Text("Michael Paulin '25")
                    Text("Almond Heil '25")
                    Text("Budhil Thijm '25")
                    Text("Ethan Hughes '25")
                    Text("Anthony Schwindt '25")
                    
                    Text("Faculty Instructor")
                        .font(.headline)
                    Text("Professor Leah Pearlmutter")
                }
                .transition(.asymmetric(
                    insertion: .move(edge: .top),
                    removal: .move(edge: .top)
                ))//transition
                
            }//seleciton
            
        }//vstack
        .frame(maxWidth: .infinity)
        .padding(.horizontal)
        .background(
            RoundedRectangle(cornerRadius: 8)
                .fill(Color.appContainer.opacity(0.75))
                .overlay(
                    RoundedRectangle(cornerRadius: 8)
                        .stroke(Color.appBorder, lineWidth: 1)
                )
        )
        .clipShape(RoundedRectangle(cornerRadius: 8))
    }
    
} //ProfileView

#Preview {
    SettingsView(isLoggedIn: .constant(true))
}


