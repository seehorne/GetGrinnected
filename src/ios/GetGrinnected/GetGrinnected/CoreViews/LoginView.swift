//
//  LoginView.swift
//  GetGrinnected
//
//  Created by Budhil Thijm on 4/1/25.
//

import Foundation
import SwiftUI

struct LoginView: View {
    @Binding var isLoggedIn: Bool
    
    @State private var email = ""
    @State private var password = ""
    @State private var errorMessage = ""
    @State private var success = Bool()
    @StateObject private var userProfile = UserProfile()
    
    var body: some View {
        
        NavigationStack{
            VStack {
                //image logo with overlaid circle underneath
                Logo(size: 200)
                
                //Signin Email Text Field
                InputView(text: $email, title: "Grinnell Email", placeholder: "Enter your Grinnell Email")
                if !errorMessage.isEmpty{
                    Text(errorMessage)
                        .foregroundColor(.red)
                        .font(.caption)
                }
                
//                //Signin Password Text Fields
//                InputView(text: $password, title: "Password", placeholder: "Enter your password",isSecureField: true)
                
                
                // Button
                Button {
                    //logic that checks if user exists and password is correct
//                    if UserProfile.checkUser(email, password, errorMessage){
//                        print("Log in success!")
                            //isLoggedIn = true
//                    } else{
//                        print("\(errorMessage)")
//                    }
                    errorMessage = ""
                    userProfile.loginUser(email: email) { result in
                        switch result {
                        case .success(let output):
                            print("API Response: \(output)")
                            success = true
                        case .failure(let error):
                            print("API call failed: \(error.localizedDescription)")
                            success = false
                            if let apiError = error as? APIError {
                                            switch apiError {
                                            case .signInError(let message):
                                                errorMessage = message
                                            default:
                                                errorMessage = apiError.localizedDescription
                                            }
                                        } else {
                                            errorMessage = error.localizedDescription
                                        }
                        }
                    }
                    //once successfully logged in, jump to main viewport
                } label: {
                    HStack {
                        Text("SIGN IN")
                            .fontWeight(.semibold)
                        Image (systemName: "arrow.right")
                    }
                    .foregroundColor(.white)
                    .frame(width: UIScreen.main.bounds.width - 32, height: 48)
                }//Button
                .background (.colorBlue)
                    .cornerRadius (10)
                    .padding(.top, 24)
                
                
                //Navigation to Signup
                NavigationLink{
                    SignUpView(isLoggedIn: $isLoggedIn)
                } label : {
                    Text("Don't have an account?")
                        .foregroundColor(Color.appTextSecondary)
                        .padding(.top, 4)
                }
                
                //signin button
                // NavigationLink to VerifyView when login is successful
                .navigationDestination(isPresented: $success) {
                    VerificationView(email: email)
                }

                
            }
            .padding()
        } //navigation
    } //body
}//LoginView

#Preview {
    LoginView(isLoggedIn: .constant(false))
}


