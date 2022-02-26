package com.khs.movies.ui.main.fragment.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.khs.movies.databinding.FragmentMoviesBinding
import com.khs.movies.model.common.DISCONNECTED
import com.khs.movies.model.common.OneActionDialogCallback
import com.khs.movies.model.common.TwoActionDialogCallback
import com.khs.movies.model.common.autoCleared
import com.khs.movies.model.domain.MovieDetail
import com.khs.movies.ui.main.fragment.BaseMainFragment
import com.khs.movies.ui.main.fragment.movie.adapter.PopularMovieAdapter
import com.khs.movies.ui.main.fragment.movie.adapter.UpcomingMovieAdapter
import com.khs.movies.ui.main.state.MainStateEvent
import com.khs.movies.ui.main.viewmodel.*
import com.khs.movies.util.ext.setUpOverScrollAndFixedSize
import com.khs.movies.util.framework.AppConnectivityManager
import com.khs.movies.util.framework.AppExecutors
import com.khs.movies.util.framework.LiveConnection
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MoviesFragment(
    private val appExecutors: AppExecutors,
    private val liveConnection: LiveConnection
) : BaseMainFragment() {

    private var _binding by autoCleared<FragmentMoviesBinding>()
    private var _adapterPopular by autoCleared<PopularMovieAdapter>()
    private var _adapterUpcoming by autoCleared<UpcomingMovieAdapter>()

    private val successCallBack by lazy {
        object : OneActionDialogCallback {
            override fun positive() = Unit
        }
    }

    private val apiErrorCallback by lazy {
        object : TwoActionDialogCallback {
            override fun positive() = Unit

            override fun negative() {
                processGetPopularMovies(isFromCache = true)
            }
        }
    }

    private val apiErrorCallbackUpcoming by lazy {
        object : TwoActionDialogCallback {
            override fun positive() = Unit

            override fun negative() {
                processGetUpcomingMovies(isFromCache = true)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMoviesBinding.inflate(inflater, container, false).apply {
            viewModel = super.viewModel
            lifecycleOwner = this@MoviesFragment.viewLifecycleOwner
        }
        _binding = binding
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerViews()
        subscribeObservers()
        processInit()
    }

    private fun subscribeObservers() {
        with(viewModel) {
            liveConnection.observe(viewLifecycleOwner) { conType ->
                conType?.let {
                    setMoviesFieldConnected(it != DISCONNECTED)
                }
                processGetPopularMovies(conType == DISCONNECTED)
                processGetUpcomingMovies(conType == DISCONNECTED)
            }

            numActiveJobs.observe(viewLifecycleOwner) {
                setMoviesFieldProgressShow(it > 0)
                setMoviesFieldProgressShowUpcoming(it > 0)
            }

        }
    }

    private fun processInit() {
        lifecycleScope.launch {
            if (viewModel.getMoviesFields().popularDataSource.value == null) {
                delay(2000)
                processGetPopularMovies(isFromCache = false)
                processGetUpcomingMovies(isFromCache = false)
            }
        }
    }

    private fun processGetPopularMovies(
        isFromCache: Boolean
    ) {
        with(viewModel) {
            with(getMoviesFields()) {
                setStateEvent(
                    MainStateEvent.GetPopularMoviesEvent(
                        isConnected = isConnected.value!!,
                        isFromCache = isFromCache,
                        apiErrorCallback = apiErrorCallback
                    )
                )
            }
        }
    }

    private fun processGetUpcomingMovies(
        isFromCache: Boolean
    ) {
        with(viewModel) {
            with(getMoviesFields()) {
                setStateEvent(
                    MainStateEvent.GetUpcomingMoviesEvent(
                        isConnected = isConnected.value!!,
                        isFromCache = isFromCache,
                        apiErrorCallback = apiErrorCallbackUpcoming
                    )
                )
            }
        }
    }

    private fun processUpdateIsFavorite(
        id: Long,
        isPopular: Boolean,
        isFavorite: Boolean
    ) {
        setStateEvent(
            MainStateEvent.UpdateIsFavoriteEvent(
                id = id,
                isPopular = isPopular,
                isFavorite = isFavorite,
                successCallBack = successCallBack
            )
        )
    }

    private fun setUpRecyclerViews() {
        with(_binding.rvPopularMovies) {
            val adapter = PopularMovieAdapter(
                appExecutors,
                onClick = {
                    findNavController().navigate(
                        MoviesFragmentDirections.actionMoviesToMovieDetail(
                            movieDetail = MovieDetail(
                                id = it.id,
                                title = it.title,
                                overview = it.overview,
                                posterPath = it.posterPath,
                                isPopular = true
                            )
                        )
                    )
                }
            ) {item,isFavorite ->
                processUpdateIsFavorite(item.id,true,isFavorite)
            }
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            _adapterPopular = adapter
            this.adapter = _adapterPopular
        }

        with(_binding.rvUpcomingMovies) {
            val adapter = UpcomingMovieAdapter(
                appExecutors,
                onClick = {
                    findNavController().navigate(
                        MoviesFragmentDirections.actionMoviesToMovieDetail(
                            movieDetail = MovieDetail(
                                id = it.id,
                                title = it.title,
                                overview = it.overview,
                                posterPath = it.posterPath
                            )
                        )
                    )
                }
            ) { item,isFavorite ->
               processUpdateIsFavorite(item.id,false,isFavorite)
            }
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            _adapterUpcoming = adapter
            this.adapter = _adapterUpcoming
        }

    }

    override fun initData() {
        viewModel.initMoviesField()
    }

    override fun clearDataOnDestroyFragmentVM() {
        viewModel.clearMoviesField()
    }
}