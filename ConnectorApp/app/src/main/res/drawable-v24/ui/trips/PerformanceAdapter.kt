package com.cmrk.ui.trips

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cmrk.R
import com.cmrk.databinding.RowPerformanceBinding

class PerformanceAdapter(var list:ArrayList<Data>): RecyclerView.Adapter<PerformanceAdapter.ViewHolder>() {

    private lateinit var binding: RowPerformanceBinding
    private lateinit var mContext : Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        binding= DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_performance,
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        binding.tvDate.text = list[position].start_date_time
        binding.tvLead.text = list[position].lead_count
        binding.tvVisit.text = list[position].visiting_count
    }

    override fun getItemCount() = list.size

    class ViewHolder(performanceBinding:RowPerformanceBinding):RecyclerView.ViewHolder(performanceBinding.root)

}