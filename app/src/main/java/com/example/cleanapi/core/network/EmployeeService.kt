package com.example.cleanapi.core.network


import com.example.cleanapi.modules.getemployee.data.Employee
import com.example.cleanapi.modules.getemployee.data.Employees
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface EmployeeService {

    @GET("users/{id}")
    suspend fun getEmployee(@Path("id") id: String): Employee

    @GET("users")
    suspend fun getEmployees(
        @Query("limit") limit: Int,
        @Query("skip") skip: Int
    ): Employees
}

object RetrofitClient {
    private const val BASE_URL = "https://dummyjson.com/"
    private val client = OkHttpClient.Builder().build()

    fun makeRetrofitService(): EmployeeService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(EmployeeService::class.java)
    }
}



