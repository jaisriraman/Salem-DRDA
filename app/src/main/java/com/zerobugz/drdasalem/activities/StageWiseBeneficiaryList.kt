package com.zerobugz.drdasalem.activities

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.zerobugz.drdasalem.R
import com.zerobugz.drdasalem.api.presenter.APIPresenter
import com.zerobugz.drdasalem.api.presenter.APIView
import com.zerobugz.drdasalem.model.*
import com.zerobugz.drdasalem.model.request.BeneficiaryRequest
import com.zerobugz.drdasalem.utils.Spinner.KeyPairBoolData
import com.zerobugz.drdasalem.utils.Spinner.KeyPairModel
import com.zerobugz.drdasalem.utils.Utils
import com.zerobugz.drdasalem.utils.bind
import com.zerobugz.drdasalem.utils.update
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_stagewise_beneficiary_list.finyear
import kotlinx.android.synthetic.main.activity_stagewise_beneficiary_list.ivBack
import kotlinx.android.synthetic.main.activity_stagewise_beneficiary_list.mPanchayat
import kotlinx.android.synthetic.main.activity_stagewise_beneficiary_list.mScheme
import kotlinx.android.synthetic.main.activity_stagewise_beneficiary_list.noDataFound
import kotlinx.android.synthetic.main.activity_stagewise_beneficiary_list.recyclerView
import kotlinx.android.synthetic.main.activity_stagewise_beneficiary_list.searchview
import kotlinx.android.synthetic.main.activity_stagewise_beneficiary_list.swipeRefresh
import kotlinx.android.synthetic.main.activity_stagewise_beneficiary_list.tvTitle
import kotlinx.android.synthetic.main.bottomsheet_recycler_view_item.view.*
import kotlinx.android.synthetic.main.bottomsheet_recycler_view_item1.view.*
import kotlinx.android.synthetic.main.stagewisebeneficiary_recycler_view_item.view.*

class StageWiseBeneficiaryList : AppCompatActivity() {
    private var schemeid: String? = null
    private var panchayatId: String? = null
    private var panchayatname: String? = null
    private var stageId: String? = null
    private var realm: Realm? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stagewise_beneficiary_list)
        realm = try {
            Realm.getDefaultInstance()
        } catch (e: Exception) {
            val realmConfiguration = RealmConfiguration.Builder().build()
            Realm.setDefaultConfiguration(realmConfiguration)
            Realm.getDefaultInstance()
        }

        if (intent != null && intent.extras != null) {
            schemeid = intent.getStringExtra("schemeid")
            panchayatId = intent.getStringExtra("panchayatId")
            panchayatname = intent.getStringExtra("panchayatname")
            stageId = intent.getStringExtra("stageId")
        }
        mPanchayat.text = "" + panchayatname.toString()
        swipeRefresh.isRefreshing = true
        swipeRefresh.setOnRefreshListener {
//            getPanchayatList()
            getYearList()
        }
//        getPanchayatList()
        getYearList()

//        if (finyear.selectedItems.size > 0 && schemeid != null && panchayatId != null) {
//
//            getBeneficiaryList(finyear.selectedItems[0].key, schemeid!!, panchayatId!!)
//        }

