package com.mj.covid19detector.util

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.mj.covid19detector.vo.CovidInfo
import com.mj.covid19detector.vo.CovidRawInfo
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import org.xml.sax.InputSource
import java.io.StringReader
import javax.xml.parsers.DocumentBuilderFactory

class XmlParser{

    private val TAG = XmlParser::class.java.simpleName
    lateinit var document: Document



    fun getData(xmlData: String): CovidInfo{

        if (xmlData.isNotEmpty()) {

            try {
                val covidInfo = CovidInfo()

                val dbFactory = DocumentBuilderFactory.newInstance()
                val dBuilder = dbFactory.newDocumentBuilder()

                document = dBuilder.parse(InputSource(StringReader(xmlData)))


                document.documentElement.normalize()


                val nodeList: NodeList = document.getElementsByTagName("item")

                //직전일과 대비하여 비교만 하자
                for (i in 0 until nodeList.length) run {

                    val node: Node = nodeList.item(i)

                    if (node.nodeType == Node.ELEMENT_NODE) {
                        val element = node as Element

                        val tmpInfo = CovidInfo.Info()

                        tmpInfo.accDefRate = getTagValue("accDefRate", element) ?: ""
                        tmpInfo.careCnt = getTagValue("careCnt", element) ?: ""
                        tmpInfo.clearCnt = getTagValue("clearCnt", element) ?: ""
                        tmpInfo.createDt = getTagValue("createDt", element) ?: ""
                        tmpInfo.deathCnt = getTagValue("deathCnt", element) ?: ""
                        tmpInfo.decideCnt = getTagValue("decideCnt", element) ?: ""
                        tmpInfo.accExamCnt = getTagValue("accExamCnt", element) ?: ""
                        tmpInfo.accExamCompCnt = getTagValue("accExamCompCnt", element) ?: ""

//                        covidInfo.data?.add(tmpInfo)
                        covidInfo.data?.add(tmpInfo)

//                        Log.d(TAG + "누적 확진률:", getTagValue("accDefRate", element) ?: "")
//                        Log.d(TAG + "검사진행 수:", getTagValue("examCnt", element) ?: "")
//                        Log.d(TAG + "누적 검사:", getTagValue("accExamCnt", element) ?: "")
//                        Log.d(TAG + "누적 검사 완료수:", getTagValue("accExamCompCnt", element) ?: "")
//                        Log.d(TAG + "치료중인 환자 수:", getTagValue("careCnt", element) ?: "")
//                        Log.d(TAG + "완치자 수:", getTagValue("clearCnt", element) ?: "")
//                        Log.d(TAG + "사망자 수:", getTagValue("deathCnt", element) ?: "")
//                        Log.d(TAG + "확진자 수:", getTagValue("decideCnt", element) ?: "")
//                        Log.d(TAG + "데이터 등록일자:", getTagValue("createDt", element) ?: "")
                    }
                }
                return covidInfo
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return CovidInfo()
    }

    private fun getTagValue(
        tag: String,
        eElement: Element
    ): String? {

        if (eElement.getElementsByTagName(tag).item(0) == null){
            return ""
        }

        val nlList = eElement.getElementsByTagName(tag).item(0).childNodes

        if (nlList.item(0) == null){
            return ""
        }

        val nValue: Node = nlList.item(0)

        return nValue.nodeValue

    }
}