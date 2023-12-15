package com.example.sefasatiloglufinal.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sefasatiloglufinal.ProductListDetails
import com.example.sefasatiloglufinal.R
import com.example.sefasatiloglufinal.databinding.ProductsListTasarimBinding
import com.example.sefasatiloglufinal.models.Product
import com.squareup.picasso.Picasso
import java.io.Serializable
import java.util.Locale

class ProductsAdapter(private val mContext: Context, private var productsListesi: List<Product>) :
    RecyclerView.Adapter<ProductsAdapter.ProductsNesneTutucusu>() {
    private var originalProductsList: List<Product> = productsListesi.toList()

    //RecyclerView icindeki her bir ogenin gorunumunu temsil eder
    inner class ProductsNesneTutucusu(val binding: ProductsListTasarimBinding) :
        RecyclerView.ViewHolder(binding.root) {

        var imgProduct: ImageView = binding.imgProduct
        var txtTitle: TextView = binding.txtTitle
        var txtPrice: TextView = binding.txtPrice
        var favBtn: ImageView = binding.favorite

    }

    //Her bir ogenin gorunumunu olusturur.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsNesneTutucusu {
        val bindingItem =
            ProductsListTasarimBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return ProductsNesneTutucusu(bindingItem)
    }

    //Gonderilen ogelerin sayisini dondurur.
    override fun getItemCount(): Int {
        return productsListesi.size
    }
    //ViewHolder'i(holder) ve veri kümesindeki öğenin konumunu alır.
    override fun onBindViewHolder(holder: ProductsNesneTutucusu, position: Int) {
        //adaptorde bulunan ogelerin konumunu belirtir.
        val productVerileri = productsListesi[position]
        //Urunun resmini yukler (firstOrNull urunun birden fazla resmi olabileceginden resim listesinin ilk elemanini alir)
        Picasso.get()
            .load(productVerileri.images.firstOrNull())
            .into(holder.imgProduct)
        holder.txtTitle.text = productVerileri.title
        holder.txtPrice.text = "Price: ${productVerileri.price}$"

        //Urunlere tiklandiginda
        holder.itemView.setOnClickListener {
            val intent = Intent(mContext, ProductListDetails::class.java)
            intent.putExtra("product", productVerileri as Serializable)
            mContext.startActivity(intent)
        }
        //Eger urun favorilere eklenmisse kirmizi kalp ikonu goruntulenir, aksi takdirde beyaz kalp ikonu goruntulenir
        holder.favBtn.setImageResource(
            if (isFav(
                    mContext,
                    productVerileri.id
                )
            ) R.drawable.loveredicon else R.drawable.loveicon
        )

        //Update
        holder.favBtn.setOnClickListener {
            val favStatus = isFav(mContext, productVerileri.id)
            if (favStatus) {
                removeFavorites(mContext, productVerileri.id)

            } else {
                addFavorites(mContext, productVerileri.id)
            }
            holder.favBtn.setImageResource(
                if (isFav(
                        mContext,
                        productVerileri.id
                    )
                ) R.drawable.loveredicon else R.drawable.loveicon
            )

        }
    }

    fun filterProducts(charString: String) {
        if (charString.isNotEmpty()) {
            val filter = productsListesi.filter {
                it.title.toLowerCase(Locale.ROOT).contains(charString.toLowerCase(Locale.ROOT))
            }
            productsListesi = filter
        } else {
            //Eger search bos ise verileri tekrardan goster
            productsListesi = originalProductsList.toList()
        }
        notifyDataSetChanged()
    }

    // Yeni urun listesini adapter'a set etmek için
    fun updateProductsList(newList: List<Product>) {
        productsListesi = newList
        notifyDataSetChanged()
    }
    //Favori urunleri set etmek icin
    private fun addFavorites(context: Context, productId: Long) {
        val prefs = context.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        val favoriteSet: MutableSet<String> = getFavoriteSet(context).toMutableSet()
        favoriteSet.add(productId.toString())
        prefs.edit().putStringSet("KEY_FAVORITE_LIST", favoriteSet).apply()
    }
    //Favori urunler'i remove etmek
    private fun removeFavorites(context: Context, productId: Long) {
        val prefs = context.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        val favoriteSet: MutableSet<String> = getFavoriteSet(context).toMutableSet()
        favoriteSet.remove(productId.toString())
        prefs.edit().putStringSet("KEY_FAVORITE_LIST", favoriteSet).apply()
    }

    // ID'lerini içeren küme(Set) bilgisini alır.
    private fun getFavoriteSet(context: Context): Set<String> {
        val prefs = context.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        return prefs.getStringSet("KEY_FAVORITE_LIST", HashSet()) ?: HashSet()
    }

    //Urunun favorilere eklenip eklenmediğini kontrol eder.(True-False) donderir.
    private fun isFav(context: Context, productId: Long): Boolean {
        val favoriteSet: Set<String> = getFavoriteSet(context)
        return favoriteSet.contains(productId.toString())
    }


}