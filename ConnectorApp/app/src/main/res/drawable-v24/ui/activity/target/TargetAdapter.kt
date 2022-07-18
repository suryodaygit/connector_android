package com.cmrk.ui.activity.target

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cmrk.R
import com.cmrk.databinding.RowTargetBinding

class TargetAdapter(var targetList:ArrayList<Data>):RecyclerView.Adapter<TargetAdapter.ViewHolder>() {

    private lateinit var binding: RowTargetBinding
    private lateinit var mContext : Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        binding= DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_target,
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        binding.tvContactName.text = targetList[position].contact_name
        binding.tvContactNo.text = targetList[position].contact_number
        binding.tvCmrkPrice.text = targetList[position].cmrk_price
        binding.tvEnquiryDate.text = targetList[position].enquiry_date
        binding.tvCustExpPrice.text = targetList[position].cust_exp_price
        binding.tvFollowupDate.text = targetList[position].followup_date.toString()
    }

    override fun getItemCount() = targetList.size

    class ViewHolder(var leadRowBinding:RowTargetBinding):RecyclerView.ViewHolder(leadRowBinding.root)

}