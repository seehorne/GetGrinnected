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
//Header: View {
//    var title: String
    
        
        

        //Attempt #1
//        NavigationStack{
//            VStack {
//                
//            } //VStack
//            
//            .navigationTitle(title) // places the title
//            .toolbarBackground(.colorRed, for: .navigationBar) // colors bar
//            .toolbarBackground(.visible, for: .navigationBar) // makes bar visible
//            .toolbar{
//                // logo on toolbar. copied from budhil
//                Logo(size: 25)
//            } // toolbar
//        } //NavigationStack
        
        
        //Attempt #2
//        NavigationStack{
//            GeometryReader { geometryProxy in
//                let size = geometryProxy.size
//                let safeArea = geometryProxy.safeAreaInsets
//                let headerHeight = (size.height * 0.25) + safeArea.top
//                let minimumHeaderHeight = 64 + safeArea.top
//                
//                ZStack {
//                    RoundedRectangle(cornerRadius: 1, style: .continuous)
//                        .foregroundColor(Color("ColorRed"))
//                        .shadow(color: .black.opacity(0.2), radius: 5, y: 5)
//                        .frame(height: headerHeight - minimumHeaderHeight)
//                    
//                    HStack{
//                        
//                        GeometryReader{ proxy in
//                            Logo(size: 20)
//                                .position(x: proxy.frame(in: .local).minX + 40,
//                                          y: proxy.frame(in: .local).midY + 25) //set text to left side
//                            Text(title)
//                                .font(.largeTitle)
//                                .fontWeight(.bold)
//                                .foregroundStyle(.white)
//                                .position(x: proxy.frame(in: .local).midX,
//                                          y: proxy.frame(in: .local).midY + 25) //set text to middle
//                            
//                        }
//                        
//                    }
//                    
//                }//ZStack
//                .padding(.top, safeArea.top)
//                .position(x: geometryProxy.frame(in: .local).midX,
//                          y: minimumHeaderHeight - 5) // set to top
//                    
//            }//GeometryReader
//            .ignoresSafeArea(.all, edges: .top)
//        }
//                
//    
//    } //NavigationStack
//} //body

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

