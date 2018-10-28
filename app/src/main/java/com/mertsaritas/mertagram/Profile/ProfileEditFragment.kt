package com.mertsaritas.mertagram.Profile


import android.app.ProgressDialog
import android.support.v7.app.AppCompatActivity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.mertsaritas.mertagram.Models.Users

import com.mertsaritas.mertagram.R
import com.mertsaritas.mertagram.utils.EventbusDataEvents
import com.mertsaritas.mertagram.utils.UniversalImageLoader
import com.nostra13.universalimageloader.core.ImageLoader
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_login.view.*
import kotlinx.android.synthetic.main.fragment_profile_edit.*
import kotlinx.android.synthetic.main.fragment_profile_edit.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.lang.Exception


class  ProfileEditFragment : Fragment() {
    lateinit var circleImagePictureFragment:CircleImageView
    lateinit var gelenkullaniciBilgiler:Users
    lateinit var mDatabaseRef : DatabaseReference
    lateinit var mStorageRef: StorageReference

    var profilepictureUri:Uri?=null
    val RESİM_SEC=100

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view=inflater.inflate(R.layout.fragment_profile_edit,container,false)


        mDatabaseRef=FirebaseDatabase.getInstance().reference

        mStorageRef=FirebaseStorage.getInstance().reference

        setupKullaniciBilgileri(view)




        view.imgClose.setOnClickListener{
            activity!!.onBackPressed()
        }
        view.tvFotografiDegistir.setOnClickListener {
            var intent= Intent()
            intent.setType("image/+")
            intent.setAction(Intent.ACTION_PICK)
            startActivityForResult(intent,RESİM_SEC)
        }

        view.imgBtnDegisiklikleriKaydet.setOnClickListener {

            if(profilepictureUri!=null){
                var dialogYukleniyor=YukleniyorFragment()
                dialogYukleniyor.show(activity!!.supportFragmentManager,"yükleniyorFragmenti")

                dialogYukleniyor.isCancelable=false

                var uploadTask=mStorageRef.child("users").child(gelenkullaniciBilgiler!!.user_id!!).child(profilepictureUri!!.lastPathSegment).putFile(profilepictureUri!!)
                        .addOnCompleteListener(object: OnCompleteListener<UploadTask.TaskSnapshot>{
                            override fun onComplete(p0: Task<UploadTask.TaskSnapshot>) {
                                if(p0!!.isSuccessful){
                                    //Toast.makeText(activity,"Resim Yüklendi"+p0!!.getResult().downloadUrl.toString(),Toast.LENGTH_SHORT).show()

                                    mDatabaseRef.child("users").child(gelenkullaniciBilgiler!!.user_id!!).child("user_detail").child("profile_picture")
                                            .setValue(p0!!.getResult().downloadUrl.toString())

                                    dialogYukleniyor.dismiss()
                                    kullaniciAdiniGuncelle(view,true)
                                }
                            }


                        })
                        .addOnFailureListener(object : OnFailureListener{
                            override fun onFailure(p0: Exception) {
                                Log.e("HATA0",p0!!.message)
                                kullaniciAdiniGuncelle(view,false)
                            }

                        })
            }else{
                kullaniciAdiniGuncelle(view,null)

            }

        }
        


      return view
    }
    private fun kullaniciAdiniGuncelle(view: View, profilResmiDegisti: Boolean?) {
        if (!gelenkullaniciBilgiler!!.user_name!!.equals(view.etUserName.text.toString())) {
            mDatabaseRef.child("users").orderByChild("user_name").addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    var userNameKullanimdaMi = false
                    for(ds in p0!!.children){
                        var okunanKullaniciAdi=ds!!.getValue(Users::class.java)!!.user_name

                        if (okunanKullaniciAdi!!.equals(view.etUserName.text.toString())){

                            userNameKullanimdaMi=true
                            profilBilgileriniGuncelle(view,profilResmiDegisti,false)
                            break


                        }

                    }
                    if (userNameKullanimdaMi==false){
                        mDatabaseRef.child("users").child(gelenkullaniciBilgiler!!.user_id!!).child("user_name").setValue(view.etUserName.text.toString())
                        profilBilgileriniGuncelle(view,profilResmiDegisti,true)

                    }
                }

            })
        }else{
            profilBilgileriniGuncelle(view,profilResmiDegisti,null)
        }


    }

    private fun profilBilgileriniGuncelle(view: View, profilResmiDegisti: Boolean?, userNameDegisti: Boolean?) {

        var profilGuncellendiMi:Boolean?=null

        if(!gelenkullaniciBilgiler!!.ad_soyadi!!.equals(view.etProfileName.text.toString())) {
            mDatabaseRef.child("users").child(gelenkullaniciBilgiler!!.user_id!!).child("ad_soyadi").setValue(view.etProfileName.text.toString())
            profilGuncellendiMi=true
        }
        if (!gelenkullaniciBilgiler!!.user_detail!!.biography!!.equals(view.etUserBio.text.toString())){
            mDatabaseRef.child("users").child(gelenkullaniciBilgiler!!.user_id!!).child("user_detail").child("biography").setValue(view.etUserBio.text.toString())
            profilGuncellendiMi=true
        }
        if (!gelenkullaniciBilgiler!!.user_detail!!.web_site!!.equals(view.etUserWebSite.text.toString())) {
            mDatabaseRef.child("users").child(gelenkullaniciBilgiler!!.user_id!!).child("user_detail").child("web_site").setValue(view.etUserWebSite.text.toString())
            profilGuncellendiMi = true

        }

        if(profilResmiDegisti==null && userNameDegisti==null && profilGuncellendiMi==null){
            Toast.makeText(activity,"Değişiklik Yok",Toast.LENGTH_SHORT).show()
        }else if (userNameDegisti==false && (profilGuncellendiMi==true||profilResmiDegisti==true)){
            Toast.makeText(activity,"Bilgiler güncellendi ama kullanıcı adı kullanımda",Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(activity,"kullanıcı güncellendi",Toast.LENGTH_SHORT).show()
            activity!!.onBackPressed()
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode==RESİM_SEC && resultCode==AppCompatActivity.RESULT_OK && data!!.data != null) {
            profilepictureUri =data!!.data!!

            circleProfileImage.setImageURI(profilepictureUri)
        }
    }


    private fun setupKullaniciBilgileri(view: View?) {

        view!!.etProfileName.setText(gelenkullaniciBilgiler!!.ad_soyadi)
        view!!.etUserName.setText(gelenkullaniciBilgiler!!.user_name)

        if (!gelenkullaniciBilgiler!!.user_detail!!.biography.isNullOrEmpty()) {
            view!!.etUserBio.setText(gelenkullaniciBilgiler!!.user_detail!!.biography)

        }
        if (!gelenkullaniciBilgiler!!.user_detail!!.web_site.isNullOrEmpty()){
            view!!.etUserWebSite.setText(gelenkullaniciBilgiler!!.user_detail!!.web_site)
        }
        var imgUrl=gelenkullaniciBilgiler!!.user_detail!!.profile_picture
        UniversalImageLoader.setImage(imgUrl!!,view!!.circleProfileImage,view!!.progressBar,"")

    }




    //////////////////////////////////EventBus/////////////////////////////////////
    @Subscribe(sticky = true)
    internal fun onKullaniciBilgileriEvent(kullaniciBilgileri: EventbusDataEvents.KullaniciBilgileriniGönder){


        gelenkullaniciBilgiler=kullaniciBilgileri!!.kullanici!!



    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        EventBus.getDefault().register(this)
    }

    override fun onDetach() {
        super.onDetach()
        EventBus.getDefault().unregister(this)
    }


}

