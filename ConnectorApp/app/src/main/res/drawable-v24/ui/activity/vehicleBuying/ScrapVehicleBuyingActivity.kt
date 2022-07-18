package com.cmrk.ui.activity.vehicleBuying

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.cmrk.MainActivity
import com.cmrk.R
import com.cmrk.databinding.ActivityScrapVehicleBuyingBinding
import com.cmrk.ui.activity.vehicleDetail.VehicleDetailActivity
import com.cmrk.ui.activity.visitor_detail.VisitorPersonAreaDetailActivity
import com.cmrk.ui.profile.DashboardResponseModel
import com.cmrk.util.*
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class ScrapVehicleBuyingActivity : AppCompatActivity(), ScrapVehicleNavigator {

    companion object {
        var bundle: Bundle? = null
        var submitVisitRequestModel: SubmitVisitRequestModel? = null
        private lateinit var activityScrapVehicleBuyingBinding: ActivityScrapVehicleBuyingBinding
        lateinit var scrapVehicleViewModel: ScrapVehicleViewModel
        var dashboardResponseModel: DashboardResponseModel? = null
        var submitReqModel = SubmitVisitRequestModel()


        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        var isSwitchChecked: Boolean = false
        var isSwitchScrapVehicle: Boolean = false
    }

    private var mobileNo = ""
    private var garageName = ""
    private var contactPerson = ""
    private var ownerName = ""
    private var ownerMobileNo = ""
    private var noOFVisit = ""
    private var remark = ""
    private var employeeId = ""
    private var tripId = ""
    private var latitude = ""
    private var longitude = ""
    private var selectedVisitor=""
    private var scrap_vehicle_now="No"
    private var scrap_vehicle=""
    private var visitID=""
    private var followupDate=""
    private var businessImages :ArrayList<String> = arrayListOf()
    private var placeImages :ArrayList<String> = arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityScrapVehicleBuyingBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_scrap_vehicle_buying)

        scrapVehicleViewModel = ScrapVehicleViewModel(this, this)
        activityScrapVehicleBuyingBinding.scrapvehicleviewmodel = ScrapVehicleViewModel(this, this)

        activityScrapVehicleBuyingBinding.toolbar.ivBack.visibility = View.VISIBLE
        activityScrapVehicleBuyingBinding.toolbar.ivBack.setOnClickListener { finish() }
        activityScrapVehicleBuyingBinding.toolbar.tvActivityTitle.text =
            this.resources.getString(R.string.scrap_vehicle_buying_screen_title)

        val bundle = this@ScrapVehicleBuyingActivity.intent.extras
        submitVisitRequestModel =
            bundle?.getSerializable("VisitReqModel") as SubmitVisitRequestModel?

        if (intent.getStringExtra(VisitorPersonAreaDetailActivity.FROM) == "Visit Person Details Activity") {
            mobileNo = intent.getStringExtra("MOBILE_NO").toString()
            garageName = intent.getStringExtra("GARAGE_NAME").toString()
            contactPerson = intent.getStringExtra("CONTACT_PERSON").toString()
            ownerName = intent.getStringExtra("OWNER_NAME").toString()
            ownerMobileNo = intent.getStringExtra("OWNER_MOBILE_NO").toString()
            noOFVisit = intent.getStringExtra("NO_OF_VISIT").toString()
            remark = intent.getStringExtra("REMARK").toString()
            selectedVisitor = intent.getStringExtra("SELECTED_VISITOR").toString()
            latitude = intent.getStringExtra("LATITUDE").toString()
            longitude = intent.getStringExtra("LONGITUDE").toString()
            employeeId = AppPreference.loadLoginData(this@ScrapVehicleBuyingActivity).data?.employeeKeyId.toString()
            businessImages = intent.getStringArrayListExtra("BUSINESS") as ArrayList<String>
            placeImages = intent.getStringArrayListExtra("PLACE") as ArrayList<String>
            tripId = intent.getStringExtra("TRIP_ID").toString()
        }

        Log.d("list", (businessImages + placeImages).toString())

        activityScrapVehicleBuyingBinding.switchScrapVehicle.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                isSwitchScrapVehicle = true
                scrap_vehicle = "Yes"
            } else {
                isSwitchScrapVehicle = false
                scrap_vehicle = "No"
            }
        }
        activityScrapVehicleBuyingBinding.btnSubmit.setOnClickListener(object :
            View.OnClickListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onClick(view: View?) {
                val scarpRemark = activityScrapVehicleBuyingBinding.edtScrapRemark.text.toString()
                submitVisitRequestModel?.scrap_remark = scarpRemark
                val monthlyVehicle = activityScrapVehicleBuyingBinding.edtVehicles.text.toString()
                submitVisitRequestModel?.monthly_vehicles_vol = monthlyVehicle
                submitVisitRequestModel?.next_folowup_date = followupDate

                val currentDateandTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))
                Log.d("Date format",currentDateandTime)
               // val currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())

                submitReqModel = SubmitVisitRequestModel(
                    businessImages,
                    contactPerson,
                    currentDateandTime,
                    employeeId,
                    submitVisitRequestModel?.expected_price_per_kg,
                    garageName,
                    latitude,
                    longitude,
                    submitVisitRequestModel?.master_vehicle_id,
                    mobileNo,
                    monthlyVehicle,
                    followupDate,
                    noOFVisit,
                    ownerMobileNo,
                    ownerName,
                    placeImages,
                    remark,
                    activityScrapVehicleBuyingBinding.edtScrapRemark.text.toString(),
                    scrap_vehicle,
                    scrap_vehicle_now,
                    tripId,
                    selectedVisitor
                )


                scrapVehicleViewModel.submitScarpVehicleDetails(submitReqModel)

            }
        })


        activityScrapVehicleBuyingBinding.scrapVehicleactivity = Companion
        activityScrapVehicleBuyingBinding.btnSubmit.text = "Submit"

        if(activityScrapVehicleBuyingBinding.switchScrapVehicleSelling.isChecked){
            scrap_vehicle_now = "Yes"
            activityScrapVehicleBuyingBinding.btnSubmit.text = "Next"
        }else{
            scrap_vehicle_now = "No"
            activityScrapVehicleBuyingBinding.btnSubmit.text = "Submit"
        }

        activityScrapVehicleBuyingBinding.switchScrapVehicleSelling.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                isSwitchChecked = true
                scrap_vehicle_now = "Yes"
                activityScrapVehicleBuyingBinding.btnSubmit.text = "Next"
