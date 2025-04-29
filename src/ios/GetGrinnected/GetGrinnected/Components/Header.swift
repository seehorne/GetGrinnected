//
//  Header.swift
//  GetGrinnected
//
//  Created by Michael Paulin on 4/8/25.
//  Edited by Budhil Thijm on 4/14/25

import SwiftUI

@ViewBuilder
func Header(_ safeAreaTop: CGFloat, title: String) -> some View {
    
    VStack (){
        //only have this on for the homescreen view
        
        //title and other icons
        HStack( spacing: 10){
            Text(title)
                .font(.title)
                .bold()
        }//hstack for content and buttons
        .padding(.top,10)
        .frame(maxWidth: .infinity)
        
        
    }//vstack of the header
    .environment(\.colorScheme, .dark)
    .padding([.horizontal,.bottom], 15)
    .padding(.top, safeAreaTop + 10)
    .background{
        Rectangle()
            .fill(.colorBlue)
    }
}


#Preview {
    GeometryReader{proxy in
        let safeAreaTop = proxy.safeAreaInsets.top
        ScrollView(.vertical, showsIndicators: false){
            VStack(){
                Header(safeAreaTop, title: "Header")
                
                VStack{
                    ForEach(1...10, id: \.self){_ in
                        RoundedRectangle(cornerRadius: 10, style: .continuous)
                            .fill(Color.blue.gradient)
                            .frame(height: 100)
                        
                    }
                }
            }

        }
        .edgesIgnoringSafeArea(.top)
        
        
    }
}

