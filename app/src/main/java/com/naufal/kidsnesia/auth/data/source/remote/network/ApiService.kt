package com.naufal.kidsnesia.auth.data.source.remote.network

import com.naufal.kidsnesia.auth.data.source.remote.response.LoginRequest
import com.naufal.kidsnesia.auth.data.source.remote.response.LoginResponse
import com.naufal.kidsnesia.auth.data.source.remote.response.OtpRequest
import com.naufal.kidsnesia.auth.data.source.remote.response.OtpResponse
import com.naufal.kidsnesia.auth.data.source.remote.response.RegisterRequest
import com.naufal.kidsnesia.auth.data.source.remote.response.RegisterResponse
import com.naufal.kidsnesia.auth.data.source.remote.response.ResendOtpResponse
import com.naufal.kidsnesia.main_features.data.source.remote.response.DetailEventResponse
import com.naufal.kidsnesia.main_features.data.source.remote.response.DetailProductResponse
import com.naufal.kidsnesia.main_features.data.source.remote.response.DetailVideoResponse
import com.naufal.kidsnesia.main_features.data.source.remote.response.EventResponse
import com.naufal.kidsnesia.main_features.data.source.remote.response.ListVideoResponse
import com.naufal.kidsnesia.main_features.data.source.remote.response.NotaResponse
import com.naufal.kidsnesia.main_features.data.source.remote.response.PelangganResponse
import com.naufal.kidsnesia.main_features.data.source.remote.response.ProductResponse
import com.naufal.kidsnesia.purchase.data.source.remote.response.CartMerchRequest
import com.naufal.kidsnesia.purchase.data.source.remote.response.CartMerchResponse
import com.naufal.kidsnesia.purchase.data.source.remote.response.CartRequest
import com.naufal.kidsnesia.purchase.data.source.remote.response.CartResponse
import com.naufal.kidsnesia.purchase.data.source.remote.response.CheckoutMerchRequest
import com.naufal.kidsnesia.purchase.data.source.remote.response.CheckoutMerchResponse
import com.naufal.kidsnesia.purchase.data.source.remote.response.CheckoutRequest
import com.naufal.kidsnesia.purchase.data.source.remote.response.CheckoutResponse
import com.naufal.kidsnesia.purchase.data.source.remote.response.DeleteCartResponse
import com.naufal.kidsnesia.purchase.data.source.remote.response.DetailCartMerchResponse
import com.naufal.kidsnesia.purchase.data.source.remote.response.DetailCartResponse
import com.naufal.kidsnesia.purchase.data.source.remote.response.ListCartMerchResponse
import com.naufal.kidsnesia.purchase.data.source.remote.response.ListCartResponse
import com.naufal.kidsnesia.purchase.data.source.remote.response.PilihBankMerchRequest
import com.naufal.kidsnesia.purchase.data.source.remote.response.PilihBankMerchResponse
import com.naufal.kidsnesia.purchase.data.source.remote.response.PilihBankRequest
import com.naufal.kidsnesia.purchase.data.source.remote.response.PilihBankResponse
import com.naufal.kidsnesia.purchase.data.source.remote.response.UploadBuktiMerchResponse
import com.naufal.kidsnesia.purchase.data.source.remote.response.UploadBuktiResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
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

    @POST("event/cart")
    suspend fun createCart(
        @Header("Authorization") token: String,
        @Body request: CartRequest
    ): CartResponse

    @GET("event/cart/listcart")
    suspend fun getListCart(
        @Header("Authorization") token: String
    ): ListCartResponse

    @GET("event/cart/{idPembelianEvent}")
    suspend fun getDetailCart(
        @Header("Authorization") token: String,
        @Path("idPembelianEvent") idPembelianEvent: String
    ): DetailCartResponse

    @POST("event/checkout/{idPembelianEvent}")
    @Headers("Content-Type: application/json", "Accept: application/json")
    suspend fun createCheckout(
        @Header("Authorization") token: String,
        @Path("idPembelianEvent") idPembelianEvent: String,
        @Body request: CheckoutRequest
    ): CheckoutResponse

    @POST("event/pembayaran/pilih-bank/{idPembelianEvent}")
    suspend fun pilihBank(
        @Header("Authorization") token: String,
        @Path("idPembelianEvent") idPembelianEvent: String,
        @Body request: PilihBankRequest
    ): PilihBankResponse

    @Multipart
    @POST("event/pembayaran/{idPembayaranEvent}/upload-bukti")
    suspend fun uploadBukti(
        @Header("Authorization") token: String,
        @Path("idPembayaranEvent") id: String,
        @Part file: MultipartBody.Part
    ): UploadBuktiResponse

    @GET("merch")
    suspend fun getProduct(
    ): ProductResponse

    @GET("merch/{idMerch}")
    suspend fun getDetailProduct(
        @Path("idMerch") idMerch: String
    ): DetailProductResponse

    @POST("merch/cart")
    suspend fun createMerchCart(
        @Header("Authorization") token: String,
        @Body request: CartMerchRequest
    ): CartMerchResponse

    @GET("merch/cart/listcart")
    suspend fun getListMerchCart(
        @Header("Authorization") token: String
    ): ListCartMerchResponse

    @GET("merch/cart/{idPembelianMerch}")
    suspend fun getDetailCartMerch(
        @Header("Authorization") token: String,
        @Path("idPembelianMerch") idPembelianMerch: String
    ): DetailCartMerchResponse

    @POST("merch/checkout/{idPembelianMerch}")
    @Headers("Content-Type: application/json", "Accept: application/json")
    suspend fun createCheckoutMerch(
        @Header("Authorization") token: String,
        @Path("idPembelianMerch") idPembelianMerch: String,
        @Body request: CheckoutMerchRequest
    ): CheckoutMerchResponse

    @POST("merch/pembayaran/pilih-bank/{idPembelianMerch}")
    suspend fun pilihBankMerch(
        @Header("Authorization") token: String,
        @Path("idPembelianMerch") idPembelianMerch: String,
        @Body request: PilihBankMerchRequest
    ): PilihBankMerchResponse

    @Multipart
    @POST("merch/pembayaran/{idPembayaranMerch}/upload-bukti")
    suspend fun uploadBuktiMerch(
        @Header("Authorization") token: String,
        @Path("idPembayaranMerch") id: String,
        @Part file: MultipartBody.Part
    ): UploadBuktiMerchResponse



    @GET("videos")
    suspend fun getListVideo(
        @Header("Authorization") token: String
    ): ListVideoResponse

    @GET("videos/{idVideo}")
    suspend fun getDetailVideo(
        @Header("Authorization") token: String,
        @Path("idVideo") idVideo: String
    ): DetailVideoResponse


//    @DELETE ("event/cart/{idPembelianEvent}")
//    suspend fun deleteCart(
//        @Header("Authorization") token: String,
//        @Path("idPembelianEvent") idPembelianEvent: Int
//    ): DeleteCartResponse

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