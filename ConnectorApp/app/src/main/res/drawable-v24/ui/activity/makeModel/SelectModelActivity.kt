package com.cmrk.ui.activity.makeModel

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.cmrk.R
import com.cmrk.databinding.SelectMakeActivityBinding
import com.cmrk.ui.activity.SubmitLeadReqModel
import com.cmrk.ui.activity.vehicleBuying.ScrapVehicleBuyingActivity
import com.cmrk.ui.activity.vehicleDetail.VehicleDetailActivity
import com.cmrk.util.Controller.Companion.MAKE_NAME
import com.cmrk.util.Controller.Companion.modelList_ResponseModel


class SelectModelActivity : AppCompatActivity(), SelectMakeModelNavigator {
    private lateinit var activityBinding: SelectMakeActivityBinding
    private var vehicleMakeList: ArrayList<SelectMakeModel> =
        ArrayList()
    private var vehicleModelList: ArrayList<SelectMakeModel> =
        ArrayList()
    private var makeList: ArrayList<MakeListResponseModel.Data> =
        ArrayList()
    private var modelList: ArrayList<ModelListResponseModel.Data> =
        ArrayList()
    private var selectMakeModelViewModel: SelectMakeModelViewModel? = null
    var FROM: String? = ""
    var bundle: Bundle? = null
    var submitLeadReqModel: SubmitLeadReqModel? = null
//    var MAKE_NAME: String? = ""
     var visitId=""
     var empId=""
     var tripId=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding =
            DataBindingUtil.setContentView(this, R.layout.select_make_activity)

        selectMakeModelViewModel = this?.let { SelectMakeModelViewModel(this, it) }

        activityBinding.selectMakeModelViewModel = SelectMakeModelViewModel(this, this)

        bundle = intent.extras
        submitLeadReqModel = bundle?.getSerializable("LeadReqModel") as SubmitLeadReqModel?

        activityBinding.toolbar.ivBack.visibility = View.VISIBLE
        activityBinding.toolbar.ivBack.setOnClickListener { finish() }
        activityBinding.toolbar.tvActivityTitle.text =
            "Select Make"

