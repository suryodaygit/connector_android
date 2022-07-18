package com.cmrk.ui.activity.lead

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmrk.R
import com.cmrk.databinding.ActivityLeadBinding
import com.cmrk.ui.activity.visit.*

class LeadActivity:AppCompatActivity(), LeadNavigator {
    private lateinit var activityLeadBinding: ActivityLeadBinding
    private lateinit var visitViewModel: LeadViewModel
    private lateinit var leadList:ArrayList<Data>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityLeadBinding = DataBindingUtil.setContentView(this, R.layout.activity_lead)
        activityLeadBinding.toolbar.ivBack.visibility = View.VISIBLE
        activityLeadBinding.toolbar.ivBack.setOnClickListener { finish() }
        activityLeadBinding.toolbar.tvActivityTitle.text = "Lead Details"
        visitViewModel = this.let { LeadViewModel(this, it) }
        val type = intent.getStringExtra("TYPE").toString()
        val empId = intent.getStringExtra("EMPLOYEE_ID").toString()
        visitViewModel.getLeadList(empId,type)
    }

    override fun getLeadListSuccess(responseModel: LeadResponseModel) {
        leadList= responseModel.data
        activityLeadBinding.tvCount.text = "Count = " + responseModel.data.size
        if(responseModel.data.size == 0){
            activityLeadBinding.llTable.visibility = View.GONE
        }else{
            activityLeadBinding.llTable.visibility = View.VISIBLE
        }
        setAdapter()
    }

    override fun onError(error: String) {
    }

    private fun setAdapter(){
        val adapter = LeadAdapter(leadList)
        activityLeadBinding.rvVisitList.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        activityLeadBinding.rvVisitList.adapter = adapter
    }

    override fun onBack() {
    }
}