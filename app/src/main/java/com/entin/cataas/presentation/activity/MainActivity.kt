package com.entin.cataas.presentation.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.entin.cataas.R
import com.entin.cataas.databinding.ActivityMainBinding
import com.entin.cataas.presentation.screens.main.MainScreenViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainScreenViewModel: MainScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        downloadTags()
        showSplashScreen(mainScreenViewModel)
        setupActionBar()
        bindView()
    }

    private fun bindView() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun downloadTags() {
        mainScreenViewModel.fetchTags()
    }

    private fun showSplashScreen(mainScreenViewModel: MainScreenViewModel) {
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                mainScreenViewModel.stateSplashScreen.value
            }
        }
    }

    private fun setupActionBar() {
        val actionBar = supportActionBar
        actionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.title_action_bar)
            elevation = 0.0F
        }
    }
}
