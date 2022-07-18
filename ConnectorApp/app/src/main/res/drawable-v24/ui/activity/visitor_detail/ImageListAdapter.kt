package com.cmrk.ui.activity.visitor_detail

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cmrk.R
import com.cmrk.databinding.ImageListRowBinding


class ImageListAdapter(private var imageList: List<Data>,private var deleteimage:deleteImage):RecyclerView.Adapter<ImageListAdapter.ViewHolder>() {

    private lateinit var binding: ImageListRowBinding
    private lateinit var mContext : Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        binding= DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.image_list_row,
            parent,
            false
        )

        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       Glide.with(mContext)
           .load(imageList[position].full_path)
           .into(binding.ivPhoto)

        binding.rlRootView.setOnClickListener {
            deleteimage.deleteSingleImage(position)
        }
    }

    override fun getItemCount() = imageList.size

    class ViewHolder(var imageListRowBinding: ImageListRowBinding):RecyclerView.ViewHolder(imageListRowBinding.root)

}