package com.cmrk.ui.activity.lead

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cmrk.R
import com.cmrk.databinding.RowLeadBinding

class LeadAdapter(var leadList:ArrayList<Data>):RecyclerView.Adapter<LeadAdapter.ViewHolder>() {

    private lateinit var binding: RowLeadBinding
    private lateinit var mContext : Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        binding= DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_lead,
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        binding.tvGarageName.text = leadList[position].garage_name
        binding.tvDate.text = leadList[position].date
        binding.tvMake.text = leadList[position].make
        binding.tvModel.text = leadList[position].model
        binding.tvCmrkPrice.text = leadList[position].vehicle_value
        binding.tvCustomerPrice.text = leadList[position].expected_amount
        binding.tvStatus.text = leadList[position].lead_status
    }

    override fun getItemCount() = leadList.size

    class ViewHolder(var leadRowBinding:RowLeadBinding):RecyclerView.ViewHolder(leadRowBinding.root)

}