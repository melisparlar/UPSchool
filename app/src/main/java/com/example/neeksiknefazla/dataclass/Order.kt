package com.example.neeksiknefazla.dataclass

import java.io.Serializable

data class Order (
        var orderId:String = "",
        var client_email:String = "",
        var product_name:String = "",
        var company_name:String = "",
        var number:String = "",
        var price:String = "",
        var type:String = "",
        var image_url:String = ""
        ) : Serializable {
}