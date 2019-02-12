package com.mertsaritas.mertagram.Models

class Posts {

    var user_id:String?=null
    var post_id:String?=null
    var yukleme_tarihi:String?=null
    var aciklama:String?=null
    var file_uri:String?=null

    constructor()
    constructor(user_id: String?, post_id: String?, yukleme_tarihi: String?, aciklama: String?, file_uri: String?) {
        this.user_id = user_id
        this.post_id = post_id
        this.yukleme_tarihi = yukleme_tarihi
        this.aciklama = aciklama
        this.file_uri = file_uri
    }

    override fun toString(): String {
        return "Posts(user_id=$user_id, post_id=$post_id, yukleme_tarihi=$yukleme_tarihi, aciklama=$aciklama, photo_uri=$file_uri)"
    }

}