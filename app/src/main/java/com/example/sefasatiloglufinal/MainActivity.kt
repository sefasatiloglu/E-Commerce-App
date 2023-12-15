package com.example.sefasatiloglufinal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.sefasatiloglufinal.configs.ApiClient
import com.example.sefasatiloglufinal.databinding.ActivityMainBinding
import com.example.sefasatiloglufinal.models.JWTData
import com.example.sefasatiloglufinal.models.JWTUser
import com.example.sefasatiloglufinal.services.DummyService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var dummyService: DummyService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Retrofit servisini olustur
        dummyService = ApiClient.getClient().create(DummyService::class.java)

        binding.btnLogin.setOnClickListener {
            //Kullanici adi ve sifreyi EditText'lerden al
            val username = binding.username.text.toString().trim()
            val password = binding.password.text.toString().trim()

            //JTWUser sinifini kullanarak kullanici bilgilerini olustur
            val jwtUser = JWTUser(username, password)

            //Kullanici adi ve sifre kontrolu
            if (jwtUser.username == "kminchelle" && jwtUser.password == "0lelplR") {
                //Retrofit ile sunucuya giris istegi yap
                dummyService.login(jwtUser).enqueue(object : Callback<JWTData> {
                    override fun onResponse(call: Call<JWTData>, response: Response<JWTData>) {
                        //Sunucudan gelen cevap basarili ise
                        val user = response.body()
                        Log.d("userstatus", response.code().toString())
                        if (user != null) {
                            //Giris basarili ve kullaniciya Welcome mesaji
                            Toast.makeText(applicationContext, "Welcome Back", Toast.LENGTH_SHORT)
                                .show()
                            val intent = Intent(applicationContext, ProductsList::class.java)
                            startActivity(intent)
                        }
                    }
                    //Sunucu tarifinda bir hata olursa
                    override fun onFailure(call: Call<JWTData>, t: Throwable) {
                        Log.e("Login Fail", t.toString())
                    }
                })
            } else {
                //Kullanici adi ve sifre hataliysa kullaniciya bilgi verme
                Toast.makeText(applicationContext, "Username or Password Fail", Toast.LENGTH_SHORT)
                    .show()
            }

        }


    }
}