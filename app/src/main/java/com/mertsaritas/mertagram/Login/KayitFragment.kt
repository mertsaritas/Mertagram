package com.mertsaritas.mertagram.Login


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.mertsaritas.mertagram.Models.UserDetail
import com.mertsaritas.mertagram.Models.Users

import com.mertsaritas.mertagram.R
import com.mertsaritas.mertagram.utils.EventbusDataEvents
import kotlinx.android.synthetic.main.fragment_kayit.*
import kotlinx.android.synthetic.main.fragment_kayit.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe



class KayitFragment : Fragment() {

    var telNo = ""
    var verificationID = ""
    var gelenKod = ""
    var gelenEmail = ""
    var emailIleKayitIslemi= true
    lateinit var mAuth:FirebaseAuth
    lateinit var mRef:DatabaseReference
    lateinit var progressBar: ProgressBar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        var view = inflater!!.inflate(R.layout.fragment_kayit, container, false)

        progressBar=view.pbKullaniciKayit

        mAuth = FirebaseAuth.getInstance()


        view.tvGirisYap.setOnClickListener {
            var intent=Intent (activity,LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)

        }

        mRef =FirebaseDatabase.getInstance().reference

        view.etAdSoyad.addTextChangedListener(watcher)
        view.etKullaniciAdi.addTextChangedListener(watcher)
        view.etSifre.addTextChangedListener(watcher)

