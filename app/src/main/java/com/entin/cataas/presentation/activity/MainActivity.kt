package com.entin.cataas.presentation.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.entin.cataas.R
import com.entin.cataas.presentation.screens.main.MainScreenViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mainScreenViewModel: MainScreenViewModel by viewModels()
        mainScreenViewModel.fetchTags()

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                mainScreenViewModel.stateSplashScreen.value
            }
        }

        setContentView(R.layout.activity_main)
    }
}