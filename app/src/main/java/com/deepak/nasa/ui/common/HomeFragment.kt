package com.deepak.nasa.ui.common

import android.app.DatePickerDialog
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat.getColor
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.deepak.nasa.R
import com.deepak.nasa.data.network.Endpoints.TYPE_IMAGE
import com.deepak.nasa.data.network.Endpoints.TYPE_VIDEO
import com.deepak.nasa.data.network.Resource.*
import com.deepak.nasa.data.network.response.Apod
import com.deepak.nasa.databinding.FragmentHomeBinding
import com.deepak.nasa.di.component.FragmentComponent
import com.deepak.nasa.ui.BaseFragment
import com.deepak.nasa.utils.common.*
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Callback
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.*

class HomeFragment: BaseFragment() {


    private lateinit var binding: FragmentHomeBinding
    private var animated : Boolean = false
    private var buttonActionId : Int = 0
    private lateinit var snackbar: Snackbar

    override fun provideLayoutId() = R.layout.fragment_home

    override fun injectDependencies(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun setupView(view: View) {
        binding = FragmentHomeBinding.bind(view)
        init()
    }

    private fun init() {
        makeSnackBar()
        subscribeApodData()
        subscribeHdUrl()
        subscribeMediaType()
        binding.zoomPlay.setOnClickListener(buttonCLickListener)
        binding.buttonCalendar.setOnClickListener { showDatePicker() }
    }

    private fun subscribeMediaType() {
        commonViewModel.mediaType.observe(this.viewLifecycleOwner, Observer {
            it?.let {
                when (it) {
                    TYPE_IMAGE -> {
                        binding.zoomPlay.text = getString(R.string.zoom)
                        buttonActionId = R.id.action_homeFragment_to_photoFragment
                    }

                    TYPE_VIDEO -> {
                        binding.zoomPlay.text = getString(R.string.play)
                        buttonActionId = R.id.action_homeFragment_to_videoFragment
                    }
                }
            }
        })
    }

    private fun subscribeApodData() {
        commonViewModel.apodData.observe(this.viewLifecycleOwner, Observer {
            when (it) {
                is Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Success -> {
                    hideSnackBar(snackbar)
                    render(it.data)
                }
                is NetworkError -> {
                    binding.progressBar.visibility = View.GONE
                    showSnackBar(getString(R.string.no_internet), snackbar)
                }
                is Error -> {
                    binding.progressBar.visibility = View.GONE
                    showSnackBar(it.message, snackbar)
                }
            }
        })
    }

    private fun subscribeHdUrl() {
        commonViewModel.dataUrl.observe(viewLifecycleOwner, Observer {
            binding.zoomPlay.isEnabled = it != null
        })
        commonViewModel.dataUrl.observe(viewLifecycleOwner, Observer {
            binding.zoomPlay.isEnabled = it != null
        })
    }

    private fun render(apod: Apod?) {
        binding.tvHeadline.text = apod?.title
        binding.tvDescription.text = apod?.explanation
        loadImage(apod)
    }

    private fun loadImage(apod: Apod?) {
        apod?.let{
            if (apod.mediaType == TYPE_IMAGE) {
                picassoLoadImage(apod.url)
            } else {
                if (apod.url.contains("youtube")) {
                    // if youtube url, get thumbnail from url

                    getYoutubeThumbnail(apod.url).apply {
                        picassoLoadImage(this)
                    }

                } else {
                    // if not youtube url, try getting the frame from video
                    compositeDisposable.add(Single.fromCallable { getVideoFrame(apod.url) }
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ bitmap ->
                            binding.ivApod.setImageBitmap(bitmap)
                            Log.d("LOAD VIDEO THUMBNAIL","SUCCESS!")
                        }, { throwable ->
                            showSnackBar(getString(R.string.cannot_get_thumbnail), snackbar)
                            Log.e("LOAD HUMBNAIL ERROR",throwable.toString())
                        }))
                }
                binding.progressBar.visibility = View.GONE
                if (!animated) animateViewsIn(binding)
            }
        }
    }

    private fun picassoLoadImage(url: String){
        picasso.load(url)
            .fit().centerCrop().into(binding.ivApod, imageLoadCallBack)
    }

    private val buttonCLickListener : View.OnClickListener = View.OnClickListener {
        animated = false
        findNavController().navigate(buttonActionId)
    }

    private val imageLoadCallBack: Callback = object : Callback {
        override fun onSuccess() {
            binding.progressBar.visibility = View.GONE
            if (!animated) animateViewsIn(binding)
        }

        override fun onError(e: Exception?) {
            Log.e("Error loading APOD",e.toString())
            binding.progressBar.visibility = View.GONE
            showSnackBar(getString(R.string.error_loading_image), snackbar)
        }
    }

    private fun getYoutubeThumbnail(url:String) = "http://img.youtube.com/vi/${getYoutubeVideoId(url)}/hqdefault.jpg"

    private fun showDatePicker() {
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            //set date
            commonViewModel.setDate(year, month, dayOfMonth)
        }
        DatePickerDialog(
            requireContext(),
            R.style.Nasa_DatePickerTheme,
            dateSetListener,
            commonViewModel.getDate().get(Calendar.YEAR),
            commonViewModel.getDate().get(Calendar.MONTH),
            commonViewModel.getDate().get(Calendar.DAY_OF_MONTH)
        ).also {
            it.datePicker.maxDate = Calendar.getInstance().timeInMillis
            it.show()
            it.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(resources.getColor(R.color.nasa_orange))
            it.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(resources.getColor(R.color.nasa_orange))
        }
    }

    private fun makeSnackBar() {

        snackbar = Snackbar.make(binding.snackBarView, getString(R.string.no_internet), Snackbar.LENGTH_INDEFINITE)
            .setAction(getString(R.string.action_retry)) {
                commonViewModel.getApodForDate()
            }
            .setTextColor(getColor(requireContext(),R.color.nasa_white))
            .setActionTextColor(getColor(requireContext(), R.color.nasa_white))
            .setBackgroundTint(getColor(requireContext(), R.color.snackBar_color))
    }

    override fun onDestroy() {
        super.onDestroy()
        if(!compositeDisposable.isDisposed) compositeDisposable.dispose()
    }
}