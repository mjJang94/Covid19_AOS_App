package com.mj.covid19detector.model

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.*
import com.mj.covid19detector.net.RetrofitConnection
import com.mj.covid19detector.util.Util
import com.mj.covid19detector.util.XmlParser
import com.mj.covid19detector.vo.CovidInfo
import com.mj.covid19detector.vo.CovidRawInfo
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import org.koin.experimental.property.inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val retrofitConnection: RetrofitConnection) : ViewModel() {

    private val coroutineExceptionHanlder = CoroutineExceptionHandler { coroutineContext, throwable ->
        throwable.printStackTrace()
    }

    private val ioDispatchers = Dispatchers.IO + coroutineExceptionHanlder
    private val uiDispatchers = Dispatchers.Main + coroutineExceptionHanlder

    var covidInfo = MutableLiveData<CovidInfo>().apply {
        value = CovidInfo()
    }


    init {
        getData()
    }


    private fun getData() {

        viewModelScope.launch(ioDispatchers) {

            retrofitConnection.getCovidInfo()
                .enqueue(object : Callback<CovidRawInfo> {
                    override fun onResponse(
                        call: Call<CovidRawInfo>,
                        response: Response<CovidRawInfo>
                    ) {
                        if (response.isSuccessful) {
                            val tmpData = response.body()

                            covidInfo.postValue(XmlParser().getData(tmpData?.covidXmlInfo ?: ""))

                        } else {
//                            covidInfo.value = CovidInfo()
                        }
                    }

                    override fun onFailure(call: Call<CovidRawInfo>, t: Throwable) {

                    }
                })
        }
    }


    class MainViewModelFactory(val retrofitConnection: RetrofitConnection) :
        ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MainViewModel(retrofitConnection) as T
        }
    }
}