//
//  MainNavView.swift
//  GetGrinnected
//
//  Created by Ellie Seehorn on 3/4/25.
//

import Foundation
import SwiftUI

struct MainNavView: View {
    // boolean to keep track if we are logged in
    @Binding var isLoggedIn: Bool
    
    var body: some View {
        //creates the bottom bar for navigatio
        TabView {
            HomeView()
                .tabItem {
                    Label("Home", systemImage: "house.fill")
                }
            SearchView()
                .tabItem {
                    Label("Search", systemImage: "magnifyingglass")
                }
            FavoritesView()
                .tabItem {
                    Label("Favorites", systemImage: "heart.fill")
                }
            SettingsView(username:  UserDefaults.standard.string(forKey: "username") ?? "", email:  UserDefaults.standard.string(forKey: "email") ?? "", isLoggedIn: $isLoggedIn)
                .tabItem {
                    Label("Settings", systemImage: "gearshape.fill")
                }
        }
    }
}

#Preview {
    MainNavView(isLoggedIn: .constant(true))
}
