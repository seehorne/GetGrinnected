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
                    //Header
                    Header(safeAreaTop, title: "Calendar", searchBarOn: false)
                    
                    //Content
                    VStack{
                        // Horizontal Scrollable Hstack for the dates
                        /// How do we get the "today?"
                        /// If Day is selected, highlight it with our app colors.
                        ScrollView(.horizontal, showsIndicators: false){
                            HStack{
                                ForEach(0..<7){_ in
                                    Text("Sun")
                                        .font(.caption)
                                        .foregroundColor(.gray)
                                }
                            }
                        }
                        .padding(.leading, 20)
                        
                        //Body (today, selected day)
                        VStack{
                            HStack{
                                Text("Today")
                                    .font(.caption)
                                    .foregroundColor(.gray)
                                Text("March 4th")
                                    .font(.caption2)
                                    .fontWeight(.bold)
                            }//Today
                            
                            //Includes event cards from that day (sorted according to those parameters
                            EventListView()
                        }//The body
                        .padding(.horizontal)
                        
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
