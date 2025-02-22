package com.example.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.ui.utils.ProgressLoading

open class BaseFragment<VB : ViewBinding>(
    open val bindingInflater: (LayoutInflater) -> VB
) : Fragment() {

    private var _binding: VB? = null
    open val binding get() = checkNotNull(_binding)

 
    override fun onResume() {
        super.onResume()
 
        registerListeners()
    }
 
    override fun onPause() {
        unRegisterListeners()
        super.onPause()
    }
 
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater(layoutInflater)
        return binding.root
    }
 
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getFragmentArguments()
        binding.initializeUI()
        setupObservers()
    }
 
    open fun registerListeners() {}
 
    open fun unRegisterListeners() {}
 
    open fun getFragmentArguments() {}
 
    open fun VB.initializeUI() {}
 
    open fun setupObservers() {}
 
    fun showLoading() = ProgressLoading.show(requireActivity())
 
    fun hideLoading() = ProgressLoading.dismiss()

 
    open fun releaseView() {
        _binding = null
    }
 
    override fun onDestroyView() {
        super.onDestroyView()
        releaseView()
    }
}