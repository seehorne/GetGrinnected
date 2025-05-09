//
//  TagFilterModel.swift
//  GetGrinnected
//
//  Created by Michael Paulin on 5/8/25.
//

import SwiftData

/*
 This model is meant to be used in order to filter by tags with a predicate.
 You cannot compare string arrays in a predicate, so the tag string arrays need to be made into this type.
 */
@Model
class TagFilterModel: Identifiable {
    @Attribute(.unique) var tag: String
    
    init(tag: String) {
        self.tag = tag
    }
}
