package com.khs.movies.binding

import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.khs.movies.R
import com.khs.movies.persistance.entitiy.PopularMovie
import com.khs.movies.persistance.entitiy.UpcomingMovie
import com.khs.movies.ui.main.fragment.movie.adapter.PopularMovieAdapter
import com.khs.movies.ui.main.fragment.movie.adapter.UpcomingMovieAdapter

@BindingAdapter(
    value = ["imageUrl", "image", "resImage", "errorImage", "resPlaceHolderImage"],
    requireAll = false
)
fun setImage(
    imageView: AppCompatImageView,
    imageUrl: String?,
    image: Drawable?,
    @DrawableRes resImage: Int?,
    errorImage: Drawable?,
    @DrawableRes resPlaceHolderImage: Int?
) {
    val requestOptions =
        RequestOptions.placeholderOf(resPlaceHolderImage ?: R.drawable.ic_movie)
            .error(errorImage)

    Glide.with(imageView.context).setDefaultRequestOptions(requestOptions)
        .load(image ?: (resImage ?: imageUrl))
        .into(imageView)
}

@BindingAdapter("items")
fun getPopularMovies(
    view: RecyclerView, items: List<PopularMovie>?
) {
    (view.adapter as PopularMovieAdapter).submitList(
        items ?: emptyList()
    )
}

@BindingAdapter("items")
fun getUpcomingMovies(
    view: RecyclerView, items: List<UpcomingMovie>?
) {
    (view.adapter as UpcomingMovieAdapter).submitList(
        items ?: emptyList()
    )
}

