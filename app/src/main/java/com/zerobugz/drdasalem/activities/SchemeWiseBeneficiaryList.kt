package com.zerobugz.drdasalem.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
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
import kotlinx.android.synthetic.main.activity_scheme_wise_beneficiary.*
import kotlinx.android.synthetic.main.activity_scheme_wise_beneficiary.finyear
import kotlinx.android.synthetic.main.activity_scheme_wise_beneficiary.ivBack
import kotlinx.android.synthetic.main.activity_scheme_wise_beneficiary.mPanchayat
import kotlinx.android.synthetic.main.activity_scheme_wise_beneficiary.mScheme
import kotlinx.android.synthetic.main.activity_scheme_wise_beneficiary.noDataFound
import kotlinx.android.synthetic.main.activity_scheme_wise_beneficiary.recyclerView
import kotlinx.android.synthetic.main.activity_scheme_wise_beneficiary.searchview
import kotlinx.android.synthetic.main.activity_scheme_wise_beneficiary.tvTitle
import kotlinx.android.synthetic.main.paymentwise_recycler_view_item.view.*
import kotlinx.android.synthetic.main.stagewise_recycler_view_item.view.*

class SchemeWiseBeneficiaryList : AppCompatActivity() {
    private var realm: Realm? = null
    var schemeId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scheme_wise_beneficiary)
        realm = try {
            Realm.getDefaultInstance()
        } catch (e: Exception) {
            val realmConfiguration = RealmConfiguration.Builder().build()
            Realm.setDefaultConfiguration(realmConfiguration)
            Realm.getDefaultInstance()
        }
        if (intent != null && intent.extras != null) {
            schemeId = intent.getStringExtra("schemeId")
        }

        Utils.showProgress(this)
