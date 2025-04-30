//
//  SearchBar.swift
//  GetGrinnected
//
//  Created by Budhil Thijm on 4/29/25.
//

import SwiftUI

struct SearchBar: View {
    @Binding var inputText: String
    var safeAreaTop: CGFloat
    
    var body: some View {
        HStack( spacing: 15){
            HStack(spacing: 8){
                Image(systemName: "magnifyingglass")
                    .foregroundColor(.white)
                TextField("Search", text: Binding(
                    get: { inputText },
                    set: { inputText = $0 }))
                .foregroundColor(.white)
                .tint(.white)
            }
            .padding(.vertical, 10)
            .padding(.horizontal, 15)
            .background{
                RoundedRectangle(cornerRadius: 10, style: .continuous)
                    .fill(.black)
                    .opacity(0.15)
            }//background
            
            
            //image
            Logo(size: 35)
        }//HStack for searching
        .padding(.top, safeAreaTop + 10)
        .background{
            Rectangle()
                .fill(.colorBlue)
        }
    }
}



//Cannot use #Preview for a bindable/state variable
