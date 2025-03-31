//
//  ProfileView.swift
//  GetGrinnected
//
//  Created by Ellie Seehorn on 3/4/25.
//

import Foundation
import SwiftUI

struct ProfileView: View {
    @available(iOS 13.0.0, *)
    var body: some View {
        VStack {
            if #available(iOS 15.0, *) {
                Image(systemName: "globe")
                    .imageScale(.large)
                    .foregroundStyle(.tint)
            } else {
                // Fallback on earlier versions
            }
            Text("Hello, Profile!")
        }
        .padding()
    }
}

//#Preview {
//    ProfileView()
//}