        view.btnGiris.setOnClickListener {


            var userNameKullanimdaMi = false
            mRef.child("users").addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {




                    if(p0!!.getValue()!=null){
                        for (user in p0!!.children){
                            var okunanKullanici=user.getValue(Users::class.java)
                            if(okunanKullanici!!.user_name!!. equals(view.etKullaniciAdi.text.toString()) )     {
                                Toast.makeText(activity , "Kullaıcı Adı Kullanılyor" , Toast.LENGTH_SHORT).show()
                                userNameKullanimdaMi=true
                                break

                            }


                        }
                        if (userNameKullanimdaMi==false)           {

                        }
                    }

                }

            })




            progressBar.visibility=View.VISIBLE
            ///kullanıcı email ile giriş yapacak
            if (emailIleKayitIslemi) {

                var sifre=view.etSifre.text.toString()
                var adSoyad =view.etAdSoyad.text.toString()
                var userName=view.etKullaniciAdi.text.toString()

                mAuth.createUserWithEmailAndPassword(gelenEmail  ,  sifre )
                        .addOnCompleteListener(object : OnCompleteListener<AuthResult>{
                            override fun onComplete(p0: Task<AuthResult>) {

                                if (p0!!.isSuccessful){
                                    Toast.makeText(activity , "Oturum email ile açıldı Açıldı" + mAuth.currentUser!!.uid  ,  Toast.LENGTH_SHORT).show()

                                    var userID=mAuth.currentUser!!.uid.toString()
                                    ///oturum açan kullanıcının vrilerinin databaseye kaydedelim
                                    var kaydedilecekKullaniciDetaylari=UserDetail("0","0","0","" ,"" , "")
                                    var kaydedilecekKullanici=Users(gelenEmail , sifre , userName,adSoyad,"","" , userID , kaydedilecekKullaniciDetaylari )

                                    mRef.child("users").child(userID).setValue(kaydedilecekKullanici)
                                            .addOnCompleteListener(object: OnCompleteListener<Void>{
                                                override fun onComplete(p0: Task<Void>) {
                                                    if (p0!!.isSuccessful){
                                                        Toast.makeText(activity , "Kullanıcı Kydedildi", Toast.LENGTH_SHORT).show()
                                                        progressBar.visibility=View.INVISIBLE
                                                    }else {
                                                        progressBar.visibility=View.INVISIBLE
                                                        mAuth.currentUser!!.delete()
                                                                .addOnCompleteListener(object : OnCompleteListener<Void> {
                                                                    override fun onComplete(p0: Task<Void>) {
                                                                        if (p0!!.isSuccessful){
                                                                            Toast.makeText(activity , "Kullanıcı Kaydedilmedi Lütfen Tekrar  Deneyin", Toast.LENGTH_SHORT).show()
                                                                        }
                                                                    }

                                                                })
                                                    }

                                                }

                                            })



                                }else {
                                    progressBar.visibility=View.INVISIBLE
                                    Toast.makeText(activity , "oturum Açılamadı" + p0!!.exception,Toast.LENGTH_SHORT).show()
                                }
                            }

                        })

            }

            //kullanıcı telefon numarası ile giriş yapacaktır
            else {

                var sifre=view.etSifre.text.toString()
                var sahteEmail=telNo+"@mert.com"
                var adSoyad =view.etAdSoyad.text.toString()
                var userName=view.etKullaniciAdi.text.toString()
                mAuth.createUserWithEmailAndPassword( sahteEmail  ,  sifre )
                        .addOnCompleteListener(object : OnCompleteListener<AuthResult>{
                            override fun onComplete(p0: Task<AuthResult>) {

                                if (p0!!.isSuccessful){
                                    Toast.makeText(activity , "Oturum tel no ile Açıldı Uid : " + mAuth.currentUser!!.uid  ,  Toast.LENGTH_SHORT).show()

                                    var userID=mAuth.currentUser!!.uid.toString()
                                    ///oturum açan kullanıcının vrilerinin databaseye kaydedelim

                                    var kaydedilecekKullaniciDetaylari=UserDetail("0","0","0","" ,"" , "")
                                    var kaydedilecekKullanici=Users("",sifre,userName,adSoyad,telNo,sahteEmail,userID , kaydedilecekKullaniciDetaylari)

                                    mRef.child("users").child(userID).setValue(kaydedilecekKullanici)
                                            .addOnCompleteListener(object: OnCompleteListener<Void>{
                                                override fun onComplete(p0: Task<Void>) {
                                                    if (p0!!.isSuccessful){
                                                        Toast.makeText(activity , "Kullanıcı Kydedildi", Toast.LENGTH_SHORT).show()
                                                        progressBar.visibility=View.INVISIBLE
                                                    }else {
                                                        progressBar.visibility=View.INVISIBLE
                                                        mAuth.currentUser!!.delete()
                                                                .addOnCompleteListener(object : OnCompleteListener<Void> {
                                                                    override fun onComplete(p0: Task<Void>) {
                                                                        if (p0!!.isSuccessful){
                                                                            Toast.makeText(activity , "Kullanıcı Kaydedilmedi Lütfen Tekrar  Deneyin", Toast.LENGTH_SHORT).show()
                                                                        }
                                                                    }

                                                                })
                                                    }

                                                }

                                            })

                                }else {
                                    progressBar.visibility=View.INVISIBLE
                                    Toast.makeText(activity , "oturum Açılamadı" + p0!!.exception,Toast.LENGTH_SHORT).show()
                                }
                            }

                        })

            }









        }


        return view
    }

    var watcher:TextWatcher=object: TextWatcher{
        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s!!.length>5){

                if  (etAdSoyad.text.toString().length>5 && etKullaniciAdi.text.toString().length>5 && etSifre.text.toString().length>5){

                    btnGiris.isEnabled=true
                    btnGiris.setTextColor(ContextCompat.getColor(activity!!, R.color.beyaz))
                    btnGiris.setBackgroundResource(R.drawable.register_button_aktif)

                }else {
                    btnGiris.isEnabled = false
                    btnGiris.setTextColor(ContextCompat.getColor(activity!!, R.color.sonukmavi))
                    btnGiris.setBackgroundResource(R.drawable.register_button)
                }


            }else {
                btnGiris.isEnabled = false
                btnGiris.setTextColor(ContextCompat.getColor(activity!!, R.color.sonukmavi))
                btnGiris.setBackgroundResource(R.drawable.register_button)
            }
        }

    }


    //////////////////////////////////EventBus/////////////////////////////////////
    @Subscribe(sticky = true)
    internal fun onKayitEvent(kayitBilgileri: EventbusDataEvents.KayitBilgileriniGonder){

        if(kayitBilgileri.emailkayit==true) {
            emailIleKayitIslemi=true
            gelenEmail = kayitBilgileri.email!!

            Toast.makeText(activity,"Gelen Mail"+gelenEmail,Toast.LENGTH_SHORT).show()
            Log.e("mert", "Gelen Emaill : " + gelenEmail)
        }else{
            emailIleKayitIslemi=false
            telNo=kayitBilgileri.telNo!!
            verificationID=kayitBilgileri.verificationID!!
            gelenEmail=kayitBilgileri.code!!

            Toast.makeText(activity,"Gelen Kod : "+gelenKod+"VerifiationID"+verificationID,Toast.LENGTH_SHORT).show()

        }


    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        EventBus.getDefault().register(this)
    }

    override fun onDetach() {
        super.onDetach()
        EventBus.getDefault().unregister(this)
    }
    //////////////////////////////////EventBus///////////////////////////////////

}
