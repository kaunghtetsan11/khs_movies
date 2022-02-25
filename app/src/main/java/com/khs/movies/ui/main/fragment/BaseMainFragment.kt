package com.khs.movies.ui.main.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import com.khs.movies.model.common.StateEvent
import com.khs.movies.model.common.UICommunicationListener
import com.khs.movies.ui.FragmentViewModel
import com.khs.movies.ui.main.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope


abstract class BaseMainFragment(
    private val isActiveJobsCancel: Boolean = true
) : Fragment() {

    lateinit var uiCommunicationListener: UICommunicationListener
    val viewModel: MainViewModel by activityViewModels()
    private val fragmentViewModel: FragmentViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(fragmentViewModel) {
            initData { initData() }
            clear = { clearDataOnDestroyFragmentVM() }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            uiCommunicationListener = context as UICommunicationListener
        } catch (e: ClassCastException) {
            e.printStackTrace()
        }
    }

    protected fun setStateEvent(
        stateEvent: StateEvent,
        coroutineScope: CoroutineScope = fragmentViewModel.viewModelScope
    ) {
        viewModel.setStateEvent(
            stateEvent = stateEvent,
            coroutineScope = coroutineScope
        )
    }

    protected abstract fun initData()

    protected abstract fun clearDataOnDestroyFragmentVM()

    override fun onDestroy() {
        super.onDestroy()
        if (isActiveJobsCancel)
            viewModel.cancelActiveJobs()
    }
}