package com.zerobugz.drdasalem.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zerobugz.drdasalem.R
import com.zerobugz.drdasalem.api.presenter.APIPresenter
import com.zerobugz.drdasalem.api.presenter.APIView
import com.zerobugz.drdasalem.model.BeneficiaryPaymentListResponseResults
import com.zerobugz.drdasalem.model.PaymentStagesResults
import com.zerobugz.drdasalem.model.WorkStageListResponseResults
import com.zerobugz.drdasalem.model.request.BeneficiaryRequest
import com.zerobugz.drdasalem.model.request.StagesRequest
import com.zerobugz.drdasalem.utils.Toaster
import com.zerobugz.drdasalem.utils.Utils
import com.zerobugz.drdasalem.utils.bind
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.Sort
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_add_work_order.ivBack
import kotlinx.android.synthetic.main.activity_add_work_order.mBenificiaryName
import kotlinx.android.synthetic.main.activity_add_work_order.mBlock
import kotlinx.android.synthetic.main.activity_add_work_order.mHabitant
import kotlinx.android.synthetic.main.activity_add_work_order.mPanchayat
import kotlinx.android.synthetic.main.activity_add_work_order.mScheme
import kotlinx.android.synthetic.main.activity_add_work_stage.*
import kotlinx.android.synthetic.main.workstage_recycler_view_item.view.*

class AddBilling : AppCompatActivity() {
    private var realm: Realm? = null
    private var beneficiaryId: String? = null
    private var edit: Boolean = false
    val valueChecked = ArrayList<StagesRequest>()
    val hashMap = HashMap<String, StagesRequest>()
    private var stageId: String? = null
    private var paymentId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_billing)
        realm = try {
            Realm.getDefaultInstance()
        } catch (e: Exception) {
            val realmConfiguration = RealmConfiguration.Builder().build()
            Realm.setDefaultConfiguration(realmConfiguration)
            Realm.getDefaultInstance()
        }
        ivBack.setOnClickListener {

            val intent = Intent(this@AddBilling, PaymentBeneficiaryList::class.java)
            startActivity(intent)

//            onBackPressed()
        }
        if (intent != null && intent.extras != null) {
            beneficiaryId = intent.getStringExtra("beneficiaryId")
            edit = intent.getBooleanExtra("edit", false)
        }


        Utils.showProgress(this)

        val realmResults =
            realm!!.where<BeneficiaryPaymentListResponseResults>().equalTo("_id", beneficiaryId)
                .findFirst()
        if (realmResults != null) {
            val beneficiaryRequest = BeneficiaryRequest()
            beneficiaryRequest.blockid = realmResults!!.blockid
            beneficiaryRequest.panjayatid = realmResults!!.panjayatid
            beneficiaryRequest.schemeid = realmResults!!.schemeid
            beneficiaryRequest.habitantid = realmResults!!.habitantid
            beneficiaryRequest.beneficiaryid = beneficiaryId

            APIPresenter(this, object : APIView() {
                override fun onSuccess() {
                    Utils.hideProgress()
                    setRecyclerAdapter()
                }

                override fun onException() {
                    Utils.hideProgress()
                }

            }).getWorkStageList(beneficiaryRequest)
        }

        btnSave.setOnClickListener {

            val results =
                realm!!.where<BeneficiaryPaymentListResponseResults>()
                    .equalTo("_id", beneficiaryId)
                    .findFirst()
            if (results != null) {
                val beneficiaryRequest = BeneficiaryRequest()
                beneficiaryRequest.blockid = results!!.blockid
                beneficiaryRequest.panjayatid = results!!.panjayatid
                beneficiaryRequest.schemeid = results!!.schemeid
                beneficiaryRequest.habitantid = results!!.habitantid
                beneficiaryRequest.userid = results!!.userid
                beneficiaryRequest.beneficiaryid = beneficiaryId

                if (stageId != null && stageId!!.isNotEmpty() && paymentId != null && !paymentId.isNullOrEmpty()) {
                    beneficiaryRequest.stageid = stageId
                    beneficiaryRequest.paymentid = paymentId
                    Utils.showProgress(this)
                    APIPresenter(this, object : APIView() {
                        override fun onSuccess() {
                            Utils.hideProgress()

                            val intent = Intent(this@AddBilling, AddBilling::class.java)
                            intent.putExtra("beneficiaryId", beneficiaryId)
                            startActivity(intent)

//                            finish()

                        }

                        override fun onException() {
                            Utils.hideProgress()
                        }

                    }).addBeneficiaryPaymentStage(beneficiaryRequest)

                } else {
                    Toaster.showShort(this, "Select Payment Stage")
                }

            }

        }


    }

    private fun setRecyclerAdapter() {
        val realmResults =
            realm!!.where<WorkStageListResponseResults>().equalTo("_id", beneficiaryId)
                .findFirst()

        if (realmResults != null) {
            mBenificiaryName.text = "" + realmResults.firstname + " " + realmResults.lastname
            mScheme.text = "" + realmResults.schemename
            mHabitant.text = "" + realmResults.habitationname
            mPanchayat.text = "" + realmResults.panjayatname
            mBlock.text = "" + realmResults.blockname

        }

        val realmResultsAll =
            realm!!.where<PaymentStagesResults>().sort("paymentorder", Sort.ASCENDING)
                .findAll()


        recyclerView.bind(realmResultsAll, R.layout.workstage_recycler_view_item) { item ->
            checkbox.text = "" + item.paymentname
            checkbox.isChecked = item.paymentstages
            checkbox.isClickable = item.clickablestatus


            checkbox.setOnCheckedChangeListener { _, isChecked ->

                if (isChecked) {
                    stageId = item.stageid
                    paymentId = item._id
                }
            }

        }
    }
}