package com.mertsaritas.mertagram.Profile

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.mertsaritas.mertagram.R
import com.mertsaritas.mertagram.utils.BottomnavigationViewHelper
import kotlinx.android.synthetic.main.activity_profile_setting.*

class ProfileSettingActivity : AppCompatActivity() {
    private val ACTİVİTY_NO=4
    private val TAG="ProfileActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_setting)
        setupNavigationView()
        setupToolbar()
        fragmentNavigations()
    }

    private fun fragmentNavigations() {
        tvProfileSettingsHesapAyarlari.setOnClickListener {
            profileSettingsRoot.visibility= View.GONE
            var transaction=supportFragmentManager.beginTransaction()
            transaction.replace(R.id.profileSettingsContainer,ProfileEditFragment())
            transaction.addToBackStack("editProfileFragmentEklendi")
            transaction.commit()

        }
        tvCikisYap.setOnClickListener{
            var dialog=SignOutFragment()
            dialog.show(supportFragmentManager,"çıkışYapDialogGösterr")
        }


    }

    private fun setupToolbar() {
        imgBack.setOnClickListener{
            onBackPressed()
        }


    }

    override fun onBackPressed() {
        profileSettingsRoot.visibility= View.VISIBLE
        super.onBackPressed()
    }

    fun setupNavigationView(){
            BottomnavigationViewHelper.setupBottomNavigationView(bottomNavigationView)
            BottomnavigationViewHelper.setupNavigation(this,bottomNavigationView)
            var menu=bottomNavigationView.menu
            var menuItem=menu.getItem(ACTİVİTY_NO)
            menuItem.setChecked(true)
        }
    }