package com.example.logintask.dashboard.activity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.View
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import com.example.healthqrapp.lib.utils.Constant
import com.example.logintask.R
import com.example.logintask.dashboard.DashboardActivity
import com.example.logintask.dashboard.fragment.home.HomeFragment
import com.example.logintask.databinding.ActivityReviewBinding
import com.example.logintask.lib.base.BaseActivity
import com.example.logintask.lib.utils.SuccessDialog
import com.example.logintask.lib.utils.getPreferenceData
import com.example.logintask.lib.utils.showToast

class ReviewActivity : BaseActivity() {

    private lateinit var binding: ActivityReviewBinding
    private val  CHANNEL_ID = "channel_id"
    private val notificationID = 101
    override fun getLayout() = R.layout.activity_review

    override fun init() {
        binding = DataBindingUtil.setContentView(this, getLayout())
        binding.toolbar.tvActivityTitle.text = "Review"
        binding.btnSubmit.btn.text = "Submit"
        setOnclickListener()
        setData()
        createNotification()
    }

    private fun setOnclickListener() {
        binding.toolbar.ivBack.setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
        }

        binding.btnSubmit.btn.setOnClickListener {
           // showToast(this, "Vendor id is generated successfully.....")
            SuccessDialog(this," Application no 1092093 generated susccessfully.","Review")
            HomeFragment.COMPLETED_PAGE_STATUS ="Review"
            sendNotification()
        }

        val selectedEntity = getPreferenceData(this, Constant.SELECTED_ENTITY, "")

