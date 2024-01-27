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
import com.example.guessthepicture.DialogClickType
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
            binding.img.setImageURI(Uri.parse(data[position].picture))
            binding.addname.setText(data[position].name?:"")
            binding.addname.isEnabled = false

            binding.btnaddImage.visibility = View.GONE
            binding.btnAddAudio.visibility=View.VISIBLE

            binding.img.setOnClickListener {
                imgview.viewHandler(position, DialogClickType.Image)
            }

            binding.imgDelete.setOnClickListener {
                imgview.viewHandler(position, DialogClickType.Delete)
            }

            binding.btnAddAudio.setOnClickListener {
                imgview.viewHandler(position, DialogClickType.Audio)
            }
        }
//        notifyDataSetChanged()

    }

    override fun getItemCount(): Int {
        return data.size

    }

    interface ViewHandler {
        fun viewHandler(position: Int, clickType: DialogClickType)
    }


}