package com.zerobugz.drdasalem.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
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
import kotlinx.android.synthetic.main.activity_panchayat_summary.*
import kotlinx.android.synthetic.main.activity_panchayat_summary.ivBack
import kotlinx.android.synthetic.main.activity_panchayat_summary.mScheme
import kotlinx.android.synthetic.main.activity_panchayat_summary.noDataFound
import kotlinx.android.synthetic.main.activity_panchayat_summary.recyclerView
import kotlinx.android.synthetic.main.activity_panchayat_summary.searchview
import kotlinx.android.synthetic.main.activity_panchayat_summary.swipeRefresh
import kotlinx.android.synthetic.main.activity_panchayat_summary.tvTitle
import kotlinx.android.synthetic.main.bottomsheet_panchayat_recycler_view_item.view.*
import kotlinx.android.synthetic.main.summary_recycler_view_item.view.*
import kotlinx.android.synthetic.main.summary_recycler_view_item.view.llHeader

class PanchayatSummary : AppCompatActivity() {
    private var realm: Realm? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_panchayat_summary)
        realm = try {
            Realm.getDefaultInstance()
        } catch (e: Exception) {
            val realmConfiguration = RealmConfiguration.Builder().build()
            Realm.setDefaultConfiguration(realmConfiguration)
            Realm.getDefaultInstance()
        }
        swipeRefresh.isRefreshing = true
        swipeRefresh.setOnRefreshListener {
//            getSchemaList()
            getYearList()
        }
//        getSchemaList()
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
                return false
            }


        })


        ivBack.setOnClickListener {
            onBackPressed()
        }

    }

    private fun setSearchRecyclerAdapter(newText: String) {
        if (swipeRefresh.isRefreshing) {
            swipeRefresh.isRefreshing = false
        }
        val rResponseResults = realm!!.where<PanchayatWiseListResponse>()
            .contains("panjayatname", newText)
            .findAll()
        if (rResponseResults.size > 0) {
            recyclerView.visibility = View.VISIBLE
            noDataFound.visibility = View.GONE

            recyclerView.update(rResponseResults)
        } else {
            recyclerView.visibility = View.GONE
            noDataFound.visibility = View.VISIBLE
        }
    }

    private fun getBeneficiaryList(schemeId: String, yearid: String) {
        swipeRefresh.isRefreshing = true
        val rResponse = realm!!.where<OTPResponseResults>().findFirst()
        val beneficiaryRequest = BeneficiaryRequest()
        beneficiaryRequest.schemeid = schemeId
        beneficiaryRequest.blockid = rResponse!!.blockid.toString()
        beneficiaryRequest.yearid = yearid

        //   beneficiaryRequest.panjayatid = panjayatId

        APIPresenter(this, object : APIView() {
            override fun onSuccess() {
                swipeRefresh.isRefreshing = false
                setRecyclerAdapter()

            }

            override fun onException() {
                swipeRefresh.isRefreshing = false
            }

        }).getPanjayatWiseCountlist(beneficiaryRequest)
    }

    @SuppressLint("SetTextI18n")
    private fun setRecyclerAdapter() {

        val listResponse = realm!!.where<PanchayatWiseListResponse>().findAll()


        recyclerView.bind(listResponse, R.layout.summary_recycler_view_item) { item ->

            blockName.text = "" + item.panjayatname
            totalBeneficiary.text = "" + item.totalbeneficiary
            llHeader.setOnClickListener {

                if(item.stagelist!!.size>0){
                    val dialog = BottomSheetDialog(this@PanchayatSummary)
                    dialog.setContentView(R.layout.bottomsheetdialog_panchayat_summary)
                    val cancel = dialog.findViewById<ImageView>(R.id.cancelBtn)
                    val nameToolbar = dialog.findViewById<TextView>(R.id.nameToolbar)
                    val recyclerView = dialog.findViewById<RecyclerView>(R.id.bottomSheetRecyclerView)
                    val arrowForward = dialog.findViewById<RelativeLayout>(R.id.arrowForward)

                    recyclerView!!.bind(
                        item.stagelist!!,
                        R.layout.bottomsheet_panchayat_recycler_view_item
                    ) { item ->

                        if (item.stagename != null) {
                            labelName.text = "" + item.stagename
                        } else if (item.paymentname != null) {
                            labelName.text = "" + item.paymentname
                        }


                        if (item.stagecount != null) {
                            labelCount.text = "" + item.stagecount
                        } else if (item.paymentcount != null) {
                            labelCount.text = "" + item.paymentcount
                        }

                        llHeader.setOnClickListener {

                        }

                    }


                    arrowForward!!.setOnClickListener {

                    }


                    dialog.show()



                    nameToolbar!!.text = "" + item.panjayatname

                    cancel!!.setOnClickListener { dialog.dismiss() }

                }



            }

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

                    if (fincyear.selectedItems.size > 0 && mScheme.selectedItems.size > 0 ) {
                        getBeneficiaryList(

                            mScheme.selectedItems[0].key,
                            fincyear.selectedItems[0].key
                        )
                    }

                    Log.i("val_driver", i.toString() + " : " + items[i].key + " : " + items[i].name)
                }
            }
        }
        if (fincyear.selectedItems.size > 0 && mScheme.selectedItems.size > 0 ) {
            getBeneficiaryList(

                mScheme.selectedItems[0].key,
                fincyear.selectedItems[0].key
            )
        }

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
        fincyear!!.setItems(arrayList1, -1) { items: List<KeyPairBoolData> ->
            for (i in items.indices) {
                if (items[i].isSelected) {

                    if (fincyear.selectedItems.size > 0 && mScheme.selectedItems.size > 0 ) {
                        getBeneficiaryList(

                            mScheme.selectedItems[0].key,
                            fincyear.selectedItems[0].key
                        )
                    }

                    Log.i("val_driver", i.toString() + " : " + items[i].key + " : " + items[i].name)
                }
            }
        }


    }


    override fun onResume() {
        super.onResume()
        getYearList()
    }

}