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
            .toolbarBackground(.colorRed, for: .navigationBar) // colors bar
            .toolbarBackground(.visible, for: .navigationBar) // makes bar visible
            .toolbar{
                // logo on toolbar. copied from budhil
                Logo(size: 25)
            } // toolbar
        } //NavigationStack
    } //body
} //Header

#Preview {
    Header(title: "Title")
}

