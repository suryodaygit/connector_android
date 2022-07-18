package com.example.logintask.lib.base

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.healthqrapp.lib.interfaces.BasePresenter

abstract class BaseActivity : AppCompatActivity() {

    lateinit var mContext : Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayout())
        setViewModel()
        init()
        setObserver()
    }

/*
* abstract function to get layout id for setting content view
*/
    abstract fun getLayout():Int

/*
*  abstract function for initializing
*/
    abstract fun init()

 /*
 * abstract set Observer
 */
    abstract fun setObserver()

    /*
* abstract set ViewModel
*/
    abstract fun setViewModel()
}