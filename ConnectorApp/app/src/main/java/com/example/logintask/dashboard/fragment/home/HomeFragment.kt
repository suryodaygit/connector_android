package com.example.logintask.dashboard.fragment.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.example.healthqrapp.lib.utils.Constant
import com.example.logintask.dashboard.activity.*
import com.example.logintask.databinding.FragmentHomeBinding
import com.example.logintask.lib.utils.getPreferenceData

class HomeFragment : Fragment() {

    private var homeBinding:FragmentHomeBinding?=null

    companion object{
         var COMPLETED_PAGE_STATUS =""
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = homeBinding!!.root
        setOnClickListener()
        return root
    }

    private fun setOnClickListener(){
        homeBinding?.btnReview?.btn?.text = "Review"
        homeBinding?.llBasicDetails?.visibility = View.VISIBLE

        val userName= activity?.let { getPreferenceData(it,Constant.USER_NAME,"") }
        homeBinding?.tvUserName?.text = "Welcome $userName"
        //homeBinding?.llLead?.visibility = View.VISIBLE

        homeBinding?.llBasic?.setOnClickListener {
            startActivity(Intent(activity, BasicActivity::class.java))
        }

        homeBinding?.llAddress?.setOnClickListener {
            startActivity(Intent(activity, AddressActivity::class.java))
        }

        homeBinding?.llOther?.setOnClickListener {
            startActivity(Intent(activity, OtherActivity::class.java))
        }

        homeBinding?.llDocument?.setOnClickListener {
            startActivity(Intent(activity, DocumentActivity::class.java))
        }

        homeBinding?.llBusinessDetail?.setOnClickListener {
            startActivity(Intent(activity, BusinessActivity::class.java))
        }

        homeBinding?.llReview?.setOnClickListener {
            startActivity(Intent(activity, ReviewActivity::class.java))
        }

        val imageUrl = activity?.let { getPreferenceData(it, Constant.PROFILE_IMAGE,"").toUri() }
        homeBinding?.profileImage?.setImageURI(imageUrl)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        homeBinding = null
    }

    override fun onResume() {
        super.onResume()
        when(COMPLETED_PAGE_STATUS) {
            "Basic" -> {
                homeBinding?.ivBasicCompleteStatus?.visibility = View.VISIBLE
            }
            "Address" -> {
                homeBinding?.ivAddressCompleteStatus?.visibility = View.VISIBLE
            }
            "Business" -> {
                homeBinding?.ivBusinessCompleteStatus?.visibility = View.VISIBLE
            }
            "Other" -> {
                homeBinding?.ivOtherCompleteStatus?.visibility = View.VISIBLE
            }
            "Document"-> {
                homeBinding?.ivDocumentCompleteStatus?.visibility = View.VISIBLE
            }
            "Review" ->{
                homeBinding?.tvStatus?.text = "Status : Under Review"
            }
        }
    }
}