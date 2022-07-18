package com.example.healthqrapp.lib.interfaces

/**
 * OnRecyclerClickListener is used to determine click events in RecyclerView.
 *
 * @author Komal Ardekar
 */
interface OnRecyclerClickListener {

    /**
     * @param where    Based on EnumClicks identify what item was clicked
     * @param view     view of the item clicked
     * @param position current item position which was clicked
     */
    fun onRecyclerClick(where: EnumClicks, position: Int)
}