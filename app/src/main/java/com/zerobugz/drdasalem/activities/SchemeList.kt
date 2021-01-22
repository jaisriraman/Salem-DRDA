package com.zerobugz.drdasalem.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.zerobugz.drdasalem.R
import com.zerobugz.drdasalem.api.presenter.APIPresenter
import com.zerobugz.drdasalem.api.presenter.APIView
import com.zerobugz.drdasalem.model.SchemeResponse
import com.zerobugz.drdasalem.utils.bind
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_scheme_list.*
import kotlinx.android.synthetic.main.schema_list_item.view.*

class SchemeList : AppCompatActivity() {
    private var realm: Realm? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scheme_list)

        realm = try {
            Realm.getDefaultInstance()
        } catch (e: Exception) {
            val realmConfiguration = RealmConfiguration.Builder().build()
            Realm.setDefaultConfiguration(realmConfiguration)
            Realm.getDefaultInstance()
        }

        swipeRefresh.isRefreshing = true
        getSchemaList()
        swipeRefresh.setOnRefreshListener {
            getSchemaList()
        }
        ivBack.setOnClickListener {
            onBackPressed()
        }


    }

    private fun getSchemaList() {
        APIPresenter(this, object : APIView() {
            override fun onSuccess() {
                swipeRefresh.isRefreshing = false
                val rResponse = realm!!.where<SchemeResponse>().findFirst()

                if (rResponse != null && rResponse.result!!.size > 0) {
                    schemaList.bind(
                        rResponse?.result!!.toList(),
                        R.layout.schema_list_item
                    ) { item ->
                        scheme_name.text = "" + item.schemename
                        childItem.setOnClickListener {
                            val intent = Intent(this@SchemeList, AddBeneficiary::class.java)
                            intent.putExtra("schemeid", item._id)
                            startActivity(intent)
                        }


                    }.layoutManager(LinearLayoutManager(this@SchemeList))
                }


            }

            override fun onException() {
                swipeRefresh.isRefreshing = false
            }

        }).getSchemeList()
    }


}