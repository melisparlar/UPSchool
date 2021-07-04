package com.example.neeksiknefazla.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.neeksiknefazla.R
import com.example.neeksiknefazla.dataclass.Order
import java.util.ArrayList

class SepetAdapter(private val ordersList: ArrayList<Order>):
        RecyclerView.Adapter<SepetAdapter.ViewHolder>()  {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var prdName : TextView? = null
        var prdPrice : TextView? = null
        var prdNumber : TextView? = null
        var companyName : TextView? = null
        var type : TextView? = null
        var imageViewProductImage : ImageView? = null

        init {
            prdName = itemView.findViewById(R.id.textViewInvendus2)
            prdPrice = itemView.findViewById(R.id.textViewFiyat)
            prdNumber = itemView.findViewById(R.id.textViewSiparisAdet)
            companyName = itemView.findViewById(R.id.textView8)
            imageViewProductImage = itemView.findViewById(R.id.imageViewYemekResim)
            type = itemView.findViewById(R.id.textView48)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.sepet_tasarim, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.prdName?.setText(ordersList[position].product_name)
        viewHolder.prdNumber?.setText(ordersList[position].number + " package")
        viewHolder.prdPrice?.setText((ordersList[position]).price + " TL")
        viewHolder.companyName?.setText(ordersList[position].company_name)
        viewHolder.type?.setText(ordersList[position].type)
        viewHolder.imageViewProductImage?.load(ordersList[position].image_url)
    }

    override fun getItemCount() = ordersList.size

}