//        getPanchayatList()
        getYearList()

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

    private fun getBeneficiaryList(yearid: String, schemeId: String, panjayatId: String) {


        val rResponse = realm!!.where<OTPResponseResults>().findFirst()
        val beneficiaryRequest = BeneficiaryRequest()
        beneficiaryRequest.schemeid = schemeId
        beneficiaryRequest.blockid = rResponse!!.blockid.toString()
        beneficiaryRequest.panjayatid = panjayatId
        beneficiaryRequest.yearid = yearid

        APIPresenter(this, object : APIView() {
            override fun onSuccess() {
                Utils.hideProgress()
                setRecyclerAdapter()

            }

            override fun onException() {
                Utils.hideProgress()
            }

        }).getSchmewiseBeneficiary(beneficiaryRequest)
    }

    private fun setRecyclerAdapter() {
        Utils.hideProgress()
        val rResponse = realm!!.where<SchemeWiseBeneficiaryResponse>().findFirst()
        total_beneficiary_count.text = "" + rResponse?.totalbeneficiarycount

        val rResponseResults = realm!!.where<StageWise>().findAll()
        val rResponseResults1 = realm!!.where<PaymentWise>().findAll()


        recyclerView.bind(rResponseResults, R.layout.stagewise_recycler_view_item) { item ->
            textname.text = "" + item.stagename
            textcount.text = "" + item.stagecount

            llHeader.setOnClickListener {
                if (!item.stageid.isNullOrEmpty()) {
                    val intent =
                        Intent(this@SchemeWiseBeneficiaryList, StageWiseBeneficiaryList::class.java)
                    intent.putExtra("schemeid", schemeId)
                    intent.putExtra("panchayatId", mPanchayat.selectedItems[0].key)
                    intent.putExtra("stageId", item.stageid)
                    startActivity(intent)
                }

            }


        }.layoutManager(LinearLayoutManager(this@SchemeWiseBeneficiaryList))

        //   recyclerView.update(rResponseResults)

        recyclerView1.bind(rResponseResults1, R.layout.paymentwise_recycler_view_item) { item ->
            textname1.text = "" + item.paymentname
            textcount1.text = "" + item.paymentcount

            llHeader1.setOnClickListener {

                if (!item.paymentid.isNullOrEmpty()) {
                    val intent =
                        Intent(this@SchemeWiseBeneficiaryList, StageWiseBeneficiaryList::class.java)
                    intent.putExtra("schemeid", schemeId)
                    intent.putExtra("panchayatId", mPanchayat.selectedItems[0].key)
                    intent.putExtra("stageId", item.stageid)
                    startActivity(intent)
                }


            }


        }.layoutManager(LinearLayoutManager(this@SchemeWiseBeneficiaryList))

        //  recyclerView1.update(rResponseResults1)
        /*   if (rResponseResults.size > 0) {
               recyclerView.visibility = View.VISIBLE
               noDataFound.visibility = View.GONE



           } else {
               noDataFound.visibility = View.VISIBLE
               recyclerView.visibility = View.GONE
           }*/

    }

    private fun setSearchRecyclerAdapter(text: String) {


        val rResponseResults = realm!!.where<WorkOrderBeneficiaryListResponseResults>()
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
                        rResponseResults[value]!!.lastname
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

                    if (finyear.selectedItems.size > 0 && schemeId != null && mPanchayat.selectedItems.size > 0) {
                        getBeneficiaryList(
                            finyear.selectedItems[0].key,
                            schemeId!!,
                            mPanchayat.selectedItems[0].key
                        )
                    }

                    Log.i("val_driver", i.toString() + " : " + items[i].key + " : " + items[i].name)
                }
            }
        }

    }

    private fun getPanchayatList() {
        val rResponse = realm!!.where<OTPResponseResults>().findFirst()
        if (rResponse != null) {
            val blockId = rResponse.blockid
            val id = rResponse._id
            APIPresenter(this, object : APIView() {
                override fun onSuccess() {
                    setPanchayatItems()
                }

                override fun onException() {

                }

            }).getPanjayatList(blockId.toString(), id.toString())
        } else {
            Log.e("empty", "empty")
        }

    }

    private fun setPanchayatItems() {

        val panchayatList = ArrayList<KeyPairModel>()
        val query1 = realm!!.where<PanjayatResponseResults>().findAll()
        for (k in 0 until query1.size) {

            if (k == 0) {
                panchayatList!!.add(
                    KeyPairModel(
                        (query1[k]?.panjayatid),
                        query1[k]?.panjayatname,
                        true
                    )
                )
            } else {
                panchayatList!!.add(
                    KeyPairModel(
                        (query1[k]?.panjayatid),
                        query1[k]?.panjayatname,
                        false
                    )
                )
            }


        }
        val panchayatArray = ArrayList<KeyPairBoolData>()
        for (k in panchayatList!!.indices) {
            val h = KeyPairBoolData()
            h.id = (k + 1).toLong()
            h.name = panchayatList[k].value
            h.key = panchayatList[k].key
            h.isSelected = panchayatList[k].aBoolean
            panchayatArray.add(h)
        }
        mPanchayat!!.setItems(panchayatArray, -1) { items: List<KeyPairBoolData> ->
            for (i in items.indices) {
                if (items[i].isSelected) {
                    Log.i("val_driver", i.toString() + " : " + items[i].key + " : " + items[i].name)
                    Utils.showProgress(this)
                    if (finyear.selectedItems.size > 0 && schemeId != null && mPanchayat.selectedItems.size > 0) {
                        getBeneficiaryList(
                            finyear.selectedItems[0].key,
                            schemeId!!,
                            mPanchayat.selectedItems[0].key
                        )
                    }
                }
            }
        }

        if (finyear.selectedItems.size > 0 && schemeId != null && mPanchayat.selectedItems.size > 0) {
            getBeneficiaryList(
                finyear.selectedItems[0].key,
                schemeId!!,
                mPanchayat.selectedItems[0].key
            )
        }

    }





    private fun getYearList() {

        APIPresenter(this, object : APIView() {
            override fun onSuccess() {
                setYearItems()
                getPanchayatList()
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

                    if (finyear.selectedItems.size > 0 && schemeId != null && mPanchayat.selectedItems.size > 0) {
                        getBeneficiaryList(
                            finyear.selectedItems[0].key,
                            schemeId!!,
                            mPanchayat.selectedItems[0].key
                        )
                    }

                    Log.i("val_driver", i.toString() + " : " + items[i].key + " : " + items[i].name)
                }
            }
        }


    }




    override fun onResume() {
        super.onResume()
        // getPanchayatList()
    }
}