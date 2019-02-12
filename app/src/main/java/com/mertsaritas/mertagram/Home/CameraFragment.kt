package com.mertsaritas.mertagram.Home


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mertsaritas.mertagram.R
import com.mertsaritas.mertagram.utils.EventbusDataEvents
import com.otaliastudios.cameraview.CameraView
import kotlinx.android.synthetic.main.fragment_camera.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class CameraFragment : Fragment() {
    var myCamera:CameraView?=null
    var kameraIzniVerildiMi = false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        var view=inflater.inflate(R.layout.fragment_camera,container,false)

        myCamera=view!!.camera_view

        return view


    }
    override fun onResume() {
        super.onResume()
        if (kameraIzniVerildiMi==true)
        myCamera!!.start()
    }

    override fun onPause() {
        super.onPause()
        myCamera!!.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (myCamera!=null)
            myCamera!!.destroy()
    }

    //////////////////////////////////EventBus/////////////////////////////////////
    @Subscribe(sticky = true)
    internal fun onKameraIzinEvent(izinDurumu: EventbusDataEvents.KameraIzinBilgisiGonder) {
        kameraIzniVerildiMi=izinDurumu.kameraIzniVerildiMi!!


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