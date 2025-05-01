//
//  ContentView.swift
//  GetGrinnected
//
//  Created by Ellie Seehorn on 3/4/25.
//

import Foundation
import SwiftUI

struct ContentView: View {
    @AppStorage("isLoggedIn") private var isLoggedIn = false
    
    // store standard font size of 20 in app storage
    @AppStorage("FontSize") private var fontSize: Double = 20.0
    
    @Environment(\.font) private var font
    
    var body: some View {
        Group {
            if isLoggedIn {
                MainNavView(isLoggedIn: $isLoggedIn)
                    .navigationBarBackButtonHidden(true)
            } else{
                LoginView(isLoggedIn: $isLoggedIn)
            }
        }
        .font(.system(size: fontSize))
    }//body
}

class AuthManager: ObservableObject {
    @Published var isLoggedIn = false
}

#Preview {
    ContentView()
}
