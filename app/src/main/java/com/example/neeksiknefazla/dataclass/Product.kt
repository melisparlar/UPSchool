package com.example.neeksiknefazla.dataclass
import java.io.Serializable

data class Product (
        var productId:String = "",
        var company_name:String = "",
        var donation:String = "",
        var on_sale:String = "",
        var price:String = "",
        var product_name:String = "",
        var company_email: String = "",
        var image_url:String = ""
        ) : Serializable {

}




