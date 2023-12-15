package com.example.sefasatiloglufinal.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sefasatiloglufinal.adapter.CategoryAdapter
import com.example.sefasatiloglufinal.configs.ApiClient
import com.example.sefasatiloglufinal.databinding.FragmentCategoryBinding
import com.example.sefasatiloglufinal.models.Category
import com.example.sefasatiloglufinal.services.DummyService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CategoryFragment : Fragment(), CategoryAdapter.CategoryClick {
    private lateinit var binding: FragmentCategoryBinding
    private lateinit var adapter: CategoryAdapter
    private lateinit var categoryList: ArrayList<Category>
    private lateinit var dummyService: DummyService
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoryBinding.inflate(inflater, container, false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dummyService = ApiClient.getClient().create(DummyService::class.java)
        binding.rv.setHasFixedSize(true)
        binding.rv.layoutManager = LinearLayoutManager(requireContext())

        dummyService.getCategory().enqueue(object : Callback<List<Category>> {
            override fun onResponse(
                call: Call<List<Category>>,
                response: Response<List<Category>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        Log.d("Response", response.body().toString())
                        adapter =
                            CategoryAdapter(requireContext(), categoryList, this@CategoryFragment)
                        binding.rv.adapter = adapter
                        adapter.notifyDataSetChanged()


                    }
                } else {
                    Log.d("response", "${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Category>>, t: Throwable) {
                Log.d("TAG", "onFailure: $t ")
            }
        })
    }

    override fun clickCategory(category: Category) {
    }

}