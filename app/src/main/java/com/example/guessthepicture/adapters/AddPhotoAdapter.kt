package com.example.guessthepicture.adapters

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.guessthepicture.databinding.PhotoDisplayViewItemBinding
import com.example.guessthepicture.roomdb.GameDB
import com.example.guessthepicture.roomdb.PersonEntity

class AddPhotoAdapter(var context: Context,
                      var data: ArrayList<PersonEntity>,
                      var imgview: ViewHandler

                      ): RecyclerView.Adapter<AddPhotoAdapter.ViewHolder>() {
//    private var onClickListener: OnClickListener? = null

    inner class ViewHolder(var binding : PhotoDisplayViewItemBinding): RecyclerView.ViewHolder(binding.root){
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AddPhotoAdapter.ViewHolder {
        var binding = PhotoDisplayViewItemBinding.inflate(LayoutInflater.from(parent.context), parent ,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddPhotoAdapter.ViewHolder, position: Int) {
        holder.apply {
//                binding.img.setImageURI(Uri.parse(data!![position].picture))
                Log.e("imgAdded", "onBindViewHolder:$data")
            imgview.viewHandler(binding.img)
        }
//        notifyDataSetChanged()

    }

    override fun getItemCount(): Int {
        return 6

    }

    interface ViewHandler {
        fun viewHandler(position: ImageView)
    }


}