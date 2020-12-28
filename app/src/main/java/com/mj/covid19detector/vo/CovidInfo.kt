package com.mj.covid19detector.vo

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root
import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement


@Root(name = "response")
class CovidInfo {

    @ElementList(name = "body")
    val body: List<Items>? = null

}

class Items {
    @ElementList(name = "items")
    val items: List<Item>? = null
}

class Item {
    @ElementList(name = "item")
    val item: List<Info>? = arrayListOf()
}

class Info {

        @Element
        var accDefRate: String? = ""

        @Element
        var accExamCnt: String? = ""

        @Element
        var accExamCompCnt: String? = ""

        @Element
        var careCnt: String? = ""

        @Element
        var clearCnt: String? = ""

        @Element
        var createDt: String? = ""

        @Element
        var deathCnt: String? = ""

        @Element
        var decideCnt: String? = ""

        @Element
        var examCnt: String? = ""

        @Element
        var resutlNegCnt: String? = ""

        @Element
        var seq: String? = ""

        @Element
        var stateDt: String? = ""

        @Element
        var stateTime: String? = ""
}