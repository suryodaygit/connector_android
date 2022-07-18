package com.cmrk.ui.activity.visitor

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.cmrk.R
import com.cmrk.databinding.ActivityVisitorBinding
import com.cmrk.ui.activity.SubmitLeadReqModel
import com.cmrk.ui.activity.visitor_detail.VisitorPersonAreaDetailActivity
import com.cmrk.ui.profile.DashboardResponseModel
import com.cmrk.util.AppPreference
import com.cmrk.util.DateUtils
import java.util.*
import kotlin.collections.ArrayList

class VisitorActivity : AppCompatActivity(), VisitorsNavigator {
    private lateinit var activityVisitorBinding: ActivityVisitorBinding
    val submitLeadrequestModel = SubmitLeadReqModel()
    var bundle: Bundle? = null
    var dashboardResponseModel: DashboardResponseModel? = null
    private var visitorList: ArrayList<VisitorsModel> =
        ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityVisitorBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_visitor)

        activityVisitorBinding.dashboardViewModel = VisitorsViewModel(this, this)

        activityVisitorBinding.toolbar.ivBack.visibility = View.VISIBLE
        activityVisitorBinding.toolbar.ivBack.setOnClickListener { finish() }
        activityVisitorBinding.toolbar.tvActivityTitle.text =
            this.resources.getString(R.string.visitor_screen_title)

       /* activityVisitorBinding.btnNext.setOnClickListener {
            val intent = Intent(this, VisitorPersonAreaDetailActivity::class.java)
            startActivity(intent)
        }*/

        visitorList.add(VisitorsModel("Individual ELV Owner"))
        visitorList.add(VisitorsModel("Automobile Garage"))
        visitorList.add(VisitorsModel("Vehicle Sale Agent"))
        visitorList.add(VisitorsModel("Spare Parts Agent"))
        visitorList.add(VisitorsModel("Spare parts Shop"))
        visitorList.add(VisitorsModel("Car reseller"))
        visitorList.add(VisitorsModel("Authorized OEM dealership"))
        visitorList.add(VisitorsModel("Authorized car Reseller"))
        visitorList.add(VisitorsModel("Others"))
        activityVisitorBinding.dashboardViewModel?.visitorsListAdapter?.setData(visitorList)
        activityVisitorBinding.rvRoutList.visibility = View.VISIBLE

    }

    override fun onVisitorListItemClick(visitorsModel: VisitorsModel) {
       // submitLeadrequestModel.visitor = visitorsModel.name
        val dashboardData = intent.getSerializableExtra("DashboardData") as DashboardResponseModel
        val tripId =dashboardData.data?.tripKeyId
        val intent = Intent(this, VisitorPersonAreaDetailActivity::class.java)
        intent.putExtra("VisitorPerson", visitorsModel.name)
        intent.putExtra("LeadReqModel", submitLeadrequestModel)
        intent.putExtra("tripId",tripId)
        startActivity(intent)
    }

    override fun onError(error: String) {

    }

    override fun onBack() {
    }

    override fun onResume() {
        super.onResume()
        bundle = this.intent.extras
        dashboardResponseModel = bundle?.getSerializable("DashboardData") as DashboardResponseModel?
        submitLeadrequestModel?.employeeKeyId =
            AppPreference.loadLoginData(this).data?.employeeKeyId
        submitLeadrequestModel?.tripKeyId = dashboardResponseModel?.data?.tripKeyId
        activityVisitorBinding.txtTripId.text =
            this.resources.getString(R.string.TripID).plus(" ")
                .plus(dashboardResponseModel?.data?.tripId)
        activityVisitorBinding.txtDate.text = this.resources?.getString(R.string.date_str)
            .plus(" ").plus(DateUtils.getCurrentDate(Calendar.getInstance().time))
        activityVisitorBinding.txtDailyVisitCount.text =
            this.resources.getString(R.string.daily_visit_count).plus(" ")
                .plus(dashboardResponseModel?.data?.visitingCount)
        activityVisitorBinding.txtLeadCount.text =
            this.resources.getString(R.string.lead_count).plus(" ")
                .plus(dashboardResponseModel?.data?.leadCount)
    }
}