package com.zerobugz.drdasalem.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.zerobugz.drdasalem.R
import com.zerobugz.drdasalem.api.presenter.APIPresenter
import com.zerobugz.drdasalem.api.presenter.APIView
import com.zerobugz.drdasalem.model.OTPResponseResults
import com.zerobugz.drdasalem.model.SchemeWiseCounts
import com.zerobugz.drdasalem.model.TotalBeneficiaryResponse
import com.zerobugz.drdasalem.model.YearResponseResults
import com.zerobugz.drdasalem.model.request.TotalBeneficiaryRequest
import com.zerobugz.drdasalem.utils.SharedPreference
import com.zerobugz.drdasalem.utils.Spinner.KeyPairBoolData
import com.zerobugz.drdasalem.utils.Spinner.KeyPairModel
import com.zerobugz.drdasalem.utils.Utils
import com.zerobugz.drdasalem.utils.bind
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.exceptions.RealmMigrationNeededException
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.content_main.recyclerView
import kotlinx.android.synthetic.main.dashboard_schemelist.view.*


class MainActivity : AppCompatActivity() {
    private var realm: Realm? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Realm.init(this)
        val realmConfiguration = RealmConfiguration.Builder().build()
        realm = try {
            Realm.getDefaultInstance()
        } catch (e: Exception) {
            try {
                Realm.deleteRealm(realmConfiguration)
                Realm.getInstance(realmConfiguration)
            } catch (r: RealmMigrationNeededException) {
                Realm.deleteRealm(realmConfiguration)
                Realm.getInstance(realmConfiguration)
            }
        }

        setSupportActionBar(findViewById(R.id.toolbar))
        navigation_view.setCheckedItem(R.id.nav_dashboard)
        navigation_view!!.itemIconTintList = null

