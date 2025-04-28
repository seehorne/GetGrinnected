//
//  ContentView.swift
//  GetGrinnected
//
//  Created by Ellie Seehorn on 3/4/25.
//

import Foundation
import SwiftUI

struct ContentView: View {
    @State private var isLoggedIn = false
    
    var body: some View {
        Group {
            if isLoggedIn {
                MainNavView()
                    .navigationBarBackButtonHidden(true)
            } else{
                LoginView(isLoggedIn: $isLoggedIn)
            }
        }
    }//body
}

class AuthManager: ObservableObject {
    @Published var isLoggedIn = false
}

#Preview {
    ContentView()
}
