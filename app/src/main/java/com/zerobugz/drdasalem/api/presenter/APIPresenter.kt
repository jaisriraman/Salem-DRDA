package com.zerobugz.drdasalem.api.presenter

import android.app.Activity
import com.zerobugz.drdasalem.api.RetrofitClient
import com.zerobugz.drdasalem.model.*
import com.zerobugz.drdasalem.model.request.BeneficiaryRequest
import com.zerobugz.drdasalem.model.request.LoginRequest
import com.zerobugz.drdasalem.model.request.TotalBeneficiaryRequest
import com.zerobugz.drdasalem.model.request.WorkOrderRequest
import com.zerobugz.drdasalem.utils.Toaster
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class APIPresenter {
    private lateinit var apiView: APIView
    private lateinit var getActivity: Activity

    constructor(activity: Activity, apiView: APIView) {
        getActivity = activity
        this.apiView = apiView
    }

    fun userLogin(loginRequest: LoginRequest) {

        val call: Call<LoginResponse?>? = RetrofitClient
            .instance?.api?.userLogin(loginRequest)

        call?.enqueue(object : Callback<LoginResponse?> {
            override fun onResponse(
                call: Call<LoginResponse?>,
                response: Response<LoginResponse?>
            ) {
                val loginResponse: LoginResponse? = response.body()
                if (loginResponse?.status != 0) {
                    Realm.init(getActivity)
                    Realm.getDefaultInstance().use { realmT ->
                        val results: RealmResults<LoginResponse> =
                            realmT.where<LoginResponse>().findAll()
                        realmT.executeTransaction { results.deleteAllFromRealm() }
                        realmT.executeTransaction { realm: Realm ->
                            realm.insertOrUpdate(
                                loginResponse
                            )
                        }
                    }

                    apiView.onSuccess()
                } else {
                    apiView.onException()
                    Toaster.showShort(getActivity, loginResponse.message)

                }
            }

            override fun onFailure(call: Call<LoginResponse?>, t: Throwable) {
                apiView.onException()
            }
        })
    }

    fun resendOTP(loginRequest: LoginRequest) {

        val call: Call<LoginResponse?>? = RetrofitClient
            .instance?.api?.resendOTP(loginRequest)

        call?.enqueue(object : Callback<LoginResponse?> {
            override fun onResponse(
                call: Call<LoginResponse?>,
                response: Response<LoginResponse?>
            ) {
                val loginResponse: LoginResponse? = response.body()
                if (loginResponse?.status != 0) {
                    Toaster.showShort(getActivity, loginResponse!!.message)
                    apiView.onSuccess()
                } else {
                    apiView.onException()
                    Toaster.showShort(getActivity, loginResponse.message)

                }
            }

            override fun onFailure(call: Call<LoginResponse?>, t: Throwable) {
                apiView.onException()
            }
        })
    }


    fun otpVerification(loginRequest: LoginRequest) {

        val call: Call<OTPResponse?>? = RetrofitClient
            .instance?.api?.otpVerification(loginRequest)

        call?.enqueue(object : Callback<OTPResponse?> {
            override fun onResponse(
                call: Call<OTPResponse?>,
                response: Response<OTPResponse?>
            ) {
                val otpResponse: OTPResponse? = response.body()

                if (otpResponse?.status != 0) {
                    Realm.init(getActivity)
                    Realm.getDefaultInstance().use { realmT ->
                        val results: RealmResults<OTPResponse> =
                            realmT.where<OTPResponse>().findAll()
                        val results1: RealmResults<OTPResponseResults> =
                            realmT.where<OTPResponseResults>().findAll()
                        val results2: RealmResults<SchemeResults> =
                            realmT.where<SchemeResults>().findAll()
                        realmT.executeTransaction { results.deleteAllFromRealm() }
                        realmT.executeTransaction { results1.deleteAllFromRealm() }
                        realmT.executeTransaction { results2.deleteAllFromRealm() }
                        realmT.executeTransaction { realm: Realm ->
                            realm.insertOrUpdate(
                                otpResponse
                            )
                        }
                    }
                    apiView.onSuccess()
                } else {
                    apiView.onException()
                    Toaster.showShort(getActivity, otpResponse.message)
                }
            }

            override fun onFailure(call: Call<OTPResponse?>, t: Throwable) {
                apiView.onException()
            }
        })
    }


    fun getPanjayatList(blockid: String, id: String) {

        val call: Call<PanjayatResponse?>? = RetrofitClient
            .instance?.api?.getPanjayatlist(blockid, id)

        call?.enqueue(object : Callback<PanjayatResponse?> {
            override fun onResponse(
                call: Call<PanjayatResponse?>,
                response: Response<PanjayatResponse?>
            ) {
                val panjayatResponse: PanjayatResponse? = response.body()

                if (panjayatResponse?.status != 0) {
                    Realm.init(getActivity)
                    Realm.getDefaultInstance().use { realmT ->
                        val results: RealmResults<PanjayatResponse> =
                            realmT.where<PanjayatResponse>().findAll()
                        val results1: RealmResults<PanjayatResponseResults> =
                            realmT.where<PanjayatResponseResults>().findAll()
                        realmT.executeTransaction { results.deleteAllFromRealm() }
                        realmT.executeTransaction { results1.deleteAllFromRealm() }
                        realmT.executeTransaction { realm: Realm ->
                            realm.insertOrUpdate(
                                panjayatResponse
                            )
                        }
                    }
                    apiView.onSuccess()
                } else {
                    apiView.onException()
                    Toaster.showShort(getActivity, panjayatResponse.message)
                }
            }

            override fun onFailure(call: Call<PanjayatResponse?>, t: Throwable) {
                apiView.onException()
                println("=================================" + t.stackTrace)
            }
        })
    }


    fun getSchemeList() {

        val call: Call<SchemeResponse?>? = RetrofitClient
            .instance?.api?.getSchemeList()

        call?.enqueue(object : Callback<SchemeResponse?> {
            override fun onResponse(
                call: Call<SchemeResponse?>,
                response: Response<SchemeResponse?>
            ) {
                val schemeResponse: SchemeResponse? = response.body()

                if (schemeResponse?.status != 0) {
                    Realm.init(getActivity)
                    Realm.getDefaultInstance().use { realmT ->
                        val results: RealmResults<SchemeResponse> =
                            realmT.where<SchemeResponse>().findAll()
                        val results1: RealmResults<SchemeResponseResults> =
                            realmT.where<SchemeResponseResults>().findAll()
                        realmT.executeTransaction { results.deleteAllFromRealm() }
                        realmT.executeTransaction { results1.deleteAllFromRealm() }
                        realmT.executeTransaction { realm: Realm ->
                            realm.insertOrUpdate(
                                schemeResponse
                            )
                        }
                    }
                    apiView.onSuccess()
                } else {
                    apiView.onException()
                    Toaster.showShort(getActivity, schemeResponse.message)
                }
            }

            override fun onFailure(call: Call<SchemeResponse?>, t: Throwable) {
                apiView.onException()
            }
        })
    }


    fun getBlockList() {

        val call: Call<BlockResponse?>? = RetrofitClient
            .instance?.api?.getBlockList()

        call?.enqueue(object : Callback<BlockResponse?> {
            override fun onResponse(
                call: Call<BlockResponse?>,
                response: Response<BlockResponse?>
            ) {
                val blockResponse: BlockResponse? = response.body()

                if (blockResponse?.status != 1) {
                    Realm.init(getActivity)
                    Realm.getDefaultInstance().use { realmT ->
                        val results: RealmResults<BlockResponse> =
                            realmT.where<BlockResponse>().findAll()
                        val results1: RealmResults<BlockResponseResults> =
                            realmT.where<BlockResponseResults>().findAll()
                        realmT.executeTransaction { results.deleteAllFromRealm() }
                        realmT.executeTransaction { results1.deleteAllFromRealm() }
                        realmT.executeTransaction { realm: Realm ->
                            realm.insertOrUpdate(
                                blockResponse
                            )
                        }
                    }
                    apiView.onSuccess()
                } else {
                    apiView.onException()
                    Toaster.showShort(getActivity, blockResponse.message)
                }
            }

            override fun onFailure(call: Call<BlockResponse?>, t: Throwable) {
                apiView.onException()
            }
        })
    }



    fun getHabitentList(blockid: String, panjayatid: String) {

        val call: Call<HabitantResponse?>? = RetrofitClient
            .instance?.api?.getHabitantlist(blockid, panjayatid)

        call?.enqueue(object : Callback<HabitantResponse?> {
            override fun onResponse(
                call: Call<HabitantResponse?>,
                response: Response<HabitantResponse?>
            ) {
                val habitantResponse: HabitantResponse? = response.body()

                if (habitantResponse?.status != 0) {
                    Realm.init(getActivity)
                    Realm.getDefaultInstance().use { realmT ->
                        val results: RealmResults<HabitantResponse> =
                            realmT.where<HabitantResponse>().findAll()
                        val results1: RealmResults<HabitentResponseResults> =
                            realmT.where<HabitentResponseResults>().findAll()
                        realmT.executeTransaction { results.deleteAllFromRealm() }
                        realmT.executeTransaction { results1.deleteAllFromRealm() }
                        realmT.executeTransaction { realm: Realm ->
                            realm.insertOrUpdate(
                                habitantResponse
                            )
                        }
                    }
                    apiView.onSuccess()
                } else {
                    apiView.onException()
                    Toaster.showShort(getActivity, habitantResponse.message)
                }
            }

            override fun onFailure(call: Call<HabitantResponse?>, t: Throwable) {
                apiView.onException()
            }
        })
    }


    fun addBeneficiary(beneficiaryRequest: BeneficiaryRequest) {

        val call: Call<BeneficiaryResponse?>? = RetrofitClient
            .instance?.api?.addBeneficiary(beneficiaryRequest)

        call?.enqueue(object : Callback<BeneficiaryResponse?> {
            override fun onResponse(
                call: Call<BeneficiaryResponse?>,
                response: Response<BeneficiaryResponse?>
            ) {
                val beneficiaryResponse: BeneficiaryResponse? = response.body()
                if (beneficiaryResponse?.status != 0) {
                    Toaster.showShort(getActivity, beneficiaryResponse!!.message)
                    apiView.onSuccess()
                } else {
                    apiView.onException()
                    Toaster.showShort(getActivity, beneficiaryResponse.message)
                }
            }

            override fun onFailure(call: Call<BeneficiaryResponse?>, t: Throwable) {
                apiView.onException()
            }
        })
    }

    fun getBeneficiaryList(beneficiaryRequest: BeneficiaryRequest) {

        val call: Call<BeneficiaryListResponse?>? = RetrofitClient
            .instance?.api?.getBeneficiaryList(beneficiaryRequest)

        call?.enqueue(object : Callback<BeneficiaryListResponse?> {
            override fun onResponse(
                call: Call<BeneficiaryListResponse?>,
                response: Response<BeneficiaryListResponse?>
            ) {
                val beneficiaryResponse: BeneficiaryListResponse? = response.body()
                if (beneficiaryResponse?.status != 0) {

                    Realm.init(getActivity)
                    Realm.getDefaultInstance().use { realmT ->
                        val results: RealmResults<BeneficiaryListResponse> =
                            realmT.where<BeneficiaryListResponse>().findAll()
                        val results1: RealmResults<BeneficiaryListResponseResults> =
                            realmT.where<BeneficiaryListResponseResults>().findAll()
                        realmT.executeTransaction { results.deleteAllFromRealm() }
                        realmT.executeTransaction { results1.deleteAllFromRealm() }
                        realmT.executeTransaction { realm: Realm ->
                            realm.insertOrUpdate(
                                beneficiaryResponse
                            )
                        }
                    }
                    //    Toaster.showShort(getActivity, beneficiaryResponse!!.message)
                    apiView.onSuccess()
                } else {
                    apiView.onException()
                    Toaster.showShort(getActivity, beneficiaryResponse.message)
                }
            }

            override fun onFailure(call: Call<BeneficiaryListResponse?>, t: Throwable) {
                apiView.onException()
            }
        })
    }

    fun addWorkOrder(request: WorkOrderRequest) {

        val call: Call<WorkOrderResponse?>? = RetrofitClient
            .instance?.api?.addWorkDate(request)

        call?.enqueue(object : Callback<WorkOrderResponse?> {
            override fun onResponse(
                call: Call<WorkOrderResponse?>,
                response: Response<WorkOrderResponse?>
            ) {
                val workOrderResponse: WorkOrderResponse? = response.body()
                if (workOrderResponse?.status != 0) {
                    Toaster.showShort(getActivity, workOrderResponse!!.message)
                    apiView.onSuccess()
                } else {
                    apiView.onException()
                    Toaster.showShort(getActivity, workOrderResponse.message)
                }
            }

            override fun onFailure(call: Call<WorkOrderResponse?>, t: Throwable) {
                apiView.onException()
            }
        })
    }

    fun getWorkStageBeniciaryList(beneficiaryRequest: BeneficiaryRequest) {

        val call: Call<WorkOrderBeneficiaryListResponse?>? = RetrofitClient
            .instance?.api?.workStageList(beneficiaryRequest)

        call?.enqueue(object : Callback<WorkOrderBeneficiaryListResponse?> {
            override fun onResponse(
                call: Call<WorkOrderBeneficiaryListResponse?>,
                response: Response<WorkOrderBeneficiaryListResponse?>
            ) {
                val beneficiaryResponse: WorkOrderBeneficiaryListResponse? = response.body()
                if (beneficiaryResponse?.status != 0) {

                    Realm.init(getActivity)
                    Realm.getDefaultInstance().use { realmT ->
                        val results: RealmResults<WorkOrderBeneficiaryListResponse> =
                            realmT.where<WorkOrderBeneficiaryListResponse>().findAll()
                        val results1: RealmResults<WorkOrderBeneficiaryListResponseResults> =
                            realmT.where<WorkOrderBeneficiaryListResponseResults>().findAll()
                        realmT.executeTransaction { results.deleteAllFromRealm() }
                        realmT.executeTransaction { results1.deleteAllFromRealm() }
                        realmT.executeTransaction { realm: Realm ->
                            realm.insertOrUpdate(
                                beneficiaryResponse
                            )
                        }
                    }
                    //    Toaster.showShort(getActivity, beneficiaryResponse!!.message)
                    apiView.onSuccess()
                } else {
                    apiView.onException()
                    Toaster.showShort(getActivity, beneficiaryResponse.message)
                }
            }

            override fun onFailure(call: Call<WorkOrderBeneficiaryListResponse?>, t: Throwable) {
                apiView.onException()
            }
        })
    }

    fun getWorkStageList(beneficiaryRequest: BeneficiaryRequest) {

        val call: Call<WorkStageListResponse?>? = RetrofitClient
            .instance?.api?.workOrderStageWiseList(beneficiaryRequest)

        call?.enqueue(object : Callback<WorkStageListResponse?> {
            override fun onResponse(
                call: Call<WorkStageListResponse?>,
                response: Response<WorkStageListResponse?>
            ) {
                val response1: WorkStageListResponse? = response.body()
                if (response1?.status != 0) {

                    Realm.init(getActivity)
                    Realm.getDefaultInstance().use { realmT ->
                        val results: RealmResults<WorkStageListResponse> =
                            realmT.where<WorkStageListResponse>().findAll()
                        val results1: RealmResults<WorkStageListResponseResults> =
                            realmT.where<WorkStageListResponseResults>().findAll()
                        val results2: RealmResults<StageListResponseResults> =
                            realmT.where<StageListResponseResults>().findAll()
                        val results3: RealmResults<PaymentStagesResults> =
                            realmT.where<PaymentStagesResults>().findAll()

                        realmT.executeTransaction { results.deleteAllFromRealm() }
                        realmT.executeTransaction { results1.deleteAllFromRealm() }
                        realmT.executeTransaction { results2.deleteAllFromRealm() }
                        realmT.executeTransaction { results3.deleteAllFromRealm() }
                        realmT.executeTransaction { realm: Realm ->
                            realm.insertOrUpdate(
                                response1
                            )
                        }
                    }
                    //    Toaster.showShort(getActivity, beneficiaryResponse!!.message)
                    apiView.onSuccess()
                } else {
                    apiView.onException()
                    Toaster.showShort(getActivity, response1.message)
                }
            }

            override fun onFailure(call: Call<WorkStageListResponse?>, t: Throwable) {
                apiView.onException()
            }
        })
    }



    fun getPaymentBeniciaryList(beneficiaryRequest: BeneficiaryRequest) {

        val call: Call<BeneficiaryPaymentListResponse?>? = RetrofitClient
            .instance?.api?.getBeneficiaryPaymentlist(beneficiaryRequest)

        call?.enqueue(object : Callback<BeneficiaryPaymentListResponse?> {
            override fun onResponse(
                call: Call<BeneficiaryPaymentListResponse?>,
                response: Response<BeneficiaryPaymentListResponse?>
            ) {
                val beneficiaryResponse: BeneficiaryPaymentListResponse? = response.body()
                if (beneficiaryResponse?.status != 0) {

                    Realm.init(getActivity)
                    Realm.getDefaultInstance().use { realmT ->
                        val results: RealmResults<BeneficiaryPaymentListResponse> =
                            realmT.where<BeneficiaryPaymentListResponse>().findAll()
                        val results1: RealmResults<BeneficiaryPaymentListResponseResults> =
                            realmT.where<BeneficiaryPaymentListResponseResults>().findAll()
                        realmT.executeTransaction { results.deleteAllFromRealm() }
                        realmT.executeTransaction { results1.deleteAllFromRealm() }
                        realmT.executeTransaction { realm: Realm ->
                            realm.insertOrUpdate(
                                beneficiaryResponse
                            )
                        }
                    }
                    //    Toaster.showShort(getActivity, beneficiaryResponse!!.message)
                    apiView.onSuccess()
                } else {
                    apiView.onException()
                    Toaster.showShort(getActivity, beneficiaryResponse.message)
                }
            }

            override fun onFailure(call: Call<BeneficiaryPaymentListResponse?>, t: Throwable) {
                apiView.onException()
            }
        })
    }


    fun getBeneficiaryWisePaymentList(beneficiaryRequest: BeneficiaryRequest) {

        val call: Call<PaymentStageListResponse?>? = RetrofitClient
            .instance?.api?.getBeneficiaryWisePaymentList(beneficiaryRequest)

        call?.enqueue(object : Callback<PaymentStageListResponse?> {
            override fun onResponse(
                call: Call<PaymentStageListResponse?>,
                response: Response<PaymentStageListResponse?>
            ) {
                val response1: PaymentStageListResponse? = response.body()
                if (response1?.status != 0) {

                    Realm.init(getActivity)
                    Realm.getDefaultInstance().use { realmT ->
                        val results: RealmResults<PaymentStageListResponse> =
                            realmT.where<PaymentStageListResponse>().findAll()
                        val results1: RealmResults<PaymentStageListResponseResults> =
                            realmT.where<PaymentStageListResponseResults>().findAll()
                        val results2: RealmResults<PaymentStagesResults> =
                            realmT.where<PaymentStagesResults>().findAll()
                        realmT.executeTransaction { results.deleteAllFromRealm() }
                        realmT.executeTransaction { results1.deleteAllFromRealm() }
                        realmT.executeTransaction { results2.deleteAllFromRealm() }
                        realmT.executeTransaction { realm: Realm ->
                            realm.insertOrUpdate(
                                response1
                            )
                        }
                    }
                    //    Toaster.showShort(getActivity, beneficiaryResponse!!.message)
                    apiView.onSuccess()
                } else {
                    apiView.onException()
                    Toaster.showShort(getActivity, response1.message)
                }
            }

            override fun onFailure(call: Call<PaymentStageListResponse?>, t: Throwable) {
                apiView.onException()
            }
        })
    }


    fun addBeneficiaryWorkOrderStage(beneficiaryRequest: BeneficiaryRequest) {
        val call: Call<AddBeneficiaryWorkOrderStageResponse?>? = RetrofitClient
            .instance?.api?.addBeneficiaryWorkOrderStage(beneficiaryRequest)
        call?.enqueue(object : Callback<AddBeneficiaryWorkOrderStageResponse?> {
            override fun onResponse(
                call: Call<AddBeneficiaryWorkOrderStageResponse?>,
                response: Response<AddBeneficiaryWorkOrderStageResponse?>
            ) {
                val response1: AddBeneficiaryWorkOrderStageResponse? = response.body()
                if (response1?.status != 0) {

                    Toaster.showShort(getActivity, response1!!.message)
                    apiView.onSuccess()
                } else {
                    apiView.onException()
                    Toaster.showShort(getActivity, response1.message)
                }
            }

            override fun onFailure(
                call: Call<AddBeneficiaryWorkOrderStageResponse?>,
                t: Throwable
            ) {
                apiView.onException()
            }
        })
    }

    fun addBeneficiaryPaymentStage(beneficiaryRequest: BeneficiaryRequest) {
        val call: Call<AddBeneficiaryPaymentStageResponse?>? = RetrofitClient
            .instance?.api?.addBeneficiaryPaymentStage(beneficiaryRequest)
        call?.enqueue(object : Callback<AddBeneficiaryPaymentStageResponse?> {
            override fun onResponse(
                call: Call<AddBeneficiaryPaymentStageResponse?>,
                response: Response<AddBeneficiaryPaymentStageResponse?>
            ) {
                val response1: AddBeneficiaryPaymentStageResponse? = response.body()
                if (response1?.status != 0) {

                    Toaster.showShort(getActivity, response1!!.message)
                    apiView.onSuccess()
                } else {
                    apiView.onException()
                    Toaster.showShort(getActivity, response1.message)
                }
            }

            override fun onFailure(
                call: Call<AddBeneficiaryPaymentStageResponse?>,
                t: Throwable
            ) {
                apiView.onException()
            }
        })
    }

    fun getTotalBeneficiaryList(totalbeneficiaryRequest: TotalBeneficiaryRequest) {

        val call: Call<TotalBeneficiaryResponse?>? = RetrofitClient
            .instance?.api?.getTotalbeneficiary(totalbeneficiaryRequest)

        call?.enqueue(object : Callback<TotalBeneficiaryResponse?> {
            override fun onResponse(
                call: Call<TotalBeneficiaryResponse?>,
                response: Response<TotalBeneficiaryResponse?>
            ) {
                val response1: TotalBeneficiaryResponse? = response.body()

                if (response1?.status != 0) {
                    Realm.init(getActivity)
                    Realm.getDefaultInstance().use { realmT ->
                        val results: RealmResults<TotalBeneficiaryResponse> =
                            realmT.where<TotalBeneficiaryResponse>().findAll()
                        val results1: RealmResults<SchemeWiseCounts> =
                            realmT.where<SchemeWiseCounts>().findAll()
                        realmT.executeTransaction { results.deleteAllFromRealm() }
                        realmT.executeTransaction { results1.deleteAllFromRealm() }
                        realmT.executeTransaction { realm: Realm ->
                            realm.insertOrUpdate(
                                response1
                            )
                        }
                    }
                    apiView.onSuccess()
                } else {
                    apiView.onException()
                    Toaster.showShort(getActivity, response1.message)
                }
            }

            override fun onFailure(call: Call<TotalBeneficiaryResponse?>, t: Throwable) {
                apiView.onException()
            }
        })
    }


    fun getSchmewiseBeneficiary(beneficiaryRequest: BeneficiaryRequest) {

        val call: Call<SchemeWiseBeneficiaryResponse?>? = RetrofitClient
            .instance?.api?.getSchmeWiseBeneficiary(beneficiaryRequest)

        call?.enqueue(object : Callback<SchemeWiseBeneficiaryResponse?> {
            override fun onResponse(
                call: Call<SchemeWiseBeneficiaryResponse?>,
                response: Response<SchemeWiseBeneficiaryResponse?>
            ) {
                val response1: SchemeWiseBeneficiaryResponse? = response.body()
                if (response1?.status != 0) {

                    Realm.init(getActivity)
                    Realm.getDefaultInstance().use { realmT ->
                        val results: RealmResults<SchemeWiseBeneficiaryResponse> =
                            realmT.where<SchemeWiseBeneficiaryResponse>().findAll()
                        val results1: RealmResults<StageWise> =
                            realmT.where<StageWise>().findAll()
                        val results2: RealmResults<PaymentWise> =
                            realmT.where<PaymentWise>().findAll()
                        realmT.executeTransaction { results.deleteAllFromRealm() }
                        realmT.executeTransaction { results1.deleteAllFromRealm() }
                        realmT.executeTransaction { results2.deleteAllFromRealm() }
                        realmT.executeTransaction { realm: Realm ->
                            realm.insertOrUpdate(
                                response1
                            )
                        }
                    }
                    //    Toaster.showShort(getActivity, beneficiaryResponse!!.message)
                    apiView.onSuccess()
                } else {
                    apiView.onException()
                    Toaster.showShort(getActivity, response1.message)
                }
            }

            override fun onFailure(call: Call<SchemeWiseBeneficiaryResponse?>, t: Throwable) {
                apiView.onException()
            }
        })
    }


    fun getStagesWiseBeneficiarylist(beneficiaryRequest: BeneficiaryRequest) {

        val call: Call<StageWiseBeneficiaryListResponse?>? = RetrofitClient
            .instance?.api?.getStagesWiseBeneficiarylist(beneficiaryRequest)

        call?.enqueue(object : Callback<StageWiseBeneficiaryListResponse?> {
            override fun onResponse(
                call: Call<StageWiseBeneficiaryListResponse?>,
                response: Response<StageWiseBeneficiaryListResponse?>
            ) {
                val response1: StageWiseBeneficiaryListResponse? = response.body()
                if (response1?.status != 0) {

                    Realm.init(getActivity)
                    Realm.getDefaultInstance().use { realmT ->
                        val results: RealmResults<StageWiseBeneficiaryListResponse> =
                            realmT.where<StageWiseBeneficiaryListResponse>().findAll()
                        val results1: RealmResults<RStageWiseBeneficiaryList> =
                            realmT.where<RStageWiseBeneficiaryList>().findAll()
                        realmT.executeTransaction { results.deleteAllFromRealm() }
                        realmT.executeTransaction { results1.deleteAllFromRealm() }
                        realmT.executeTransaction { realm: Realm ->
                            realm.insertOrUpdate(
                                response1
                            )
                        }
                    }
                    //    Toaster.showShort(getActivity, beneficiaryResponse!!.message)
                    apiView.onSuccess()
                } else {
                    apiView.onException()
                    Toaster.showShort(getActivity, response1.message)
                }
            }

            override fun onFailure(call: Call<StageWiseBeneficiaryListResponse?>, t: Throwable) {
                apiView.onException()
            }
        })
    }


    fun getBlockWiseCountList(beneficiaryRequest: BeneficiaryRequest) {

        val call: Call<BlockWiseCountListResponse?>? = RetrofitClient
            .instance?.api?.getBlockWiseCountList(beneficiaryRequest)

        call?.enqueue(object : Callback<BlockWiseCountListResponse?> {
            override fun onResponse(
                call: Call<BlockWiseCountListResponse?>,
                response: Response<BlockWiseCountListResponse?>
            ) {
                val response1: BlockWiseCountListResponse? = response.body()
                if (response1?.status == 0) {

                    Realm.init(getActivity)
                    Realm.getDefaultInstance().use { realmT ->
                        val results: RealmResults<BlockWiseCountListResponse> = realmT.where<BlockWiseCountListResponse>().findAll()
                        val results1: RealmResults<BlockWiseListResponse> = realmT.where<BlockWiseListResponse>().findAll()
                        val results2: RealmResults<BlockWiseStageListResponse> = realmT.where<BlockWiseStageListResponse>().findAll()
                        val results3: RealmResults<BlockWiseHeaders> = realmT.where<BlockWiseHeaders>().findAll()
                        realmT.executeTransaction { results.deleteAllFromRealm() }
                        realmT.executeTransaction { results1.deleteAllFromRealm() }
                        realmT.executeTransaction { results2.deleteAllFromRealm() }
                        realmT.executeTransaction { results3.deleteAllFromRealm() }
                        realmT.executeTransaction { realm: Realm ->
                            realm.insertOrUpdate(
                                response1
                            )
                        }
                    }
                    //    Toaster.showShort(getActivity, beneficiaryResponse!!.message)
                    apiView.onSuccess()
                } else {
                    apiView.onException()
                    Toaster.showShort(getActivity, response1?.message)
                }
            }

            override fun onFailure(call: Call<BlockWiseCountListResponse?>, t: Throwable) {
                apiView.onException()
            }
        })
    }

    fun getPanjayatWiseCountlist(beneficiaryRequest: BeneficiaryRequest) {

        val call: Call<PanchayatWiseCountListResponse?>? = RetrofitClient
            .instance?.api?.getPanjayatWiseCountlist(beneficiaryRequest)

        call?.enqueue(object : Callback<PanchayatWiseCountListResponse?> {
            override fun onResponse(
                call: Call<PanchayatWiseCountListResponse?>,
                response: Response<PanchayatWiseCountListResponse?>
            ) {
                val response1: PanchayatWiseCountListResponse? = response.body()
                if (response1?.status == 0) {

                    Realm.init(getActivity)
                    Realm.getDefaultInstance().use { realmT ->
                        val results: RealmResults<PanchayatWiseCountListResponse> =
                            realmT.where<PanchayatWiseCountListResponse>().findAll()
                        val results1: RealmResults<PanchayatWiseListResponse> =
                            realmT.where<PanchayatWiseListResponse>().findAll()
                        val results2: RealmResults<PanchayatWiseStageListResponse> =
                            realmT.where<PanchayatWiseStageListResponse>().findAll()
                        val results3: RealmResults<PanchayatWiseBillListResponse> =
                            realmT.where<PanchayatWiseBillListResponse>().findAll()
                        realmT.executeTransaction { results.deleteAllFromRealm() }
                        realmT.executeTransaction { results1.deleteAllFromRealm() }
                        realmT.executeTransaction { results2.deleteAllFromRealm() }
                        realmT.executeTransaction { results3.deleteAllFromRealm() }
                        realmT.executeTransaction { realm: Realm ->
                            realm.insertOrUpdate(
                                response1
                            )
                        }
                    }
                    //    Toaster.showShort(getActivity, beneficiaryResponse!!.message)
                    apiView.onSuccess()
                } else {
                    apiView.onException()
                    Toaster.showShort(getActivity, response1?.message)
                }
            }

            override fun onFailure(call: Call<PanchayatWiseCountListResponse?>, t: Throwable) {
                apiView.onException()
            }
        })
    }









    fun getYearList() {

        val call: Call<YearResponse?>? = RetrofitClient
            .instance?.api?.getYearList()

        call?.enqueue(object : Callback<YearResponse?> {
            override fun onResponse(
                call: Call<YearResponse?>,
                response: Response<YearResponse?>
            ) {
                val yearResponse: YearResponse? = response.body()

                if (yearResponse?.status != 0) {
                    Realm.init(getActivity)
                    Realm.getDefaultInstance().use { realmT ->
                        val results: RealmResults<YearResponse> =
                            realmT.where<YearResponse>().findAll()
                        val results1: RealmResults<YearResponseResults> =
                            realmT.where<YearResponseResults>().findAll()
                        realmT.executeTransaction { results.deleteAllFromRealm() }
                        realmT.executeTransaction { results1.deleteAllFromRealm() }
                        realmT.executeTransaction { realm: Realm ->
                            realm.insertOrUpdate(
                                yearResponse
                            )
                        }
                    }
                    apiView.onSuccess()
                } else {
                    apiView.onException()
                    Toaster.showShort(getActivity, yearResponse.message)
                }
            }

            override fun onFailure(call: Call<YearResponse?>, t: Throwable) {
                apiView.onException()
            }
        })
    }




    fun getDistrictPanjayatList(blockid: String) {

        val call: Call<PanjayatResponse?>? = RetrofitClient
            .instance?.api?.getDistrictPanjayatList(blockid)

        call?.enqueue(object : Callback<PanjayatResponse?> {
            override fun onResponse(
                call: Call<PanjayatResponse?>,
                response: Response<PanjayatResponse?>
            ) {
                val panjayatResponse: PanjayatResponse? = response.body()

                if (panjayatResponse?.status != 0) {
                    Realm.init(getActivity)
                    Realm.getDefaultInstance().use { realmT ->
                        val results: RealmResults<PanjayatResponse> =
                            realmT.where<PanjayatResponse>().findAll()
                        val results1: RealmResults<PanjayatResponseResults> =
                            realmT.where<PanjayatResponseResults>().findAll()
                        realmT.executeTransaction { results.deleteAllFromRealm() }
                        realmT.executeTransaction { results1.deleteAllFromRealm() }
                        realmT.executeTransaction { realm: Realm ->
                            realm.insertOrUpdate(
                                panjayatResponse
                            )
                        }
                    }
                    apiView.onSuccess()
                } else {
                    apiView.onException()
                    Toaster.showShort(getActivity, panjayatResponse.message)
                }
            }

            override fun onFailure(call: Call<PanjayatResponse?>, t: Throwable) {
                apiView.onException()
                println("=================================" + t.stackTrace)
            }
        })
    }




}