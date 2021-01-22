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
import com.zerobugz.drdasalem.utils.bind
import com.zerobugz.drdasalem.utils.update
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_work_stage_list.finyear
import kotlinx.android.synthetic.main.activity_work_stage_list.ivBack
import kotlinx.android.synthetic.main.activity_work_stage_list.mPanchayat
import kotlinx.android.synthetic.main.activity_work_stage_list.mScheme
import kotlinx.android.synthetic.main.activity_work_stage_list.noDataFound
import kotlinx.android.synthetic.main.activity_work_stage_list.recyclerView
import kotlinx.android.synthetic.main.activity_work_stage_list.searchview
import kotlinx.android.synthetic.main.activity_work_stage_list.swipeRefresh
import kotlinx.android.synthetic.main.activity_work_stage_list.tvTitle
import kotlinx.android.synthetic.main.workorder_recycler_view_item.view.*

class WorkStageList : AppCompatActivity() {
    private var realm: Realm? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work_stage_list)
        realm = try {
            Realm.getDefaultInstance()
        } catch (e: Exception) {
            val realmConfiguration = RealmConfiguration.Builder().build()
            Realm.setDefaultConfiguration(realmConfiguration)
            Realm.getDefaultInstance()
        }
//        swipeRefresh.isRefreshing = true
        swipeRefresh.setOnRefreshListener {
            getYearList()
        }
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
            val intent = Intent(this@WorkStageList, MainActivity::class.java)
            startActivity(intent)
//            onBackPressed()
        }

    }

    private fun getBeneficiaryList(yearid: String, schemeId: String, panjayatId: String) {
//        swipeRefresh.isRefreshing = true
        val rResponse = realm!!.where<OTPResponseResults>().findFirst()
        val beneficiaryRequest = BeneficiaryRequest()
        beneficiaryRequest.schemeid = schemeId
        beneficiaryRequest.blockid = rResponse!!.blockid.toString()
        beneficiaryRequest.panjayatid = panjayatId
        beneficiaryRequest.yearid = yearid

        APIPresenter(this, object : APIView() {
            override fun onSuccess() {
                setRecyclerAdapter()

            }

            override fun onException() {
                swipeRefresh.isRefreshing = false
            }

        }).getWorkStageBeniciaryList(beneficiaryRequest)
    }

    private fun setRecyclerAdapter() {
        if (swipeRefresh.isRefreshing) {
            swipeRefresh.isRefreshing = false
        }
        val rResponseResults = realm!!.where<WorkOrderBeneficiaryListResponseResults>().findAll()
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


            recyclerView.bind(realmResults, R.layout.workorder_recycler_view_item) {

                    item ->
                mSno.text = "" + item.sNo
                mBenificiaryName.text = "" + item.firstname + " " + item.lastname
                mHabitantName.text = "" + item.habitationname


                if (item.workorderdate != null && item.workorderdate!!.isNotEmpty()) {
                    mAction.text = "Update"
                    mAction.setTextColor(resources.getColor(R.color.QIBus_softui_red))

                    mAction.setOnClickListener {

                        val intent = Intent(this@WorkStageList, AddWorkStage::class.java)
                        intent.putExtra("beneficiaryId", item._id)
                        intent.putExtra("edit", true)
                        startActivity(intent)
                    }

                }/* else {
                    mAction.setTextColor(resources.getColor(R.color.QIBus_softui_blue))
                    mAction.text = "Add"
                    mAction.setOnClickListener {
                        val intent = Intent(this@WorkStageList, AddWorkOrder::class.java)
                        intent.putExtra("workOrderId", item._id)
                        intent.putExtra("edit", true)
                        startActivity(intent)
                    }
                }*/

                mHabitantName.text = "" + item.habitationname

            }.layoutManager(LinearLayoutManager(this@WorkStageList))
        } else {
            noDataFound.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        }

    }

    private fun setSearchRecyclerAdapter(text: String) {

        if (swipeRefresh.isRefreshing) {
            swipeRefresh.isRefreshing = false
        }
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
                getPanchayatList()
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

                    if (finyear.selectedItems.size > 0 && mScheme.selectedItems.size > 0 && mPanchayat.selectedItems.size > 0) {
                        getBeneficiaryList(
                            finyear.selectedItems[0].key,
                            mScheme.selectedItems[0].key,
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
                    if (finyear.selectedItems.size > 0 && mScheme.selectedItems.size > 0 && mPanchayat.selectedItems.size > 0) {
                        getBeneficiaryList(
                            finyear.selectedItems[0].key,
                            mScheme.selectedItems[0].key,
                            mPanchayat.selectedItems[0].key
                        )
                    }
                }
            }
        }

//        if (finyear.selectedItems.size > 0 && mScheme.selectedItems.size > 0 && mPanchayat.selectedItems.size > 0) {
//            getBeneficiaryList(finyear.selectedItems[0].key, mScheme.selectedItems[0].key, mPanchayat.selectedItems[0].key)
//        }

    }

    private fun getYearList() {

        APIPresenter(this, object : APIView() {
            override fun onSuccess() {
                setYearItems()
                getSchemaList()
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

                    if (finyear.selectedItems.size > 0 && mScheme.selectedItems.size > 0 && mPanchayat.selectedItems.size > 0) {
                        getBeneficiaryList(
                            finyear.selectedItems[0].key,
                            mScheme.selectedItems[0].key,
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
//        getYearList()
    }
}