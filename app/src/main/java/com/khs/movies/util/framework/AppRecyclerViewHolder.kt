package com.khs.movies.util.framework

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.recyclerview.widget.RecyclerView

/**
 * A generic ViewHolder that works with a [ViewDataBinding].
 * @param <ItemViewBinding> The type of the ViewDataBinding.
</T> */
open class AppRecyclerViewHolder<out ItemViewBinding : ViewDataBinding> constructor(val binding: ItemViewBinding) :
    RecyclerView.ViewHolder(binding.root), LifecycleOwner {
    private val lifecycleRegistry by lazy { LifecycleRegistry(this) }

    init {
        lifecycleRegistry.currentState = Lifecycle.State.INITIALIZED
    }

    fun markAttach() {
        lifecycleRegistry.currentState = Lifecycle.State.STARTED
    }

    fun markDetach() {
        lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
    }

    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }
}