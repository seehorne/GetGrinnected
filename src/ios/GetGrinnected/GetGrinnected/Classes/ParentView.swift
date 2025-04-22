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
 selectedTags: the tags selected to filter by. default is any
 timeSpan: the time span we want to see events for. default is now to the start of tomorrow
 
 */
class EventListParentViewModel: ObservableObject {
    @Published var viewedDate = Date.now
    @Published var lastDate = Date.now.addingTimeInterval(86400 * 13)
    @Published var selectedTags = EventTags.any
    @Published var timeSpan = DateInterval(start: Date.now, end: Date.now.startOfNextDay)
}
