package com.example.sefasatiloglufinal.services

import com.example.sefasatiloglufinal.models.CartResponse
import com.example.sefasatiloglufinal.models.Category
import com.example.sefasatiloglufinal.models.JWTData
import com.example.sefasatiloglufinal.models.JWTUser
import com.example.sefasatiloglufinal.models.Products
import com.example.sefasatiloglufinal.models.Root
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface DummyService {

    //Bu methodun HTTP POST istegi yapacagi endpoint'i belirtir.
    //fun login bu metot JWTUser turunden parametre alir. Bu asenkron bir cagrinin sonucunu temsil eden bir Retrofit "CALL" objesidir.
    //@Body anotasyonu bu istegin govdesinde(body)hangi verinin bulunacagini belirtir.
    @POST("auth/login")
    fun login(@Body jwtUser: JWTUser): Call<JWTData>

    //@Query anatasyonu bu istegin sorgu parametrelerini belirtir.
    @GET("products")
    fun allProducts(@Query("limit") limit: Int): Call<Products>

    @GET("products/search")
    fun filterProducts(@Query("q") keyword: String): Call<Products>

    @GET("products/categories")
    fun getCategory(): Call<List<Category>>

    @GET("products/category/{category}")
    fun filterCategory(@Path("category") categoryname: String): Call<Products>

    @POST("carts/add")
    fun addProduct(): Call<Products>

    @GET("carts/user/5")
    fun getCart(): Call<CartResponse>

    @GET("users/1")
    fun getUser(): Call<Root>

    @PUT("users/{userId}")
    fun updateUser(@Path("userId") userId: String): Call<Root>


}