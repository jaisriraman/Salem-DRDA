package com.zerobugz.drdasalem.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zerobugz.drdasalem.R
import com.zerobugz.drdasalem.api.presenter.APIPresenter
import com.zerobugz.drdasalem.api.presenter.APIView
import com.zerobugz.drdasalem.model.LoginResponse
import com.zerobugz.drdasalem.model.request.LoginRequest
import com.zerobugz.drdasalem.utils.SharedPreference
import com.zerobugz.drdasalem.utils.Toaster
import com.zerobugz.drdasalem.utils.Utils
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.exceptions.RealmMigrationNeededException
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private var realm: Realm? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

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
        initialData()
        btnContinue.setOnClickListener {
            if (edMobileNumber.text.toString().trim().length == 10) {

                val loginRequest = LoginRequest()
                loginRequest.mobile = edMobileNumber.text.toString().trim()

                Utils.showProgress(this)
                APIPresenter(this, object : APIView() {
                    override fun onSuccess() {
                        Utils.hideProgress()
                        val rResponse = realm!!.where<LoginResponse>().findFirst()
                        val sp = SharedPreference()
                        sp.putString(this@LoginActivity, "id", rResponse!!.id)
                        sp.putString(this@LoginActivity, "token", rResponse.token)

                        Utils.openActivity(this@LoginActivity, VerifyActivity::class.java, true)
                    }

                    override fun onException() {
                        Utils.hideProgress()
                    }

                }).userLogin(loginRequest)
            } else {
                Toaster.showShort(this, "Enter Valid Mobile Number")
            }

        }


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