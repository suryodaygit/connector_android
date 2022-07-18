package com.cmrk.ui.activity.sparepartsSelling

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.cmrk.MainActivity
import com.cmrk.R
import com.cmrk.databinding.ActivitySparePartsSellingBinding
import com.cmrk.ui.activity.SubmitLeadReqModel
import com.cmrk.ui.activity.makeModel.*
import com.cmrk.ui.activity.vehicleDetail.VehicleDetailActivity
import com.cmrk.util.Controller

class SparePartsSellingActivity : AppCompatActivity(), SelectMakeModelNavigator,
    SubmitInquiryNavigator {
    private lateinit var activitySparePartsSellingBinding: ActivitySparePartsSellingBinding
    private var vehicleOEMMakeList: ArrayList<SelectMakeModel> =
        ArrayList()
    var isShow = false
    var bundle: Bundle? = null
    var submitLeadReqModel: SubmitLeadReqModel? = null
    var isReqOfUsedSpareParts: String = "no"
    var isInterestedtoGetDailyOffers: String = "no"
    private var submitInquiryViewModel: SubmitInquiryViewModel? = null
    var make=""
    var model=""
    var manufactureYear=""
    var regNo=""
    var kerbWeight=""
    var vehicleOwnerShip=""
    var vehicleCondition=""
    var selectedFualType=""
    var tankCapacity =""
    var expDate=""
    var towingCharge=""
    var expectedAmount=""
    var orcAvailable=""
    var visitID=""
    var vehicaleImages:ArrayList<String> =arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySparePartsSellingBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_spare_parts_selling)

        activitySparePartsSellingBinding.selectMakeModelViewModel =
            SelectMakeModelViewModel(this, this)

        submitInquiryViewModel = this?.let { SubmitInquiryViewModel(this, it) }

        activitySparePartsSellingBinding.toolbar.ivBack.visibility = View.VISIBLE
        activitySparePartsSellingBinding.toolbar.ivBack.setOnClickListener { finish() }
        activitySparePartsSellingBinding.toolbar.tvActivityTitle.text =
            "Used Spare Parts Selling"

        bundle = intent.extras
        submitLeadReqModel =
            bundle?.getSerializable("LeadReqModel") as SubmitLeadReqModel?

        activitySparePartsSellingBinding.switchReqSpareParts.setOnCheckedChangeListener { compoundButton, isChecked ->
            isReqOfUsedSpareParts = if (isChecked) {
                "yes"
            } else {
                "no"
            }
        }

        activitySparePartsSellingBinding.switchOffers.setOnCheckedChangeListener { compoundButton, isChecked ->
            isInterestedtoGetDailyOffers = if (isChecked) {
                "yes"
            } else {
                "no"
            }
        }

        activitySparePartsSellingBinding.btnSubmit.setOnClickListener(object :
            View.OnClickListener {
            override fun onClick(view: View?) {
                val sparePartRequirement = isReqOfUsedSpareParts
                submitLeadReqModel?.sparePartRequirement = isReqOfUsedSpareParts
                val searchVehocle =   activitySparePartsSellingBinding.edtSearchVehicles.text.toString()
                submitLeadReqModel?.vehiclesForSpareParts =
                    activitySparePartsSellingBinding.edtSearchVehicles.text.toString()
                val spareParts =  activitySparePartsSellingBinding.edtSpareParts.text.toString()
                submitLeadReqModel?.specificSparePart =
                    activitySparePartsSellingBinding.edtSpareParts.text.toString()
                val immediateSpareParts = activitySparePartsSellingBinding.edtImmediateSpareParts.text.toString()
                submitLeadReqModel?.immediateRequirement =
                    activitySparePartsSellingBinding.edtImmediateSpareParts.text.toString()
                val priceExpectation =  activitySparePartsSellingBinding.edtPriceExpectation.text.toString()
                submitLeadReqModel?.priceExpectationPerKg =
                    activitySparePartsSellingBinding.edtPriceExpectation.text.toString()
                val intrestedDailyOffer = isInterestedtoGetDailyOffers
                submitLeadReqModel?.dailyOfferInterest = isInterestedtoGetDailyOffers
              /*  submitLeadReqModel?.sparePartRemark =
                    activitySparePartsSellingBinding.edtRemark.text.toString()*/

                make=  intent.getStringExtra("MAKE").toString()
                model= intent.getStringExtra("MODEL").toString()
                manufactureYear =  intent.getStringExtra("MANUFACTURE_YEAR").toString()
                regNo = intent.getStringExtra("REGNO").toString()
                kerbWeight = intent.getStringExtra("KERB_WEIGHT").toString()
                vehicleOwnerShip= intent.getStringExtra("VEHICLE_OWNERSHIP").toString()
                vehicleCondition = intent.getStringExtra("VEHICLE_CONDITION").toString()
                selectedFualType = intent.getStringExtra("SELECT_FUEL_TYPE").toString()
                tankCapacity = intent.getStringExtra("TANK_CAPACITY").toString()
                expDate = intent.getStringExtra("EXP_DATE").toString()
                towingCharge = intent.getStringExtra("TOWING_CHARG").toString()
                expectedAmount = intent.getStringExtra("EXPECTED_AMOUNT").toString()
                orcAvailable = intent.getStringExtra("ORC_AVAILABLE").toString()
                val empId=  intent.getStringExtra("EMP_ID").toString()
                val tripId =  intent.getStringExtra("TRIP_ID").toString()
                val vehicleId=  intent.getStringExtra("VEHICLE_ID").toString()
                val visitId =  intent.getStringExtra("VISIT_ID").toString()
                vehicaleImages = intent.getStringArrayListExtra("VEHICAL_IMAGE") as ArrayList<String>
                Log.d("Value",visitId)


                Log.d("vehicaleImages",vehicaleImages.toString())
                submitLeadReqModel = SubmitLeadReqModel(empId,
                    tripId,
                    make,
                    model,
                    vehicleId,
                    manufactureYear,
                    regNo,
                    kerbWeight,
                    vehicleOwnerShip,
                    vehicleCondition,
                    selectedFualType,
                     tankCapacity,
                     expDate,
                     towingCharge,
                     expectedAmount,
                    orcAvailable,
                     vehicaleImages,
                    sparePartRequirement,
                    spareParts,
                    sparePartRequirement,
                    immediateSpareParts,
                    priceExpectation,
                    intrestedDailyOffer,
                    visitId)
                submitLeadReqModel?.let { submitInquiryViewModel?.submitInquiry(it) }
            }
        })
        activitySparePartsSellingBinding.imgShow.setOnClickListener {
            if (!isShow) {
                activitySparePartsSellingBinding.rvOemMakeList.visibility = View.VISIBLE
                activitySparePartsSellingBinding.imgShow.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.icon_up_arrow
                    )
                )
                isShow = true
            } else {
                activitySparePartsSellingBinding.rvOemMakeList.visibility = View.GONE
                activitySparePartsSellingBinding.imgShow.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.icon_down_arrow
                    )
                )
                isShow = false
            }
        }

        vehicleOEMMakeList.add(SelectMakeModel("Audi"))
        vehicleOEMMakeList.add(SelectMakeModel("Bentley"))
        vehicleOEMMakeList.add(SelectMakeModel("BMW"))
        vehicleOEMMakeList.add(SelectMakeModel("Datsun"))
        vehicleOEMMakeList.add(SelectMakeModel("Ferrari"))
        vehicleOEMMakeList.add(SelectMakeModel("Froce Motors"))
        vehicleOEMMakeList.add(SelectMakeModel("Frod"))
        vehicleOEMMakeList.add(SelectMakeModel("Honda"))
        vehicleOEMMakeList.add(SelectMakeModel("Hyundai"))
        vehicleOEMMakeList.add(SelectMakeModel("Jaguar"))
        vehicleOEMMakeList.add(SelectMakeModel("Jeep"))
        vehicleOEMMakeList.add(SelectMakeModel("Kia"))
        vehicleOEMMakeList.add(SelectMakeModel("Lamborghini"))
        vehicleOEMMakeList.add(SelectMakeModel("Mahindra"))
        vehicleOEMMakeList.add(SelectMakeModel("Maruti Suzuki"))
        activitySparePartsSellingBinding.selectMakeModelViewModel?.selectOEMMakeListAdapter?.setData(
            vehicleOEMMakeList
        )
//        activitySparePartsSellingBinding.rvOemMakeList.visibility = View.VISIBLE

    }

    override fun onMakeItemClick(selectMakeModel: MakeListResponseModel.Data) {
    }

    override fun onModelItemClick(selectMakeModel: ModelListResponseModel.Data) {
        val intent = Intent(this, VehicleDetailActivity::class.java)
        startActivity(intent)
    }

    override fun ongetMakeListSuccess(response: MakeListResponseModel) {

    }

    override fun ongetModelListSuccess(response: ModelListResponseModel) {

    }

    override fun onSuccessofSubmitLead(response: SubmitScrapVehicleResponse) {
        Controller.ISFROM=""
        startActivity(MainActivity.getInstance(this))
        finish()
    }

    override fun onError(error: String) {

    }

    override fun onBack() {
    }

}