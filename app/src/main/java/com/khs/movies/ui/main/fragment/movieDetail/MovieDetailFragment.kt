package com.khs.movies.ui.main.fragment.movieDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.khs.movies.databinding.FragmentMovieDetailBinding
import com.khs.movies.model.common.EventObserver
import com.khs.movies.model.common.OneActionDialogCallback
import com.khs.movies.model.common.autoCleared
import com.khs.movies.ui.main.fragment.BaseMainFragment
import com.khs.movies.ui.main.state.MainStateEvent
import com.khs.movies.ui.main.viewmodel.clearMovieDetailField
import com.khs.movies.ui.main.viewmodel.getMoviesDetailFields
import com.khs.movies.ui.main.viewmodel.initMovieDetailField
import com.khs.movies.ui.main.viewmodel.setMovieDetailFieldIsCheckedFavorite

class MovieDetailFragment : BaseMainFragment() {

    private var _binding by autoCleared<FragmentMovieDetailBinding>()

    private val navArgs by navArgs<MovieDetailFragmentArgs>()

    private val successCallBack by lazy {
        object : OneActionDialogCallback {
            override fun positive() = Unit
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMovieDetailBinding.inflate(inflater, container, false).apply {
            item = navArgs.movieDetail
            viewModel = super.viewModel
            lifecycleOwner = this@MovieDetailFragment.viewLifecycleOwner
        }
        _binding = binding
        return _binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding.checkFavorite.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setMovieDetailFieldIsCheckedFavorite(isChecked)
        }
        subscribeObservers()
        processGetFavoriteMovie(
            id = navArgs.movieDetail!!.id,
            isPopular = navArgs.movieDetail!!.isPopular
        )
    }

    private fun subscribeObservers() {
        with(viewModel) {
            getMoviesDetailFields().isCheckFavorite.observe(viewLifecycleOwner, EventObserver {
                processUpdateIsFavorite(
                    id = navArgs.movieDetail!!.id,
                    isPopular = navArgs.movieDetail!!.isPopular,
                    isFavorite = it
                )
            })
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

    private fun processGetFavoriteMovie(
        id: Long,
        isPopular: Boolean
    ) {
        setStateEvent(
            MainStateEvent.GetFavoriteMovieEvent(
                id = id,
                isPopular = isPopular
            )
        )
    }

    override fun initData() {
        viewModel.initMovieDetailField()
    }

    override fun clearDataOnDestroyFragmentVM() {
        viewModel.clearMovieDetailField()
    }
}