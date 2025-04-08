//
//  Header.swift
//  GetGrinnected
//
//  Created by Michael Paulin on 4/8/25.
//

import SwiftUI

struct Header: View {
    var title: String
    
    var body: some View {
        NavigationStack{
            VStack {
                
            } //VStack
            
            .navigationTitle(title) // places the title
            .toolbarBackground(Color.red, for: .navigationBar) // colors bar
            .toolbarBackground(.visible, for: .navigationBar) // makes bar visible
            .toolbar{
                // logo on toolbar. copied from budhil
                Image("white_circle")
                    .resizable()
                    .scaledToFill()
                    .frame(width: 75, height: 75)
                    .padding(.vertical, 32)
                    .overlay(
                        Image("gglogo_nt")
                            .resizable()
                            .scaledToFill()
                            .frame(width: 75, height: 75)
                            .padding(.vertical, 32)
                    )
            } // toolbar
        } //NavigationStack
    } //body
} //Header

#Preview {
    Header(title: "Title")
}

