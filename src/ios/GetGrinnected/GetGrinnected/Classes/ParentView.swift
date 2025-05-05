//
//  ParentView.swift
//  GetGrinnected
//
//  Created by Michael Paulin on 4/22/25.
//

import Foundation

/*
 EventListParentViewModel is an observable object meant to store data used to update view models from a parent view.
 
 viewedDate: the date is currently being viewed. default is today
 lastDate: the furthest date in the future we can see. default is 2 weeks
 selectedTags: the set of tags selected to filter by. default is empty
 timeSpan: the time span we want to see events for. default is now to the start of tomorrow
 
 */
class EventListParentViewModel: ObservableObject {
    @Published var viewedDate = Date.now //contains info about viewed date
    @Published var selectedTags: Set<EventTags> = [] //about selected tags
    @Published var timeSpan: (start: Date, end: Date) = (Date(), Date().startOfNextDay)//time span by day
    @Published var lastFetched: Date? //about when last fetched to update
    @Published var forceRefreshRequested: Bool = false //about whether or not a force refresh was requested
    let cacheExpiration: TimeInterval = 600 // 10 minutes
    
    //if it was requested, force refresh by setting force refreshrequested as true. 
    func forceRefresh() {
        print("Force refresh triggered")
        self.lastFetched = nil
        forceRefreshRequested = true
    }
    
}