        when (selectedEntity) {
            "Company(Pvt/Public)" -> {
                binding.llPrivateLtd.visibility = View.VISIBLE
            }
            "Partnership Firm" -> {
                binding.llPartnership.visibility = View.VISIBLE
            }
            "Proprietor" -> {
                binding.llProprietor.visibility = View.VISIBLE
            }
            "Individual" -> {
            }
        }
    }

    private fun setData() {
        binding.etEntity.setText(getPreferenceData(this, Constant.ENTITY, ""))
        binding.etCompanyName.setText(getPreferenceData(this, Constant.COMPANY_NAME, ""))
        binding.etPanCardNumber.setText(getPreferenceData(this, Constant.PAN_CARD_NO, ""))
        binding.etName.setText(getPreferenceData(this, Constant.NAME, ""))
        binding.etMobileNumber.setText(getPreferenceData(this, Constant.MOBILE_NO, ""))
        binding.etAlternetMobileNumber.setText(
            getPreferenceData(
                this,
                Constant.ALTERNATE_MOBILE_NO,
                ""
            )
        )
        binding.etEmail.setText(getPreferenceData(this, Constant.EMAIL, ""))
        binding.etAadharCardNo.setText(getPreferenceData(this, Constant.AADHAR_CARD_NO, ""))
        binding.etGstNo.setText(getPreferenceData(this, Constant.GST_NO, ""))
        binding.tvDob.text = getPreferenceData(this, Constant.DOB, "")
        binding.etPincode.setText(getPreferenceData(this, Constant.RESIDENTIAL_PINCODE, ""))
        binding.etCity.setText(getPreferenceData(this, Constant.RESIDENTIAL_CITY, ""))
        binding.etState.setText(getPreferenceData(this, Constant.RESIDENTIAL_STATE, ""))
        binding.etAddress.setText(getPreferenceData(this, Constant.RESIDENTIAL_ADDRESS, ""))
        binding.etOfficePincode.setText(getPreferenceData(this, Constant.OFFICE_PIN_CODE, ""))
        binding.etOfficeCity.setText(getPreferenceData(this, Constant.OFFICE_CITY, ""))
        binding.etOfficeState.setText(getPreferenceData(this, Constant.OFFICE_STATE, ""))
        binding.etOfficeAdress.setText(getPreferenceData(this, Constant.OFFICE_ADDRESS, ""))
        binding.etResidentialDistrict.setText(
            getPreferenceData(
                this,
                Constant.RESIDENTIAL_DISTRICT,
                ""
            )
        )
        binding.etOfficeDistrict.setText(getPreferenceData(this, Constant.OFFICE_DISTRICT, ""))
        binding.etPresentOccupation.setText(
            getPreferenceData(
                this,
                Constant.PRESENT_OCCUPATION,
                ""
            )
        )
        binding.etBankCustId.setText(getPreferenceData(this, Constant.SURYODAY_CUSTOMER_ID, ""))
        binding.etStaffName.setText(getPreferenceData(this, Constant.STAFF_NAME, ""))
        binding.etIfscNo.setText(getPreferenceData(this, Constant.IFSC_CODE, ""))
        binding.etAccountNo.setText(getPreferenceData(this, Constant.BANK_ACCOUNT_NO, ""))
        binding.etBankName.setText(getPreferenceData(this, Constant.BANK_NAME, ""))
        binding.etBranchName.setText(getPreferenceData(this, Constant.BRANCH_NAME, ""))
        binding.etAccountHolderName.setText(getPreferenceData(this, Constant.ACCOUNT_HOLDER_NAME, ""))

        binding.etRelationWithStaff.setText(
            getPreferenceData(
                this,
                Constant.RELATIONSHIP_WITH_STAFF,
                ""
            )
        )
        binding.ivDocument1.setImageURI(
            getPreferenceData(
                this,
                Constant.CUSTOMER_PHOTO,
                ""
            ).toUri()
        )
        binding.ivCancelledCheque.setImageURI(
            getPreferenceData(
                this,
                Constant.CANCELLED_CHEQUE,
                ""
            ).toUri()
        )
        binding.ivAddressProof.setImageURI(
            getPreferenceData(
                this,
                Constant.CUSTOMER_ADDRESS_PROOF,
                ""
            ).toUri()
        )
        binding.ivIdentityProof.setImageURI(
            getPreferenceData(
                this,
                Constant.IDENTITY_PROOF,
                ""
            ).toUri()
        )
        binding.ivProprietorshipDeclaration.setImageURI(
            getPreferenceData(
                this,
                Constant.PROPRIENTORSHIP_DECLARATION_PHOTO,
                ""
            ).toUri()
        )
        binding.ivGstinNo.setImageURI(
            getPreferenceData(
                this,
                Constant.PROPRIENTORSHIP_GSTNO_PHOTO,
                ""
            ).toUri()
        )
        binding.ivProprientorAddressProof.setImageURI(
            getPreferenceData(
                this,
                Constant.PROPRIENTORSHIP_ADDRESS_PROOF,
                ""
            ).toUri()
        )
        binding.ivPartnershipGstinNo.setImageURI(
            getPreferenceData(
                this,
                Constant.PROPRIENTORSHIP_GSTNO_PHOTO,
                ""
            ).toUri()
        )
        binding.ivPartnershipPanNo.setImageURI(
            getPreferenceData(
                this,
                Constant.PARTNERSHIP_PAN_PHOTO,
                ""
            ).toUri()
        )
        binding.ivPartnershipAddressProof.setImageURI(
            getPreferenceData(
                this,
                Constant.PARTNERSHIP_ADDRESS_PHOTO,
                ""
            ).toUri()
        )
        binding.ivSignature.setImageURI(
            getPreferenceData(
                this,
                Constant.PARTNERSHIP_SIGNATURE_PHOTO,
                ""
            ).toUri()
        )
        binding.ivPartnershipDeed.setImageURI(
            getPreferenceData(
                this,
                Constant.PARTNERSHIP_DEED_PHOTO,
                ""
            ).toUri()
        )
        binding.ivPrivateGstinNo.setImageURI(
            getPreferenceData(
                this,
                Constant.PRIVATE_GST_NO_PHOTO,
                ""
            ).toUri()
        )
        binding.ivPrivatePanNo.setImageURI(
            getPreferenceData(
                this,
                Constant.PRIVATE_PAN_NO_PHOTO,
                ""
            ).toUri()
        )
        binding.ivPrivateAddressProof.setImageURI(
            getPreferenceData(
                this,
                Constant.PRIVATE_ADDRESS_PHOTO,
                ""
            ).toUri()
        )
        binding.ivCertificateIncorparation.setImageURI(
            getPreferenceData(
                this,
                Constant.PRIVATE_INCORPORATION_CERTIFICATE_PHOTO,
                ""
            ).toUri()
        )
        binding.ivPrivateSignature.setImageURI(
            getPreferenceData(
                this,
                Constant.PRIVATE_SIGNATURE_PHOTO,
                ""
            ).toUri()
        )
        binding.ivAadharFront.setImageURI(
            getPreferenceData(
                this,
                Constant.SELF_AADHAR_FRONT,
                ""
            ).toUri()
        )
        binding.ivAadharBack.setImageURI(
            getPreferenceData(
                this,
                Constant.SELF_AADHAR_BACK,
                ""
            ).toUri()
        )


        if (getPreferenceData(this, Constant.MSME_CHECK, "") == "Yes") {
            binding.rbMsmeYes.isChecked = true
            binding.llMsmeNo.visibility = View.VISIBLE
            binding.etMsmeNumber.setText(getPreferenceData(this, Constant.MSME_REGISTRATION_NO, ""))
        } else {
            binding.rbMsmeNo.isChecked = true
            binding.llMsmeNo.visibility = View.GONE
        }

        if (getPreferenceData(this, Constant.BSM_CHECK, "") == "Yes") {
            binding.llBsmNo.visibility = View.VISIBLE
            binding.rgBsmYes.isChecked = true
            binding.etBsmNumber.setText(getPreferenceData(this, Constant.BSM_ID, ""))
        } else {
            binding.llBsmNo.visibility = View.GONE
            binding.rgBsmNo.isChecked = true
        }

        if (getPreferenceData(this, Constant.ACCOUNT_SURYODAY_BANK_CHECK, "") == "Yes") {
            binding.llSuryodayBankAccount.visibility = View.VISIBLE
            binding.rgBankYes.isChecked = true
        } else {
            binding.llSuryodayBankAccount.visibility = View.GONE
            binding.rgBankNo.isChecked = true
        }

        if (getPreferenceData(this, Constant.SSFB_STAFF_CHECK, "") == "Yes") {
            binding.llSsfbStaff.visibility = View.VISIBLE
            binding.rgSsfbYes.isChecked = true
        } else {
            binding.llSsfbStaff.visibility = View.GONE
            binding.rgSsfbNo.isChecked = true
        }
    }

    private fun createNotification(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "Notification Title"
            val description_Title = "Notification Description"
            val importance =NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID,name,importance).apply {
                description = description_Title
            }

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotification(){
        val builder = NotificationCompat.Builder(this,CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Connector")
            .setContentText("Application no 1092093 generated susccessfully.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)){
            notify(notificationID,builder.build())
        }
    }
    override fun setObserver() {
    }

    override fun setViewModel() {
    }
}