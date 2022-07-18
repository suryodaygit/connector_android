package com.cmrk.ui.activity.lead

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmrk.R
import com.cmrk.databinding.ActivityTargetBinding
import com.cmrk.ui.activity.target.Data
import com.cmrk.ui.activity.target.TargetAdapter
import com.cmrk.ui.activity.target.TargetNavigator
import com.cmrk.ui.activity.target.TargetResponseModel
import com.cmrk.ui.activity.target.TargetViewModel

class TargetActivity:AppCompatActivity(), TargetNavigator {
    private lateinit var activityLeadBinding: ActivityTargetBinding
    private lateinit var targetViewModel: TargetViewModel
    private lateinit var targetList:ArrayList<Data>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityLeadBinding = DataBindingUtil.setContentView(this, R.layout.activity_target)
        activityLeadBinding.toolbar.ivBack.visibility = View.VISIBLE
        activityLeadBinding.toolbar.ivBack.setOnClickListener { finish() }
        activityLeadBinding.toolbar.tvActivityTitle.text = "Target Details"
        targetViewModel = this.let { TargetViewModel(this, it) }
        val type = intent.getStringExtra("TYPE").toString()
        val empId = intent.getStringExtra("EMPLOYEE_ID").toString()
        targetViewModel.getTargetList(empId,type)
    }

    override fun getTargetListSuccess(responseModel: TargetResponseModel) {
        targetList= responseModel.data
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
        val adapter = TargetAdapter(targetList)
        activityLeadBinding.rvVisitList.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        activityLeadBinding.rvVisitList.adapter = adapter
    }

    override fun onBack() {
    }
}