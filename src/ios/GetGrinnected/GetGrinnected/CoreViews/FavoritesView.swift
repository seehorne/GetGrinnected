//
//  FavoritesView.swift
//  GetGrinnected
//
//  Created by Ellie Seehorn on 3/4/25.
//

import Foundation
import SwiftUI

struct FavoritesView: View {
    // the parent model used for updating our event list
    @StateObject private var viewModel = EventListParentViewModel()
    
    
    var body: some View {
        GeometryReader{proxy in
            let safeAreaTop = proxy.safeAreaInsets.top
            VStack(){
                // Header is outside of scrollable so it does not move
                Header(safeAreaTop: safeAreaTop, title: "Favorites", searchBarOn: false)
                ScrollView(.vertical, showsIndicators: false){
                //content of eventlist, show favorites true!, no search string
                    EventList(selectedEvent: -1, parentView: viewModel, searchString: "", showFavorites: true)
                /**
                 TODO: add .searchable to this eventlist, so that you can search through long list of favorites. 
                 */
                }
            }
            .edgesIgnoringSafeArea(.top)
            
        }//GeometryReader
    }
}

#Preview {
    FavoritesView()
}
