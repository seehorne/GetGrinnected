//
//  Logo.swift
//  GetGrinnected
//
//  Created by Budhil Thijm on 4/8/25.
//
import SwiftUI

//Logo Struct
struct Logo: View {
    var size: CGFloat
    
    
    var body: some View{
            // logo on toolbar. copied from budhil
            Image("white_circle")
                .resizable()
                .scaledToFill()
                .frame(width: size, height: size)
                .padding(.vertical, 32)
                .overlay(
                    Image("GetGrinnected_Logo")
                        .resizable()
                        .scaledToFill()
                        .frame(width: (size * 0.8), height: (size * 0.8))
                        .padding(.vertical, 32)
                )
        
    }
}

#Preview {
    Logo(size: 250)
}
