package com.mj.covid19detector.model

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.*
import com.mj.covid19detector.net.RetrofitConnection
import com.mj.covid19detector.util.XmlParser
import com.mj.covid19detector.vo.CovidInfo
import com.mj.covid19detector.vo.CovidRawInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import org.koin.experimental.property.inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val retrofitConnection: RetrofitConnection) : ViewModel(){


    val covidInfo : LiveData<CovidInfo> = liveData(Dispatchers.IO){
        emit(getData())
    }





    private suspend fun getData(): CovidInfo = withContext(Dispatchers.IO) {

        var tmpData = CovidInfo()

        retrofitConnection.getCovidInfo()
            .enqueue(object : Callback<CovidRawInfo> {

                override fun onResponse(
                    call: Call<CovidRawInfo>,
                    response: Response<CovidRawInfo>
                ) {
                    if (response.isSuccessful) {
                        val covidInfo = response.body()

                        tmpData = XmlParser().getData(covidInfo?.covidXmlInfo ?: "")

                    } else {
                        tmpData = CovidInfo()
                    }
                }

                override fun onFailure(call: Call<CovidRawInfo>, t: Throwable) {
                    tmpData = CovidInfo()
                }
            })


    }


     class MainViewModelFactory(val retrofitConnection: RetrofitConnection): ViewModelProvider.Factory{

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MainViewModel(retrofitConnection) as T
        }
    }
}