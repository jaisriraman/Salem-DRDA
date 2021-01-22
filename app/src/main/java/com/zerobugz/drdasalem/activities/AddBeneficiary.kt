package com.zerobugz.drdasalem.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.zerobugz.drdasalem.R
import com.zerobugz.drdasalem.api.presenter.APIPresenter
import com.zerobugz.drdasalem.api.presenter.APIView
import com.zerobugz.drdasalem.model.HabitentResponseResults
import com.zerobugz.drdasalem.model.OTPResponseResults
import com.zerobugz.drdasalem.model.PanjayatResponseResults
import com.zerobugz.drdasalem.model.YearResponseResults
import com.zerobugz.drdasalem.model.request.BeneficiaryRequest
import com.zerobugz.drdasalem.utils.Spinner.KeyPairBoolData
import com.zerobugz.drdasalem.utils.Spinner.KeyPairModel
import com.zerobugz.drdasalem.utils.Toaster
import com.zerobugz.drdasalem.utils.Utils
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_add_beneficiary.*

class AddBeneficiary : AppCompatActivity() {
    private var schemeid: String? = null
    private var realm: Realm? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_beneficiary)
        if (intent != null && intent.extras != null) {
            schemeid = intent.getStringExtra("schemeid")
        }
        realm = try {
            Realm.getDefaultInstance()
        } catch (e: Exception) {
            val realmConfiguration = RealmConfiguration.Builder().build()
            Realm.setDefaultConfiguration(realmConfiguration)
            Realm.getDefaultInstance()
        }
        ivBack.setOnClickListener {
            onBackPressed()
        }
        getPanchayatList()
        getYearList()

        btnSave.setOnClickListener {

            val rResponse = realm!!.where<OTPResponseResults>().findFirst()


            if (rResponse?._id != null &&
                rResponse.blockid != null &&
                schemeid != null && edFirstName.length() > 0 &&
                edLastName.length() > 0 &&
                mPanchayat.selectedItems.size > 0 &&
                finyear.selectedItems.size > 0 &&
                mHabitant.selectedItems.size > 0
            ) {
                val beneficiaryRequest = BeneficiaryRequest()
                beneficiaryRequest.firstname = edFirstName.text.toString()
                beneficiaryRequest.lastname = edLastName.text.toString()
                beneficiaryRequest.remarks = remarks.text.toString()
                beneficiaryRequest.userid = rResponse!!._id.toString()
                beneficiaryRequest.schemeid = schemeid.toString()
                beneficiaryRequest.blockid = rResponse!!.blockid.toString()
                beneficiaryRequest.panjayatid = mPanchayat.selectedItems[0].key
                beneficiaryRequest.habitantid = mHabitant.selectedItems[0].key
                beneficiaryRequest.yearid = finyear.selectedItems[0].key


                APIPresenter(this, object : APIView() {
                    override fun onSuccess() {
                        Utils.hideProgress()
                        finish()
                    }

                    override fun onException() {
                        Utils.hideProgress()

                    }

                }).addBeneficiary(beneficiaryRequest)


            } else {
                Toaster.showShort(this, "Enter Required fields")
            }


        }
    }

    private fun getPanchayatList() {
        mHabitant.isEnabled = false

        val rResponse = realm!!.where<OTPResponseResults>().findFirst()

        if (rResponse != null) {
            Utils.showProgress(this)
            val blockId = rResponse.blockid
            val id = rResponse._id
            APIPresenter(this, object : APIView() {
                override fun onSuccess() {
                    Utils.hideProgress()
                    setPanchayatItems()
                }

                override fun onException() {
                    Utils.hideProgress()

                }

            }).getPanjayatList(blockId.toString(), id.toString())
        } else {
            Log.e("empty", "empty")
        }


    }

    private fun geHabitentList(key: String) {

        val rResponse = realm!!.where<OTPResponseResults>().findFirst()

        if (rResponse != null) {
            Utils.showProgress(this)
            val blockId = rResponse.blockid
            APIPresenter(this, object : APIView() {
                override fun onSuccess() {
                    Utils.hideProgress()
                    setHabinetItems()
                }

                override fun onException() {
                    Utils.hideProgress()

                }

            }).getHabitentList(blockId.toString(), key)
        } else {
            Log.e("empty", "empty")
        }


    }


    private fun getYearList() {

            APIPresenter(this, object : APIView() {
                override fun onSuccess() {
                    Utils.hideProgress()
                    setYearItems()
                }

                override fun onException() {
                    Utils.hideProgress()

                }

            }).getYearList()

    }


    private fun setPanchayatItems() {

        val panchayatList = ArrayList<KeyPairModel>()
        val query1 = realm!!.where<PanjayatResponseResults>().findAll()
        for (k in 0 until query1.size) {
            panchayatList!!.add(
                KeyPairModel(
                    (query1[k]?.panjayatid),
                    query1[k]?.panjayatname,
                    false
                )
            )
        }
        val panchayatArray = ArrayList<KeyPairBoolData>()
        for (k in panchayatList!!.indices) {
            val h = KeyPairBoolData()
            h.id = (k + 1).toLong()
            h.name = panchayatList[k].value
            h.key = panchayatList[k].key
            h.isSelected = false
            panchayatArray.add(h)
        }
        mPanchayat!!.setItems(panchayatArray, -1) { items: List<KeyPairBoolData> ->
            for (i in items.indices) {
                if (items[i].isSelected) {
                    Log.i("val_driver", i.toString() + " : " + items[i].key + " : " + items[i].name)
                    geHabitentList(items[i].key)
                }
            }
        }


    }

    private fun setHabinetItems() {

        val habinetList = ArrayList<KeyPairModel>()
        val query1 = realm!!.where<HabitentResponseResults>().findAll()
        for (k in 0 until query1.size) {
            habinetList!!.add(
                KeyPairModel(
                    (query1[k]?._id),
                    query1[k]?.habitationname,
                    false
                )
            )
        }
        val habinetArray = ArrayList<KeyPairBoolData>()
        for (k in habinetList!!.indices) {
            val h = KeyPairBoolData()
            h.id = (k + 1).toLong()
            h.name = habinetList[k].value
            h.key = habinetList[k].key
            h.isSelected = false
            habinetArray.add(h)
        }
        mHabitant!!.setItems(habinetArray, -1) { items: List<KeyPairBoolData> ->
            for (i in items.indices) {
                if (items[i].isSelected) {
                    Log.i("val_driver", i.toString() + " : " + items[i].key + " : " + items[i].name)
                }
            }
        }
        mHabitant.isEnabled = true


    }



    private fun setYearItems() {

        val yearList = ArrayList<KeyPairModel>()
        val query1 = realm!!.where<YearResponseResults>().findAll()
        for (k in 0 until query1.size) {
            yearList!!.add(
                KeyPairModel(
                    (query1[k]?._id),
                    query1[k]?.yearname,
                    false
                )
            )
        }
        val yearArray = ArrayList<KeyPairBoolData>()
        for (k in yearList!!.indices) {
            val h = KeyPairBoolData()
            h.id = (k + 1).toLong()
            h.name = yearList[k].value
            h.key = yearList[k].key
            h.isSelected = false
            yearArray.add(h)
        }
        finyear!!.setItems(yearArray, -1) { items: List<KeyPairBoolData> ->
            for (i in items.indices) {
                if (items[i].isSelected) {
                    Log.i("val_driver", i.toString() + " : " + items[i].key + " : " + items[i].name)
                }
            }
        }


    }





}