package com.example.guessthepicture.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.guessthepicture.R
import com.example.guessthepicture.roomdb.PersonEntity

class AddAudioAdapter(var audioList: ArrayList<PersonEntity>, var onclick:itemClickListener) : RecyclerView.Adapter<AddAudioAdapter.ViewHolder>(),View.OnClickListener {
    var playingPosition: Int = -1
    var state = 0
    class ViewHolder(var view: View): RecyclerView.ViewHolder(view){
        var tvFileName: TextView = view.findViewById(R.id.tvFileName)
        var ibPlayPause: ImageButton = view.findViewById(R.id.ibPlayPause)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.audio_record_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.tvFileName.setText(audioList[position].audioRecord)
        holder.itemView.setOnClickListener{

            onclick.onItemClick(position, audioList[position].audioRecord)
        }
        if (position == playingPosition && state == 1) {
            holder.ibPlayPause.setImageResource(R.drawable.baseline_pause_circle_24)
        } else if (position == playingPosition && state == 0) {
            holder.ibPlayPause.setImageResource(R.drawable.baseline_play_circle_24)
        }else{
            holder.ibPlayPause.setImageResource(R.drawable.baseline_play_circle_24)
        }
    }

    fun updatePosition(position: Int, state: Int) {
        this.playingPosition = position
        this.state = state
        notifyDataSetChanged()
        Log.e("UpdatePosition", "Position State: $position $state")
    }

    override fun getItemCount(): Int {
        return 0
    }

    interface itemClickListener {
        fun onItemClick(position: Int,audio:String)
    }

    override fun onClick(p0: View?) {
       when(p0!!.id){

       }
    }
}