package com.zerobugz.drdasalem.api

import com.zerobugz.drdasalem.model.*
import com.zerobugz.drdasalem.model.request.BeneficiaryRequest
import com.zerobugz.drdasalem.model.request.LoginRequest
import com.zerobugz.drdasalem.model.request.TotalBeneficiaryRequest
import com.zerobugz.drdasalem.model.request.WorkOrderRequest
import retrofit2.Call
import retrofit2.http.*

interface Api {

    @POST("login")
    fun userLogin(
        @Body loginRequest: LoginRequest?
    ): Call<LoginResponse?>?

    @POST("resendotp")
    fun resendOTP(
        @Body loginRequest: LoginRequest?
    ): Call<LoginResponse?>?

    @POST("otpverification")
    fun otpVerification(
        @Body loginRequest: LoginRequest?
    ): Call<OTPResponse?>?

    @GET("getuserwisepanjayatlist/{blockid}/{id}")
    fun getPanjayatlist(
        @Path("blockid") blockid: String, @Path("id") id: String?
    ): Call<PanjayatResponse?>?

    @GET("gethabitant/{blockid}/{panjayatid}")
    fun getHabitantlist(
        @Path("blockid") blockid: String, @Path("panjayatid") id: String?
    ): Call<HabitantResponse?>?

    @GET("getscheme")
    fun getSchemeList(): Call<SchemeResponse?>?

    @GET("admin/block")
    fun getBlockList(): Call<BlockResponse?>?

    @POST("addbeneficiary")
    fun addBeneficiary(
        @Body beneficiaryRequest: BeneficiaryRequest?
    ): Call<BeneficiaryResponse?>?

    @POST("beneficiarylist")
    fun getBeneficiaryList(
        @Body beneficiaryRequest: BeneficiaryRequest?
    ): Call<BeneficiaryListResponse?>?

    @POST("addworkdate")
    fun addWorkDate(
        @Body request: WorkOrderRequest?
    ): Call<WorkOrderResponse?>?


    @POST("workstagelist")
    fun workStageList(
        @Body request: BeneficiaryRequest?
    ): Call<WorkOrderBeneficiaryListResponse?>?


    @POST("workorderstagewiselist")
    fun workOrderStageWiseList(
        @Body request: BeneficiaryRequest?
    ): Call<WorkStageListResponse?>?


    @POST("getbeneficiarypaymentlist")
    fun getBeneficiaryPaymentlist(
        @Body request: BeneficiaryRequest?
    ): Call<BeneficiaryPaymentListResponse?>?


    @POST("getbeneficiarywisepaymentlist")
    fun getBeneficiaryWisePaymentList(
        @Body request: BeneficiaryRequest?
    ): Call<PaymentStageListResponse?>?

    @POST("addbeneficiaryworkorderstage")
    fun addBeneficiaryWorkOrderStage(
        @Body request: BeneficiaryRequest?
    ): Call<AddBeneficiaryWorkOrderStageResponse?>?


    @POST("addbeneficiarypaymentstage")
    fun addBeneficiaryPaymentStage(
        @Body request: BeneficiaryRequest?
    ): Call<AddBeneficiaryPaymentStageResponse?>?


    @POST("gettotalbeneficiary")
    fun getTotalbeneficiary(
        @Body request: TotalBeneficiaryRequest?
    ): Call<TotalBeneficiaryResponse?>?
//    fun getTotalbeneficiary(@Path("userid") blockid: String): Call<TotalBeneficiaryResponse?>?


    @POST("getschmewisebeneficiary")
    fun getSchmeWiseBeneficiary(
        @Body request: BeneficiaryRequest?
    ): Call<SchemeWiseBeneficiaryResponse?>?

    @POST("getstageswisebeneficiarylist")
    fun getStagesWiseBeneficiarylist(
        @Body request: BeneficiaryRequest?
    ): Call<StageWiseBeneficiaryListResponse?>?

    @POST("getblockwisecountlist")
    fun getBlockWiseCountList(
        @Body request: BeneficiaryRequest?
    ): Call<BlockWiseCountListResponse?>?

    @POST("getpanjayatwisecountlist")
    fun getPanjayatWiseCountlist(
        @Body request: BeneficiaryRequest?
    ): Call<PanchayatWiseCountListResponse?>?


    @GET("getyear")
    fun getYearList(): Call<YearResponse?>?


}