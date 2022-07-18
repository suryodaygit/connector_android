package com.cmrk.ui.activity.visit

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmrk.R
import com.cmrk.databinding.ActivityVisitBinding

class VisitActivity:AppCompatActivity(),VisitNavigator {
    private lateinit var activityVisitBinding: ActivityVisitBinding
    private lateinit var visitViewModel: VisitViewModel
    private lateinit var visitList:ArrayList<Data>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityVisitBinding = DataBindingUtil.setContentView(this, R.layout.activity_visit)
        activityVisitBinding.toolbar.ivBack.visibility = View.VISIBLE
        activityVisitBinding.toolbar.ivBack.setOnClickListener { finish() }
        activityVisitBinding.toolbar.tvActivityTitle.text = "Visit Details"
        visitViewModel = this.let { VisitViewModel(this, it) }
        val type = intent.getStringExtra("TYPE").toString()
        val empId = intent.getStringExtra("EMPLOYEE_ID").toString()
        visitViewModel.getVisitList(empId,type)
    }

    override fun getVisitListSuccess(responseModel: VisitResponseModel) {
        visitList= responseModel.data
        activityVisitBinding.tvCount.text = "Count = " + responseModel.data.size
        if(responseModel.data.size == 0){
            activityVisitBinding.llTable.visibility = View.GONE
        }else{
            activityVisitBinding.llTable.visibility = View.VISIBLE
        }
        setAdapter()
    }

    override fun onError(error: String) {
    }

    private fun setAdapter(){
        val adapter = VisitListAdapter(visitList)
        activityVisitBinding.rvVisitList.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        activityVisitBinding.rvVisitList.adapter = adapter
    }

    override fun onBack() {
    }
}