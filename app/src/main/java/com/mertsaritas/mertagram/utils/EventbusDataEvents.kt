package com.mertsaritas.mertagram.utils

import com.mertsaritas.mertagram.Models.Users

class EventbusDataEvents {
    internal class KayitBilgileriniGonder(var telNo:String?, var email:String?,var verificationID:String?, var code:String?,var emailkayit:Boolean)

    internal class KullaniciBilgileriniGönder(var kullanici:Users?)

}
