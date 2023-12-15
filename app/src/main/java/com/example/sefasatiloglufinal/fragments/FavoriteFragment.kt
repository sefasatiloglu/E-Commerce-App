package com.example.sefasatiloglufinal.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.sefasatiloglufinal.adapter.ProductsAdapter
import com.example.sefasatiloglufinal.configs.ApiClient
import com.example.sefasatiloglufinal.databinding.FragmentFavoriteBinding
import com.example.sefasatiloglufinal.models.Product
import com.example.sefasatiloglufinal.models.Products
import com.example.sefasatiloglufinal.services.DummyService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavoriteFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var dummyService: DummyService
    private lateinit var adapter: ProductsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    //Ara yuz cagrildiginda
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        dummyService = ApiClient.getClient().create(DummyService::class.java)
        binding.rv.setHasFixedSize(true)
        binding.rv.layoutManager = GridLayoutManager(requireContext(), 2)
        getBasket()


    }

    private fun getFavoriteSet(context: Context): Set<String> {
        val prefs = context.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        return prefs.getStringSet("KEY_FAVORITE_LIST", HashSet()) ?: HashSet()
    }

    private fun getBasket() {
        dummyService.allProducts(20).enqueue(object : Callback<Products> {
            override fun onResponse(call: Call<Products>, response: Response<Products>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        val allProducts: List<Product> = it.products // Tüm urunlerin listesi

                        // Favori urunlerin ID'lerini al
                        val favoriteIdList = getFavoriteSet(requireContext())
                        // Favori urunleri tespit et
                        val favoriteProducts =
                            allProducts.filter { favoriteIdList.contains(it.id.toString()) }

                        // RecyclerView'e favori urunleri bagla
                        adapter = ProductsAdapter(requireContext(), favoriteProducts)
                        binding.rv.adapter = adapter
                        adapter.notifyDataSetChanged()


                    }
                }
            }

            override fun onFailure(call: Call<Products>, t: Throwable) {
                Log.d("TAG", "onFailure: $t ")
            }
        })
    }
}