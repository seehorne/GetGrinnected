//
//  MainNavView.swift
//  GetGrinnected
//
//  Created by Ellie Seehorn on 3/4/25.
//

import Foundation
import SwiftUI

struct MainNavView: View {
    var body: some View {
        //creates the bottom bar for navigatio
        TabView {
            HomescreenView()
                .tabItem {
                    Label("Home", systemImage: "house.fill")
                }
            CalendarView()
                .tabItem {
                    Label("Calendar", systemImage: "calendar")
                }
            FavoritesView()
                .tabItem {
                    Label("Favorites", systemImage: "heart.fill")
                }
            ProfileView()
                .tabItem {
                    Label("Profile", systemImage: "person.fill")
                }
        }
    }
}

#Preview {
    MainNavView()
}
