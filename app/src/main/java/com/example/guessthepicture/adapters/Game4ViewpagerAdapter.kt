package com.example.guessthepicture.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.example.guessthepicture.R

class Game4ViewpagerAdapter (private val context: Context, private val images: List<Int>) : PagerAdapter() {

    override fun getCount(): Int {
        return images.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.image_item_layout, container, false)

        val imageView = view.findViewById<ImageView>(R.id.imgstep)
        imageView.setImageResource(images[position])

        container.addView(view)
        return view
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }
}