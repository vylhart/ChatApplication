package com.example.chatapplication.common

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