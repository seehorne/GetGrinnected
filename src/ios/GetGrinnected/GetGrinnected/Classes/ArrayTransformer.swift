//
//  ArrayTransformer.swift
//  GetGrinnected
//
//  Created by Budhil Thijm on 4/25/25.
//
import Foundation

@objc(ArrayTransformer)
final class ArrayTransformer: NSSecureUnarchiveFromDataTransformer {
    override class var allowedTopLevelClasses: [AnyClass] {
        return [NSArray.self, NSString.self]
    }

    class var allowsReverseTransformation: Bool {
        return true
    }

    class var name: NSValueTransformerName {
        return NSValueTransformerName(rawValue: "ArrayTransformer")
    }

    static func register() {
        ValueTransformer.setValueTransformer(
            ArrayTransformer(),
            forName: ArrayTransformer.name
        )
    }
}

