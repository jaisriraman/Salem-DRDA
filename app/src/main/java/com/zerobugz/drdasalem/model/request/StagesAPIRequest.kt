package com.zerobugz.drdasalem.model.request

open class StagesAPIRequest {
    constructor(_id: String?, checked: Boolean) {
        this._id = _id
        this.stagestatus = checked
    }

    var _id: String? = null
    var beneficiaryid: String? = null
    var schemeid: String? = null
    var payment: String? = null
    var stagestatus: Boolean = false
}