//
//  EventCreationView.swift
//  GetGrinnected
//
//  Created by Ellie Seehorn on 3/4/25.
//

import Foundation
import SwiftUI

struct EventCreationView: View {
    var body: some View {
        
        GeometryReader{proxy in
            let safeAreaTop = proxy.safeAreaInsets.top
            ScrollView(.vertical, showsIndicators: false){
                VStack(){
                    Header(safeAreaTop, title: "Event Creation")
                    
                    
                    //content
                    VStack {
                        Image(systemName: "globe")
                            .imageScale(.large)
                            .foregroundStyle(.tint)
                        Text("Hello, event creation!")
                    }
                    .padding()
                    .frame(minHeight: proxy.size.height)//height
                            
                }
            }//scroll view
            .edgesIgnoringSafeArea(.top)
            
        }//GeometryReader
    }
}

#Preview {
    EventCreationView()
}
