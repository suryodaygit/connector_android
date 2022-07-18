package com.cmrk.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.cmrk.R
import com.cmrk.databinding.FragmentProfileBinding
import com.cmrk.ui.activity.followup.FollowupActivity
import com.cmrk.ui.activity.lead.LeadActivity
import com.cmrk.ui.activity.lead.TargetActivity
import com.cmrk.ui.activity.startTrip.StartTripActivity
import com.cmrk.ui.activity.visit.VisitActivity
import com.cmrk.ui.activity.visitor.VisitorActivity
import com.cmrk.util.AppPreference
import com.cmrk.util.Controller
import com.cmrk.util.DateUtils
import com.cmrk.util.Toaster
import java.util.*


class ProfileFragment : Fragment(), ProfileNavigator {

    private var _binding: FragmentProfileBinding? = null
    private var profileViewModel: ProfileViewModel? = null
    private var profileResponseModel: DashboardResponseModel? = null
    private lateinit var selectedDay : String
    private var employeeId=""
    companion object {
        var TRIP_ID: String = ""
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        /*bind view Model*/
        profileViewModel = activity?.let { ProfileViewModel(this, it) }
        /*END*/

        /*get Dashboard data using API*/
        profileViewModel?.getDashboardData()
        /*END*/
        _binding?.txtTripStartEndButton?.setOnClickListener {
            if (TRIP_ID == "") {
                val intent = Intent(activity, StartTripActivity::class.java)
                intent.putExtra("DashboardData", profileResponseModel)
                activity?.startActivity(intent)
            } else {
                openEndTRIPDialog()
            }
        }
//        _binding?.lylAddNewEnquiry?.setOnClickListener {
//            val bundle = Bundle()
//            bundle.putSerializable("DashboardData", profileResponseModel)
//            val intent = Intent(activity, VisitorActivity::class.java)
//            intent.putExtras(bundle)
//            this.startActivity(intent)
//        }
        _binding?.lylNewVisit?.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("DashboardData", profileResponseModel)
            val intent = Intent(activity, VisitorActivity::class.java)
            intent.putExtras(bundle)
            this.startActivity(intent)
        }
        fillData()

        binding.txtToday.setOnClickListener {
            selectedDay = "1"
            binding.txtToday.setBackgroundResource(R.drawable.login_btn_bg)
            binding.txtYesterday.setBackgroundResource(0)
            binding.txtMonth.setBackgroundResource(0)

            binding.txtToday.setTextColor(resources.getColor(R.color.white))
            binding.txtYesterday.setTextColor(resources.getColor(R.color.black))
            binding.txtMonth.setTextColor(resources.getColor(R.color.black))

            binding.txtVisitCount.text = profileResponseModel?.data?.today?.visit.toString()
            binding.txtLeadCount.text = profileResponseModel?.data?.today?.lead.toString()
            binding.txtFollowUpsCount.text = profileResponseModel?.data?.today?.followups.toString()
            binding.txtTarget.text = profileResponseModel?.data?.today?.enquiry.toString()
        }

        binding.txtYesterday.setOnClickListener {
            selectedDay = "2"
            binding.txtYesterday.setBackgroundResource(R.drawable.login_btn_bg)
            binding.txtToday.setBackgroundResource(0)
            binding.txtMonth.setBackgroundResource(0)

            binding.txtToday.setTextColor(resources.getColor(R.color.black))
            binding.txtYesterday.setTextColor(resources.getColor(R.color.white))
            binding.txtMonth.setTextColor(resources.getColor(R.color.black))

            binding.txtVisitCount.text = profileResponseModel?.data?.yesterday?.visit.toString()
            binding.txtLeadCount.text = profileResponseModel?.data?.yesterday?.lead.toString()
            binding.txtFollowUpsCount.text = profileResponseModel?.data?.yesterday?.followups.toString()
            binding.txtTarget.text = profileResponseModel?.data?.yesterday?.enquiry.toString()
        }

        binding.txtMonth.setOnClickListener {
            selectedDay = "3"
            binding.txtMonth.setBackgroundResource(R.drawable.login_btn_bg)
            binding.txtYesterday.setBackgroundResource(0)
            binding.txtToday.setBackgroundResource(0)

            binding.txtToday.setTextColor(resources.getColor(R.color.black))
            binding.txtYesterday.setTextColor(resources.getColor(R.color.black))
            binding.txtMonth.setTextColor(resources.getColor(R.color.white))

            binding.txtVisitCount.text = profileResponseModel?.data?.month?.visit.toString()
            binding.txtLeadCount.text = profileResponseModel?.data?.month?.lead.toString()
            binding.txtFollowUpsCount.text = profileResponseModel?.data?.month?.followups.toString()
            binding.txtTarget.text = profileResponseModel?.data?.month?.enquiry.toString()
        }

