package com.deepak.nasa.ui.photo

import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.deepak.nasa.R
import com.deepak.nasa.databinding.FragmentPhotoBinding
import com.deepak.nasa.di.component.FragmentComponent
import com.deepak.nasa.ui.BaseFragment
import com.squareup.picasso.Callback

class PhotoFragment: BaseFragment() {


    private lateinit var binding: FragmentPhotoBinding

    override fun provideLayoutId() = R.layout.fragment_photo

    override fun injectDependencies(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun setupView(view: View) {
        binding = FragmentPhotoBinding.bind(view)
        commonViewModel.dataUrl.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.progressBar.visibility = View.GONE
                picasso.load(it)
                    .resize(1080,1080)
                    .onlyScaleDown()
                    .into(binding.zoomView, imageLoadCallBack)
            }
        })
    }

    private val imageLoadCallBack: Callback = object : Callback {
        override fun onSuccess() {
            binding.progressBar.visibility = View.GONE
        }

        override fun onError(e: Exception?) {
            Log.e("Error loading APOD",e.toString())
            binding.progressBar.visibility = View.GONE
        }
    }
}