         visitId= intent.getStringExtra("VISIT_ID").toString()
         empId= intent.getStringExtra("EMP_ID").toString()
         tripId= intent.getStringExtra("TRIP_ID").toString()
        FROM = intent.getStringExtra("FROM")
        if (FROM.equals("MAKE")) {
            selectMakeModelViewModel?.getMakeList()
//            vehicleMakeList.add(SelectMakeModel("Audi"))
//            vehicleMakeList.add(SelectMakeModel("Bentley"))
//            vehicleMakeList.add(SelectMakeModel("BMW"))
//            vehicleMakeList.add(SelectMakeModel("Datsun"))
//            vehicleMakeList.add(SelectMakeModel("Ferrari"))
//            vehicleMakeList.add(SelectMakeModel("Froce Motors"))
//            vehicleMakeList.add(SelectMakeModel("Frod"))
//            vehicleMakeList.add(SelectMakeModel("Honda"))
//            vehicleMakeList.add(SelectMakeModel("Hyundai"))
//            vehicleMakeList.add(SelectMakeModel("Jaguar"))
//            vehicleMakeList.add(SelectMakeModel("Jeep"))
//            vehicleMakeList.add(SelectMakeModel("Kia"))
//            vehicleMakeList.add(SelectMakeModel("Lamborghini"))
//            vehicleMakeList.add(SelectMakeModel("Mahindra"))
//            vehicleMakeList.add(SelectMakeModel("Maruti Suzuki"))

        } else {

            activityBinding.toolbar.tvActivityTitle.text =
                "Select Model"
            if (!MAKE_NAME.equals("")) {
                MAKE_NAME?.let { selectMakeModelViewModel?.getModelList(it) }
            }

//            vehicleModelList.add(SelectMakeModel("A4"))
//            vehicleModelList.add(SelectMakeModel("A6"))
//            vehicleModelList.add(SelectMakeModel("A8 L"))
//            vehicleModelList.add(SelectMakeModel("e-tron"))
//            vehicleModelList.add(SelectMakeModel("e-tron GT"))
//            vehicleModelList.add(SelectMakeModel("e-tronSportback"))
//            vehicleModelList.add(SelectMakeModel("Q2"))
//            vehicleModelList.add(SelectMakeModel("Q7"))
//            vehicleModelList.add(SelectMakeModel("RS Q8"))
//            vehicleModelList.add(SelectMakeModel("RS5"))
//            vehicleModelList.add(SelectMakeModel("RS7 Sportback"))
//            vehicleModelList.add(SelectMakeModel("SS Sportback"))
//            activityBinding.selectMakeModelViewModel?.selectModelListAdapter?.setData(
//                vehicleModelList
//            )
//            activityBinding.rvModelList.visibility = View.VISIBLE
//            activityBinding.rvRoutList.visibility = View.GONE
        }


//        if (!AppPreference.getLogin()) {
//            Handler().postDelayed({
//                startActivity(LoginActivity.getInstance(this))
//                finish()
//            }, 3000)
//        }
//        else {
//            Handler().postDelayed({
//                startActivity(DashboardActivity.getInstance(this))
//                finish()
//            }, 3000)
//        }


    }

    override fun onMakeItemClick(selectMakeModel: MakeListResponseModel.Data) {
//        MAKE_NAME = selectMakeModel.make
        MAKE_NAME = selectMakeModel.make
        selectMakeModel.make?.let { selectMakeModelViewModel?.getModelList(it) }
        activityBinding.toolbar.tvActivityTitle.text =
            "Select Model"
//        vehicleModelList.add(SelectMakeModel("A4"))
//        vehicleModelList.add(SelectMakeModel("A6"))
//        vehicleModelList.add(SelectMakeModel("A8 L"))
//        vehicleModelList.add(SelectMakeModel("e-tron"))
//        vehicleModelList.add(SelectMakeModel("e-tron GT"))
//        vehicleModelList.add(SelectMakeModel("e-tronSportback"))
//        vehicleModelList.add(SelectMakeModel("Q2"))
//        vehicleModelList.add(SelectMakeModel("Q7"))
//        vehicleModelList.add(SelectMakeModel("RS Q8"))
//        vehicleModelList.add(SelectMakeModel("RS5"))
//        vehicleModelList.add(SelectMakeModel("RS7 Sportback"))
//        vehicleModelList.add(SelectMakeModel("SS Sportback"))
//        activityBinding.selectMakeModelViewModel?.selectModelListAdapter?.setData(vehicleModelList)
//        activityBinding.rvRoutList.visibility = View.GONE
//        activityBinding.rvModelList.visibility = View.VISIBLE
    }

    override fun onModelItemClick(selectMakeModel: ModelListResponseModel.Data) {
        modelList_ResponseModel = selectMakeModel
//        val returnIntent = Intent()
//        returnIntent.putExtra("make", MAKE_NAME)
//        returnIntent.putExtra("model", selectMakeModel)
//        setResult(RESULT_OK, returnIntent)

        val intent = Intent(this, VehicleDetailActivity::class.java)
        intent.putExtra("LeadReqModel", submitLeadReqModel)
        intent.putExtra("VISIT_ID",visitId)
        intent.putExtra("EMP_ID",empId)
        intent.putExtra("TRIP_ID",tripId)
        startActivity(intent)
        finish()
//        val intent = Intent(this, VehicleDetailActivity::class.java)
//        startActivity(intent)
    }

    override fun ongetMakeListSuccess(response: MakeListResponseModel) {
        makeList = response.data
        activityBinding.selectMakeModelViewModel?.selectMakeModelListAdapter?.setData(
            makeList
        )
        activityBinding.rvRoutList.visibility = View.VISIBLE
        activityBinding.rvModelList.visibility = View.GONE
//        response.data?.let { makeList.add(it) }
    }

    override fun ongetModelListSuccess(response: ModelListResponseModel) {
        modelList = response.data
        activityBinding.selectMakeModelViewModel?.selectModelListAdapter?.setData(modelList)
        activityBinding.rvRoutList.visibility = View.GONE
        activityBinding.rvModelList.visibility = View.VISIBLE
    }

    override fun onError(error: String) {

    }

    override fun onBack() {
    }

}