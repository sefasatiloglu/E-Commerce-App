package com.example.sefasatiloglufinal.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sefasatiloglufinal.configs.ApiClient
import com.example.sefasatiloglufinal.databinding.FragmentUserBinding
import com.example.sefasatiloglufinal.models.Root
import com.example.sefasatiloglufinal.services.DummyService
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UserFragment : Fragment() {
    private lateinit var binding: FragmentUserBinding
    private lateinit var userList: ArrayList<Root>
    private lateinit var dummyService: DummyService
    private lateinit var user: Root

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dummyService = ApiClient.getClient().create(DummyService::class.java)


        dummyService.getUser().enqueue(object : Callback<Root> {
            override fun onResponse(call: Call<Root>, response: Response<Root>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        Picasso.get().load(responseBody.image).into(binding.ftimgUser)
                        binding.ftfirstName.text =
                            "${responseBody.firstName} ${responseBody.lastName}"
                        binding.ftemailUser.text = "${responseBody.email}"
                        binding.ftnumberUser.text = "${responseBody.phone}"
                        binding.ftlocationUser.text = "${responseBody.address.address}"
                        binding.ftuniUser.text = "${responseBody.university}"
                        binding.ftdepUser.text = "${responseBody.company.department}"

                        //User Update
                        dummyService.updateUser("2").enqueue(object : Callback<Root> {
                            override fun onResponse(call: Call<Root>, response: Response<Root>) {
                                if (response.isSuccessful) {
                                    val responseBodyUpdate = response.body()
                                    if (responseBodyUpdate != null) {
                                        Log.d("ResponseUpdate", "${response.code()}: ")
                                        Log.d("ResponseUpdate", "$responseBodyUpdate")
                                    } else {
                                        Log.d("Check", "Null")
                                    }
                                }
                            }

                            override fun onFailure(call: Call<Root>, t: Throwable) {
                                Log.d("Service Error", "$t")
                            }
                        })
                    }
                } else {
                    Log.d("Check", "Null")
                }
            }

            override fun onFailure(call: Call<Root>, t: Throwable) {
                Log.d("Service Error", "$t")
            }
        })


    }
}

