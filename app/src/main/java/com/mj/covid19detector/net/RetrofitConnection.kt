package com.mj.covid19detector.net

import com.mj.covid19detector.config.SERVICE_KEY
import com.mj.covid19detector.vo.CovidInfo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitConnection {

    @GET("openapi/service/rest/Covid19/getCovid19InfStateJson")
    fun getCovidInfo(
        @Query("ServiceKey") serviceKey: String,
        @Query("pageNo") pageNo: String,
        @Query("numOfRows") numOfRows: String,
        @Query("startCreateDt") startCreateDt: String,
        @Query("endCreateDt") endCreateDt: String
    ): Call<CovidInfo>
}