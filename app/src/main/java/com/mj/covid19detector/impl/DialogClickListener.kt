package com.mj.covid19detector.impl

import android.content.DialogInterface

interface DialogClickListener {

    fun positiveClick(dialog: DialogInterface)
    fun negetiveClick(dialog: DialogInterface)
}