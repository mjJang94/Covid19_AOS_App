package com.mj.covid19detector.util

import android.util.Log
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import org.xml.sax.InputSource
import java.io.StringReader
import javax.xml.parsers.DocumentBuilderFactory

class XmlParser constructor() {

    private val TAG = XmlParser::class.java.simpleName

    lateinit var document: Document


    fun getData(xmlData: String) {

        if (xmlData.isNotEmpty()) {

            try {

                val dbFactory = DocumentBuilderFactory.newInstance()
                val dBuilder = dbFactory.newDocumentBuilder()

                document = dBuilder.parse(InputSource(StringReader(xmlData)))


                document.documentElement.normalize()


                val nodeList: NodeList = document.getElementsByTagName("item")

                for (i in 0 until nodeList.length) run {

                    val node: Node = nodeList.item(i)

                    if (node.nodeType == Node.ELEMENT_NODE) {
                        val element = node as Element
                        Log.d(TAG + "누적 확진률:", getTagValue("accDefRate", element) ?: "")
                        Log.d(TAG + "검사진행 수:", getTagValue("examCnt", element) ?: "")
                        Log.d(TAG + "누적 검사:", getTagValue("accExamCnt", element) ?: "")
                        Log.d(TAG + "누적 검사 완료수:", getTagValue("accExamCompCnt", element) ?: "")
                        Log.d(TAG + "치료중인 환자 수:", getTagValue("careCnt", element) ?: "")
                        Log.d(TAG + "완치자 수:", getTagValue("clearCnt", element) ?: "")
                        Log.d(TAG + "사망자 수:", getTagValue("deathCnt", element) ?: "")
                        Log.d(TAG + "확진자 수:", getTagValue("decideCnt", element) ?: "")
                        Log.d(TAG + "데이터 등록일자:", getTagValue("createDt", element) ?: "")
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
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