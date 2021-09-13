package com.example.shows_tonimatic.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shows_tonimatic.model.LoginRequest
import com.example.shows_tonimatic.model.LoginResponse
import com.example.shows_tonimatic.networking.ApiModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {
    private val loginResultLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    fun getLoginResultLiveData(): LiveData<Boolean> {
        return loginResultLiveData
    }

    fun login(email: String, password: String) {
        ApiModule.retrofit.login(LoginRequest(email, password)).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                loginResultLiveData.value = response.isSuccessful
                if (response.isSuccessful) {
                    val prefs = ApiModule.prefs
                    with (prefs.edit()) {
                        putString("access-token", response.headers()["access-token"])
                        putString("client", response.headers()["client"])
                        putString("token-type", response.headers()["token-type"])
                        putString("expiry", response.headers()["expiry"])
                        putString("uid", response.headers()["uid"])
                        putString("content-type", response.headers()["content-type"])
                        putString("user email", response.body()?.user?.email)
                        putInt("user id", response.body()?.user!!.id)
                        putString("user image", response.body()?.user?.imageUrl)
                        apply()
                    }
                }
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                loginResultLiveData.value = false
            }
        })
    }
}
