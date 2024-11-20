package com.mksolution.newsshortadmin

data class NotificationData(
    var  topic : String ?= null,
    var  data : HashMap<String,String>
)
