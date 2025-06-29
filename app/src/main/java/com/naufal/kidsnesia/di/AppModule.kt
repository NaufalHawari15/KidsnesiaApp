package com.naufal.kidsnesia.di

import com.naufal.kidsnesia.auth.data.UserRepository
import com.naufal.kidsnesia.auth.domain.repository.IUserRepository
import com.naufal.kidsnesia.auth.domain.usecase.UserInteractor
import com.naufal.kidsnesia.auth.domain.usecase.UserUseCase
import com.naufal.kidsnesia.auth.presentasi.login.LoginViewModel
import com.naufal.kidsnesia.auth.presentasi.otp.OtpViewModel
import com.naufal.kidsnesia.auth.presentasi.profile.ProfileViewModel
import com.naufal.kidsnesia.auth.presentasi.register.RegisterViewModel
import com.naufal.kidsnesia.main_features.data.source.EventRepository
import com.naufal.kidsnesia.main_features.domain.repository.IEventRepository
import com.naufal.kidsnesia.main_features.domain.usecase.EventInteractor
import com.naufal.kidsnesia.main_features.domain.usecase.EventUseCase
import com.naufal.kidsnesia.main_features.presentation.dashboard.DashboardViewModel
import com.naufal.kidsnesia.main_features.presentation.detail.detail_merch.DetailMerchViewModel
import com.naufal.kidsnesia.main_features.presentation.detail.detail_event.DetailViewModel
import com.naufal.kidsnesia.main_features.presentation.detail.detail_video.DetailVideoViewModel
import com.naufal.kidsnesia.main_features.presentation.event.EventViewModel
import com.naufal.kidsnesia.main_features.presentation.video.VideoViewModel
import com.naufal.kidsnesia.purchase.data.source.PurchaseRepository
import com.naufal.kidsnesia.purchase.domain.repository.IPurchaseRepository
import com.naufal.kidsnesia.purchase.domain.usecase.PurchaseInteractor
import com.naufal.kidsnesia.purchase.domain.usecase.PurchaseUseCase
import com.naufal.kidsnesia.purchase.presentation.cart.CartViewModel
import com.naufal.kidsnesia.purchase.presentation.cart.event.DetailEventCartViewModel
import com.naufal.kidsnesia.purchase.presentation.cart.merch.DetailMerchCartViewModel
import com.naufal.kidsnesia.purchase.presentation.transaksi.event.TransaksiViewModel
import com.naufal.kidsnesia.purchase.presentation.transaksi.membership.TransaksiMembershipViewModel
import com.naufal.kidsnesia.purchase.presentation.transaksi.membership.UploadMemberViewModel
import com.naufal.kidsnesia.purchase.presentation.transaksi.merch.TransaksiMerchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val useCaseModule = module {
    single<IUserRepository> { UserRepository(get(), get()) }
    factory<UserUseCase> { UserInteractor(get()) }

    single<IEventRepository> { EventRepository(get()) }
    factory<EventUseCase> { EventInteractor(get()) }

    single<IPurchaseRepository> { PurchaseRepository(get(), get()) }
    factory<PurchaseUseCase> { PurchaseInteractor(get()) }
}

val viewModelModule = module {
    viewModel { RegisterViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { OtpViewModel(get()) }
    viewModel { DashboardViewModel(get(), get(), get()) }
    viewModel { EventViewModel(get()) }
    viewModel { DetailViewModel(get(), get(), get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { DetailMerchViewModel(get(), get(), get()) }

    viewModel { VideoViewModel(get(), get()) }
    viewModel { DetailVideoViewModel(get(), get()) }
    viewModel { TransaksiMembershipViewModel(get(), get()) }
    viewModel { UploadMemberViewModel(get(), get()) }

    viewModel { CartViewModel(get(), get()) }
    viewModel { DetailEventCartViewModel(get(), get()) }
    viewModel { DetailMerchCartViewModel(get(), get()) }
    viewModel { TransaksiViewModel(get(), get()) }
    viewModel { TransaksiMerchViewModel(get(), get()) }
//    viewModel { PembelianViewModel(get(), get()) }
//    viewModel { DetailPembayaranViewModel(get()) }
//    viewModel { NotaViewModel(get()) }
//    viewModel { HistoryViewModel(get()) }

}