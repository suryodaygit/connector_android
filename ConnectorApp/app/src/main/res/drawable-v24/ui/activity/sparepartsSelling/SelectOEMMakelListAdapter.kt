package com.cmrk.ui.activity.sparepartsSelling

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cmrk.R
import com.cmrk.databinding.OemMakeListRowBinding
import com.cmrk.ui.activity.makeModel.SelectMakeModel
import com.cmrk.ui.activity.makeModel.SelectMakeModelViewModel


class SelectOEMMakelListAdapter(
    private var visitorsViewModel: SelectMakeModelViewModel,
    private var surveyList: List<SelectMakeModel>
) : RecyclerView.Adapter<SelectOEMMakelListAdapter.RoutListAdapterHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RoutListAdapterHolder {
        return RoutListAdapterHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.context),
                R.layout.oem_make_list_row,
                viewGroup,
                false
            ) as OemMakeListRowBinding

        )
    }

    override fun getItemCount(): Int {
        return surveyList.size
    }

    fun setData(surveyList: List<SelectMakeModel>) {
        this.surveyList = surveyList
        notifyDataSetChanged()
    }


    override fun onBindViewHolder(routListAdapterHolder: RoutListAdapterHolder, position: Int) {
        routListAdapterHolder.bindView(visitorsViewModel, surveyList[position])
    }

    class RoutListAdapterHolder(var oemMakeListRowBinding: OemMakeListRowBinding) :
        RecyclerView.ViewHolder(oemMakeListRowBinding.root) {

        fun bindView(
            dashboardViewModel: SelectMakeModelViewModel,
            sekectmakemodel: SelectMakeModel
        ) {
            oemMakeListRowBinding.selectMakeviewmodel = dashboardViewModel
            oemMakeListRowBinding.oemmakemodel = sekectmakemodel
            oemMakeListRowBinding.executePendingBindings()
        }

    }

}