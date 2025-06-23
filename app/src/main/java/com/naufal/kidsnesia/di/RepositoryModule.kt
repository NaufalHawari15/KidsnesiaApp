package com.naufal.kidsnesia.di

import android.app.Application
import com.naufal.kidsnesia.auth.data.source.local.AuthLocalDataSource
import com.naufal.kidsnesia.auth.data.source.local.UserPreference
import com.naufal.kidsnesia.auth.data.source.local.dataStore
import com.naufal.kidsnesia.auth.data.source.remote.AuthRemoteDataSource
import com.naufal.kidsnesia.main_features.data.source.remote.EventRemoteDataSource

import org.koin.dsl.module

val repositoryModule = module {
    single { AuthRemoteDataSource(get()) }
    single { AuthLocalDataSource(get()) }
    single { EventRemoteDataSource(get()) }
//    single { PurchaseRemoteDataSource(get()) }

    single { UserPreference(get<Application>().dataStore) }
}