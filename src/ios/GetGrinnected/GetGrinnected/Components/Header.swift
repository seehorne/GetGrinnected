//
//  Header.swift
//  GetGrinnected
//
//  Created by Michael Paulin on 4/8/25.
//  Edited by Budhil Thijm on 4/14/25

import SwiftUI

struct Header: View{
    @Binding var inputText: String
    var safeAreaTop: CGFloat
    var title: String
    var searchBarOn: Bool
    
    init(
        inputText: Binding<String> = .constant(
            ""
        ),
        // <--- default binding to nil
        safeAreaTop: CGFloat,
        title: String,
        searchBarOn: Bool
    ) {
        self._inputText = inputText
        self.safeAreaTop = safeAreaTop
        self.title = title
        self.searchBarOn = searchBarOn
    }

    
    var body: some View {
        VStack (){
            //title and other icons
            HStack( spacing: 10){
                Text(title)
                    .font(.title)
                    .bold()
                    .foregroundStyle(.colorWhite)
            }//hstack for content and buttons
            .padding(.bottom)
            .frame(maxWidth: .infinity)
            
            //only have this on for the homescreen view
            if(searchBarOn){
                HStack( spacing: 15){
                    HStack(spacing: 8){
                        Image(systemName: "magnifyingglass")
                            .foregroundColor(.white)
                        TextField("Search", text: Binding(
                            get: { inputText },
                            set: { inputText = $0 }))
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
                
            }//only if searchbar is on
            
            
        }//vstack of the header
        .environment(\.colorScheme, .dark)
        .padding(.horizontal, 15)
        .padding(.top, safeAreaTop + 10)
        .background{
            Rectangle()
                .fill(.colorBlue)
        }
    }
}




#Preview {
    GeometryReader{proxy in
        let safeAreaTop = proxy.safeAreaInsets.top
        ScrollView(.vertical, showsIndicators: false){
            VStack(){
                //basic input here
                Header(
                    safeAreaTop: safeAreaTop,
                    title: "Header",
                    searchBarOn: false
                )
                
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

