package com.zerobugz.drdasalem.model

import io.realm.RealmObject

open class BlockWiseStageListResponse : RealmObject() {

    var _id: String? = null
    var stagename: String? = null
    var blockid: String? = null
    var stageid: String? = null
    var stagecount: String? = null
    var paymentname: String? = null
    var paymentid: String? = null
    var paymentcount: String? = null


    /*  var stagename: String? = null
          get() = if (field == null) {
              paymentname
          } else {
              field
          }*/
}