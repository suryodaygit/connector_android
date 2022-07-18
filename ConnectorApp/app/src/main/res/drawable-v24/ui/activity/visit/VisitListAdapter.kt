package com.cmrk.ui.activity.visit

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cmrk.R
import com.cmrk.databinding.RowVisitBinding

class VisitListAdapter(var visitList:ArrayList<Data>):RecyclerView.Adapter<VisitListAdapter.ViewHolder>() {

    private lateinit var binding: RowVisitBinding
    private lateinit var mContext : Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        binding= DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_visit,
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        binding.tvGarageName.text = visitList[position].garage_name
        binding.tvDate.text = visitList[position].date
        binding.tvPersonMet.text = visitList[position].contact_person
        binding.tvOwnerName.text = visitList[position].owner_name
        binding.tvPhoneNo.text = visitList[position].mobile_number
    }

    override fun getItemCount() = visitList.size

    class ViewHolder(var visitRowBinding:RowVisitBinding):RecyclerView.ViewHolder(visitRowBinding.root)

}