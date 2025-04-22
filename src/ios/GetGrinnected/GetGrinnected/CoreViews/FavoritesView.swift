//
//  FavoritesView.swift
//  GetGrinnected
//
//  Created by Ellie Seehorn on 3/4/25.
//

import Foundation
import SwiftUI

struct FavoritesView: View {
    var body: some View {
        GeometryReader{proxy in
            let safeAreaTop = proxy.safeAreaInsets.top
            VStack(){
                    Header(safeAreaTop, title: "Favorites", searchBarOn: true)
                    ScrollView(.vertical, showsIndicators: false){
                    
                    
                    //content
                    VStack {
                        Image(systemName: "globe")
                            .imageScale(.large)
                            .foregroundStyle(.tint)
                        Text("Hello, favorites!")
                    }
                    .padding()
                    .frame(minHeight: proxy.size.height)//height
                            
                }
            }
            .edgesIgnoringSafeArea(.top)
            
        }//GeometryReader
    }
}

#Preview {
    FavoritesView()
}
