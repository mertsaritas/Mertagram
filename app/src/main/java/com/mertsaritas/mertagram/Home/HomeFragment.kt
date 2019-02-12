package com.mertsaritas.mertagram.Home

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mertsaritas.mertagram.R
import com.mertsaritas.mertagram.utils.BottomnavigationViewHelper
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : Fragment() {

    lateinit var fragmentView:View
    private val ACTİVİTY_NO=0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentView=inflater?.inflate(R.layout.fragment_home,container,false)
        return fragmentView
    }

    override fun onResume() {
        setupNavigationView()
        super.onResume()
    }
    fun setupNavigationView(){

        var fragmentBottomNowView=fragmentView.bottomNavigationView
        BottomnavigationViewHelper.setupBottomNavigationView(fragmentBottomNowView)
        BottomnavigationViewHelper.setupNavigation(activity!!,fragmentBottomNowView)
        var menu=fragmentBottomNowView.menu
        var menuItem=menu.getItem(ACTİVİTY_NO)
        menuItem.setChecked(true)
    }
}
