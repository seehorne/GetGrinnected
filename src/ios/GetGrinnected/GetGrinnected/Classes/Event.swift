//
//  Event.swift
//  GetGrinnected
//
//  Created by Budhil Thijm on 4/8/25.
//


struct Event: Codable {
   var Title: String
   var Date: String
   var Time: String
   var StartTimeISO: String
   var EndTimeISO: String
   var AllDay: Bool
   var Location: String
   var Description: String
   var Audience: [String]
   var Org: String
   var Tags: [String]
   var ID: Int
}
