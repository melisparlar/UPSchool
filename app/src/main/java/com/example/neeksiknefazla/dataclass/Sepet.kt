package com.example.neeksiknefazla.dataclass

import java.io.Serializable

data class Sepet (
        var sepetId:String = "",
        var client_email:String = "",
        var total_amount:String = "" ,
        var isPayed:Boolean = false ) : Serializable {
}