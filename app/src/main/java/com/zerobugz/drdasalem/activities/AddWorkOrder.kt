package com.zerobugz.drdasalem.activities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import com.zerobugz.drdasalem.R
import com.zerobugz.drdasalem.api.presenter.APIPresenter
import com.zerobugz.drdasalem.api.presenter.APIView
import com.zerobugz.drdasalem.model.BeneficiaryListResponseResults
import com.zerobugz.drdasalem.model.request.WorkOrderRequest
import com.zerobugz.drdasalem.utils.Toaster
import com.zerobugz.drdasalem.utils.Utils
import com.zerobugz.drdasalem.utils.Utils.dateformat
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_add_work_order.*
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*

class AddWorkOrder : AppCompatActivity() {
    private var workOrderId: String? = null
    private var edit: Boolean = false
    private var realm: Realm? = null
    val myCalendar = Calendar.getInstance()
    private var formattedDate: String? = null
    private var formattedTime: String? = null
    val myFormat = "dd-MM-yyyy hh:mm:a"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_work_order)

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
        if (intent != null && intent.extras != null) {
            workOrderId = intent.getStringExtra("workOrderId")
            edit = intent.getBooleanExtra("edit", false)
        }

        //  val sdf = SimpleDateFormat(myFormat, Locale.US)
        //  mSelectDate.setText(sdf.format(System.currentTimeMillis()))
        val realmResults =
            realm!!.where<BeneficiaryListResponseResults>().equalTo("_id", workOrderId)
                .findFirst()

        if (realmResults != null) {
            mBenificiaryName.text = "" + realmResults.firstname + " " + realmResults.lastname
            mScheme.text = "" + realmResults.schemename
            mHabitant.text = "" + realmResults.habitationname
            mPanchayat.text = "" + realmResults.panjayatname
            mBlock.text = "" + realmResults.blockname

            if (!realmResults.workorderdate.isNullOrEmpty())
                mSelectDate.setText("" + dateformat(realmResults.workorderdate))
        }

        mSelectDate.setOnClickListener {

            selectDate()
        }

        btnSave.setOnClickListener {


            if (mSelectDate.text.isNotEmpty()) {
                Utils.showProgress(this)
                val workOrder = WorkOrderRequest()
                workOrder._id = workOrderId
                val dateValue = Utils.dateutcformat(mSelectDate.text.toString())
                workOrder.workorderdate = dateValue.toString()
                APIPresenter(this, object : APIView() {
                    override fun onSuccess() {
                        Utils.hideProgress()
                        finish()
                    }

                    override fun onException() {
                        Utils.hideProgress()
                    }

                }).addWorkOrder(workOrder)
            } else {
                Toaster.showShort(this, "Select Date")
            }

        }

    }

    private fun selectDate() {
        val mYear: Int = myCalendar.get(Calendar.YEAR)
        val mMonth: Int = myCalendar.get(Calendar.MONTH)
        val mDay: Int = myCalendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { datePicker: DatePicker?, year: Int, monthofYear: Int, dayofMonth: Int ->
                val sdf = SimpleDateFormat("dd-MM-yyyy")
                formattedDate = sdf.format(Date(year - 1900, monthofYear, dayofMonth))
                selectTime()
            },
            mYear,
            mMonth,
            mDay
        )
        // datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    fun selectTime() {
        val mHour: Int = myCalendar.get(Calendar.HOUR_OF_DAY)
        val mMinute: Int = myCalendar.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(
            this,
            TimePickerDialog.OnTimeSetListener { timePicker: TimePicker?, hourOfDay: Int, minute: Int ->
                val mSDF = SimpleDateFormat("hh:mm a")
                formattedTime = mSDF.format(Time(hourOfDay, minute, 0))
                mSelectDate.setText("$formattedDate $formattedTime")
            },
            mHour,
            mMinute,
            false
        )
        timePickerDialog.show()
    }

}