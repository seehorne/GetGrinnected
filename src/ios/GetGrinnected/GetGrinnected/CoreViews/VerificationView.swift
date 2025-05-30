//
//  VerificationView.swift
//  GetGrinnected
//
//  Created by Ellie Seehorn on 4/26/25.
//


import SwiftUI

import Foundation
import SwiftUI

struct VerificationView: View {
    @Binding var isLoggedIn: Bool
    @State private var code = ""
    @State private var email: String
    @State private var submitMessage: String
    @State private var errorMessage = ""
    @State private var isProcessing = false
    @State private var verifyError = ""
    @State private var resendError = ""
    @StateObject private var userProfile = UserProfile()
    
    init(email: String, message: String, isLoggedIn: Binding<Bool>) {
        self.email = email
        self.submitMessage = message
        _isLoggedIn = isLoggedIn // Properly initialize the binding
        self.errorMessage = ""
        self.verifyError = ""
    }
    
    var body: some View {
        
        NavigationStack{
            VStack {
                //image logo with overlaid circle underneath
                Logo(size: 200)
                    .accessibilityHidden(true)
                
                //Signin Email Text Field
                InputView(text: $code, title: "Type your code here", placeholder: "Hint: its six digits long and went to your email")
                    .accessibilityLabel("Verification code input")
                    .accessibilityHint("Enter the six-digit code sent to your email")
                if !verifyError.isEmpty{
                    Text(verifyError)
                        .foregroundColor(.red)
                        .font(.caption)
                        .accessibilityLabel("Verification error: \(verifyError)")
                }
                
                
                // Button
                Button {
                    verifyError = "" //reset verification error
                    //triggers API call
                    userProfile.verifyUser(email: email, code: code) { result in
                        switch result {
                        case .success(let output):
                            //if succeeded, log it
                            print("API Response: \(output)")
                            self.isLoggedIn=true //and we are good to go to the next one
                            userProfile.updateLoginState(isLoggedIn: true) //set our state to logged in
                            userProfile.setLocalEmail(newEmail: self.email)
                        case .failure(let error):
                            print("API call failed:\(error.localizedDescription)")
                            if let apiError = error as? UserProfile.APIError {//treat the error as API error object
                                            switch apiError {
                                            case .signInError(let message):
                                                verifyError = message //use the response message if there was one
                                            default:
                                                verifyError = apiError.localizedDescription
                                            }
                                        } else {
                                            verifyError = error.localizedDescription
                                        }
                        }
                    }
                    //once successfully logged in, jump to main viewport
                } label: {
                    HStack {
                        Text(self.submitMessage)
                            .fontWeight(.semibold)
                        Image (systemName: "arrow.right")
                    }
                    .accessibilityLabel("Submit verification code")
                    .accessibilityHint("Attempts to verify your email and sign you in")
                    .foregroundColor(.white)
                    .frame(width: UIScreen.main.bounds.width - 32, height: 48)
                }//Button
                .background (.colorBlue)
                    .cornerRadius (10)
                    .padding(.top, 24)
                
                Button{//this button is for resending the code
                    userProfile.resendOTP(email: email) { result in
                        resendError = ""
                        switch result {
                        case .success(let output):
                            print("API Response: \(output)")
                        case .failure(let error):
                            print("API call failed: \(error.localizedDescription)")
                            //this wouldn't be an error that's the users fault
                            //or something they could fix in a way other than just trying again
                            //but still probably good to let them know if something happened
                            resendError = userProfile.getErrorMessage(error: error);
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
                .accessibilityLabel("Resend verification code")
                .accessibilityHint("Sends a new six-digit code to your email")
                .background (.white)
                    .cornerRadius (10)
                    .padding(.top, 24)
                
            }
            .padding()
            
            if !resendError.isEmpty{ //if there is a resend error, render it here
                Text(resendError)
                    .foregroundColor(.red)
                    .font(.caption)
                    .accessibilityLabel("Resend error: \(resendError)")
            }
        } //navigation
    } //body
}//LoginView


#Preview {
    VerificationView(email: "test@grinnell.edu", message: "Start Getting Grinnected", isLoggedIn: .constant(false))
}
