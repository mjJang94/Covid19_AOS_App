package com.mj.covid19detector

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mj.covid19detector.config.SERVICE_KEY
import com.mj.covid19detector.databinding.ActivityMainBinding
import com.mj.covid19detector.model.MainViewModel
import com.mj.covid19detector.net.RetrofitConnection
import com.mj.covid19detector.vo.CovidInfo
import org.koin.android.ext.android.inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    private val apiConnection: RetrofitConnection by inject()

    private val TAG = MainActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val viewModel: MainViewModel = ViewModelProvider(this,  MainViewModel.MainViewModelFactory()).get(MainViewModel::class.java)

        viewModel.btnClickListener.observe(this, Observer {


            apiConnection.getCovidInfo(SERVICE_KEY, "1", "10", "20200310", "20200315")
                .enqueue(object : Callback<CovidInfo> {

                    override fun onResponse(
                        call: Call<CovidInfo>,
                        response: Response<CovidInfo>
                    ) {
                        val listInfo = response.body()
                        Log.e(TAG, "$listInfo")
                    }

                    override fun onFailure(call: Call<CovidInfo>, t: Throwable) {
                        Log.e(TAG, "통신 실패: ${t.message.toString()}")
                    }
                })

        })

        binding.mainViewmodel = viewModel
        binding.lifecycleOwner = this

    }
}