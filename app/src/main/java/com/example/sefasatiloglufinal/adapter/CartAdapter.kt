package com.example.sefasatiloglufinal.adapter

import com.example.sefasatiloglufinal.databinding.CartItemBinding
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sefasatiloglufinal.models.Product
import com.squareup.picasso.Picasso

class CartAdapter(private val mContext: Context, private val productList: MutableList<Product>) :
    RecyclerView.Adapter<CartAdapter.CartNesneTutucu>() {

    inner class CartNesneTutucu(val binding: CartItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartNesneTutucu {
        val bindingItem = CartItemBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return CartNesneTutucu(bindingItem)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: CartNesneTutucu, position: Int) {
        val product = productList[position]


        holder.binding.txtCartPrice.text = "Price: ${product.price}$"
        holder.binding.txtCartTitle.text = product.title
        holder.binding.txtQuantity.text = "Quantity: ${product.quantity}"

        Picasso.get().load(product.thumbnail).into(holder.binding.imgCart)

        //Urunu silme islemi
        holder.binding.deleteItem.setOnClickListener {
            productList.removeAt(position)
            notifyDataSetChanged() //listeyi guncelleme
        }
        //Urunu azaltma islemi
        holder.binding.decreaseItem.setOnClickListener {
            val currentQuantity = product.quantity
            if (currentQuantity > 0) {
                product.quantity = currentQuantity - 1
                notifyDataSetChanged()
            }
        }
        //Urunu artirma islemi
        holder.binding.increaseItem.setOnClickListener {
            val currentQuantity = product.quantity
            product.quantity = currentQuantity + 1
            notifyDataSetChanged()
        }


    }
}