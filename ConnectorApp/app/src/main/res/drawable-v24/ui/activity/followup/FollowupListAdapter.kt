package com.cmrk.ui.activity.followup

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cmrk.R
import com.cmrk.databinding.RowFollowupBinding

class FollowupListAdapter(var followupList:ArrayList<Data>):RecyclerView.Adapter<FollowupListAdapter.ViewHolder>() {

    private lateinit var binding: RowFollowupBinding
    private lateinit var mContext : Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        binding= DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_followup,
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        binding.tvGarageName.text = followupList[position].garage_name
        binding.tvDate.text = followupList[position].date
        binding.tvPersonMet.text = followupList[position].contact_person
        binding.tvOwnerName.text = followupList[position].owner_name
        binding.tvPhoneNo.text = followupList[position].mobile_number
    }

    override fun getItemCount() = followupList.size

    class ViewHolder(var followupRowBinding:RowFollowupBinding):RecyclerView.ViewHolder(followupRowBinding.root)

}