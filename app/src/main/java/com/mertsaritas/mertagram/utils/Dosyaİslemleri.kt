package com.mertsaritas.mertagram.utils

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

                        if(dosyaTuru.equals(".jpg")|| dosyaTuru.equals(".jpeg")|| dosyaTuru.equals(".png")|| dosyaTuru.equals(".mp4")){

                            tumDosyalar.add(okunanDosyaYolu)

                        }
                    }
                }


            }


            return tumDosyalar

        }
    }

}