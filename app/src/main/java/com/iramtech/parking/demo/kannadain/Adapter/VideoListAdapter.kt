package com.iramtech.parking.demo.kannadain.Adapter
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.iramtech.parking.demo.kannadain.databinding.ItemVideoBinding
import com.iramtech.parking.demo.kannadain.model.VideoData
import com.bumptech.glide.Glide
import com.iramtech.parking.demo.kannadain.R
import com.iramtech.parking.demo.kannadain.view.Latest


class VideoListAdapter(
    val videoList: ArrayList<VideoData>,
    val latest: Latest

):RecyclerView.Adapter<VideoListAdapter.VideoListViewHolder>(){

   lateinit var onclickListner: OnclickListener


    fun updateVideoList(newVideoList:List<VideoData>){
        videoList.clear()
        videoList.addAll(newVideoList)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoListViewHolder {
       val inflater = LayoutInflater.from(parent.context)
       //val view = inflater.inflate(R.layout.item_video,parent,false)
        val view = DataBindingUtil.inflate<ItemVideoBinding>(inflater,
           R.layout.item_video,parent,false)
        onclickListner=latest
        return VideoListViewHolder(view)
    }

    override fun getItemCount(): Int {
      return videoList.size
    }

    override fun onBindViewHolder(holder: VideoListViewHolder, position: Int) {
        holder.view.videoitem=videoList[position]
        Glide.with(holder.itemView).load("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4").into(holder.view.imgvideo)


        holder.view.item.setOnClickListener {
           //Toast.makeText(holder.itemView.context,"yes here"+position,Toast.LENGTH_LONG).show()
            //onclickListner.onclickvideoItem("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4")

            var bundle = bundleOf("url" to "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4")
            holder.view.item.findNavController()?.navigate(R.id.play,bundle)
        }
    }

    //class VideoListViewHolder(var view: View): RecyclerView.ViewHolder(view)
    class VideoListViewHolder(var view: ItemVideoBinding): RecyclerView.ViewHolder(view.root)


    interface OnclickListener{
        fun onclickvideoItem(url:String)
    }

}