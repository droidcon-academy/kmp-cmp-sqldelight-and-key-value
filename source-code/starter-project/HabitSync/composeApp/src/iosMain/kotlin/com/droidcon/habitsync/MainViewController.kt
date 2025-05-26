package com.droidcon.habitsync

import androidx.compose.ui.window.ComposeUIViewController
import com.droidcon.habitsync.di.initKoinIos


fun MainViewController() = ComposeUIViewController {
    // Initialize Koin for iOS platform
    initKoinIos()
    App()
}