//        if (finyear.selectedItems.size > 0 && mScheme.selectedItems.size > 0 && mPanchayat.selectedItems.size > 0) {
//            getBeneficiaryList(
//                finyear.selectedItems[0].key,
//                mScheme.selectedItems[0].key,
//                mPanchayat.selectedItems[0].key
//            )
//        }

        searchview!!.setOnQueryTextFocusChangeListener { p0, p1 ->
            if (p0!!.hasFocus() || searchview.query.isNotEmpty()) {
                //  ivBack!!.visibility = View.GONE
                tvTitle!!.visibility = View.GONE
            } else {
                //  ivBack!!.visibility = View.VISIBLE
                tvTitle!!.visibility = View.VISIBLE
                searchview.isIconified = true
            }
        }
        searchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                TODO("Not yet implemented")
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                setSearchRecyclerAdapter(newText!!)

                //  filterSiteBreakDown(newText.toString(), recyclerView)
                return false
            }


        })


        ivBack.setOnClickListener {
            onBackPressed()
        }

    }

    private fun getBeneficiaryList(yearid: String, schemeId: String) {
        swipeRefresh.isRefreshing = true
        val rResponse = realm!!.where<OTPResponseResults>().findFirst()
        val beneficiaryRequest = BeneficiaryRequest()
        beneficiaryRequest.schemeid = schemeId
        beneficiaryRequest.blockid = rResponse!!.blockid.toString()
        beneficiaryRequest.panjayatid = panchayatId
        beneficiaryRequest.stageid = stageId
        beneficiaryRequest.yearid = yearid

        APIPresenter(this, object : APIView() {
            override fun onSuccess() {
                setRecyclerAdapter()

            }

            override fun onException() {
                swipeRefresh.isRefreshing = false
            }

        }).getStagesWiseBeneficiarylist(beneficiaryRequest)
    }

    private fun setRecyclerAdapter() {
        if (swipeRefresh.isRefreshing) {
            swipeRefresh.isRefreshing = false
        }
        val rResponseResults = realm!!.where<RStageWiseBeneficiaryList>().findAll()
        if (rResponseResults.size > 0) {
            recyclerView.visibility = View.VISIBLE
            noDataFound.visibility = View.GONE

            val realmResults = ArrayList<BeneficiaryListResponseResultsLocal>()

            for (value in 0 until rResponseResults.size) {

                val sNumber = value + 1
                realmResults.add(
                    BeneficiaryListResponseResultsLocal(
                        sNumber.toString(),
                        rResponseResults[value]!!._id,
                        rResponseResults[value]!!.schemeid,
                        rResponseResults[value]!!.blockid,
                        rResponseResults[value]!!.panjayatid,
                        rResponseResults[value]!!.habitantid,
                        rResponseResults[value]!!.workorderdate,
                        rResponseResults[value]!!.habitationname,
                        rResponseResults[value]!!.panjayatname,
                        rResponseResults[value]!!.blockname,
                        rResponseResults[value]!!.schemename,
                        rResponseResults[value]!!.firstname,
                        rResponseResults[value]!!.lastname,
                        rResponseResults[value]!!.stagestatus,
                        rResponseResults[value]!!.paymentstatus,
                        rResponseResults[value]!!.beneficiaryid
                    )
                )
            }


            recyclerView.bind(realmResults, R.layout.stagewisebeneficiary_recycler_view_item) {

                    item ->
                mSno.text = "" + item.sNo
                mBenificiaryName.text = "" + item.firstname + " " + item.lastname
                mHabitantName.text = "" + item.habitationname

                if (item.paymentstatus.equals("0")) {
                    mBilling.text = "Not Cleared"
                } else if (item.paymentstatus.equals("1")) {
                    mBilling.text = "Cleared"
                } else {
                    mBilling.text = "-"
                }

                llHeader.setOnClickListener {

                    val dialog = BottomSheetDialog(this@StageWiseBeneficiaryList)
                    dialog.setContentView(R.layout.bottomsheetdialog)
                    val cancel = dialog.findViewById<ImageView>(R.id.cancelBtn)
                    val nameToolbar = dialog.findViewById<TextView>(R.id.nameToolbar)
                    val recyclerView = dialog.findViewById<RecyclerView>(R.id.recyclerView)
                    val recyclerView1 = dialog.findViewById<RecyclerView>(R.id.recyclerView1)
                    val mBenificiaryName = dialog.findViewById<TextView>(R.id.mBenificiaryName)
                    val mScheme = dialog.findViewById<TextView>(R.id.mScheme)
                    val mHabitant = dialog.findViewById<TextView>(R.id.mHabitant)
                    val mPanchayat = dialog.findViewById<TextView>(R.id.mPanchayat)
                    val mBlock = dialog.findViewById<TextView>(R.id.mBlock)

                    dialog.show()

                    val beneficiaryRequest = BeneficiaryRequest()
                    beneficiaryRequest.blockid = item!!.blockid
                    beneficiaryRequest.panjayatid = item!!.panjayatid
                    beneficiaryRequest.schemeid = item!!.schemeid
                    beneficiaryRequest.habitantid = item!!.habitantid
                    beneficiaryRequest.beneficiaryid = item.beneficiaryid

                    APIPresenter(this@StageWiseBeneficiaryList, object : APIView() {
                        override fun onSuccess() {
                            Utils.hideProgress()

                            val realmResults =
                                realm!!.where<WorkStageListResponseResults>()
                                    .equalTo("_id", item.beneficiaryid)
                                    .findFirst()

                            if (realmResults != null) {
                                mBenificiaryName!!.text =
                                    "" + realmResults.firstname + " " + realmResults.lastname
                                mScheme!!.text = "" + realmResults.schemename
                                mHabitant!!.text = "" + realmResults.habitationname
                                mPanchayat!!.text = "" + realmResults.panjayatname
                                mBlock!!.text = "" + realmResults.blockname

                            }

                            val realmResults1 = realm!!.where<StageListResponseResults>().findAll()
                            val realmResults2 = realm!!.where<PaymentStagesResults>().findAll()

                            recyclerView!!.bind(
                                realmResults1,
                                R.layout.bottomsheet_recycler_view_item
                            ) { item ->

                                textname.text = "" + item.stagename

                                if (item.stagestatus == true) {
                                    textcount.text = "Completed"
                                } else {
                                    textcount.text = "Not Completed"
                                }
                            }

                            recyclerView1!!.bind(
                                realmResults2,
                                R.layout.bottomsheet_recycler_view_item1
                            ) { item ->

                                textname1.text = "" + item.paymentname

                                if (item.paymentstages == true) {
                                    textcount1.text = "Cleared"
                                } else {
                                    textcount1.text = "Not Cleared"
                                }

                            }

                        }

                        override fun onException() {
                            Utils.hideProgress()
                        }

                    }).getWorkStageList(beneficiaryRequest)



                    nameToolbar!!.text = "" + item.firstname + " " + item.lastname

                    cancel!!.setOnClickListener { dialog.dismiss() }

                }

            }.layoutManager(LinearLayoutManager(this@StageWiseBeneficiaryList))
        } else {
            noDataFound.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        }

    }

    private fun setSearchRecyclerAdapter(text: String) {

        if (swipeRefresh.isRefreshing) {
            swipeRefresh.isRefreshing = false
        }
        val rResponseResults = realm!!.where<RStageWiseBeneficiaryList>()
            .contains("habitationname", text)
            .or()
            .contains("panjayatname", text)
            .or()
            .contains("schemename", text)
            .or()
            .contains("blockname", text)
            .or()
            .contains("firstname", text)
            .findAll()
        if (rResponseResults.size > 0) {
            recyclerView.visibility = View.VISIBLE
            noDataFound.visibility = View.GONE

            val realmResults = ArrayList<BeneficiaryListResponseResultsLocal>()

            for (value in 0 until rResponseResults.size) {

                val sNumber = value + 1
                realmResults.add(
                    BeneficiaryListResponseResultsLocal(
                        sNumber.toString(),
                        rResponseResults[value]!!._id,
                        rResponseResults[value]!!.schemeid,
                        rResponseResults[value]!!.blockid,
                        rResponseResults[value]!!.panjayatid,
                        rResponseResults[value]!!.habitantid,
                        rResponseResults[value]!!.workorderdate,
                        rResponseResults[value]!!.habitationname,
                        rResponseResults[value]!!.panjayatname,
                        rResponseResults[value]!!.blockname,
                        rResponseResults[value]!!.schemename,
                        rResponseResults[value]!!.firstname,
                        rResponseResults[value]!!.lastname,
                        rResponseResults[value]!!.stagestatus,
                        rResponseResults[value]!!.paymentstatus,
                        rResponseResults[value]!!.beneficiaryid
                    )
                )
            }


            recyclerView.update(realmResults)
        } else {
            recyclerView.visibility = View.GONE
            noDataFound.visibility = View.VISIBLE
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun getSchemaList() {
        APIPresenter(this, object : APIView() {
            override fun onSuccess() {
                setSchemeItems()
//                getPanchayatList()
            }

            override fun onException() {

            }

        }).getSchemeList()
    }

    private fun setSchemeItems() {
        val arrayList = ArrayList<KeyPairModel>()
        val query1 = realm!!.where<SchemeResponseResults>().findAll()
        for (k in 0 until query1.size) {
            if (k == 0) {
                arrayList!!.add(
                    KeyPairModel(
                        (query1[k]?._id),
                        query1[k]?.schemename,
                        true
                    )
                )
            } else {
                arrayList!!.add(
                    KeyPairModel(
                        (query1[k]?._id),
                        query1[k]?.schemename,
                        false
                    )
                )
            }


        }
        val arrayList1 = ArrayList<KeyPairBoolData>()
        for (k in arrayList!!.indices) {
            val h = KeyPairBoolData()
            h.id = (k + 1).toLong()
            h.name = arrayList[k].value
            h.key = arrayList[k].key
            h.isSelected = arrayList[k].aBoolean
            arrayList1.add(h)
        }
        mScheme!!.setItems(arrayList1, -1) { items: List<KeyPairBoolData> ->
            for (i in items.indices) {
                if (items[i].isSelected) {

                    if (finyear.selectedItems.size > 0 && schemeid != null) {
                        getBeneficiaryList(
                            finyear.selectedItems[0].key,
                            schemeid!!,
//                            mPanchayat.selectedItems[0].key
                        )
                    }

                    Log.i("val_driver", i.toString() + " : " + items[i].key + " : " + items[i].name)
                }
            }
        }

    }

//    private fun getPanchayatList() {
//        val rResponse = realm!!.where<OTPResponseResults>().findFirst()
//        if (rResponse != null) {
//            val blockId = rResponse.blockid
//            val id = rResponse._id
//            APIPresenter(this, object : APIView() {
//                override fun onSuccess() {
//                    setPanchayatItems()
//                }
//
//                override fun onException() {
//
//                }
//
//            }).getPanjayatList(blockId.toString(), id.toString())
//        } else {
//            Log.e("empty", "empty")
//        }
//
//    }

//    private fun setPanchayatItems() {
//
//        val panchayatList = ArrayList<KeyPairModel>()
//        val query1 = realm!!.where<PanjayatResponseResults>().findAll()
//        for (k in 0 until query1.size) {
//
//            if (k == 0) {
//                panchayatList!!.add(
//                    KeyPairModel(
//                        (query1[k]?.panjayatid),
//                        query1[k]?.panjayatname,
//                        true
//                    )
//                )
//            } else {
//                panchayatList!!.add(
//                    KeyPairModel(
//                        (query1[k]?.panjayatid),
//                        query1[k]?.panjayatname,
//                        false
//                    )
//                )
//            }
//
//
//        }
//        val panchayatArray = ArrayList<KeyPairBoolData>()
//        for (k in panchayatList!!.indices) {
//            val h = KeyPairBoolData()
//            h.id = (k + 1).toLong()
//            h.name = panchayatList[k].value
//            h.key = panchayatList[k].key
//            h.isSelected = panchayatList[k].aBoolean
//            panchayatArray.add(h)
//        }
//        mPanchayat!!.setItems(panchayatArray, -1) { items: List<KeyPairBoolData> ->
//            for (i in items.indices) {
//                if (items[i].isSelected) {
//                    Log.i("val_driver panchayat", i.toString() + " : " + items[i].key + " : " + items[i].name)
//                    Utils.showProgress(this)
//                    if (finyear.selectedItems.size > 0 && schemeid != null) {
//                        getBeneficiaryList(
//                            finyear.selectedItems[0].key,
//                            schemeid!!,
////                            mPanchayat.selectedItems[0].key
//                        )
//                    }
//                }
//            }
//        }
//
//        if (finyear.selectedItems.size > 0 && schemeid != null) {
//            getBeneficiaryList(
//                finyear.selectedItems[0].key,
//                schemeid!!,
////                mPanchayat.selectedItems[0].key
//            )
//        }
//
//    }



    private fun getYearList() {

        APIPresenter(this, object : APIView() {
            override fun onSuccess() {
                setYearItems()
//                getPanchayatList()

            }

            override fun onException() {

            }

        }).getYearList()


    }

    private fun setYearItems() {

        val arrayList = ArrayList<KeyPairModel>()
        val query1 = realm!!.where<YearResponseResults>().findAll()
        for (k in 0 until query1.size) {
            if (k == 0) {
                arrayList!!.add(
                    KeyPairModel(
                        (query1[k]?._id),
                        query1[k]?.yearname,
                        true
                    )
                )
            } else {
                arrayList!!.add(
                    KeyPairModel(
                        (query1[k]?._id),
                        query1[k]?.yearname,
                        false
                    )
                )
            }


        }
        val arrayList1 = ArrayList<KeyPairBoolData>()
        for (k in arrayList!!.indices) {
            val h = KeyPairBoolData()
            h.id = (k + 1).toLong()
            h.name = arrayList[k].value
            h.key = arrayList[k].key
            h.isSelected = arrayList[k].aBoolean
            arrayList1.add(h)
        }
        finyear!!.setItems(arrayList1, -1) { items: List<KeyPairBoolData> ->
            for (i in items.indices) {
                if (items[i].isSelected) {

                    if (finyear.selectedItems.size > 0 && schemeid != null) {
                        getBeneficiaryList(
                            finyear.selectedItems[0].key,
                            schemeid!!,
//                            mPanchayat.selectedItems[0].key
                        )
                    }

                    Log.i("val_driver finyear", i.toString() + " : " + items[i].key + " : " + items[i].name)
                }
            }
        }

        if (finyear.selectedItems.size > 0 && schemeid != null) {
            getBeneficiaryList(
                finyear.selectedItems[0].key,
                schemeid!!,
//                mPanchayat.selectedItems[0].key
            )
        }

    }


    override fun onResume() {
        super.onResume()
        // getSchemaList()
    }
}