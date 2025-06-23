package com.naufal.kidsnesia.auth.data.source.remote.network

import com.naufal.kidsnesia.auth.data.source.remote.response.LoginRequest
import com.naufal.kidsnesia.auth.data.source.remote.response.LoginResponse
import com.naufal.kidsnesia.auth.data.source.remote.response.OtpRequest
import com.naufal.kidsnesia.auth.data.source.remote.response.OtpResponse
import com.naufal.kidsnesia.auth.data.source.remote.response.RegisterRequest
import com.naufal.kidsnesia.auth.data.source.remote.response.RegisterResponse
import com.naufal.kidsnesia.auth.data.source.remote.response.ResendOtpResponse
import com.naufal.kidsnesia.main_features.data.source.remote.response.DetailEventResponse
import com.naufal.kidsnesia.main_features.data.source.remote.response.EventResponse
import com.naufal.kidsnesia.main_features.data.source.remote.response.NotaResponse
import com.naufal.kidsnesia.main_features.data.source.remote.response.PelangganResponse
import com.naufal.kidsnesia.main_features.data.source.remote.response.ProductResponse


import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @POST("register")
    @Headers("Content-Type: application/json", "Accept: application/json")
    suspend fun register (
        @Body request: RegisterRequest
    ): RegisterResponse

    @POST("verify-email")
    suspend fun otp (
        @Body request: OtpRequest,
        @Header("Authorization") tokenVerifikasi: String
    ): OtpResponse

    @POST("resend-otp")
    suspend fun resend (
        @Header("Authorization") tokenVerifikasi: String
    ): ResendOtpResponse

    @POST("login")
    suspend fun login (
        @Body request: LoginRequest
    ) : LoginResponse

    @GET("profil")
    suspend fun getCurrentPelanggan(): PelangganResponse

    @GET("event")
    suspend fun getEvent (
    ): EventResponse

    @GET("event/{idEvent}")
    suspend fun getDetail(
        @Path("idEvent") idEvent: String
    ): DetailEventResponse

    @GET("merch")
    suspend fun getProduct(
    ): ProductResponse

//    @POST("pembelian")
//    suspend fun uploadBeli(
//        @Header("Authorization") token: String,
//        @Body request: RequestPembelian
//    ): PembelianResponse
//
//    @GET("pembayaran/detail")
//    suspend fun getDetailBayar(
//        @Header("Authorization") token: String
//    ): DetailPembayaranResponse
//
//    @POST("pembayaran/pilih-bank")
//    suspend fun pilihBank(
//        @Header("Authorization") token: String,
//        @Body request: PilihBankRequest
//    ): PilihBankResponse
//
//    @POST("pembayaran/konfirmasi-bayar")
//    suspend fun uploadKonfirmasi(
//        @Header("Authorization") token: String
//    ): KonfirmasiPembayaranResponse
//
//    @GET("nota-pembelian/{idPembelian}")
//    suspend fun getNota(
//        @Header("Authorization") token: String,
//        @Path("idPembelian") idPembelian: Int
//    ): NotaResponse
//
//    @GET("pembelian-event")
//    suspend fun getHistory(
//        @Header("Authorization") token: String
//    ): HistoryEventResponse
}