package com.example.neeksiknefazla.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.neeksiknefazla.dataclass.Product
import com.example.neeksiknefazla.R
import kotlinx.android.synthetic.main.urun_tasarim.view.*
import java.util.ArrayList

class AddProductAdapter (val productPosts: ArrayList<Product>) :
    RecyclerView.Adapter<AddProductAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var prdName: TextView? = null
        var no_sale: TextView? = null
        var donate: TextView? = null
        var price: TextView? = null
        var imageViewProductImage : ImageView? = null

        init {
            prdName = itemView.findViewById(R.id.textView38)
            price = itemView.findViewById(R.id.textView39)
            donate = itemView.findViewById(R.id.textView45)
            no_sale = itemView.findViewById(R.id.textView40)
            imageViewProductImage = itemView.findViewById(R.id.imageViewYemekResim)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.urun_tasarim, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.prdName?.setText(productPosts[position].product_name)
        holder.price?.setText(productPosts[position].price + " TL")
        holder.donate?.setText(productPosts[position].donation + " packages")
        holder.no_sale?.setText(productPosts[position].on_sale +" packages")
        holder.imageViewProductImage?.load(productPosts[position].image_url)

    }

    override fun getItemCount() : Int = productPosts.size

}