//                activityScrapVehicleBuyingBinding.txtFollowupDate.isClickable = true
            } else {
                isSwitchChecked = false
                scrap_vehicle_now = "No"
                activityScrapVehicleBuyingBinding.btnSubmit.text = "Submit"
//                activityScrapVehicleBuyingBinding.txtFollowupDate.isClickable = false
            }

        }

        activityScrapVehicleBuyingBinding.txtFollowupDate.setOnClickListener {
            activityScrapVehicleBuyingBinding.txtFollowupDate.isClickable = true
            scrapVehicleViewModel.openDartePickerDialog()
//            if (isSwitchChecked) {
//                activityScrapVehicleBuyingBinding.txtFollowupDate.isClickable = true
//                scrapVehicleViewModel.openDartePickerDialog()
//            }

        }
    }

    override fun onResume() {
        super.onResume()
//        submitLeadReqModel = bundle?.getSerializable("LeadReqModel") as SubmitLeadReqModel?
//        submitLeadReqModel?.garageName

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun openDatepicker() {
        val dpd = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                val selectedMonth = month + 1
                // Display Selected date in TextView
                activityScrapVehicleBuyingBinding.txtFollowupDate.text =
                    "$dayOfMonth/$selectedMonth/$year"
                val calendar = Calendar.getInstance()
                val mdformat = SimpleDateFormat("HH:mm:ss")
                val currentTime = mdformat.format(calendar.getTime())

                followupDate = "$dayOfMonth-$selectedMonth-$year $currentTime"
                submitVisitRequestModel?.next_folowup_date = "$dayOfMonth-$selectedMonth-$year"
            },
            year,
            month,
            day
        )
        dpd.datePicker.minDate = System.currentTimeMillis() - 1000
        dpd.show()
    }



    override fun successVisitorDetails(response: SubmitVisitResponseModel) {
         if (response is SubmitVisitResponseModel) {
             when (response.status) {
                 0 -> {
                     response?.message?.let {
                         Toaster.showLongToast(
                             this@ScrapVehicleBuyingActivity,
                             it
                         )
                     }
                     LoadingDialog.hideLoading()

                 }
                 1 -> {
                     response?.message?.let {
                         Toaster.showShortToast(
                             this@ScrapVehicleBuyingActivity,
                             it
                         )
                     }
                     LoadingDialog.hideLoading()
                     if(response.data.scrap_vehicle_now=="No"){
                         response?.message?.let {
                             Toaster.showShortToast(
                                 this@ScrapVehicleBuyingActivity,
                                 it
                             )
                         }
                         Controller.ISFROM=""
                         val intent = Intent(this@ScrapVehicleBuyingActivity, MainActivity::class.java)
                         startActivity(intent)
                     }else{
                         response?.message?.let {
                             Toaster.showShortToast(
                                 this@ScrapVehicleBuyingActivity,
                                 it
                             )
                         }
                         visitID = response.data.visit_key_id.toString()
                         val intent = Intent(this@ScrapVehicleBuyingActivity, VehicleDetailActivity::class.java)
                         intent.putExtra("EMP_ID",employeeId)
                         intent.putExtra("TRIP_ID",tripId)
                         intent.putExtra("VISIT_ID",visitID)
                         startActivity(intent)
                     }
                 }
                 2 -> {
                     response?.message?.let {
                         Toaster.showShortToast(
                             this@ScrapVehicleBuyingActivity,
                             it
                         )
                     }
                     LoadingDialog.hideLoading()
                     Controller.RedirectToLoginActivity(this)
                 }
             }
         }
     }

    override fun onError(error: String) {
    }

    override fun onBack() {
    }
}