package com.zerobugz.drdasalem.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class BlockWiseListResponse : RealmObject() {
    @PrimaryKey
    var _id: String? = null
    var blockid: String? = null
    var blockname: String? = null
    var totalbeneficiary: String? = null
    var stagelist: RealmList<BlockWiseStageListResponse>? = null

    //  var billwise: RealmList<BlockWiseBillListResponse>? = null



}