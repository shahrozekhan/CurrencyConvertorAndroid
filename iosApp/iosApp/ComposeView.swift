//
//  ComposeView.swift
//  iosApp
//
//  Created by Shahroze Khan on 25/11/2023.
//

import Foundation
import shared
import SwiftUI

struct ComposeView: UIViewControllerRepresentable {
    func updateUIViewController(_ uiViewController: UIViewControllerType, context: Context) {
    }

    func makeUIViewController(context: Context) -> some UIViewController {
        MainViewControllerKt.MainViewController()
    }
}
