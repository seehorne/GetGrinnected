//
//  Organizations.swift
//  GetGrinnected
//
//  Created by Budhil Thijm on 4/8/25.
//

import SwiftUI

struct Organizations: View {
    var orgName: String
    var orgDescription: String
    
    var body: some View {
        GroupBox {
            VStack(){
                Text(orgName)
                    .fontWeight(.semibold)
                    .foregroundStyle(.textPrimary)
                    .frame(alignment: .leading)
                Text(orgDescription)
                    .fontWeight(.light)
                    .foregroundStyle(.textSecondary)
                    .frame(alignment: .center)
            }//Hstack (outer)
        }//title group box
        .foregroundStyle(Color(.container))
        Divider ()
    }
}


#Preview {
    Organizations(orgName: "ISO", orgDescription: "International Student Organization")
}
