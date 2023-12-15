package com.example.sefasatiloglufinal.adapter


import com.example.sefasatiloglufinal.databinding.CartItemBinding
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sefasatiloglufinal.models.Product
import com.squareup.picasso.Picasso

class MyCartAdapter(private val mContext: Context, private val productList: MutableList<Product>) :
    RecyclerView.Adapter<MyCartAdapter.CartNesneTutucu>() {

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

        if (product.quantity == 0) {
            product.quantity = 1
        }
        holder.binding.txtQuantity.text = "Quantity: ${product.quantity}"


        Picasso.get().load(product.thumbnail).into(holder.binding.imgCart)


        holder.binding.deleteItem.setOnClickListener {
            productList.removeAt(position)
            notifyDataSetChanged()
            removeCart(mContext, product.id)
        }

        holder.binding.decreaseItem.setOnClickListener {
            val currentQuantity = product.quantity
            if (currentQuantity > 0) {
                product.quantity = currentQuantity - 1
                notifyDataSetChanged()
            }
        }

        holder.binding.increaseItem.setOnClickListener {
            val currentQuantity = product.quantity
            product.quantity = currentQuantity + 1
            notifyDataSetChanged()
        }


    }

    //Urunu sepetten silmek
    private fun removeCart(context: Context, productId: Long) {
        val prefs = context.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        val cardSet: MutableSet<String> = getCartSet().toMutableSet()
        cardSet.remove(productId.toString())
        prefs.edit().putStringSet("KEY_CART_LIST", cardSet).apply()
    }
    //SharedPreferences'ten sepet setini alır.
    private fun getCartSet(): Set<String> {
        val prefs = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        return prefs.getStringSet("KEY_CART_LIST", HashSet()) ?: HashSet()
    }


}