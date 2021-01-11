package com.mj.covid19detector.util

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Build
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.mj.covid19detector.config.DIVIDER_SEMI
import com.mj.covid19detector.config.STRING_0
import com.mj.covid19detector.config.STRING_EMPTY
import com.mj.covid19detector.config.TEN_MIN_AGO
import com.mj.covid19detector.impl.DialogClickListener
import org.json.JSONObject
import java.lang.Math.abs
import java.lang.Math.round
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

open class Util {

    /**
     * 키보드 사출
     */
    open fun showKeyboard(context: Context) {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm!!.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    /**
     * 키보드 숨기기
     */
    open fun hideKeyBoard(context: Context, view: View) {
        val imm: InputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * 인증번호 검증식
     */
    open fun verificationCertification(value: String): Boolean {
        var result = true

        if (!Pattern.matches("([0-9]{6})", value)) {
            result = false
        }

        return result
    }

    /**
     * 재설정 비밀번호 검증식
     */
    open fun verificationNewPassword(value: String): Boolean {
        var result = true

        if (!Pattern.matches("^(?!(?:[0-9]+)\$)([a-zA-Z]|[0-9a-zA-Z]){6,14}\$", value)) {
            result = false
        }

        return result
    }

    /**
     * 재설정 비밀번호 확인 검증식
     */
    open fun verificationNewPasswordCheck(valueA: String, valueB: String): Boolean {
        var result = true

        if (TextUtils.isEmpty(valueA) or TextUtils.isEmpty(valueB)) {
            result = false
        } else {
            if (valueA != valueB) {
                result = false
            }
        }
        return result
    }

    /**
     * 휴대폰 형식 표현
     */
    open fun verificationPhoneNum(value: String): Boolean {

        val regEx = "(\\d{3})(\\d{4})(\\d{4})"

        var tmpStr = value.replace(" ", "")

        if (!Pattern.matches(regEx, tmpStr)) {
            return false
        }
        return true
    }

    /**
     * 이메일 검증식
     */
    open fun verificationEmail(email: String): Boolean {

        var result = true

        if (!Pattern.matches(
                "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}\$",
                email
            )
        ) {
            result = false
        }

        return result
    }

    /**
     * short 스낵바
     */
    open fun showShortSnackBar(view: View, message: String, callback: Snackbar.Callback?) {

        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
        val layout = snackbar.view
        layout.setBackgroundColor(Color.parseColor("#e630d679"))

        val textView =
            snackbar.view.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        textView.setTextColor(Color.parseColor("#ffffff"))
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14F)

        snackbar.show()
        if (callback != null)
            snackbar.addCallback(callback)
    }

    /**
     * center short 스낵바
     */
    open fun showCenterShortSnackBar(view: View, message: String, callback: Snackbar.Callback?) {

        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
        val layout = snackbar.view
        layout.setBackgroundColor(Color.parseColor("#e630d679"))

        val textView =
            snackbar.view.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        textView.setTextColor(Color.parseColor("#ffffff"))
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14F)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
        } else {
            textView.gravity = Gravity.CENTER_HORIZONTAL
        }

