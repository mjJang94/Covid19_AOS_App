package com.mj.covid19detector.vo

class CovidInfo {

    var data: MutableList<Info>? = ArrayList()

    class Info {
        //누적 확진률
        var accDefRate: String? = null

        //누적 검사수
        var accExamCnt: String? = null

        //누적검사완료수
        var accExamCompCnt: String ? = null

        //격리해제 수
        var clearCnt: String? = null

        //사망자 수
        var deathCnt: String? = null

        //확진자 수
        var decideCnt: String? = null

        //치료중 환자 수
        var careCnt: String? = null

        //데이터 등록일자
        var createDt: String? = null

    }
}