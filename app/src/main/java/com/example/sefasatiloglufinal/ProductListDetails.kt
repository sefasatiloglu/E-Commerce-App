package com.example.sefasatiloglufinal

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sefasatiloglufinal.configs.ApiClient
import com.example.sefasatiloglufinal.databinding.ActivityProductListDetailsBinding
import com.example.sefasatiloglufinal.models.Product
import com.example.sefasatiloglufinal.services.DummyService
import com.squareup.picasso.Picasso

class ProductListDetails : AppCompatActivity() {
    private lateinit var dummyService: DummyService
    private lateinit var binding: ActivityProductListDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductListDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dummyService = ApiClient.getClient().create(DummyService::class.java)

        // Intent'ten "product" anahtarı ile gecirilen Serializable nesnesi alınır
        val product: Product? = intent.getSerializableExtra("product") as? Product

        if (product != null) {
            productDetails(product)
        }

        //Sepete ekle butonuna tiklaninca
        binding.button.setOnClickListener {
            addCart(product!!.id)
            Toast.makeText(this, "Added to Cart", Toast.LENGTH_SHORT).show()
        }

        // Eger product null degilse, urunun puani gosterilir
        if (product != null) {
            binding.rBar.isEnabled = false //Puan vermesini engelle
            binding.rBar.rating = product.rating.toFloat()
        }

        binding.close.setOnClickListener {
            finish()
        }

    }

    private fun productDetails(product: Product) {

        binding.txtPriceDetails.text = "Price: ${product.price}$"
        binding.txtTitleDetails.text = product.title
        binding.txtDescription.text = product.description
        binding.txtBrand.text = "Brand: ${product.brand}"
        binding.txtStock.text = "Stock: ${product.stock}"
        binding.txtCategory.text = "Category: ${product.category}"

        // Picasso kutuphanesi ile urun resmini yukle
        Picasso.get()
            .load(product.images.firstOrNull())
            .into(binding.imgProductDetails)

    }

    // Sepete urun ekleyen fonksiyon
    private fun addCart(productId: Long) {
        //SharedPreferences uzerinden sepet verilerini al
        val prefs = getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        val cartSet: MutableSet<String> = getCartSet().toMutableSet()
        cartSet.add(productId.toString())
        prefs.edit().putStringSet("KEY_CART_LIST", cartSet).apply()
    }

    //SharedPreferences uzerinden mevcut sepet verilerini al
    private fun getCartSet(): Set<String> {
        val prefs = getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        return prefs.getStringSet("KEY_CART_LIST", HashSet()) ?: HashSet()
    }


}