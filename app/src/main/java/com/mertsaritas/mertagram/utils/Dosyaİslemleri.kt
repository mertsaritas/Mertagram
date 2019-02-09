package com.mertsaritas.mertagram.utils

import android.app.Fragment
import android.os.AsyncTask
import android.os.Environment
import com.iceteck.silicompressorr.SiliCompressor
import com.mertsaritas.mertagram.Profile.YukleniyorFragment
import com.mertsaritas.mertagram.Share.ShareNextFragment
import java.io.File
import java.util.*
import kotlin.Comparator

class Dosyaİslemleri {

    companion object {
        fun klasordekiDosylariGetir(klasorAdi:String):ArrayList<String>{
            var tumDosyalar=ArrayList<String>()

            var file=File(klasorAdi)

            var klasordekiTumDosyalar=file.listFiles()

            if (klasordekiTumDosyalar!=null){

                if(klasordekiTumDosyalar.size>1){
                    Arrays.sort(klasordekiTumDosyalar, object:Comparator<File>{
                        override fun compare(o1: File?, o2: File?): Int {

                            if(o1!!.lastModified()>o2!!.lastModified()) {
                                return -1

                            }else return 1


                        }

                    })



                }

                for(i in 0 ..klasordekiTumDosyalar.size-1){

                    if(klasordekiTumDosyalar[i].isFile){
                        var okunanDosyaYolu=klasordekiTumDosyalar[i].absolutePath
                        var dosyaTuru=okunanDosyaYolu.substring(okunanDosyaYolu.lastIndexOf("."))

                        if(dosyaTuru!=null && (dosyaTuru.equals(".jpg")|| dosyaTuru.equals(".jpeg")|| dosyaTuru.equals(".png")|| dosyaTuru.equals(".mp4"))){

                            tumDosyalar.add(okunanDosyaYolu)

                        }
                    }
                }


            }


            return tumDosyalar

        }

        fun compressResimDosya(fragment: android.support.v4.app.Fragment, secilenResimYolu: String?) {

            ResimCompressAsyncTask(fragment).execute(secilenResimYolu)


        }

        fun compressVideoDosya(fragment: android.support.v4.app.Fragment, secilenDosyaYolu: String) {

            VideoCompressAsyncTask(fragment).execute(secilenDosyaYolu)

        }

    }
    internal class VideoCompressAsyncTask(fragment: android.support.v4.app.Fragment): AsyncTask<String, String, String>(){

        var myFragment=fragment
        var compressFragment=YukleniyorFragment()

        override fun onPreExecute() {
            compressFragment.show(myFragment.activity!!.supportFragmentManager,"compressDialogBasladi")
            compressFragment.isCancelable=false
            super.onPreExecute()
        }

        override fun doInBackground(vararg params: String?): String? {

            var yeniOlusanDosyaninKlasoru=File(Environment.getExternalStorageDirectory().absolutePath+"/DCIM/Mertagram/compressed/")

            if(yeniOlusanDosyaninKlasoru.isDirectory || yeniOlusanDosyaninKlasoru.mkdirs()){
                var yeniDosyaninPath= SiliCompressor.with(myFragment.context).compressVideo(params[0],yeniOlusanDosyaninKlasoru.path)
                return yeniDosyaninPath
            }

            return null

        }

        override fun onPostExecute(yeniDosyaninPath: String?) {

            if(!yeniDosyaninPath.isNullOrEmpty()){

                compressFragment.dismiss()
                (myFragment as ShareNextFragment).uploadStorage(yeniDosyaninPath)


            }

            super.onPostExecute(yeniDosyaninPath)
        }




    }

    internal class ResimCompressAsyncTask(fragment: android.support.v4.app.Fragment): AsyncTask<String, String, String>(){

        var myFragment=fragment
        var compressFragment=YukleniyorFragment()

        override fun onPreExecute() {


            compressFragment.show(myFragment.activity!!.supportFragmentManager,"compressDialogBasladi")
            compressFragment.isCancelable=false
            super.onPreExecute()
        }

        override fun doInBackground(vararg params: String?): String {

            var yeniOlusanDosyaninKlasoru=File(Environment.getExternalStorageDirectory().absolutePath+"/DCIM/Mertagram/compressed/")
            //  var yeniDosyaYolu = SiliCompressor.with(myFragment.context).compress(params[0], yeniOlusanDosyaninKlasoru)
            var yeniDosyaYolu = params[0]!!

            //sıkıstırılarak olusturulmus yeni  dosyanın yolunu verir
            return yeniDosyaYolu
        }

        override fun onPostExecute(filePath: String?) {

            //Log.e("HATA","yENİ DOSYANIN PATHI : "+filePath)
            compressFragment.dismiss()
            //(myFragment as ShareNextFragment).uploadStorage(filePath)
            super.onPostExecute(filePath)
        }


    }

}