        navigation_view.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_dashboard -> {
                    drawer.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_add_beneficiary -> {
                    drawer.closeDrawer(GravityCompat.START)
                    Utils.openActivity(this, SchemeList::class.java, false)
                    true
                }

                R.id.nav_add_work_order -> {
                    drawer.closeDrawer(GravityCompat.START)
                    Utils.openActivity(this, WorkOrderList::class.java, false)
                    true
                }
                R.id.nav_add_work_stage -> {
                    drawer.closeDrawer(GravityCompat.START)
                    Utils.openActivity(this, WorkStageList::class.java, false)
                    true
                }
                R.id.nav_add_payment_billing -> {
                    drawer.closeDrawer(GravityCompat.START)
                    Utils.openActivity(this, PaymentBeneficiaryList::class.java, false)
                    true
                }

                R.id.nav_panjayat_summary -> {
                    drawer.closeDrawer(GravityCompat.START)
                    Utils.openActivity(this, PanchayatSummary::class.java, false)
                   // Utils.openActivity(this, PaymentBeneficiaryList::class.java, false)
                    true
                }

                R.id.nav_district_panjayat_summary -> {
                    drawer.closeDrawer(GravityCompat.START)
                    Utils.openActivity(this, DistrictPanchayatSummary::class.java, false)
                    // Utils.openActivity(this, PaymentBeneficiaryList::class.java, false)
                    true
                }

                R.id.nav_block_summary -> {
                    drawer.closeDrawer(GravityCompat.START)
                    Utils.openActivity(this, BlockSummary::class.java, false)
                   // Utils.openActivity(this, PaymentBeneficiaryList::class.java, false)
                    true
                }
            }
            true
        }
        val drawerToggle = ActionBarDrawerToggle(this, drawer, R.string.open, R.string.close)
        drawer.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
        supportActionBar!!.title = "DashBoard"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        getYearList()

        val rResponse = realm!!.where<OTPResponseResults>().findFirst()


        println("USER ID : "+ rResponse!!._id.toString())
        val navigationView = findViewById<View>(R.id.navigation_view) as NavigationView
        val hView = navigationView.getHeaderView(0)
        val username = hView.findViewById<View>(R.id.username) as TextView
        val mobileNumber = hView.findViewById<View>(R.id.mobileNumber) as TextView


        if (rResponse != null) {
            username.text = "" + rResponse!!.name
            mobileNumber.text = "" + rResponse!!.mobile
            Utils.showProgress(this)

            navigation_view.menu.clear()
            navigation_view.inflateMenu(R.menu.activity_main_drawer)
            val navMenu: Menu = navigation_view.menu


            if (rResponse.rolename.equals("District level")) {
                navMenu.findItem(R.id.nav_add_beneficiary).isVisible = false
                navMenu.findItem(R.id.nav_add_work_order).isVisible = false
                navMenu.findItem(R.id.nav_add_work_stage).isVisible = false
                navMenu.findItem(R.id.nav_add_payment_billing).isVisible = false
                navMenu.findItem(R.id.nav_panjayat_summary).isVisible = false
                navMenu.findItem(R.id.nav_district_panjayat_summary).isVisible = true
                navMenu.findItem(R.id.nav_block_summary).isVisible = true

                blockHeader.visibility = View.VISIBLE
                panchayatHeader.visibility = View.VISIBLE

            } else if (rResponse.rolename.equals("Block level")) {
                navMenu.findItem(R.id.nav_add_beneficiary).isVisible = false
                navMenu.findItem(R.id.nav_add_work_order).isVisible = false
                navMenu.findItem(R.id.nav_add_work_stage).isVisible = false
                navMenu.findItem(R.id.nav_add_payment_billing).isVisible = false
                navMenu.findItem(R.id.nav_panjayat_summary).isVisible = true
                navMenu.findItem(R.id.nav_block_summary).isVisible = false
                navMenu.findItem(R.id.nav_district_panjayat_summary).isVisible = false

                blockHeader.visibility = View.GONE
                panchayatHeader.visibility = View.VISIBLE
            } else if (rResponse.rolename.equals("Panjayat level")) {
                blockHeader.visibility = View.GONE
                panchayatHeader.visibility = View.GONE
                if (rResponse.desiginationname.equals("overseas", ignoreCase = true)) {
                    navMenu.findItem(R.id.nav_add_beneficiary).isVisible = true
                    navMenu.findItem(R.id.nav_add_work_order).isVisible = true
                    navMenu.findItem(R.id.nav_add_work_stage).isVisible = true
                    navMenu.findItem(R.id.nav_add_payment_billing).isVisible = false
                    navMenu.findItem(R.id.nav_panjayat_summary).isVisible = true
                    navMenu.findItem(R.id.nav_block_summary).isVisible = false
                    navMenu.findItem(R.id.nav_district_panjayat_summary).isVisible = false //TODO

                } else if (rResponse.desiginationname.equals("Z.Dy.BDO", ignoreCase = true)) {
                    navMenu.findItem(R.id.nav_add_beneficiary).isVisible = false
                    navMenu.findItem(R.id.nav_add_work_order).isVisible = false
                    navMenu.findItem(R.id.nav_add_work_stage).isVisible = false
                    navMenu.findItem(R.id.nav_add_payment_billing).isVisible = true
                    navMenu.findItem(R.id.nav_panjayat_summary).isVisible = true
                    navMenu.findItem(R.id.nav_block_summary).isVisible = false
                    navMenu.findItem(R.id.nav_district_panjayat_summary).isVisible = false //TODO

                }

            }


        } else {
            initialData()
            Utils.openActivity(this, SplashActivity::class.java, true)
        }


    }

    @SuppressLint("SetTextI18n")
    private fun setRecyclerAdapter() {

        val totalBeneficiaryResponse = realm!!.where<TotalBeneficiaryResponse>().findFirst()

        if (totalBeneficiaryResponse != null) {

            /* val responses = realm!!.where<OTPResponseResults>().findFirst()

             if (responses != null) {

                 if (responses.desiginationname.equals("Overseas")) {

                 } else if(responses.desiginationname.equals("Z.Dy.BDO")){

                 }


             }*/


            totalBlockCount.text = "" + totalBeneficiaryResponse!!.totalblock
            totalPanjayatCount.text = "" + totalBeneficiaryResponse!!.totalpanjayat
            totalBeneficiaryCount.text = "" + totalBeneficiaryResponse!!.totalbeneficiarycount
        }

        val schemeWiseCounts = realm!!.where<SchemeWiseCounts>().findAll()
        recyclerView.bind(schemeWiseCounts, R.layout.dashboard_schemelist) { item ->
            schemeCount.text = "" + item.totalbeneficiary
            schemeName.text = "" + item.schemename
            headerLayout.setOnClickListener {
                val intent = Intent(this@MainActivity, SchemeWiseBeneficiaryList::class.java)
                intent.putExtra("schemeId", item.schemeid)
                startActivity(intent)
            }


        }

    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.navigation_drawer, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        return when (item.itemId) {
            android.R.id.home -> {
                drawer.openDrawer(GravityCompat.START)
                true
            }
            R.id.action_logout -> {
                initialData()
                Utils.openActivity(this, SplashActivity::class.java, true)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }



    private fun getYearList() {

        APIPresenter(this, object : APIView() {
            override fun onSuccess() {
                setYearItems()

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

                    if (fincyear.selectedItems.size > 0) {
                        println("inside : zzzz")
                        getTotalBeneficiaryLists(fincyear.selectedItems[0].key)
                    }

                    Log.i("val_driver", i.toString() + " : " + items[i].key + " : " + items[i].name)
                }
            }
        }

        if (fincyear.selectedItems.size > 0) {
            getTotalBeneficiaryLists(fincyear.selectedItems[0].key)
        }


    }

    private fun getTotalBeneficiaryLists(yearid: String) {

        val rResponse = realm!!.where<OTPResponseResults>().findFirst()

        if (rResponse?._id != null) {
            println("inside : zzzz")
            val totalbeneficiaryRequest = TotalBeneficiaryRequest()
            totalbeneficiaryRequest.userid = rResponse!!._id.toString()
            totalbeneficiaryRequest.yearid = yearid

            APIPresenter(this, object : APIView() {
                override fun onSuccess() {
                    Utils.hideProgress()
                    setRecyclerAdapter()
                    println("inside : bbb")

                }

                override fun onException() {
                    Utils.hideProgress()
                }

            }).getTotalBeneficiaryList(totalbeneficiaryRequest)
        }


    }


    override fun onResume() {
        super.onResume()
        navigation_view.setCheckedItem(R.id.nav_dashboard)
    }

    private fun initialData() {
        val sp = SharedPreference()
        sp.clearSharedPreference(this)
        Realm.init(this)
        val realm = Realm.getDefaultInstance()
        try {
            realm.executeTransaction { realm1: Realm -> realm1.deleteAll() }
        } finally {
            realm.close()
        }
    }
}