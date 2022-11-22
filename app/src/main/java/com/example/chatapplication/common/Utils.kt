package com.example.chatapplication.common

import android.annotation.SuppressLint
import android.app.Application
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.util.Log
import com.example.chatapplication.common.Constants.TAG
import com.example.chatapplication.domain.model.Contact

fun formatNumber(number: String): String? {
    var number = number.trim()
    if(! number.startsWith("+91")){
        number = "+91$number"
    }
    var clean = ""
    for(c in number){
        if(c in '0'..'9' || c=='+'){
            clean += c
        }
    }
    return if(clean.length==13)  clean else null
}


@SuppressLint("Range")
fun getPhoneContacts(app: Application): List<Contact> {
    val list = mutableListOf<Contact>()
    try{
        val cursor = app.contentResolver.query(Phone.CONTENT_URI, null, null, null, Phone.DISPLAY_NAME+" ASC")
        while (cursor?.moveToNext() == true){
            val name = cursor.getString(cursor.getColumnIndex(Phone.DISPLAY_NAME))
            val number = formatNumber( cursor.getString(cursor.getColumnIndex(Phone.NUMBER))) ?: continue
            list.add(Contact(name=name, number=number))
            //Log.d(TAG, "getPhoneContacts: $name $number")
        }
        cursor?.close()
    } catch (e: Exception){
        Log.e(TAG, "getPhoneContacts: ", e)
    }
    return list.distinctBy { it.number }
}