package com.example.healthqrapp.lib.utils

import android.net.Uri

object Constant {

     var SELECTED_ITEM_COUNT="0"
     var EMAIL ="email"
     var LOGIN_TYPE="login type"
     var USER_NAME = "user name"
     var ROLE=""
     var SOCIAL_LOGIN="false"
     var PASSWORD = "password"
     var SKIP_LOGIN ="skip login"
     var CART_COUNT = "cart count"
     var USER_ID="User Id"
     var CART_ID="cart Id"
     var ITEM_ID="Item Id"
     var PAYMENT_KEY="rzp_test_NZedMh9GhWjNa2"
     var SECRETE_KEY = "glAoJjiLAOFlO8RzXhd6Ynz3"
     var USER_EMAIL=""
     var PAYMENT_ID=""
     var PAYMENT_SIGNATURE=""
     var ITEM_GROUP_ID = "1"
     var UPLOAD_DOCUMENT =""
     var ENTITY ="entity"
     var COMPANY_NAME ="company name"
     var PAN_CARD_NO="pan card no"
     var NAME="name"
     var MOBILE_NO="mobile no"
     var ALTERNATE_MOBILE_NO="alternate mobile no"
     var GST_NO ="gst no"
     var DOB ="dob"
     var AADHAR_CARD_NO="adhar no"
     var MSME_REGISTRATION_NO="msme registration no"
     var BSM_ID = "bsm id"
     var MSME_CHECK="No"
     var BSM_CHECK = "No"
     var RESIDENTIAL_PINCODE="residential pin code"
     var RESIDENTIAL_CITY = "residetial city"
     var RESIDENTIAL_DISTRICT = "residential district"
     var RESIDENTIAL_STATE = "residential state"
     var RESIDENTIAL_ADDRESS = "residential address"
     var OFFICE_PIN_CODE = "office pin code"
     var OFFICE_CITY = "office city"
     var OFFICE_DISTRICT = "office district"
     var OFFICE_STATE = "office state"
     var OFFICE_ADDRESS = "office address"
     var PRESENT_OCCUPATION = "present occupation"
     var CALLING_DATE = "calling date"
     var CALLING_TIME = "calling time"
     var PREFERRED_LANGUAGE = "preferred language"
     var ACCOUNT_SURYODAY_BANK_CHECK = "account suryoday bank"
     var SURYODAY_CUSTOMER_ID = "suryoday customer id"
     var MEETING_DATE = "meeting date"
     var MEETING_TIME = "meeting time"
     var SSFB_STAFF_CHECK = "ssfb staff check"
     var STAFF_NAME = "staff name"
     var RELATIONSHIP_WITH_STAFF = "relationship with staff"
     var SELECTED_ENTITY = "selected entity"
     var SELECTED_ENTITY_POSITION = "selected entity position"
     var PROFILE_IMAGE = "profile image"
     var CUSTOMER_PHOTO = "customer photo"
     var CANCELLED_CHEQUE = "cancelled cheque"
     var CUSTOMER_ADDRESS_PROOF = "customer address proof"
     var IDENTITY_PROOF = "identity proof"
     var PROPRIENTORSHIP_DECLARATION_PHOTO = "proprietorship declaration photo"
     var PROPRIENTORSHIP_GSTNO_PHOTO = "proprietorship gst no photo"
     var PROPRIENTORSHIP_ADDRESS_PROOF = "proprietorship address proof"
     var PARTNERSHIP_GSTNO_PHOTO = "partnership gstno photo"
     var PARTNERSHIP_PAN_PHOTO = "partnership pan photo"
     var PARTNERSHIP_ADDRESS_PHOTO = "partnership address photo"
     var PARTNERSHIP_SIGNATURE_PHOTO = "partnership signature photo"
     var PARTNERSHIP_DEED_PHOTO = "partnership deed photo"
     var PRIVATE_GST_NO_PHOTO = "private gst no"
     var PRIVATE_PAN_NO_PHOTO = "private pan no photo"
     var PRIVATE_ADDRESS_PHOTO = "private address photo"
     var PRIVATE_INCORPORATION_CERTIFICATE_PHOTO = "private address photo"
     var PRIVATE_SIGNATURE_PHOTO = "private address photo"
     var SELF_AADHAR_FRONT = "self aadhar front"
     var SELF_AADHAR_BACK = "self aadhar back"
     var IFSC_CODE = "ifsc code"
     var BANK_ACCOUNT_NO = "bank account no"
     var BANK_NAME = "bank name"
     var BRANCH_NAME = "branch name"
     var ACCOUNT_HOLDER_NAME = "account holder name"
     var LOCATION ="location"
     var USERNAME="user name"

     var mCurrentPhotoPath: String? = null


     /*Shared Preferences Keys*/
    const val PREF_ADDTOCART_DATA = "UserData"
    var CART_TITLE="CartTitle"

    var BASE_URL ="http://13.233.79.85/eportal/api/" /*when (BuildConfig.BUILD_TYPE) {
        "debug" -> DEV_BASE_URL
        "preprod" -> PRE_PROD_BASE_URL
        "staging" -> STAG_BASE_URL
        else -> PROD_BASE_URL
    }*/

    /**
     * @return All user data.
     *//*
    fun getUserData(): InsuranceDetailsModel {
        return Gson().fromJson(
            Utility.getPreference(PREF_ADDTOCART_DATA),  InsuranceDetailsModel::class.java   )
    }*/

    /***
     * get cart title
     */
    fun getCartTitle():String?{
        return Utility.getPreference(CART_TITLE)
    }

    /***
     * get cart image
     */
    /*fun getCartImage():Int{
        return getUserData().image
    }*/

    /***
     * get cart size
     */
   /* fun getCartSize():String{
        return getUserData().size
    }*/

    /***
     * get cart price
     */
  /*  fun getCartPrice():String{
        return getUserData().amount
    }*/

}