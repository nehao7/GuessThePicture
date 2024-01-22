package com.example.guessthepicture.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.guessthepicture.databinding.PhotoDisplayViewItemBinding
import com.example.guessthepicture.roomdb.GameDB
import com.example.guessthepicture.roomdb.PersonEntity

class AddPhotoAdapter(var context: Context,
                      val data: ArrayList<PersonEntity>

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

        holder.binding.img.setImageURI(Uri.parse(data[position].picture))
    }

    override fun getItemCount(): Int {
        return data.size
    }


}