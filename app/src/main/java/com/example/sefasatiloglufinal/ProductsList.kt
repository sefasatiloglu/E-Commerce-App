package com.example.sefasatiloglufinal

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sefasatiloglufinal.adapter.CartAdapter
import com.example.sefasatiloglufinal.configs.ApiClient
import com.example.sefasatiloglufinal.databinding.ActivityProductsListBinding
import com.example.sefasatiloglufinal.fragments.FavoriteFragment
import com.example.sefasatiloglufinal.fragments.HomeFragment
import com.example.sefasatiloglufinal.fragments.UserFragment
import com.example.sefasatiloglufinal.models.CartResponse
import com.example.sefasatiloglufinal.models.Product
import com.example.sefasatiloglufinal.models.Products
import com.example.sefasatiloglufinal.models.Root
import com.example.sefasatiloglufinal.services.DummyService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductsList : AppCompatActivity() {
    private lateinit var binding: ActivityProductsListBinding
    private lateinit var dummyService: DummyService
    private lateinit var adapter: CartAdapter
    private var selectorFragment: Fragment? = null
    private lateinit var userProfile: View
    private lateinit var textUserProfile: TextView
    private lateinit var numberProfile: TextView
    private lateinit var emailUser: TextView
    private lateinit var locationUser: TextView
    private lateinit var uniUser: TextView
    private lateinit var depUser: TextView
    private lateinit var imgUser: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dummyService = ApiClient.getClient().create(DummyService::class.java)

        //Eger bir onceki durum (savedInstanceState) null ise, baslangic fragment'i yerlestir.
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container,
                HomeFragment()
            ).commit()
        }

        // Navigation Drawer'da bulunan kullanici profili view'lerine erisim
        userProfile = binding.navView.getHeaderView(0)
        textUserProfile = userProfile.findViewById(R.id.firstLastName)
        numberProfile = userProfile.findViewById(R.id.numberUser)
        imgUser = userProfile.findViewById(R.id.imgUser)
        emailUser = userProfile.findViewById(R.id.emailUser)
        locationUser = userProfile.findViewById(R.id.locationUser)
        uniUser = userProfile.findViewById(R.id.uniUser)
        depUser = userProfile.findViewById(R.id.depUser)

        //Navigation Drawer'i (cekmece menusu) acma ve kapama islevselligi ekler.
        val toggle = ActionBarDrawerToggle(
            this, binding.drawerLayout,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)

        //Navigation Drawer'in acilip kapatılma ikonunu (hamburger veya geri oku) otomatik olarak degistirir.
        toggle.syncState()

        // Bottom Navigation'daki seceneklerin dinlenmesi
        binding.bottom.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navHome -> if (selectorFragment !is HomeFragment) {
                    selectorFragment = HomeFragment()
                }

                R.id.Favori -> if (selectorFragment !is FavoriteFragment) {
                    selectorFragment = FavoriteFragment()
                }

                R.id.navItem -> {
                    cartDialog()
                }

                R.id.Profile -> if (selectorFragment !is FavoriteFragment) {
                    selectorFragment = UserFragment()

                }


            }
            //Secilen fragment varsa, fragment'i yerleştir
            if (selectorFragment != null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, selectorFragment!!).commit()
            }
            true
        }

        dummyService.getUser().enqueue(object : Callback<Root> {
            override fun onResponse(call: Call<Root>, response: Response<Root>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        Glide.with(this@ProductsList)
                            .load(responseBody.image)
                            .into(imgUser)
                        textUserProfile.text = "${responseBody.firstName} ${responseBody.lastName}"
                        numberProfile.text = responseBody.phone
                        emailUser.text = "${responseBody.email}"
                        locationUser.text = "${responseBody.address.address}"
                        uniUser.text = "${responseBody.university}"
                        depUser.text = "${responseBody.company.department}"


                    }
                } else {
                    Log.d("Error", "Baglanamadim ")
                }
            }

            override fun onFailure(call: Call<Root>, t: Throwable) {
                Log.d("Service Error", "$t")
            }
        })


    }

    private fun cartDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.cart_tasarim)

        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setGravity(Gravity.CENTER)
        dialog.show()

        val rv = dialog.findViewById<RecyclerView>(R.id.rvCart)
        val totalPrice = dialog.findViewById<TextView>(R.id.totalPrice)
        val closeBtn = dialog.findViewById<ImageView>(R.id.closeBtn)
        val buyButton = dialog.findViewById<Button>(R.id.buyBtn)

        closeBtn.setOnClickListener {
            dialog.dismiss()
        }

        rv.layoutManager = LinearLayoutManager(this)
        rv.setHasFixedSize(true)



        dummyService = ApiClient.getClient().create(DummyService::class.java)


        dummyService.getCart().enqueue(object : Callback<CartResponse> {
            override fun onResponse(call: Call<CartResponse>, response: Response<CartResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { _ ->
                        val cartResponse = response.body()
                        val allProducts = mutableListOf<Product>()

                        for (cart in cartResponse!!.carts) {
                            allProducts.addAll(cart.products)
                            totalPrice.text = "Total: ${cart.total}$"

                        }

                        adapter = CartAdapter(this@ProductsList, allProducts)
                        rv.adapter = adapter
                        adapter.notifyDataSetChanged()

                        buyButton.setOnClickListener {
                            dummyService.addProduct().enqueue(object : Callback<Products> {
                                override fun onResponse(
                                    call: Call<Products>,
                                    response: Response<Products>
                                ) {
                                    Log.d("Response Cart Add", "${response.code()}")
                                }

                                override fun onFailure(call: Call<Products>, t: Throwable) {
                                    Log.d("Service Error", "$t")
                                }
                            })
                            Toast.makeText(
                                this@ProductsList,
                                "Your order has been processed",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }
                } else {
                    Log.d("Error", "Null")
                }
            }

            override fun onFailure(call: Call<CartResponse>, t: Throwable) {
                Log.d("Service Error", "$t")
            }
        })

    }

}