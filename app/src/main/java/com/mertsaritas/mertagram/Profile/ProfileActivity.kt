package com.mertsaritas.mertagram.Profile

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.mertsaritas.mertagram.Login.LoginActivity
import com.mertsaritas.mertagram.Models.Users
import com.mertsaritas.mertagram.R
import com.mertsaritas.mertagram.utils.BottomnavigationViewHelper
import com.mertsaritas.mertagram.utils.EventbusDataEvents
import com.mertsaritas.mertagram.utils.UniversalImageLoader
import kotlinx.android.synthetic.main.activity_profile.*
import org.greenrobot.eventbus.EventBus

class ProfileActivity : AppCompatActivity() {

    private val ACTİVİTY_NO=4
    private val TAG="ProfileActivity"
    lateinit var mAuth: FirebaseAuth
    lateinit var mAuthListener: FirebaseAuth.AuthStateListener
    lateinit var mUser : FirebaseUser
    lateinit var mRef:DatabaseReference

    var ilkAcilis = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        setupAuthListener()
        mAuth = FirebaseAuth.getInstance()
        mUser = mAuth.currentUser!!
        mRef = FirebaseDatabase.getInstance().reference


        setupToolbar()

        kullaniciBilgileriniGetir()

    }



    private fun kullaniciBilgileriniGetir() {
        tvProfileDüzenleButton.isEnabled=false
        imgProfileSettings.isEnabled=false
        mRef.child("users").child(mUser.uid).addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0!!.getValue()!= null) {
                    var okunanKullaniciBilgiler = p0!!.getValue(Users::class.java)


                    EventBus.getDefault().postSticky(EventbusDataEvents.KullaniciBilgileriniGönder(okunanKullaniciBilgiler))
                    tvProfileDüzenleButton.isEnabled=true
                    imgProfileSettings.isEnabled=true


                    tvProfilAdiToolbarr.setText(okunanKullaniciBilgiler!!.user_name)
                    tvProfilGercekAdi.setText(okunanKullaniciBilgiler!!.ad_soyadi)
                    tvFollewerSayisi.setText(okunanKullaniciBilgiler!!.user_detail!!.follower)
                    tvFollowingSayisi.setText(okunanKullaniciBilgiler!!.user_detail!!.following)
                    tvPostSayisi.setText(okunanKullaniciBilgiler!!.user_detail!!.post)

                    if (ilkAcilis){
                        ilkAcilis=false
                        var imgUrl:String=okunanKullaniciBilgiler!!.user_detail!!.profile_picture!!
                        UniversalImageLoader.setImage(imgUrl,circleProfileİmage,progressBar3,"")
                    }

                    if (!okunanKullaniciBilgiler!!.user_detail!!.biography!!.isNullOrEmpty()){
                        tvBiyografii.visibility=View.VISIBLE
                        tvBiyografii.setText(okunanKullaniciBilgiler!!.user_detail!!.biography!!)

                    }else{
                        tvBiyografii.visibility=View.GONE
                    }
                    if (!okunanKullaniciBilgiler!!.user_detail!!.web_site!!.isNullOrEmpty()){
                        tvWebSitesi.visibility=View.VISIBLE
                        tvWebSitesi.setText(okunanKullaniciBilgiler!!.user_detail!!.web_site!!)


                    }else{
                        tvWebSitesi.visibility=View.GONE
                    }




                }




            }


        })
    }


    private fun setupToolbar() {
        imgProfileSettings.setOnClickListener(){
            var intent=Intent(this,ProfileSettingActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }
        tvProfileDüzenleButton.setOnClickListener {

            profileRoot.visibility=View.GONE
            var transaction=supportFragmentManager.beginTransaction()
            transaction.replace(R.id.profileContainer,ProfileEditFragment())
            transaction.addToBackStack("editProfileFragmentEklendi")
            transaction.commit()
        }
    }

    override fun onResume() {
        setupNavigationView()
        super.onResume()
    }

    fun setupNavigationView(){
        BottomnavigationViewHelper.setupBottomNavigationView(bottomNavigationView)
        BottomnavigationViewHelper.setupNavigation(this,bottomNavigationView)
        var menu=bottomNavigationView.menu
        var menuItem=menu.getItem(ACTİVİTY_NO)
        menuItem.setChecked(true)
    }

    override fun onBackPressed() {
        profileRoot.visibility=View.VISIBLE
        super.onBackPressed()
    }

    private fun setupAuthListener() {
        mAuthListener     = object:FirebaseAuth.AuthStateListener{
            override fun onAuthStateChanged(p0: FirebaseAuth) {

                var user = FirebaseAuth.getInstance().currentUser

                if(user == null )     {

                    var intent= Intent(this@ProfileActivity,LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    finish()


                }else {




                }
            }

        }
    }


    override fun onStart() {
        super.onStart()
        mAuth.addAuthStateListener(mAuthListener)
    }

    override fun onStop() {
        super.onStop()

        if (mAuthListener != null)     {
            mAuth.removeAuthStateListener(mAuthListener)
        }
    }

}