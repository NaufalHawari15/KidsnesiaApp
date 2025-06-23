package com.naufal.kidsnesia.main_features.domain.usecase

import com.naufal.kidsnesia.auth.data.Resource
import com.naufal.kidsnesia.main_features.data.source.remote.response.DetailEventResponse
import com.naufal.kidsnesia.main_features.data.source.remote.response.EventResponse
import com.naufal.kidsnesia.main_features.data.source.remote.response.ProductResponse
import kotlinx.coroutines.flow.Flow

interface EventUseCase {
    fun listEvent(): Flow<Resource<EventResponse>>

    fun detailEvent(idEvent:String): Flow<Resource<DetailEventResponse>>

    fun listProduct(): Flow<Resource<ProductResponse>>
}