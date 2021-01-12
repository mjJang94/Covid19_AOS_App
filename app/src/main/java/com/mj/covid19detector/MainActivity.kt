package com.mj.covid19detector

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mj.covid19detector.config.STRING_0
import com.mj.covid19detector.config.STRING_EMPTY
import com.mj.covid19detector.databinding.ActivityMainBinding
import com.mj.covid19detector.impl.DialogClickListener
import com.mj.covid19detector.model.MainViewModel
import com.mj.covid19detector.net.RetrofitConnection
import com.mj.covid19detector.util.Util
import com.mj.covid19detector.vo.CovidInfo
import org.koin.android.ext.android.inject

class MainActivity : BaseActivity() {

    private val apiConnection: RetrofitConnection by inject()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)

        //MainviewModel 생성
        val viewModel: MainViewModel = ViewModelProvider(
            this,
            MainViewModel.MainViewModelFactory(apiConnection)
        ).get(MainViewModel::class.java)


        //ViewMoel 등록
        binding.mainViewmodel = viewModel
        //Lifecycle 등록
        binding.lifecycleOwner = this


        //ViewMoel의 showErrorDialog 데이터를 관측하도록 등록.
        //api 통신이 실패하거나 정상적인 통신 후 받아온 데이터가 온전치 못할 때 에러 다이얼로그를 출력
        viewModel.showErrorDialog.observe(this, Observer {
            if (it == true) {
                Util().showErrorDialog(
                    this,
                    getString(R.string.error_network_title),
                    getString(R.string.error_network_msg),
                    getString(R.string.common_confirm),
                    STRING_EMPTY,
                    object : DialogClickListener {
                        override fun negetiveClick(dialog: DialogInterface) {
                        }

                        override fun positiveClick(dialog: DialogInterface) {
                            dialog.dismiss()
                        }
                    })
            }
        })

        //ViewMoel의 covidInfo 데이터를 관측하도록 등록.
        //api 통신 후 변경이 관측된 데이터가 특정 개수를 넘기면 ui에 값 세팅
        viewModel.covidInfo.observe(this, Observer {

            if (it.data != null && it.data?.size!! >= 2) {

                infectDataInitialize(binding, it)
                quarantineReleaseDataInitialize(binding, it)
                treatmentDataInitialize(binding, it)
                deathDataInitialize(binding, it)
                examStateInitialize(binding, it)

            } else {
                if (viewModel.beforeApiConnection.value != true) {
                    viewModel.showErrorDialog.postValue(true)
                }
            }
        })
    }

    //확진환자
    @SuppressLint("SetTextI18n")
    private fun infectDataInitialize(
        binding: ActivityMainBinding,
        it: CovidInfo
    ) {
        binding.txtInfectedPatients.text = Util().getNumberWithComma(it.data?.get(0)?.decideCnt)

        val infectedPatientDifferenceCount =
            Util().getDifferenceCount(it.data?.get(0)?.decideCnt, it.data?.get(1)?.decideCnt)

        val symbol = Util().getSymbol(infectedPatientDifferenceCount)

        binding.txtInfectedPatientsCount.text =
            "전일대비 ( $symbol${Util().getNumberWithComma(infectedPatientDifferenceCount)})"
    }

    //격리해제
    @SuppressLint("SetTextI18n")
    private fun quarantineReleaseDataInitialize(
        binding: ActivityMainBinding,
        it: CovidInfo
    ) {
        binding.txtQuarantineRelease.text = Util().getNumberWithComma(it.data?.get(0)?.clearCnt)

        val quarantineReleaseeCount =
            Util().getDifferenceCount(it.data?.get(0)?.clearCnt, it.data?.get(1)?.clearCnt)

        val symbol = Util().getSymbol(quarantineReleaseeCount)

        binding.txtQuarantineReleaseCount.text =
            "( $symbol${Util().getNumberWithComma(quarantineReleaseeCount)})"
    }

    //치료중
    @SuppressLint("SetTextI18n")
    private fun treatmentDataInitialize(
        binding: ActivityMainBinding,
        it: CovidInfo
    ) {
        binding.txtUnderTreatment.text = Util().getNumberWithComma(it.data?.get(0)?.careCnt)

        val careCount =
            Util().getDifferenceCount(it.data?.get(0)?.careCnt, it.data?.get(1)?.careCnt)

        val symbol = Util().getSymbol(careCount)

        binding.txtUnderTreatmentCount.text = "( $symbol${Util().getNumberWithComma(careCount)})"
    }

    //사망자
    @SuppressLint("SetTextI18n")
    private fun deathDataInitialize(
        binding: ActivityMainBinding,
        it: CovidInfo
    ) {
        binding.txtDeath.text = Util().getNumberWithComma(it.data?.get(0)?.deathCnt)

        val deathCount =
            Util().getDifferenceCount(it.data?.get(0)?.deathCnt, it.data?.get(1)?.deathCnt)

        val symbol = Util().getSymbol(deathCount)

        binding.txtDeathCount.text = "( $symbol${Util().getNumberWithComma(deathCount)})"
    }

    //검사 현황판
    @SuppressLint("SetTextI18n")
    private fun examStateInitialize(
        binding: ActivityMainBinding,
        it: CovidInfo
    ) {
        binding.txtTotalExamCount.text =
            "${Util().getNumberWithComma(it.data?.get(0)?.accExamCnt ?: STRING_0)} 건"
        binding.txtTotalDoneExamCount.text =
            "${Util().getNumberWithComma(it.data?.get(0)?.accExamCompCnt ?: STRING_0)} 건"
        binding.txtDecideCount.text =
            "${String.format("%.1f", it.data?.get(0)?.accDefRate?.toFloat())} %"
    }
}