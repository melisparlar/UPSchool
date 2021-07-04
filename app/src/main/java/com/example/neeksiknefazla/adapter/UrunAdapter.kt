package com.example.neeksiknefazla.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.neeksiknefazla.R
import com.example.neeksiknefazla.dataclass.Product
import java.util.ArrayList

class UrunAdapter(private val invendusList: ArrayList<Product>,
                  private val yemekResimList: ArrayList<String>,
                  private val icerikClickListener: IcerikClickListener):
    RecyclerView.Adapter<UrunAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var prdName : TextView? = null
        var prdPrice : TextView? = null
        var don : TextView? = null
        var imageViewProductImage : ImageView? = null

        init {
            prdName = itemView.findViewById(R.id.textView49)
            prdPrice = itemView.findViewById(R.id.textView51)
            don = itemView.findViewById(R.id.textView50)
            imageViewProductImage = itemView.findViewById(R.id.imageViewProduct)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.client_product_tasarim, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.prdName?.setText(invendusList[position].product_name)
        viewHolder.prdPrice?.setText(invendusList[position].price + " TL")
        viewHolder.don?.setText("Askıda: " + invendusList[position].donation + " packages")
        viewHolder.imageViewProductImage?.load(yemekResimList[position])

        viewHolder.imageViewProductImage?.setOnClickListener {
            icerikClickListener.onIcerikClickListener(invendusList[position])
        }

    }

    override fun getItemCount() = invendusList.size

    interface IcerikClickListener {
        fun onIcerikClickListener(data: Product)
    }

}