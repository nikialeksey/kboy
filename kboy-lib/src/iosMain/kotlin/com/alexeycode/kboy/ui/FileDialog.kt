package com.alexeycode.kboy.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.UIKit.UIDocumentPickerDelegateProtocol
import platform.UIKit.UIDocumentPickerViewController
import platform.UIKit.UISceneActivationStateForegroundActive
import platform.UIKit.UIViewController
import platform.UIKit.UIWindowScene
import platform.UniformTypeIdentifiers.UTTypeItem
import platform.darwin.NSObject


@Composable
fun FileDialog(
    onFilePicked: (NSURL) -> Unit,
    onCompleted: () -> Unit
) {
    LaunchedEffect(Unit) {
        val pickerController = UIDocumentPickerViewController(forOpeningContentTypes = listOf(UTTypeItem))
        pickerController.allowsMultipleSelection = false
        pickerController.delegate = object : NSObject(), UIDocumentPickerDelegateProtocol {
            override fun documentPicker(
                controller: UIDocumentPickerViewController,
                didPickDocumentAtURL: NSURL,
            ) {
                if (didPickDocumentAtURL.startAccessingSecurityScopedResource()) {
                    onFilePicked(didPickDocumentAtURL)
                }
                onCompleted()
            }

            override fun documentPickerWasCancelled(controller: UIDocumentPickerViewController) {
                onCompleted()
            }
        }
        UIApplication.sharedApplication.topMostViewController()?.presentViewController(
            pickerController,
            animated = true,
            completion = null,
        )
    }
}

private fun UIApplication.topMostViewController(): UIViewController? {
    val keyWindow = this.connectedScenes
        .filterIsInstance<UIWindowScene>()
        .firstOrNull { it.activationState == UISceneActivationStateForegroundActive }
        ?.keyWindow

    var topController = keyWindow?.rootViewController
    while (topController?.presentedViewController != null) {
        topController = topController.presentedViewController
    }

    return topController
}

