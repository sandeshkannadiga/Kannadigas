package com.iramtech.parking.demo.kannadain.view


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.iramtech.parking.demo.kannadain.Adapter.VideoListAdapter
import com.iramtech.parking.demo.kannadain.R
import com.iramtech.parking.demo.kannadain.viewmodel.VideosListViewmodel
import kotlinx.android.synthetic.main.fragment_latest.*

/**
 * A simple [Fragment] subclass.
 */
class Latest : Fragment() {

    private val title by lazy(LazyThreadSafetyMode.NONE) { arguments?.getString("title") ?: "" }
    private lateinit var viewModel:VideosListViewmodel
    private val videoListAdapter = VideoListAdapter(arrayListOf())


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_latest, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //latestFragmentLabel.text = title

        viewModel = ViewModelProviders.of(this).get(VideosListViewmodel::class.java)
        viewModel.refresh()
        recyclerviewlist.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = videoListAdapter
        }
        ObserveViewModel()
    }

    fun ObserveViewModel(){
        viewModel.videos.observe(this, Observer { videos->
            videos?.let {
                recyclerviewlist.visibility = View.VISIBLE
                videoListAdapter.updateVideoList(videos)
            }})

        viewModel.videosLoadError.observe(this, Observer { isError->
            isError?.let {
                listError.visibility = if(it) View.VISIBLE else View.GONE
            }
        })

        viewModel.loading.observe(this, Observer { isLoading->
            isLoading?.let {
                loadingView.visibility = if(it) View.VISIBLE else View.GONE
                if(it){
                    listError.visibility = View.GONE
                    recyclerviewlist.visibility = View.GONE
                }
            }
        })
    }

}
