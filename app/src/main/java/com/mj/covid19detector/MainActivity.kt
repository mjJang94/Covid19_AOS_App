package com.mj.covid19detector

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mj.covid19detector.databinding.ActivityMainBinding
import com.mj.covid19detector.model.MainViewModel
import com.mj.covid19detector.net.RetrofitConnection
import com.mj.covid19detector.util.XmlParser
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


        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)

        val viewModel: MainViewModel = ViewModelProvider(
            this,
            MainViewModel.MainViewModelFactory()
        ).get(MainViewModel::class.java)

        binding.mainViewmodel = viewModel
        binding.lifecycleOwner = this

//        viewModel.btnClickListener.observe(this, Observer {

            apiConnection.getCovidInfo()
                .enqueue(object : Callback<CovidInfo> {

                    override fun onResponse(
                        call: Call<CovidInfo>,
                        response: Response<CovidInfo>
                    ) {
                        if (response.isSuccessful) {
                            val covidInfo = response.body()

                            Toast.makeText(this@MainActivity, "통신 성공", Toast.LENGTH_SHORT).show()


                            val xmlParser = XmlParser().getData(covidInfo?.covidXmlInfo ?: "")

                        } else {
                            Toast.makeText(this@MainActivity, "통신 실패", Toast.LENGTH_SHORT).show()
                        }

                    }

                    override fun onFailure(call: Call<CovidInfo>, t: Throwable) {
                        Toast.makeText(this@MainActivity, "통신 실패", Toast.LENGTH_SHORT).show()
                    }
                })

//        })


    }
}