        binding.llVisit.setOnClickListener{
            val i =Intent(activity, VisitActivity::class.java)
            i.putExtra("TYPE",selectedDay)
            i.putExtra("EMPLOYEE_ID",employeeId)
            startActivity(i)
        }

        binding.llLead.setOnClickListener{
            val i =Intent(activity, LeadActivity::class.java)
            i.putExtra("TYPE",selectedDay)
            i.putExtra("EMPLOYEE_ID",employeeId)
            startActivity(i)
        }

        binding.llFollowUp.setOnClickListener{
            val i =Intent(activity, FollowupActivity::class.java)
            i.putExtra("TYPE",selectedDay)
            i.putExtra("EMPLOYEE_ID",employeeId)
            startActivity(i)
        }

        binding.llTarget.setOnClickListener{
            val i =Intent(activity, TargetActivity::class.java)
            i.putExtra("TYPE",selectedDay)
            i.putExtra("EMPLOYEE_ID",employeeId)
            startActivity(i)
        }

        return root
    }

    private fun openEndTRIPDialog() {
        activity?.let {
            AlertDialog.Builder(it).setIcon(R.drawable.app_logo)
                .setTitle(this.resources.getString(R.string.end_trip_str))
                .setMessage(this.resources.getString(R.string.end_trip_confirmation_message))
                .setPositiveButton(this.resources.getString(R.string.yes)) { dialog, _ ->
                    val bundle = Bundle()
                    bundle.putSerializable("DashboardData", profileResponseModel)
                    val intent = Intent(activity, StartTripActivity::class.java)
                    intent.putExtras(bundle)
                    activity?.startActivity(intent)
                }.setNegativeButton(
                    this.resources.getString(R.string.no)
                ) { dialog, _ ->
                    dialog.dismiss()
                }.show()
        }
    }

    private fun fillData() {
        _binding?.txtUsername?.text = activity?.let {
            context?.resources
                ?.getString(R.string.hello_str)
                .plus(" ").plus(AppPreference.loadLoginData(it).data?.name)
        }
        _binding?.txtDate?.text = context?.resources?.getString(R.string.date_str)
            .plus(" ").plus(DateUtils.getCurrentDate(Calendar.getInstance().time))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun ongetDashboardDataSuccess(dashboardResponseModel: DashboardResponseModel) {
        profileResponseModel = dashboardResponseModel
        employeeId = dashboardResponseModel.data?.employeeKeyId.toString()

        selectedDay = "1"
        binding.txtToday.setBackgroundResource(R.drawable.login_btn_bg)
        binding.txtYesterday.setBackgroundResource(0)
        binding.txtMonth.setBackgroundResource(0)

        binding.txtToday.setTextColor(resources.getColor(R.color.white))
        binding.txtYesterday.setTextColor(resources.getColor(R.color.black))
        binding.txtMonth.setTextColor(resources.getColor(R.color.black))

        binding.txtVisitCount.text = profileResponseModel?.data?.today?.visit.toString()
        binding.txtLeadCount.text = profileResponseModel?.data?.today?.lead.toString()
        binding.txtFollowUpsCount.text = profileResponseModel?.data?.today?.followups.toString()
        binding.txtTarget.text = profileResponseModel?.data?.today?.enquiry.toString()

        // dashboardResponseModel.data?.tripId ="82"
        if (dashboardResponseModel.data?.tripId.toString() == "null") {
            binding?.txtTripId!!.visibility = View.GONE
            TRIP_ID = ""
            _binding?.lylNewVisit?.isEnabled = false
            _binding?.txtTripStartEndButton?.text =
                context?.resources?.getString(R.string.start_trip_str)
        } else {
            _binding?.lylNewVisit?.isEnabled = true
            _binding?.txtTripId?.visibility = View.VISIBLE
            _binding?.txtTripId?.text = context?.resources?.getString(R.string.TripID).plus(" ")
                .plus(profileResponseModel?.data?.tripId)
            TRIP_ID = dashboardResponseModel.data?.tripId.toString()
            Log.d("trip id", TRIP_ID)
            _binding?.txtTripStartEndButton?.text =
                context?.resources?.getString(R.string.end_trip_str)
        }
    }

    override fun onFailed(error: String) {
        activity?.let { Toaster.showShortToast(it, error) }
    }

    override fun onError(error: String) {
        Log.d("getError", error)
    }

    override fun onBack() {

    }

    override fun onResume() {
        super.onResume()
        if (Controller.ISFROM == "END TRIP" || Controller.ISFROM == "START TRIP") {
            profileViewModel?.getDashboardData()
        }
    }
}