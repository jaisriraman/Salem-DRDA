package com.zerobugz.drdasalem.model

class BeneficiaryListResponseResultsLocal {
    constructor()
    constructor(
        value: String,
        _id: String?,
        schemeid: String?,
        blockid: String?,
        panjayatid: String?,
        habitantid: String?,
        workorderdate: String?,
        habitationname: String?,
        panjayatname: String?,
        blockname: String?,
        schemename: String?,
        firstname: String?,
        lastname: String?
    ) {
        this.sNo = value
        this._id = _id
        this.schemeid = schemeid
        this.blockid = blockid
        this.panjayatid = panjayatid
        this.habitantid = habitantid
        this.workorderdate = workorderdate
        this.habitationname = habitationname
        this.panjayatname = panjayatname
        this.blockname = blockname
        this.schemename = schemename
        this.firstname = firstname
        this.lastname = lastname
    }


    constructor(
        value: String,
        _id: String?,
        schemeid: String?,
        blockid: String?,
        panjayatid: String?,
        habitantid: String?,
        workorderdate: String?,
        habitationname: String?,
        panjayatname: String?,
        blockname: String?,
        schemename: String?,
        firstname: String?,
        lastname: String?,
        stagestatus: String?,
        paymentstatus: String?,
        beneficiaryid: String?
    ) {
        this.sNo = value
        this._id = _id
        this.schemeid = schemeid
        this.blockid = blockid
        this.panjayatid = panjayatid
        this.habitantid = habitantid
        this.workorderdate = workorderdate
        this.habitationname = habitationname
        this.panjayatname = panjayatname
        this.blockname = blockname
        this.schemename = schemename
        this.firstname = firstname
        this.lastname = lastname
        this.stagestatus = stagestatus
        this.paymentstatus = paymentstatus
        this.beneficiaryid = beneficiaryid
    }


    var sNo: String? = null
    var _id: String? = null
    var schemeid: String? = null
    var blockid: String? = null
    var panjayatid: String? = null
    var habitantid: String? = null
    var workorderdate: String? = null
    var habitationname: String? = null
    var panjayatname: String? = null
    var blockname: String? = null
    var schemename: String? = null
    var firstname: String? = null
    var lastname: String? = null
    var stagestatus: String? = null
    var paymentstatus: String? = null
    var beneficiaryid: String? = null

}