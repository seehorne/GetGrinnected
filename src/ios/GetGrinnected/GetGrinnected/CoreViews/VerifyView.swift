//
//  VerifyView.swift
//  GetGrinnected
//
//  Created by Ellie Seehorn on 4/25/25.
//

import SwiftUI

import Foundation
import SwiftUI

struct VerifyView: View {
    @State private var isLoggedIn: Bool
    @State private var code = ""
    @State private var email: String
    @State private var errorMessage = ""
    @State private var success = Bool()
    @State private var verifyError = ""
    @StateObject private var userProfile = UserProfile()
    
    init(email: String) {
        self.email = email
        self.isLoggedIn = false
    }
    
    var body: some View {
        
        NavigationStack{
            VStack {
                //image logo with overlaid circle underneath
                Logo(size: 200)
                
                //Signin Email Text Field
                InputView(text: $code, title: "Type your code here", placeholder: "Hint: its six digits long and went to your email")
                if !verifyError.isEmpty{
                    Text(verifyError)
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
                    
                    userProfile.verifyUser(email: email, code: code) { result in
                        switch result {
                        case .success(let output):
                            print("API Response: \(output)")
                            success=true
                        case .failure(let error):
                            print("API call failed: \(error.localizedDescription)")
                            success=false
                            if let apiError = error as? APIError {
                                            switch apiError {
                                            case .signInError(let message):
                                                verifyError = message
                                            default:
                                                verifyError = apiError.localizedDescription
                                            }
                                        } else {
                                            errorMessage = error.localizedDescription
                                        }
                        }
                    }
                    //once successfully logged in, jump to main viewport
                } label: {
                    HStack {
                        Text("start getting grinnected")
                            .fontWeight(.semibold)
                        Image (systemName: "arrow.right")
                    }
                    .foregroundColor(.white)
                    .frame(width: UIScreen.main.bounds.width - 32, height: 48)
                }//Button
                .background (.colorBlue)
                    .cornerRadius (10)
                    .padding(.top, 24)
                
                Button{
                    userProfile.resendOTP(email: email) { result in
                        switch result {
                        case .success(let output):
                            print("API Response: \(output)")
                        case .failure(let error):
                            print("API call failed: \(error.localizedDescription)")
                        }
                    }
                }label: {
                    HStack {
                        Text("resend code")
                            .fontWeight(.semibold)
                    }
                    .foregroundColor(.colorBlue)
                    .frame(width: UIScreen.main.bounds.width - 48, height: 32)
                }//Button
                .background (.white)
                    .cornerRadius (10)
                    .padding(.top, 24)
//                //Navigation to Signup
//                NavigationLink{
//                    SignUpView(isLoggedIn: $isLoggedIn)
//                } label : {
//                    Text("Don't have an account?")
//                        .foregroundColor(Color.appTextSecondary)
//                        .padding(.top, 4)
//                }
//                
                //signin button
                
            }
            .padding()
            
            .navigationDestination(isPresented: $success) {
                MainNavView()
            }
        } //navigation
    } //body
}//LoginView


#Preview {
    LoginView(isLoggedIn: .constant(false))
}
