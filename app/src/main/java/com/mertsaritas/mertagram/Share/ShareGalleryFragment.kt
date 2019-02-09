package com.mertsaritas.mertagram.Share


import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.util.EventLog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter

import com.mertsaritas.mertagram.R
import com.mertsaritas.mertagram.utils.Dosyaİslemleri
import com.mertsaritas.mertagram.utils.EventbusDataEvents
import com.mertsaritas.mertagram.utils.ShareActivityGridViewAdapter
import com.mertsaritas.mertagram.utils.UniversalImageLoader
import kotlinx.android.synthetic.main.activity_share.*
import kotlinx.android.synthetic.main.fragment_share_gallery.*
import kotlinx.android.synthetic.main.fragment_share_gallery.view.*
import org.greenrobot.eventbus.EventBus


class ShareGalleryFragment : Fragment() {

    var secilenResimYolu :String?=null
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

                setupGridView(Dosyaİslemleri.klasordekiDosylariGetir(klasorPath.get(position)))


            }


        }

        view.tvIleriButton.setOnClickListener {
            activity!!.anaLayout.visibility=View.GONE
            activity!!.fragmentContainerLayout.visibility=View.VISIBLE
            var transaction=activity!!.supportFragmentManager.beginTransaction()

            EventBus.getDefault().postSticky(EventbusDataEvents.paylasilacakResmiGonder(secilenResimYolu,dosyaTuruResimMi))

            transaction.replace(R.id.fragmentContainerLayout,ShareNextFragment())
            transaction.addToBackStack("shareNextFragmentEklendi")
            transaction.commit()
        }


        return view
    }
    fun setupGridView(secilenKlasordekiDosyalar : ArrayList<String>) {
        var gridAdapter = ShareActivityGridViewAdapter(activity, R.layout.tek_sutun_grid_resim, secilenKlasordekiDosyalar)


        gridResimler.adapter = gridAdapter
        secilenResimYolu=secilenKlasordekiDosyalar.get(0)
        resimVeyaVideoGoster(secilenKlasordekiDosyalar.get(0))

        gridResimler.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                secilenResimYolu=secilenKlasordekiDosyalar.get(position)
                resimVeyaVideoGoster(secilenKlasordekiDosyalar.get(position))

            }

        })

    }

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

}
