package com.example.strayfriends.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.strayfriends.R
import com.example.strayfriends.listener.ProductOnClickListener
import com.example.strayfriends.model.Product
import org.w3c.dom.Text

class ProductsAdapter(private var clickListener : ProductOnClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var products : MutableList<Product> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ProductsViewHolder (
                LayoutInflater.from(parent.context).inflate(R.layout.card_product, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is ProductsViewHolder ->{
                holder.bind(products[position], clickListener)
            }
        }
    }

    override fun getItemCount(): Int {
        return products.size
    }

    fun submitList (productsList : MutableList<Product>) {
        products.addAll(productsList)
        notifyDataSetChanged()
    }

    fun submitProduct(mProduct : Product){
        products.add(mProduct)
        notifyDataSetChanged()
    }

    fun deleteList(){
        products.clear()
        notifyDataSetChanged()
    }

    class ProductsViewHolder constructor(itemView : View) : RecyclerView.ViewHolder(itemView){
        private val prodName : TextView = itemView.findViewById(R.id.product_name_textView)
        private val prodPrice : TextView = itemView.findViewById(R.id.product_price_textView)
        private val prodType : TextView = itemView.findViewById(R.id.product_type_textView)

        fun bind(product : Product, action : ProductOnClickListener){
            val priceModel = product.price
            if(priceModel == "" && priceModel <= "0"){
                prodPrice.text = "Gr??tis"
            } else {
                prodPrice.text = "R$$priceModel"
            }
            prodName.text = product.name
            prodType.text = product.type

            itemView.setOnClickListener() {
                action.onItemClick(product)
            }
        }
    }

}