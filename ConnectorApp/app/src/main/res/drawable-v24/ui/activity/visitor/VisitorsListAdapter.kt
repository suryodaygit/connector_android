package com.cmrk.ui.activity.visitor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cmrk.R
import com.cmrk.databinding.VisitorListRowBinding


class VisitorsListAdapter(
    private var visitorsViewModel: VisitorsViewModel,
    private var surveyList: List<VisitorsModel>
) : RecyclerView.Adapter<VisitorsListAdapter.VisitorListAdapterHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): VisitorListAdapterHolder {
        return VisitorListAdapterHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.context),
                R.layout.visitor_list_row,
                viewGroup,
                false
            ) as VisitorListRowBinding

        )
    }

    override fun getItemCount(): Int {
        return surveyList.size
    }

    fun setData(surveyList: List<VisitorsModel>) {
        this.surveyList = surveyList
        notifyDataSetChanged()
    }


    override fun onBindViewHolder(routListAdapterHolder: VisitorListAdapterHolder, position: Int) {
        routListAdapterHolder.bindView(visitorsViewModel, surveyList[position])
    }

    class VisitorListAdapterHolder(var companyListRowBinding: VisitorListRowBinding) :
        RecyclerView.ViewHolder(companyListRowBinding.root) {

        fun bindView(
            visitorsListViewModel: VisitorsViewModel,
            visitorlistmodel: VisitorsModel
        ) {
            companyListRowBinding.visitorviewmodel = visitorsListViewModel
            companyListRowBinding.visitormodel = visitorlistmodel
            companyListRowBinding.executePendingBindings()
        }

    }

}