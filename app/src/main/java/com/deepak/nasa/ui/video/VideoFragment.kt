package com.deepak.nasa.ui.video

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.commit451.youtubeextractor.Stream
import com.commit451.youtubeextractor.YouTubeExtractor
import com.deepak.nasa.R
import com.deepak.nasa.databinding.FragmentVideoBinding
import com.deepak.nasa.di.component.FragmentComponent
import com.deepak.nasa.ui.BaseFragment
import com.deepak.nasa.utils.common.getYoutubeVideoId
import com.deepak.nasa.utils.common.hideSnackBar
import com.deepak.nasa.utils.common.showSnackBar
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.material.snackbar.Snackbar
import io.reactivex.android.schedulers.AndroidSchedulers

class VideoFragment: BaseFragment() {


    private lateinit var binding: FragmentVideoBinding
    private lateinit var player: SimpleExoPlayer
    private lateinit var snackbar: Snackbar

    override fun provideLayoutId() = R.layout.fragment_video

    override fun injectDependencies(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun setupView(view: View) {
        binding = FragmentVideoBinding.bind(view)
        makeSnackBar()
        createVideoPlayer()
        subscribeVideoUrl()
    }

    private fun subscribeVideoUrl() {
        commonViewModel.dataUrl.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it.contains("youtube")) {
                    getYouTubeUrl(it)
                }else {
                    val videoUri: Uri = Uri.parse(it)
                    val videoSource: MediaSource? = buildMediaSource(videoUri, requireContext())
                    player.prepare(videoSource, false, false)
                }
            }
        })
    }

    private fun getYouTubeUrl(it: String) {
        val extractor = YouTubeExtractor.Builder().build()
        compositeDisposable.add(extractor.extract(getYoutubeLink(it))
            .subscribeOn(schedulerProvider.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                it.streams
                    .filterIsInstance<Stream.VideoStream>()
                    .max()?.url.apply {
                        Log.d("after extraction", this ?: "no url")
                        buildMediaSource(Uri.parse(this), requireContext())?.let {mediaSource ->
                            player.prepare(mediaSource, false, false)
                        }
                    }
            }, {
                Log.e("Error extracting Url", it.toString())
                binding.progressBar.visibility = View.GONE
                showSnackBar(getString(R.string.cannot_retrieve_url), snackbar)
            }))
    }



    private fun getYoutubeLink(url:String) = getYoutubeVideoId(url) ?: "".also {
        Log.d("before extraction" , url)
    }

    private fun buildMediaSource(uri: Uri, context: Context) =
        ProgressiveMediaSource.Factory(DefaultDataSourceFactory(context, "nasa")).createMediaSource(uri)



    private fun createVideoPlayer() {
        player = ExoPlayerFactory.newSimpleInstance(requireContext())
        with(player){
            playWhenReady = true
            addListener(playerEventListener)
            binding.video.player = player
        }

    }

    private val playerEventListener : Player.EventListener = object : Player.EventListener {
        override fun onPlayerError(error: ExoPlaybackException?) {
            super.onPlayerError(error)
            binding.progressBar.visibility = View.GONE
            showSnackBar(getString(R.string.cannot_play), snackbar)
        }

        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            super.onPlayerStateChanged(playWhenReady, playbackState)
            when (playbackState) {
                Player.STATE_READY -> {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }
    private fun makeSnackBar() {

        snackbar = Snackbar.make(binding.snackBarView, getString(R.string.no_internet), Snackbar.LENGTH_INDEFINITE)
            .setAction(getString(R.string.action_retry)) {
                commonViewModel.reloadVideo()
            }
            .setTextColor(ContextCompat.getColor(requireContext(),R.color.nasa_white))
            .setActionTextColor(ContextCompat.getColor(requireContext(), R.color.nasa_white))
            .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.snackBar_color))
    }

    private fun releasePlayer() {
        player.stop()
        player.release()
    }

    override fun onDestroy() {
        super.onDestroy()
        if(!compositeDisposable.isDisposed) compositeDisposable.dispose()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (snackbar.isShownOrQueued) hideSnackBar(snackbar)
        releasePlayer()
    }
}