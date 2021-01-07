package com.mj.covid19detector

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mj.covid19detector.databinding.ActivityMainBinding
import com.mj.covid19detector.model.MainViewModel
import com.mj.covid19detector.net.RetrofitConnection
import com.mj.covid19detector.util.XmlParser
import com.mj.covid19detector.vo.CovidInfo
import com.mj.covid19detector.vo.CovidRawInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : BaseActivity() {

    private val apiConnection: RetrofitConnection by inject()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)

        val viewModel: MainViewModel = ViewModelProvider(
            this,
            MainViewModel.MainViewModelFactory(apiConnection)
        ).get(MainViewModel::class.java)



        binding.mainViewmodel = viewModel
        binding.lifecycleOwner = this


        viewModel.covidInfo.observe(this, Observer<CovidInfo> { data ->
            Log.e("Main", data.data?.get(0)?.accDefRate ?: "null")
        })

        Log.e("asd", "asdas")

    }

}