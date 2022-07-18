package com.example.logintask.lib.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.example.logintask.application.MainApplication
import com.example.logintask.lib.APIComponent

/**
 * BaseFragment extending Fragment to handel common functions.
 *
 * @author Komal ardekar
 */
abstract class BaseFragment : Fragment() {

    lateinit var itemView: View
    lateinit var apiComponent: APIComponent
    lateinit var mContext: Context


    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()

    }

    private fun getVisibleFragment(): Fragment? {
        val fragmentManager = activity?.supportFragmentManager
        val fragments = fragmentManager?.fragments
        if (fragments != null) {
            for (fragment in fragments) {
                if (fragment != null && fragment.isVisible)
                    return fragment
            }
        }
        return null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        itemView = inflater.inflate(getLayout(), container, false)
        apiComponent = MainApplication.instance.getAPIComponent()
        mContext = activity as Context
        setClickListeners()
        return itemView
    }

    /**
     * Abstract function to get layout res id for inflating content view.
     */
    @LayoutRes
    abstract fun getLayout(): Int

    /**
     * Abstract function to setup click listeners.
     */
    abstract fun setClickListeners()


}