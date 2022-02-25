package com.khs.movies.util.framework

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

/**
 * A generic RecyclerView adapter that uses Data Binding & DiffUtil.
 *
 * @param <ItemDataModel> Type of the items in the list
 * @param <ItemViewBinding> The type of the ViewDataBinding
</V></T> */
abstract class AppRecyclerAdapter<ItemDataModel, ItemViewBinding : ViewDataBinding>(
    appExecutors: AppExecutors,
    diffCallback: DiffUtil.ItemCallback<ItemDataModel>
) : ListAdapter<ItemDataModel, AppRecyclerViewHolder<ItemViewBinding>>(
    AsyncDifferConfig.Builder(diffCallback)
        .setBackgroundThreadExecutor(appExecutors.diskIO())
        .build()
) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AppRecyclerViewHolder<ItemViewBinding> {
        val binding = createBinding(parent)
        return AppRecyclerViewHolder(binding)
    }

    protected abstract fun createBinding(parent: ViewGroup): ItemViewBinding

    override fun onBindViewHolder(holder: AppRecyclerViewHolder<ItemViewBinding>, position: Int) {
        bind(holder.binding, getItem(position), position)
        holder.binding.executePendingBindings()
    }

    protected abstract fun bind(binding: ItemViewBinding, item: ItemDataModel, position: Int)

    override fun onViewAttachedToWindow(holder: AppRecyclerViewHolder<ItemViewBinding>) {
        super.onViewAttachedToWindow(holder)
        holder.markAttach()
    }

    override fun onViewDetachedFromWindow(holder: AppRecyclerViewHolder<ItemViewBinding>) {
        super.onViewDetachedFromWindow(holder)
        holder.markDetach()
    }
}