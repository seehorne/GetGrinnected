//
//  LoginView.swift
//  GetGrinnected
//
//  Created by Budhil Thijm on 2025.04.01
//

import Foundation
import SwiftUI

struct LoginView: View {
    @State private var email = ""
    @State private var password = ""
    
    var body: some View {
        var onLoginSuccess: () -> Void
        
        
        NavigationStack{
            VStack {
                //image
                Image("gglogo_nt")
                    .resizable()
                    .scaledToFill()
                    .frame(width: 100, height: 100)
                    .padding(.vertical, 32)
                
                
                // Button
                Button {
                    print("Log user in..")
                    onLoginSuccess()
                } label: {
                    HStack {
                        Text("SIGN IN")
                            .fontWeight(.semibold)
                        Image (systemName: "arrow.right")
                    }
                    .foregroundColor(.white)
                    .frame(width: UIScreen.main.bounds.width - 32, height: 48)
                }
                
                .background (Color.appBlue)
                .cornerRadius (10)
                .padding(.top, 24)
                
                
                //signup button
                NavigationLink{
                    
                } label : {
                    
                }//
                
                //signin button
                Text("Hello, Grinnellian!")
            }
            .padding()
        }//navigation
    }//body
}//LoginView

#Preview {
    LoginView()
}


