//
//  Logo.swift
//  GetGrinnected
//
//  Created by Budhil Thijm on 4/8/25.
//
import SwiftUI

//Logo Struct
struct Logo: View {
    var size: Int
    
    var body: some View{
            // logo on toolbar. copied from budhil
            Image("white_circle")
                .resizable()
                .scaledToFill()
                .frame(width: 40, height: 40)
                .padding(.vertical, 32)
                .overlay(
                    Image("gglogo_nt")
                        .resizable()
                        .scaledToFill()
                        .frame(width: 40, height: 40)
                        .padding(.vertical, 32)
                )
        
    }
}

#Preview {
    Logo(size: 250)
}
