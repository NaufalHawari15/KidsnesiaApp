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
import com.naufal.kidsnesia.main_features.presentation.detail.DetailViewModel
import com.naufal.kidsnesia.main_features.presentation.event.EventViewModel

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val useCaseModule = module {
    single<IUserRepository> { UserRepository(get(), get()) }
    factory<UserUseCase> { UserInteractor(get()) }

    single<IEventRepository> { EventRepository(get()) }
    factory<EventUseCase> { EventInteractor(get()) }

//    single<IPurchaseRepository> { PurchaseRepository(get(), get())}
//    factory<PurchaseUseCase> { PurchaseInteractor(get()) }
}

val viewModelModule = module {
    viewModel { RegisterViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { OtpViewModel(get()) }
    viewModel { DashboardViewModel(get(), get()) }
    viewModel { EventViewModel(get()) }
    viewModel { DetailViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
//    viewModel { PembelianViewModel(get(), get()) }
//    viewModel { DetailPembayaranViewModel(get()) }
//    viewModel { TranskasiViewModel(get()) }
//    viewModel { NotaViewModel(get()) }
//    viewModel { HistoryViewModel(get()) }

}