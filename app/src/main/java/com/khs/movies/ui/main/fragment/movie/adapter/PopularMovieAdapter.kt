package com.khs.movies.ui.main.fragment.movie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.khs.movies.databinding.ItemPopularMovieBinding
import com.khs.movies.persistance.entitiy.PopularMovie
import com.khs.movies.ui.main.viewmodel.setMovieDetailFieldIsCheckedFavorite
import com.khs.movies.util.framework.AppExecutors
import com.khs.movies.util.framework.AppRecyclerAdapter

class PopularMovieAdapter(
    appExecutors: AppExecutors,
    val onClick: (PopularMovie) -> Unit,
    val addFavorite : (PopularMovie,Boolean) -> Unit
) : AppRecyclerAdapter<PopularMovie, ItemPopularMovieBinding>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<PopularMovie>() {
        override fun areItemsTheSame(
            oldItem: PopularMovie,
            newItem: PopularMovie
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: PopularMovie,
            newItem: PopularMovie
        ): Boolean {
            return false
        }
    }
) {
    override fun createBinding(parent: ViewGroup): ItemPopularMovieBinding {
        return ItemPopularMovieBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    }

    override fun bind(binding: ItemPopularMovieBinding, item: PopularMovie, position: Int) {
        with(binding) {
            this.item = item
            root.setOnClickListener {
                onClick(item)
            }
            checkFavorite.setOnCheckedChangeListener { _, isChecked ->
                addFavorite(item,isChecked)
            }
        }
    }
}