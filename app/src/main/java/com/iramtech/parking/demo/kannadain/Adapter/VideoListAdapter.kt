package com.iramtech.parking.demo.kannadain.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.iramtech.parking.demo.kannadain.R
import com.iramtech.parking.demo.kannadain.databinding.ItemVideoBinding
import com.iramtech.parking.demo.kannadain.model.VideoData
import kotlinx.android.synthetic.main.item_video.view.*

class VideoListAdapter(val videoList:ArrayList<VideoData>):RecyclerView.Adapter<VideoListAdapter.VideoListViewHolder>(){

    fun updateVideoList(newVideoList:List<VideoData>){
        videoList.clear()
        videoList.addAll(newVideoList)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoListViewHolder {
       val inflater = LayoutInflater.from(parent.context)
       //val view = inflater.inflate(R.layout.item_video,parent,false)
        val view = DataBindingUtil.inflate<ItemVideoBinding>(inflater,R.layout.item_video,parent,false)
        return VideoListViewHolder(view)
    }

    override fun getItemCount(): Int {
      return videoList.size
    }

    override fun onBindViewHolder(holder: VideoListViewHolder, position: Int) {
        holder.view.videoitem=videoList[position]
        /*holder.view.VideoTitle.text = videoList[position].VideoTitle
        holder.view.setOnClickListener {
           Toast.makeText(holder.view.context,"yes here",Toast.LENGTH_LONG).show()
        }*/
    }


    //class VideoListViewHolder(var view: View): RecyclerView.ViewHolder(view)
    class VideoListViewHolder(var view: ItemVideoBinding): RecyclerView.ViewHolder(view.root)
 
}