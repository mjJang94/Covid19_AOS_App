package com.mj.covid19detector.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MainViewModel : ViewModel(){

    var btnClickListener = MutableLiveData<Unit>()

    fun testBtnClick(){
        btnClickListener.value = Unit
    }


     class MainViewModelFactory: ViewModelProvider.Factory{

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MainViewModel() as T
        }
    }
}