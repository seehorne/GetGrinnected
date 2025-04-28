//
//  SearchView.swift
//  GetGrinnected
//
//  Created by Budhil Thijm on 4/28/25.
//

import SwiftUI

struct SearchView: View {
    // the parent model used for updating our event list
    @StateObject private var viewModel = EventListParentViewModel()
    @State private var searchText = ""
    @State private var isLoading = true
    
    
    var body: some View {
        GeometryReader{proxy in
            let safeAreaTop = proxy.safeAreaInsets.top
            VStack(){
                // Header is outside of scrollable so it does not move
                Header(inputText: $searchText, safeAreaTop: safeAreaTop, title: "Search", searchBarOn: true)
                ScrollView(.vertical, showsIndicators: false){
                //content of eventlist, show favorites true!, no search string
                EventList(selectedEvent: -1, parentView: viewModel, searchString: searchText, showFavorites: true)
                }
            }
            .edgesIgnoringSafeArea(.top)
            
        }//GeometryReader
    }
}

#Preview {
    SearchView()
}