        snackbar.show()
        if (callback != null)
            snackbar.addCallback(callback)
    }

    /**
     * long 스낵바
     */
    open fun showLongSnackBar(view: View, message: String) {
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        val layout = snackbar.view
        layout.setBackgroundColor(Color.parseColor("#00FF8B"))
        snackbar.setAction("Χ") {
            snackbar.dismiss()
        }

        val textView =
            snackbar.view.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        textView.setTextColor(Color.parseColor("#000000"))
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15F)

        snackbar.show()
    }

    //notificaiton list 화면에서 사용
    //1시간안의 내용이면 조금 전
    fun dateStrFromDate(timeMil: Long?): String? {
        var date = Date()
        date.time = timeMil!!
        var before10Min = Calendar.getInstance()
        before10Min.time = Date()
        before10Min.add(Calendar.MINUTE, -10)
        if (date.before(before10Min.time)) {
            val formatter = SimpleDateFormat("MM월dd일 HH시mm분")
            return formatter.format(date)
        } else {
            return TEN_MIN_AGO
        }
    }


    //**********************  MM월 dd일 형식 반환
    fun getToday(): String {
        var result = ""
        val cal = Calendar.getInstance()
        cal.time = Date()
        result = get1dayStr(cal.time) + getDaysStr(cal.get(Calendar.DAY_OF_WEEK))
        return result
    }

    fun getYesterday(): String {
        var result = ""
        val cal = Calendar.getInstance()
        cal.time = Date()
        cal.add(Calendar.DATE, -1)
        result = get1dayStr(cal.time) + getDaysStr(cal.get(Calendar.DAY_OF_WEEK))
        return result
    }


    //**********************  yyyy-MM-dd 형식 반환

    fun getTodayForSearchOption(): String {
        var result = ""
        val cal = Calendar.getInstance()
        cal.time = Date()
        result = get1dayStrYYYY_MM_DD(cal.time)
        return result
    }

    fun getYesterdayForSearchOption(): String {
        var result = ""
        val cal = Calendar.getInstance()
        cal.time = Date()
        cal.add(Calendar.DATE, -1)
        result = get1dayStrYYYY_MM_DD(cal.time)
        return result
    }


    fun getRecent7Day(): String {
        var result = ""
        val cal = Calendar.getInstance()
        cal.time = Date()
        result = get1dayStr(cal.time)
        //오늘 포함이라 -6
        cal.add(Calendar.DATE, -6)
        result = get1dayStr(cal.time) + " - " + result
        return result
    }

    fun getThisWeek(): String {
        var result = ""
        val cal = Calendar.getInstance()
        cal.time = Date()
        result = get1dayStr(cal.time)
        cal.set(Calendar.DAY_OF_WEEK, 1)
        result = get1dayStr(cal.time) + " - " + result
        return result
    }

    fun getLastWeek(): String {
        var result = ""
        val cal = Calendar.getInstance()
        cal.time = Date()
        cal.add(Calendar.WEEK_OF_YEAR, -1)
        cal.set(Calendar.DAY_OF_WEEK, cal.getActualMinimum(Calendar.DAY_OF_WEEK))
        result = get1dayStr(cal.time)
        cal.set(Calendar.DAY_OF_WEEK, cal.getActualMaximum(Calendar.DAY_OF_WEEK))
        result += " - "
        result += get1dayStr(cal.time)
        return result
    }


    fun getRecent30Day(): String {
        var result = ""
        val cal = Calendar.getInstance()
        cal.time = Date()
        result = get1dayStr(cal.time)
        //오늘 포함이라 -6
        cal.add(Calendar.DATE, -29)
        result = get1dayStr(cal.time) + " - " + result
        return result
    }

    fun getThisMonth(): String {
        var result = ""
        val cal = Calendar.getInstance()
        cal.time = Date()
        result = get1dayStr(cal.time)
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH))
        result = get1dayStr(cal.time) + " - " + result
        return result
    }

    fun getThisMonthYYYY_MM_DD(): String {
        var result = ""
        val cal = Calendar.getInstance()
        cal.time = Date()
        result = get1dayStrYYYY_MM_DD(cal.time)
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH))
        result = get1dayStrYYYY_MM_DD(cal.time) + DIVIDER_SEMI + result
        return result
    }


    fun getLastMonth(): String {
        var result = ""
        val cal = Calendar.getInstance()
        cal.time = Date()
        cal.add(Calendar.MONTH, -1)
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH))
        result = get1dayStr(cal.time)
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH))
        result += " - "
        result += get1dayStr(cal.time)
        return result
    }

    fun get1dayStr(date: Date): String {
        val formatter = SimpleDateFormat("MM월 dd일")
        val result = formatter.format(date)
        return result
    }

    fun get1dayStrYYYYMMDD(date: Date): String {
        val formatter = SimpleDateFormat("yyyyMMdd")
        val result = formatter.format(date)
        return result
    }

    fun get1dayStrYYYY_MM_DD(date: Date): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val result = formatter.format(date)
        return result
    }

    fun getDaysStr(dayOfWeekByNum: Int): String {
        var day = ""
        when (dayOfWeekByNum) {
            1 ->
                day = "일"
            2 ->
                day = "월"
            3 ->
                day = "화"
            4 ->
                day = "수"
            5 ->
                day = "목"
            6 ->
                day = "금"
            7 ->
                day = "토"
        }
        return day + "요일"
    }

    fun changeSomeText(content: String, startIndex: Int, endIndex: Int): SpannableString {

        val span = SpannableString(content)
        span.setSpan(
            ForegroundColorSpan(Color.parseColor("#13ce66")),
            startIndex,
            endIndex,
            Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )

        return span
    }

    fun jsonObjectToObject(response: JSONObject, classOfT: Class<*>): Any {

        val gson = Gson()

        return gson.fromJson(response.toString(), classOfT)
    }

    fun getNumberWithComma(strNumber: String?): String {
        var result = STRING_0

        if (strNumber != null && strNumber.isNotEmpty()) {
            try {
                val num = strNumber.toBigDecimal()
                val dec = DecimalFormat("#,###")
                result = dec.format(num)
            } catch (e: Exception) {
                result = STRING_0
            }
        } else {
            result = STRING_0
        }

        return result
    }

    fun getKRMoney(context: Context, strNumber: String?): String {
        var result = STRING_0
        result = getNumberWithComma(strNumber) + "원"
        return result
    }

    fun removeComma(strFormateValue: String?): String? {
        var tmp = STRING_0

        tmp = strFormateValue!!.replace(",", "")

        return tmp
    }

    fun getPxFromDp(dp: Int, activity: Activity): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            activity.resources.displayMetrics
        ).toInt()
    }

    /**
     * yyyy-mm-dd를 5월 23일 ・ 토요일
     *
     * @param day
     * @return
     */
    fun yyyymmddToMMDD_day(day: String?): String {
        var result = STRING_EMPTY

        val parser = SimpleDateFormat("yyyyMMdd")
        val date = parser.parse(day)
        val cal = Calendar.getInstance()
        cal.time = date
        result = get1dayStr(date) + " ・ " + getDaysStr(cal.get(Calendar.DAY_OF_WEEK))
        return result
    }

    /**
     * textview의 칼라를 바꿈
     * 버젼별로 상이한 내용을 적용하기 위함
     *
     * @param txtView
     * @param textColorForBillStateComplete
     */
    fun setTextColor(activity: Activity?, txtView: TextView?, textColor: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            txtView?.setTextColor(
                activity!!.getColor(
                    textColor
                )
            )
        } else {
            txtView?.setTextColor(
                activity?.resources!!.getColor(
                    textColor
                )
            )
        }
    }

    fun getDateFromStr(str: String?): Date {
        var result = Date()
        val parser = SimpleDateFormat("yyyy-MM-dd")
        result = parser.parse(str)
        return result
    }

    fun getStrFromDate(date: Date?): String {

        val format = SimpleDateFormat("yyyy-MM-dd HH:mm")

        return format.format(date)
    }


    /**
     * 두 날짜간 차이 구하기
     */
    fun getValueRegistDateAndToday(todayDate: String, registDate: String): Long {

        val fromDate = Util().getDateFromStr(todayDate)
        val toDate = Util().getDateFromStr(registDate)

        val calDate = fromDate.time - toDate.time

        val intervalDate = calDate / (24 * 60 * 60 * 1000)

        return abs(intervalDate)
    }

    fun replaceDateFormatToLong(str: String): Long {

        return str.replace("-", "").replace(":", "").replace(" ", "").toLong()
    }

    fun getDateFormatByKoreanFromStr(str: String?): Date {
        var result = Date()
        val parser = SimpleDateFormat("MM월 dd일", Locale.KOREAN)
        result = parser.parse(str)
        return result
    }

    //이번달 첫째날
    fun getFirstDayOnThisMonth(): Date {
        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH))
        return cal.time
    }

    //이번주 첫째날
    fun getFirstDayOnThisWeek(): Date {
        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_WEEK, cal.getActualMinimum(Calendar.DAY_OF_WEEK))
        return cal.time
    }

    fun getBefore7Day(): Date {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE, -6)
        return cal.time
    }

    fun getFirstDayOnLastWeek(): Date {
        val cal = Calendar.getInstance()
        cal.add(Calendar.WEEK_OF_YEAR, -1)
        cal.set(Calendar.DAY_OF_WEEK, cal.getActualMinimum(Calendar.DAY_OF_WEEK))
        return cal.time
    }

    fun getLastDayOnLastWeek(): Date {
        val cal = Calendar.getInstance()
        cal.add(Calendar.WEEK_OF_YEAR, -1)
        cal.set(Calendar.DAY_OF_WEEK, cal.getActualMaximum(Calendar.DAY_OF_WEEK))
        return cal.time
    }

    fun getBefore30Day(): Date {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE, -29)
        return cal.time
    }

    fun getFirstDayOnLastMonth(): Date {
        val cal = Calendar.getInstance()
        cal.add(Calendar.MONTH, -1)
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH))
        return cal.time
    }

    fun getLastDayOnLastMonth(): Date {
        val cal = Calendar.getInstance()
        cal.add(Calendar.MONTH, -1)
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH))
        return cal.time
    }

    //두 날짜 간격을 계산해서 일 단위로 반환 한다.
    fun getInterval(startDate: Date, endDate: Date): Int {
        val result =
            TimeUnit.DAYS.convert(endDate.time - startDate.time, TimeUnit.MILLISECONDS).toInt()
        return result
    }

    fun getDestructionDate(value: Int): Date {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE, value)
        return cal.time
    }

    fun setHtmlToTextView(html: String, txtView: TextView?) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            txtView?.text = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
        } else {
            txtView?.text = Html.fromHtml(html)
        }
    }

    //yyyy-MM-dd to Date
    fun getDateFromYYYY_MM_DD(yyyymmdd: String?): Date? {
        val parser = SimpleDateFormat("yyyy-MM-dd")
        val result = parser.parse(yyyymmdd)
        return result
    }

    // long to str 날짜
    fun getDateFromLong(timeMil: Long?): String {
        var result = ""
        var date = Date()
        if (timeMil != null)
            date.time = timeMil
        else
            date.time = 0

        val formatter = SimpleDateFormat("MM월dd일")
        result = formatter.format(date)
        return result
    }

    fun showErrorDialog(
        activity: Activity,
        title: String,
        msg: String,
        positBtnText: String,
        negetiBtnText: String,
        clickListener: DialogClickListener
    ) {
        val dialog = AlertDialog.Builder(activity, android.R.style.Theme_DeviceDefault_Light_Dialog)
        dialog
            .setTitle(title)
            .setMessage(msg)
            .setPositiveButton(positBtnText) { dialogInterface: DialogInterface, _: Int ->
                clickListener.positiveClick(dialogInterface)
            }
            .setNegativeButton(negetiBtnText) { dialogInterface: DialogInterface, _: Int ->
                clickListener.negetiveClick(dialogInterface)
            }
            .setCancelable(true)
            .show()
    }

    fun getDifferenceCount(a: String?, b: String?): String{
    val tmp = (a?.toInt() ?: 0).minus(b?.toInt() ?: 0)

    return tmp.toString()
    }

    fun getSymbol(value: String) : String{
        return if(value.toInt() > 0) "+" else ""
    }

}