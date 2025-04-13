//
//  ProfileView.swift
//  GetGrinnected
//
//  Created by Ellie Seehorn on 3/4/25.
//

import Foundation
import SwiftUI

struct ProfileView: View {
    @State private var email = ""
    
    var body: some View {
        
        GeometryReader{proxy in
            let safeAreaTop = proxy.safeAreaInsets.top
            ScrollView(.vertical, showsIndicators: false){
                VStack(){
                    Header(safeAreaTop, title: "Settings", searchBarOn: true)
                    
                    
                    //content
                    
                    VStack {
                        Image(systemName:"person.crop.circle")
                            .imageScale(.large)
                            .font(.system(size: 100))
                            .foregroundStyle(.colorPink)
                        
                        InputView(text: $email, title: "Change Email", placeholder: "Enter your Grinnell Email")
                    } //VStack
                    .padding()
                    .frame(minHeight: proxy.size.height)//height
                            
                }//vstack
            }//Scroll view
            .edgesIgnoringSafeArea(.top)
            
        }//GeometryReader
    } //body
} //ProfileView

#Preview {
    ProfileView()
}
