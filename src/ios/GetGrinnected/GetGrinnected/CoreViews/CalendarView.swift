//
//  CalendarView.swift
//  GetGrinnected
//
//  Created by Ellie Seehorn on 3/4/25.
//

import Foundation
import SwiftUI

struct CalendarView: View {
    var body: some View {
        
        GeometryReader{proxy in
            let safeAreaTop = proxy.safeAreaInsets.top
            ScrollView(.vertical, showsIndicators: false){
                VStack(){
                    Header(safeAreaTop, title: "Calendar", searchBarOn: false)
                    
                    
                    VStack{
                        //content
                        Image(systemName: "globe")
                            .imageScale(.large)
                            .foregroundStyle(.tint)
                        Text("Hello, calendar!")
                        
                    }
                    .frame(minHeight: proxy.size.height)//height
                            
                }
            }
            .edgesIgnoringSafeArea(.top)
            
        }//GeometryReader
        
    }
}

#Preview {
    CalendarView()
}
