package com.khs.movies.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.khs.movies.ui.main.fragment.movie.MoviesFragment
import com.khs.movies.util.framework.AppExecutors
import com.khs.movies.util.framework.LiveConnection
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject


@ActivityRetainedScoped
class MainFragmentFactory @Inject constructor(
    private val appExecutors: AppExecutors,
    private val liveConnection: LiveConnection
) : FragmentFactory() {


    override fun instantiate(classLoader: ClassLoader, className: String): Fragment =
        when (className) {
            MoviesFragment::class.java.name -> MoviesFragment(
                appExecutors = appExecutors,
                liveConnection = liveConnection
            )
            else -> super.instantiate(classLoader, className)
        }
}