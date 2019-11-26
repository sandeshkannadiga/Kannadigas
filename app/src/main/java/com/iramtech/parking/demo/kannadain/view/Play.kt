package com.iramtech.parking.demo.kannadain.view

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import kotlinx.android.synthetic.main.fragment_play.*
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.util.Util
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.iramtech.parking.demo.kannadain.R


/**
 * A simple [Fragment] subclass.
 */
class Play : Fragment(), Player.EventListener {

    lateinit var player: SimpleExoPlayer
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_play, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // playFragmentLabel.text = title
        /*var vidAddress = "https://ia800201.us.archive.org/22/items/ksnn_compilation_master_the_internet/ksnn_compilation_master_the_internet_512kb.mp4"
        val vidUri = Uri.parse(vidAddress)
        myVideo.setVideoURI(vidUri)

        val vidControl = MediaController(context)
        vidControl.setAnchorView(myVideo)
        myVideo.setMediaController(vidControl)
        myVideo.start()*/

    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT >= 24) {
            initializePlayer()

        }
    }

    override fun onResume() {
        super.onResume()
        //hideSystemUi()
        if (Util.SDK_INT < 24 || player == null) {
            initializePlayer()
        }
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT < 24) {
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT >= 24) {
            releasePlayer()
        }
    }

    private fun releasePlayer() {
        if (player != null) {
            playWhenReady = player.playWhenReady
            playbackPosition = player.currentPosition
            currentWindow = player.currentWindowIndex
            player.release()
        }
    }


    private fun hideSystemUi() {
        player_view.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL)

    }


    private fun initializePlayer() {
        player = ExoPlayerFactory.newSimpleInstance(context)
        player_view.setPlayer(player)
       /* var uri: Uri =
            Uri.parse("https://v.cdn.vine.co/r/videos/AA3C120C521177175800441692160_38f2cbd1ffb.1.5.13763579289575020226.mp4")
        */

        var uri: Uri =
            Uri.parse("http://marwadijodi.co.in/kannada.in/video/1280.avi")


        var mediaSource: MediaSource = buildMediaSource(uri)
        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);
        player.prepare(mediaSource, false, false)
        hideSystemUi()
    }

    private fun buildMediaSource(uri: Uri): MediaSource {
        val dataSourceFactory = DefaultDataSourceFactory(context, "exoplayer-codelab")
        return ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(uri)
    }

}
