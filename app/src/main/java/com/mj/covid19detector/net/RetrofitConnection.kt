package com.mj.covid19detector.net

import com.mj.covid19detector.vo.CovidInfo
import retrofit2.Call
import retrofit2.http.GET

interface RetrofitConnection {

    @GET("test") fun getCovidInfo(): Call<CovidInfo>
}



