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
    
    @State private var toVerifyMessage = "Verify your new email"
    
    // our users username
    @State private var username: String
    //string to display if username things were successful or had errors
    @State private var usernameResponseMessage: String = ""
    //our users email
    @State private var email: String
    //string to display if username things were successful or had errors
    @State private var emailResponseMessage: String = ""
    @State private var verificationCode: String = ""
    
    @StateObject private var userProfile = UserProfile()
    
    // current view color scheme
    @State private var viewColorScheme: ColorScheme = .light
    // boolean that says if we are on light mode or not
    @State private var lightModeOn: Bool = true
    
    @State private var triedToPass: Bool = false
    @State private var loggedOut: Bool = false
    
    @State var isProfileSelected = false
    @State var isAppearanceSelected = false
    @State var isAccessibilitySelected = false
    @State var isAboutSelected = false

    @State var showUsernameEditAlert = false
    @State var showEmailEditAlert = false
    @State var showVerificationAlert = false
    @State var showDeleteAccountAlert = false
    @State var showLogOutAlert = false
    
    init(username: String, email: String, isLoggedIn: Binding<Bool>) {
        self._isLoggedIn = isLoggedIn
        self._username = State(initialValue: UserDefaults.standard.string(forKey: "username") ?? "current username could not be loaded")
        self._email = State(initialValue: UserDefaults.standard.string(forKey: "email") ?? "current username could not be loaded")
    }
    
    
    var body: some View {
        GeometryReader{proxy in
            let safeAreaTop = proxy.safeAreaInsets.top
            
            VStack(){
                // Header is outside of scrollable so it does not move
                Header(safeAreaTop: safeAreaTop, title: "Settings", searchBarOn: false)
                
                ScrollView(.vertical, showsIndicators: false){
                        
                        //content
                        VStack {
                            profileEditing()
                            
                            // switch for light/dark mode
                            appearance()
                            
                            //fontsize
                            accessibility()
                            
                            //about section
                            about()
                            
                            //delete account button
                            deleteAccount()
                            
                            //logout button
                            logOut()
                        } //about
                        .padding()
                    }   //Scroll view
                    .foregroundColor(.border)
                    .tint(.border)
                    
                } //VStack
                .edgesIgnoringSafeArea(.top)
            } //GeometryReader
            .preferredColorScheme(viewColorScheme)
            //run switchAppearance when the view is shown
            .onAppear {
                switchAppearance()
                userProfile.getUsername()
                username = UserDefaults.standard.string(forKey: "username") ?? "current username could not be loaded"
                email = UserDefaults.standard.string(forKey: "email") ?? "current email could not be loaded"
            }
            // changes the viewColorScheme when lightModeOn is changed
            .onChange(of: lightModeOn){ oldValue, newValue in
                if newValue == true {
                    viewColorScheme = .light
                } else {
                    viewColorScheme = .dark
                }
            } //onChange
            .navigationDestination(isPresented: $triedToPass) {
                VerificationView(email: email, message: toVerifyMessage, isLoggedIn: $isLoggedIn)
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
            .accessibilityAddTraits(.isButton)
            .accessibilityLabel("Profile section")
            .accessibilityHint(isProfileSelected ? "Double tap to collapse" : "Double tap to expand")
            .font(.headline)
            .padding()
            
            if isProfileSelected {
                // change username field
                HStack {
                    Text("Username:")
                        .accessibilityLabel("Current username is \(username)")
                    
                    Spacer()
                    
                    Text("\(username)")
                    
                    Button(action: {
                        showUsernameEditAlert.toggle()
                    }) {
                        Image(systemName: "pencil")
                        .accessibilityLabel("Edit username")
                    } //Button
                    .accessibilityElement(children: .combine)
                    .accessibilityLabel("Username, \(username)")
                    .accessibilityHint("Double tap to edit username")
                    .alert("Enter new username", isPresented: $showUsernameEditAlert) {
                        TextField("Username", text: $username)
                        Button("OK", action: {showUsernameEditAlert.toggle()})
                    } //alert
                }
                .padding(.horizontal)
                .padding(.bottom)
                .transition(.asymmetric(
                    insertion: .move(edge: .top),
                    removal: .move(edge: .top)
                ))//transition so that it pulls down instead of appearing out of nowhere
                
                //username message
                HStack{
                        Text(usernameResponseMessage)
                            .foregroundColor(.appBorder)
                            .font(.caption)
                }
                .padding(.horizontal)
                .padding(.bottom)
                .transition(.asymmetric(
                    insertion: .move(edge: .top),
                    removal: .move(edge: .top)
                ))//transition so that it pulls down instead of appearing out of nowhere
                
                // change email field
                HStack {
                    Text("Email:")
                    .accessibilityLabel("Current email is \(email)")
                    
                    Spacer()
                    
                    Text("\(email)")
                    
                    Button(action: {
                        showEmailEditAlert.toggle()
                    }) {
                        Image(systemName: "pencil")
                        .accessibilityLabel("Edit email")
                    } //Button
                    .accessibilityElement(children: .combine)
                    .accessibilityLabel("Email, \(email)")
                    .accessibilityHint("Double tap to edit email. Remember, this still needs to be a Grinnell email")
                    .alert("Enter new email", isPresented: $showEmailEditAlert) {
                        TextField("Email", text: $email)
                        Button("OK", action: {showEmailEditAlert.toggle()})
                    } //alert
                }
                .padding(.horizontal)
                .padding(.bottom)
                .transition(.asymmetric(
                    insertion: .move(edge: .top),
                    removal: .move(edge: .top)
                ))//transition so that it pulls down instead of appearing out of nowhere=
                
                //email message
                HStack{
                    if !emailResponseMessage.isEmpty{
                        Text(emailResponseMessage)
                            .foregroundColor(.appBorder)
                            .font(.caption)
                    }
                }
                .padding(.horizontal)
                .padding(.bottom)
                .transition(.asymmetric(
                    insertion: .move(edge: .top),
                    removal: .move(edge: .top)
                ))//transition so that it pulls down instead of appearing out of nowhere
                
                // submit changes
                Button(action:  {
                    print(username)
                    print(email)
                    // set the username that has been typed iff the username has been changed
                    if username != UserDefaults.standard.string(forKey: "username")!{
                        userProfile.setUsername(newUsername: username){ result in
                            switch result {
                            case .success(let output):
                                //if succeeded, log it
                                print("API Response: \(output)")
                                print("changing username message")
                                usernameResponseMessage = "Username successfully changed"
                                userProfile.setLocalUsername(newUsername: username)
                            case .failure(let error):
                                print("API call failed:\(error.localizedDescription)")
                                //if failed, put back to original username
                                username = UserDefaults.standard.string(forKey: "username")!
                                if let apiError = error as? UserProfile.APIError {//treat the error as API error object
                                    switch apiError {
                                    case .usernameError(let message):
                                        usernameResponseMessage = message //use the response message if there was one
                                    default:
                                        usernameResponseMessage = apiError.localizedDescription
                                    }
                                } else {
                                    usernameResponseMessage = error.localizedDescription
                                }
                            }
                        }
                    }
                    else {
                        print("username didn't actually change")
                    }
                    //updates the email iff the email has been changed. esp important bc if this one gets called erroneously theyll be asked to verify erroneously and no one likes that
                    if email != UserDefaults.standard.string(forKey: "email")!{
                        userProfile.setEmail(newEmail: email){ result in
                            switch result {
                            case .success(let output):
                                showVerificationAlert.toggle()
                                DispatchQueue.main.async {
                                    triedToPass = true
                                    //if succeeded, log it
                                    print("API Response: \(output)")
                                    emailResponseMessage = "Email submit for change. Watch out for a code at the new address"
                                }
                            case .failure(let error):
                                email = UserDefaults.standard.string(forKey: "email")!
                                print("API call failed:\(error.localizedDescription)")
                                if let apiError = error as? UserProfile.APIError {//treat the error as API error object
                                                switch apiError {
                                                case .emailError(let message):
                                                    DispatchQueue.main.async {
                                                        emailResponseMessage = message //use the response message if there was one
                                                    }
                                                default:
                                                    DispatchQueue.main.async {
                                                        emailResponseMessage = apiError.localizedDescription
                                                    }
                                                }
                                            } else {
                                                emailResponseMessage = error.localizedDescription
                                            }
                            }
                        }
                    }
                    else{
                        print("email didn't actually change")
                    }
                    print("username submitted")
                    print(username)
                }) {
                    Text("Submit changes")
                        .foregroundColor(.border)
                    Image(systemName: "square.and.arrow.up")
                        .imageScale(.large)
                } //Button
                .accessibilityHint("Saves any changes made to username or email")
                .alert("Enter verification code", isPresented: $showVerificationAlert) {
                    TextField("Verification code", text: $verificationCode)
                        .accessibilityLabel("Verification code input field")
                    Button("Cancel", role: .cancel) {
                        email = UserDefaults.standard.string(forKey: "email")!
                        //set email back to original email
                    }
                    Button("OK", action: {
                        print($verificationCode)
                        userProfile.verifyUser(email: email, code: verificationCode) { result in
                            switch result {
                            case .success(let output):
                                //if succeeded, log it
                                print("API Response: \(output)")
                                userProfile.setLocalEmail(newEmail: self.email)
                            case .failure(let error):
                                email = UserDefaults.standard.string(forKey: "email")!
                                print("API call failed:\(error.localizedDescription)")
                                if let apiError = error as? UserProfile.APIError {//treat the error as API error object
                                                switch apiError {
                                                case .signInError(let message):
                                                    emailResponseMessage = message //use the response message if there was one
                                                default:
                                                    emailResponseMessage = apiError.localizedDescription
                                                }
                                            } else {
                                                emailResponseMessage = error.localizedDescription
                                            }
                            }
                        }
                        showVerificationAlert.toggle()})
                } //alert
                .padding(.horizontal)
                .padding(.bottom)
                .transition(.asymmetric(
                    insertion: .move(edge: .top),
                    removal: .move(edge: .top)
                ))//transition so that it pulls down instead of appearing out of nowhere
                
                
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
                usernameResponseMessage = ""
                emailResponseMessage = ""
            }
        }
        .navigationDestination(isPresented: $triedToPass) {
        VerificationView(email: email, message: toVerifyMessage, isLoggedIn: $isLoggedIn)
    }
    }//username change
    
    /**
     appearance()
     change lightdark
     */
    func appearance() -> some View {
        VStack {
            HStack {
                Text("Appearance")
                Image(systemName: isAppearanceSelected ? "chevron.down" : "chevron.left")
            }
            .font(.headline)
            .padding()
            .accessibilityElement(children: .combine)
            .accessibilityAddTraits(.isButton)
            .accessibilityLabel("Appearance section")
            .accessibilityHint(isAppearanceSelected ? "Double tap to collapse appearance settings" : "Double tap to expand appearance settings")
            
            if isAppearanceSelected {
                Toggle(lightModeOn ? "Light Mode" : "Dark Mode", systemImage: lightModeOn ? "lightswitch.on" : "lightswitch.off", isOn: $lightModeOn)
                    .padding()
                    .accessibilityLabel("Appearance mode toggle")
                    .accessibilityValue(lightModeOn ? "Light mode enabled" : "Dark mode enabled")
                    .accessibilityHint("Double tap to switch between light and dark mode")
            }
        } //VStack
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
                isAppearanceSelected.toggle()
            }
        }
    }//appearance
    
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
                .accessibilityElement(children: .combine)
                .accessibilityAddTraits(.isButton)
                .accessibilityLabel("Accessibility section")
                .accessibilityHint(isAccessibilitySelected ? "Double tap to collapse accessibility settings" : "Double tap to expand accessibility settings")

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
                        .accessibilityLabel("Text size")
                        .accessibilityValue("\(Int(fontSize)) points")
                        .accessibilityHint("Swipe up or down with one finger to adjust text size")
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
                .accessibilityElement(children: .combine)
                .accessibilityAddTraits(.isButton)
                .accessibilityLabel("About section")
                .accessibilityHint(isAboutSelected ? "Double tap to collapse About section" : "Double tap to expand About section")

            
            
            if(isAboutSelected){
                VStack{
                    Text("Get Grinnected was developed by Grinnellians, for Grinnellians, with the goal of creating an intuitive and accessible platform to stay informed abotu campus events.")
                    Text("We have a discord, so for those interested in expanding the toolbox offered to Grinnellians, please join! Here are our socials and github: ")
                    HStack{
                        Link(destination: URL(string: "https://discord.gg/e4PrM4RyEr")!) {
                            Image("discord_logo")
                                .resizable()
                                .frame(width: 50, height: 50)
                                .accessibilityLabel("Join our Discord server")
                        }
                        
                        // GitHub Link
                        Link(destination: URL(string: "https://github.com/seehorne/GetGrinnected")!) {
                            Image("github_logo")
                                .resizable()
                                .frame(width: 50, height: 50)
                                .accessibilityLabel("Check out our Github")
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
                    
                    Group {//read intelligibly as one thing
                        Text("Logo Design")
                            .font(.headline)
                        Text("Rei Yamada")
                            .padding(.bottom)
                    }
                    
                    Group {//read intelligibly as one thing
                        Text("Testing and Stakeholder Feedback")
                            .font(.headline)
                        Text("Yuina Iseki")
                        Text("Lily Freeman")
                        Text("Regan Stambaugh")
                            .padding(.bottom)
                    }
                        
                    Group {//read intelligibly as one thing
                        Text("Development Team")
                            .font(.headline)
                        Text("almond Heil")
                        Text("Anthony Schwindt")
                        Text("Budhil Thijm")
                        Text("Ellie Seehorn")
                        Text("Ethan Hughes")
                        Text("Michael Paulin")
                            .padding(.bottom)
                    }
                    
                    Group {//read intelligibly as one thing
                        Text("Faculty")
                            .font(.headline)
                        Text("Instructor: Professor Leah Pearlmutter")
                        Text("Digital Liberal Arts Collaborative: Morris Pelzel")
                            .padding(.bottom)
                    }
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
    
    /**
     deleteAccount()
     delete account button
     */
    func deleteAccount() -> some View {
        // Logout button
        Button(action: {
            showDeleteAccountAlert.toggle()
        }) {
            Text("Delete Account")
                .foregroundColor(.red)
                .bold()
        } //Button
        .accessibilityLabel("Delete your account")
        .accessibilityHint("Double tap to open a confirmation dialog about deleting your account. Deleting your account will permanently delete your account and its data")
        .alert("Are you sure you want to delete your account?", isPresented: $showDeleteAccountAlert) {
            Button("Cancel", role: .cancel) {}
            Button("Delete", role: .destructive) {
                // TODO add account deletion
                // we are logging out
                userProfile.deleteAccount()
                userProfile.updateLoginState(isLoggedIn: false)
                loggedOut = true
            }
            .foregroundColor(.red)
        } //alert
        .navigationDestination(isPresented: $loggedOut) {
            ContentView()
        }
        .padding()
        .frame(maxWidth: .infinity)
    }//logOut
    
    /**
     logOut()
     log out of account button
     */
    func logOut() -> some View {
        // Logout button
        Button(action: {
            showLogOutAlert.toggle()
        }) {
            Image(systemName: "rectangle.portrait.and.arrow.right")
                .imageScale(.large)
                .foregroundColor(.appContainer)
                .bold()
            Text("Log Out")
                .foregroundColor(.appContainer)
                .bold()
        } //Button
        .accessibilityLabel("Log out of your account")
        .accessibilityHint("Double tap to open a confirmation dialog about deleting your account.")
        .alert("Are you sure you want to log out?", isPresented: $showLogOutAlert) {
            Button("Cancel", role: .cancel) {}
            Button("Confirm", role: .destructive){
                // we are logging out
                userProfile.updateLoginState(isLoggedIn: false)
                loggedOut = true
            }
        } //alert
        .navigationDestination(isPresented: $loggedOut) {
            ContentView()
        }
        .padding()
        .frame(maxWidth: .infinity)
        .background(
            RoundedRectangle(cornerRadius: 8)
                .fill(.border)
                .overlay(
                    RoundedRectangle(cornerRadius: 8)
                )
        )
        .clipShape(RoundedRectangle(cornerRadius: 8))
    }//logOut
    
} //ProfileView

#Preview {
    SettingsView(username: "user123", email: "dummyemail@grinnell.edu", isLoggedIn: .constant(true))
}


