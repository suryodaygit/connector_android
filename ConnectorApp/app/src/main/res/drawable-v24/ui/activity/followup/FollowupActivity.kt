package com.cmrk.ui.activity.followup

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmrk.R
import com.cmrk.databinding.ActivityFolloupBinding
import com.cmrk.ui.activity.visit.*

class FollowupActivity:AppCompatActivity(), FollowupNavigator {
    private lateinit var activityFolloupBinding: ActivityFolloupBinding
    private lateinit var folloupVisitViewModel: FollowupViewModel
    private lateinit var folloupList:ArrayList<Data>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityFolloupBinding = DataBindingUtil.setContentView(this, R.layout.activity_folloup)
        activityFolloupBinding.toolbar.ivBack.visibility = View.VISIBLE
        activityFolloupBinding.toolbar.ivBack.setOnClickListener { finish() }
        activityFolloupBinding.toolbar.tvActivityTitle.text = "Followup Details"
        folloupVisitViewModel = this.let { FollowupViewModel(this, it) }
        val type = intent.getStringExtra("TYPE").toString()
        val empId = intent.getStringExtra("EMPLOYEE_ID").toString()
        folloupVisitViewModel.getVisitList(empId,type)
    }

    override fun getFolloupListSuccess(responseModel: FollowupResponseModel) {
        folloupList= responseModel.data
        activityFolloupBinding.tvCount.text = "Count = " + responseModel.data.size
        if(responseModel.data.size == 0){
            activityFolloupBinding.llTable.visibility = View.GONE
        }else{
            activityFolloupBinding.llTable.visibility = View.VISIBLE
        }
        setAdapter()
    }

    override fun onError(error: String) {
    }

    private fun setAdapter(){
        val adapter = FollowupListAdapter(folloupList)
        activityFolloupBinding.rvVisitList.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        activityFolloupBinding.rvVisitList.adapter = adapter
    }

    override fun onBack() {
    }
}