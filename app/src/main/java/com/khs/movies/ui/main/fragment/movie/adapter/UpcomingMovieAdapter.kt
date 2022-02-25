package com.khs.movies.ui.main.fragment.movie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.khs.movies.databinding.ItemUpcomingMovieBinding
import com.khs.movies.persistance.entitiy.PopularMovie
import com.khs.movies.persistance.entitiy.UpcomingMovie
import com.khs.movies.util.framework.AppExecutors
import com.khs.movies.util.framework.AppRecyclerAdapter

class UpcomingMovieAdapter(
    appExecutors: AppExecutors,
    val onClick: (UpcomingMovie) -> Unit,
    val addFavorite : (UpcomingMovie, Boolean) -> Unit
) : AppRecyclerAdapter<UpcomingMovie, ItemUpcomingMovieBinding>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<UpcomingMovie>() {
        override fun areItemsTheSame(
            oldItem: UpcomingMovie,
            newItem: UpcomingMovie
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: UpcomingMovie,
            newItem: UpcomingMovie
        ): Boolean {
            return false
        }
    }
) {
    override fun createBinding(parent: ViewGroup): ItemUpcomingMovieBinding {
        return ItemUpcomingMovieBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    }

    override fun bind(binding: ItemUpcomingMovieBinding, item: UpcomingMovie, position: Int) {
        with(binding) {
            this.item = item
            isShowDiv = currentList.lastIndex != position
            root.setOnClickListener {
                onClick(item)
            }
            checkFavorite.setOnCheckedChangeListener { _, isChecked ->
                addFavorite(item,isChecked)
            }
        }
    }
}