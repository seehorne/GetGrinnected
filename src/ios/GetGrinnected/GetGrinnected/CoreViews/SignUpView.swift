//
//  SignUpView.swift
//  GetGrinnected
//
//  Created by Budhil Thijm on 4/1/25.
//
import Foundation
import SwiftUI

struct SignUpView: View{
    @Binding var isLoggedIn: Bool
    //connect to LoginView and ContentView's isLogged In
    
    @State private var username = ""
    @State private var email = ""
    @State private var password = ""
    @State private var success = Bool()
    @StateObject private var userProfile = UserProfile()
    @State private var signupError = ""
    
    var body: some View{
        VStack(spacing: 20) {
            Text("Create Account")
                .bold()
                .font(.title)
                .foregroundColor(.textPrimary)
            
            /** input fields*/
            //Username
            InputView(text: $username,
                      title: "Name",
                      placeholder: "Enter your name..")
            
            
            //Signin Email Text Field
            InputView(text: $email,
                      title: "Grinnell Email",
                      placeholder: "Enter your Grinnell Email.. (@grinnell.edu)")
            
            
            //error
            if !signupError.isEmpty{
                Text(signupError)
                    .foregroundColor(.red)
                    .font(.caption)
            }//if
            
            
            //Signup button
            
            Button {
                signupError = "" //clear the error message
                userProfile.signUpUser(email: email, user: username) { result in
                    switch result {
                    case .success(let output):
                        print("API Response: \(output)")
                        success=true
                    case .failure(let error):
                        print("API call failed: \(error.localizedDescription)")
                        success=false
                        //render API error message if there was an error
                        signupError = userProfile.getErrorMessage(error: error);
                    }
                }
                    
            } label: {
                HStack{
                    Text("SIGN UP")
                        .fontWeight(.medium)
                        .padding()
                        .foregroundColor(Color.white)
                    Image(systemName: "arrow.right")
                        .padding()
                        .foregroundColor(Color.white)
                    
                }//Hstack
            }//button
            .background(Color.appBorder)
            .cornerRadius(10)
            .padding(.top, 24)
            
            .navigationDestination(isPresented: $success) {
                VerificationView(email: email, isLoggedIn: $isLoggedIn)
            }
        }//VStack
        .padding()
        
    }//Body
    
    private func attemptSignUp(){
        
        //reset error message
        signupError = ""
        
        //setusername first, no validation needed
        userProfile.setLocalUsername(newUsername: username)
        
        //validate email
        if !userProfile.setEmail(email){
            signupError = "Invalid email format! Must Include @grinnell.edu"
            return
        }
        
        //validate password
        if !userProfile.setLocalPassword(password){
            signupError = "Password must be at least 8 characters long!"
            return
        }
        
        
        //**YET TO BE IMPLEMENTED: Check in database if this user already exists*//
        //Check if user already exists
        //userProfile.alreadyExists()
        //Save user to database
        //userProfile.addToDatabase()
        
        //signup was successful
        print("User created Successfully!")
        
        
        //After saving, navigate to home page/user quiz
        isLoggedIn = true
        //MainNavView()
        
    }
}//SignupView


#Preview{
    NavigationStack{
        SignUpView(isLoggedIn: .constant(false))
    }
}

