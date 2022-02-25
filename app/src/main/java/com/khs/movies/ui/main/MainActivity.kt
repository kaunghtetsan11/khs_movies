package com.khs.movies.ui.main

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import com.khs.movies.R
import com.khs.movies.databinding.ActivityMainBinding
import com.khs.movies.ui.BaseActivity
import com.khs.movies.ui.main.viewmodel.MainViewModel
import com.khs.movies.util.ext.displaySnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    private val mainNavController: NavController by lazy {
        findNavController(R.id.main_fragments_container)
    }

    private var isDoubleBackPress = false

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        with(binding) {
            lifecycleOwner = this@MainActivity
        }
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        if (savedInstanceState == null)
            setUpToolbar()
        setListeners()
    }

    private fun setUpToolbar() {
        binding.toolbar.setupWithNavController(
            mainNavController,
            AppBarConfiguration(mainNavController.graph)
        )
    }

    private fun setListeners() {
        with(binding) {
            with(toolbar) {

                setNavigationOnClickListener {
                    mainNavController.popBackStack()
                }
                mainNavController.addOnDestinationChangedListener { _, destination: NavDestination, _ ->
                    txtTitle.text = destination.label
                    if (mainNavController.graph.startDestinationId != destination.id)
                        setNavigationIcon(R.drawable.ic_back)
                }
            }
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setUpToolbar()
    }

    override fun getRootView(): View = binding.root

    override fun onBackPressed() {
        with(mainNavController) {
            currentDestination?.let {
                if (it.id == R.id.moviesFragment) {
                    if (!isDoubleBackPress) {
                        getRootView().displaySnackBar(
                            lifecycleOwner = this@MainActivity,
                            message = R.string.back_press_exit,
                            length = Snackbar.LENGTH_SHORT
                        )
                        isDoubleBackPress = true
                        lifecycleScope.launchWhenResumed {
                            delay(2000)
                            isDoubleBackPress = false
                        }
                    } else finishAffinity()
                } else super.onBackPressed()
            }
        }
    }
}
