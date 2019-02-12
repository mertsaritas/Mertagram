package com.mertsaritas.mertagram.Share


import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter

import com.mertsaritas.mertagram.R
import com.mertsaritas.mertagram.utils.*
import kotlinx.android.synthetic.main.activity_share.*
import kotlinx.android.synthetic.main.fragment_share_gallery.*
import kotlinx.android.synthetic.main.fragment_share_gallery.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


class ShareGalleryFragment : Fragment() {

    var secilenDosyaYolu :String?=null
    var dosyaTuruResimMi : Boolean?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_share_gallery, container, false)

        var klasorPath = ArrayList<String>()
        var klasorAdlari = ArrayList<String>()

        var root = Environment.getExternalStorageDirectory().path

        var kameraResimleri= root+"/DCIM/Camera"
        var indirilenResimler=root+"/Download"
        var whatsappResimleri=root+"/WhatsApp/Media/WhatsApp Images"

//bi sıkıntı yok sadece commit etmedin diye kırmızı g
        klasorPath.add(kameraResimleri)
        klasorPath.add(indirilenResimler)
        klasorPath.add(whatsappResimleri)

        klasorAdlari.add("Kamera")
        klasorAdlari.add("İndirilenler")
        klasorAdlari.add("Whatsapp")

        var spinnerArrayAdapter = ArrayAdapter(activity, android.R.layout.simple_spinner_item, klasorAdlari)
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        view.spnKlasorAdlari.adapter = spinnerArrayAdapter

        view.spnKlasorAdlari.setSelection(0)

        view.spnKlasorAdlari.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                //setupGridView(DosyaIslemleri.klasordekiDosylariGetir(klasorPath.get(position)))

                setupRecyclerView(DosyaIslemleri.klasordekiDosylariGetir(klasorPath.get(position)))


            }


        }

        view.tvIleriButton.setOnClickListener {
            activity!!.anaLayout.visibility=View.GONE
            activity!!.fragmentContainerLayout.visibility=View.VISIBLE
            var transaction=activity!!.supportFragmentManager.beginTransaction()

            EventBus.getDefault().postSticky(EventbusDataEvents.PaylasilacakResmiGonder(secilenDosyaYolu,dosyaTuruResimMi))
            videoView.stopPlayback()
            transaction.replace(R.id.fragmentContainerLayout,ShareNextFragment())
            transaction.addToBackStack("shareNextFragmentEklendi")
            transaction.commit()
        }


        view.imgClose.setOnClickListener {
            activity!!.onBackPressed()
        }

        return view
    }

    private fun setupRecyclerView(klasordekiDosylar: ArrayList<String>) {
        var recyclerViewAdapter=ShareGalleryRecyclerAdapter(klasordekiDosylar,this.activity!!)
        recyclerViewDosyalar.adapter=recyclerViewAdapter

        var layoutManager=GridLayoutManager(this.activity,4)
        recyclerViewDosyalar.layoutManager=layoutManager

        recyclerViewDosyalar.setHasFixedSize(true);
        recyclerViewDosyalar.setItemViewCacheSize(30);
        recyclerViewDosyalar.setDrawingCacheEnabled(true);
        recyclerViewDosyalar.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);

        secilenDosyaYolu=klasordekiDosylar.get(0)

        resimVeyaVideoGoster(secilenDosyaYolu!!)


    }

   /* fun setupGridView(secilenKlasordekiDosyalar : ArrayList<String>) {
        var gridAdapter = ShareActivityGridViewAdapter(activity, R.layout.tek_sutun_grid_resim, secilenKlasordekiDosyalar)


        recyclerViewDosyalar.adapter = gridAdapter
        secilenDosyaYolu=secilenKlasordekiDosyalar.get(0)
        resimVeyaVideoGoster(secilenKlasordekiDosyalar.get(0))

        recyclerViewDosyalar.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                secilenDosyaYolu=secilenKlasordekiDosyalar.get(position)
                resimVeyaVideoGoster(secilenKlasordekiDosyalar.get(position))

            }

        })

    }*/

    private fun resimVeyaVideoGoster(dosyaYolu: String) {

        var dosyaTuru=dosyaYolu.substring(dosyaYolu.lastIndexOf("."))
        //file://asdsadasdas.mp4


        if(dosyaTuru != null){
            if(dosyaTuru.equals(".mp4")){

                videoView.visibility=View.VISIBLE
                imgCropView.visibility=View.GONE
                dosyaTuruResimMi=false
                videoView.setVideoURI(Uri.parse("file://"+dosyaYolu))
                videoView.start()

            }else {
                videoView.visibility=View.GONE
                imgCropView.visibility=View.VISIBLE
                dosyaTuruResimMi=true
                UniversalImageLoader.setImage(dosyaYolu,imgCropView,null,"file://")
        }

        }



    }

    override fun onResume() {
        super.onResume()

    }

    override fun onPause() {
        super.onPause()

    }

    override fun onDestroy() {
        super.onDestroy()
    }

    //////////////////////////////////EventBus/////////////////////////////////////
    @Subscribe
    internal fun onSecilenResimEvent(secilenDosya: EventbusDataEvents.GalerySecilenDosyaYolunuGonder) {


        secilenDosyaYolu=secilenDosya.dosyaYolu

        resimVeyaVideoGoster(secilenDosyaYolu!!)



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
