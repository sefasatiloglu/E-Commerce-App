package com.example.sefasatiloglufinal.fragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sefasatiloglufinal.R
import com.example.sefasatiloglufinal.adapter.CategoryAdapter
import com.example.sefasatiloglufinal.adapter.MyCartAdapter
import com.example.sefasatiloglufinal.adapter.ProductsAdapter
import com.example.sefasatiloglufinal.configs.ApiClient
import com.example.sefasatiloglufinal.databinding.FragmentHomeBinding
import com.example.sefasatiloglufinal.models.Category
import com.example.sefasatiloglufinal.models.Product
import com.example.sefasatiloglufinal.models.Products
import com.example.sefasatiloglufinal.services.DummyService
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment(), CategoryAdapter.CategoryClick {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var dummyService: DummyService
    private lateinit var productsListesi: ArrayList<Product>
    private lateinit var adapter: ProductsAdapter
    private lateinit var adapterCategory: CategoryAdapter
    private lateinit var categoryList: ArrayList<Category>

    private lateinit var cartAdapter: MyCartAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dummyService = ApiClient.getClient().create(DummyService::class.java)

        binding.rv.setHasFixedSize(true)
        binding.rv.layoutManager = GridLayoutManager(requireContext(), 2)

        dummyService.allProducts(20).enqueue(object : Callback<Products> {
            override fun onResponse(call: Call<Products>, response: Response<Products>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        Log.d("products", it.products.toString())
                        productsListesi = ArrayList(it.products)
                        adapter = ProductsAdapter(requireContext(), productsListesi)
                        binding.rv.adapter = adapter
                        adapter.notifyDataSetChanged()
                    }
                } else {
                    Log.d(
                        "ProductList",
                        "Failed to retrieve products. Response code: ${response.code()}"
                    )
                }
            }

            override fun onFailure(call: Call<Products>, t: Throwable) {
                Log.e("Service Error", t.toString())
            }
        })
        //Metin degisikliklerini dinleyen bir TextWatcher ekledim.
        binding.editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty()) {
                    dummyService.filterProducts(s.toString()).enqueue(object : Callback<Products> {
                        override fun onResponse(
                            call: Call<Products>,
                            response: Response<Products>
                        ) {
                            if (response.isSuccessful) {
                                response.body()?.let {
                                    productsListesi = ArrayList(it.products)
                                    adapter.updateProductsList(productsListesi)
                                }
                            } else {
                                Log.e("response code", "onResponse: ${response.code()}")
                            }
                        }

                        override fun onFailure(call: Call<Products>, t: Throwable) {
                            Log.e("Service Error", t.toString())
                        }
                    })
                } else {
                    dummyService.allProducts(20).enqueue(object : Callback<Products> {
                        override fun onResponse(
                            call: Call<Products>,
                            response: Response<Products>
                        ) {
                            if (response.isSuccessful) {
                                response.body()?.let {
                                    productsListesi = ArrayList(it.products)
                                    adapter.updateProductsList(productsListesi)
                                }
                            } else {
                                Log.e("response code", "onResponse: ${response.code()}")
                            }
                        }

                        override fun onFailure(call: Call<Products>, t: Throwable) {
                            Log.e("Service Error", t.toString())
                        }
                    })
                }


            }
        })
        //Floating Action Button'a tiklaninca kategorilerin bulunduğu bir layout gorunur hale getirilir.
        val btn = activity?.findViewById<FloatingActionButton>(R.id.fab)
        btn?.setOnClickListener {
            binding.categoryLinearLayout.visibility = View.VISIBLE
        }
        getCategory()
        //Alisveris sepeteni goruntuleme
        binding.filter.setOnClickListener {
            getMyCard()
        }
        //sepet boyutunu ayarlama
        setSize()

    }

    private fun getCategory() {
        categoryList = ArrayList()
        binding.rvCategory.layoutManager =
            GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)
        binding.rvCategory.setHasFixedSize(true)

        adapterCategory = CategoryAdapter(requireContext(), categoryList, this)
        binding.rvCategory.adapter = adapterCategory
        categoryList.add(Category("tops", R.drawable.staricon))
        categoryList.add(Category("smartphones", R.drawable.telephoneicon))
        categoryList.add(Category("laptops", R.drawable.laptopicon))
        categoryList.add(Category("motorcycle", R.drawable.motorbikeicon))
        categoryList.add(Category("home-decoration", R.drawable.decorhomeicon))
        categoryList.add(Category("fragrances", R.drawable.kadinparfum))
        categoryList.add(Category("furniture", R.drawable.perfumeicon))
        categoryList.add(Category("skincare", R.drawable.serumicon))
        categoryList.add(Category("groceries", R.drawable.groceriesicon))
        categoryList.add(Category("womens-dresses", R.drawable.womenicon))
        categoryList.add(Category("womens-shoes", R.drawable.womenshoesicon))
        categoryList.add(Category("womens-watches", R.drawable.watchwomenicon))
        categoryList.add(Category("womens-bags", R.drawable.bagwomenicon))
        categoryList.add(Category("womens-jewellery", R.drawable.diamondjewelryicon))
        categoryList.add(Category("mens-shirts", R.drawable.tshirticon))
        categoryList.add(Category("mens-shoes", R.drawable.menshoesicon))
        categoryList.add(Category("mens-watches", R.drawable.mentimeicon))
        categoryList.add(Category("automotive", R.drawable.automobileicon))
        categoryList.add(Category("lighting", R.drawable.lightningicon))


    }

    //Kategorilere tiklandiginda filtreleme islemi
    override fun clickCategory(category: Category) {
        dummyService.filterCategory(category.category_name).enqueue(object : Callback<Products> {
            override fun onResponse(call: Call<Products>, response: Response<Products>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        productsListesi = ArrayList(it.products)
                        adapter.updateProductsList(productsListesi)
                    }
                } else {
                    Log.e("response code", "onResponse: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Products>, t: Throwable) {
                Log.e("Service Error", t.toString())
            }
        })
    }

    //Sepetteki urun sayisini gosterme
    private fun setSize() {
        val cartIdList = getCartSet().size
        binding.cartSize.text = cartIdList.toString()
    }

    //Alisveris sepetini gosteren bir dialog pencerisi olusturan fonksiyon
    private fun getMyCard() {
        val dialog = Dialog(requireContext())
        //bir pencere ozelligi talep etmek icin kullanılır.
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        //Dialog icerigini belirler ve bu icerik cart_tasarim.xml adlı bir duzen dosyasına gore belirlenir.
        dialog.setContentView(R.layout.cart_tasarim)

        //Pencerenin gorunum ozelligi
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        //Yercekimi ayarlar pencere ekranin ortasina yerlestirilir
        dialog.window?.setGravity(Gravity.CENTER)
        dialog.show()

        val rv = dialog.findViewById<RecyclerView>(R.id.rvCart)
        val close = dialog.findViewById<ImageView>(R.id.closeBtn)

        val totalPrice = dialog.findViewById<TextView>(R.id.totalPrice)

        //Pencereyi kapatmak icin bir dinleyici(listener eklenir)
        close.setOnClickListener {
            dialog.dismiss() //tiklandiginda pencere "dismiss" metodu ile kapanir.
        }
        totalPrice.visibility = View.GONE

        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.setHasFixedSize(true)



        dummyService = ApiClient.getClient().create(DummyService::class.java)


        dummyService.allProducts(20).enqueue(object : Callback<Products> {
            override fun onResponse(call: Call<Products>, response: Response<Products>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        val allProducts: List<Product> = it.products // Tüm ürünlerin listesi

                        val cartIdList = getCartSet()
                        val cartProducts =
                            allProducts.filter { cartIdList.contains(it.id.toString()) }

                        // RecyclerView'e favori ürünleri bağla
                        cartAdapter = MyCartAdapter(requireContext(), cartProducts.toMutableList())
                        rv.adapter = cartAdapter
                        cartAdapter.notifyDataSetChanged()


                    }
                }
            }

            override fun onFailure(call: Call<Products>, t: Throwable) {
                Log.d("TAG", "onFailure: $t ")
            }
        })

    }

    private fun getCartSet(): Set<String> {
        val prefs = requireContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        return prefs.getStringSet("KEY_CART_LIST", HashSet()) ?: HashSet()
    }


}