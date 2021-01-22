package com.zerobugz.drdasalem.model.request

open class StagesRequest {
    constructor(_id: String?, checked: Boolean) {
        this._id = _id
        this.stagestatus = checked
    }

    var _id: String? = null
    var stagestatus: Boolean = false
}