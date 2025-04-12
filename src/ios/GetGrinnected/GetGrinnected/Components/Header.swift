//
//  Header.swift
//  GetGrinnected
//
//  Created by Michael Paulin on 4/8/25.
//

import SwiftUI

@ViewBuilder
func Header(_ safeAreaTop: CGFloat, title: String, searchBarOn: Bool) -> some View {
    
    VStack (){
        //only have this on for the homescreen view
        if(searchBarOn){
            SearchBar()//search bar
        }
        
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
            .fill(.colorRed)
    }
}

@ViewBuilder
func SearchBar() -> some View{
    HStack( spacing: 15){
        HStack(spacing: 8){
            Image(systemName: "magnifyingglass")
                .foregroundColor(.white)
            
            TextField("Search", text: .constant(""))
                .tint(.white)
        }
        .padding(.vertical, 10)
        .padding(.horizontal, 15)
        .background{
            RoundedRectangle(cornerRadius: 10, style: .continuous)
                .fill(.black)
                .opacity(0.15)
        }//background
        
        Button{
            
        } label: {
            //image
            Image("gglogo_nt")
                .resizable()
                .aspectRatio(contentMode: .fill)
                .frame(width: 35, height: 35)
                .clipShape(Circle())
                .background{
                    Circle()
                        .fill(.white)
                        .padding(-2)
                }
        }
    }//HStack for searching
}

#Preview {
    GeometryReader{proxy in
        let safeAreaTop = proxy.safeAreaInsets.top
        ScrollView(.vertical, showsIndicators: false){
            VStack(){
                Header(safeAreaTop, title: "Header", searchBarOn: true)
                
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

