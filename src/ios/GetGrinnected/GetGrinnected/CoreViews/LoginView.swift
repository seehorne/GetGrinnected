//
//  LoginView.swift
//  GetGrinnected
//
//  Created by Budhil Thijm on 4/1/25.
//

import Foundation
import SwiftUI

struct LoginView: View {
    //for maintaining login states
    @Binding var isLoggedIn: Bool
    
    @StateObject private var userProfile = UserProfile()
    @State private var email = ""
    @State private var password = ""
    @State private var errorMessage = ""
    @State private var success = Bool()
    @State private var toVerifyMessage = "Start Getting Grinnected!"
    
    var body: some View {
        
        NavigationStack{
            VStack {
                //image logo with overlaid circle underneath
                Logo(size: 200)
                
                //Signin Email Text Field
                InputView(text: $email, title: "Grinnell Email", placeholder: "Enter your Grinnell Email")
                
                //error
                if !errorMessage.isEmpty{
                    Text(errorMessage)
                        .foregroundColor(.red)
                        .font(.caption)
                }//if
                
                // Button
                Button {
                    errorMessage = ""
                    userProfile.loginUser(email: email) { result in
                        switch result {
                            case .success(let output):
                                print("API Response: \(output)")
                                //set is logged in to true, if success
                                success = true
                            case .failure(let error):
                                print("API call failed: \(error.localizedDescription)")
                                success = false
                                errorMessage = userProfile.getErrorMessage(error: error);
                        }
                    }
                } label: {
                    HStack {
                        Text("SIGN IN")
                            .fontWeight(.semibold)
                        Image (systemName: "arrow.right")
                    }
                    .foregroundColor(.white)
                    .frame(width: UIScreen.main.bounds.width - 32, height: 48)
                } //Button
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
                
                //if the API call was successful, go to verification 
                .navigationDestination(isPresented: $success) {
                    VerificationView(email: email, message: toVerifyMessage, isLoggedIn: $isLoggedIn)
                }
            }
            .padding()
        } //navigation
    } //body
}//LoginView

#Preview {
    LoginView(isLoggedIn: .constant(false))
}


