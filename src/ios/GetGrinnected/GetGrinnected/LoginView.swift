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
    
    var body: some View {
        
        NavigationStack{
            VStack {
                //image
                Image("gglogo_nt")
                    .resizable()
                    .scaledToFill()
                    .frame(width: 100, height: 100)
                    .padding(.vertical, 32)
                
                //Signin Email Text Field
                InputView(text: $email, title: "Grinnell Email", placeholder: "Enter your Grinnell Email")
                
                //Signin Password Text Fields
                InputView(text: $password, title: "Password", placeholder: "Enter your password",isSecureField: true)
                
                
                // Button
                Button {
                    print("Log user in..")
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
                    .background (Color.appRed)
                    .cornerRadius (10)
                    .padding(.top, 24)
                
                
                //Navigation to Signup
                NavigationLink{
                    SignUpView(isLoggedIn: $isLoggedIn)
                } label : {
                    Text("Don't have an account?")
                        .foregroundColor(Color.appPink)
                        .padding(.top, 4)
                }//
                
                //signin button
                Text("Hello, Grinnellian!")
            }
            .padding()
        } //navigation
    } //body
}//LoginView

#Preview {
    LoginView(isLoggedIn: .constant(false))
}


