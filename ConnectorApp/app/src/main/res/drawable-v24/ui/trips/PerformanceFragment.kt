package com.cmrk.ui.trips

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmrk.databinding.FragmentTripsBinding
import com.cmrk.ui.activity.lead.LeadAdapter
import com.cmrk.ui.activity.visit.LeadViewModel
import com.cmrk.ui.profile.ProfileViewModel

class PerformanceFragment : Fragment(),PerformanceNavigator{

    private var _binding: FragmentTripsBinding? = null
    private var performanceList :ArrayList<Data> = arrayListOf()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var performanceViewModel : PerformanceViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        performanceViewModel = activity?.let { PerformanceViewModel(this, it) }!!

        _binding = FragmentTripsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        performanceViewModel.getPerformanceList("9")
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setAdapter(){
        val adapter = PerformanceAdapter(performanceList)
        binding.rvPerformance.layoutManager = LinearLayoutManager(activity,
            LinearLayoutManager.VERTICAL,false)
        binding.rvPerformance.adapter = adapter
    }

    override fun performanceSuccess(successResponse: PerformanceResponseModel) {
        if(successResponse.data.size !=0){
            performanceList = successResponse.data
            setAdapter()
        }
    }
}