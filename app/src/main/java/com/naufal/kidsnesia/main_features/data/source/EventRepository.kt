package com.naufal.kidsnesia.main_features.data.source

import com.naufal.kidsnesia.auth.data.Resource
import com.naufal.kidsnesia.auth.data.source.remote.network.ApiResponse
import com.naufal.kidsnesia.main_features.data.source.remote.EventRemoteDataSource
import com.naufal.kidsnesia.main_features.data.source.remote.response.DetailEventResponse
import com.naufal.kidsnesia.main_features.data.source.remote.response.DetailProductResponse
import com.naufal.kidsnesia.main_features.data.source.remote.response.DetailVideoResponse
import com.naufal.kidsnesia.main_features.data.source.remote.response.EventResponse
import com.naufal.kidsnesia.main_features.data.source.remote.response.ListVideoResponse
import com.naufal.kidsnesia.main_features.data.source.remote.response.ProductResponse
import com.naufal.kidsnesia.main_features.domain.repository.IEventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class EventRepository(
    private val eventRemoteDataSource: EventRemoteDataSource,
) : IEventRepository {
    override fun listEvent(): Flow<Resource<EventResponse>> = flow {
        emit(Resource.Loading())
        eventRemoteDataSource.getEvent().collect { apiResponse ->
            when(apiResponse) {
                is ApiResponse.Success -> emit(Resource.Success(apiResponse.data))
                is ApiResponse.Error -> emit(Resource.Error(apiResponse.errorMessage))
                ApiResponse.Empty -> emit(Resource.Error("Empty Response"))
            }
        }
    }

    override fun detailEvent(idEvent: String): Flow<Resource<DetailEventResponse>> = flow {
        emit(Resource.Loading())
        eventRemoteDataSource.getDetail(idEvent).collect { apiResponse ->
            when(apiResponse) {
                is ApiResponse.Success -> emit(Resource.Success(apiResponse.data))
                is ApiResponse.Error -> emit(Resource.Error(apiResponse.errorMessage))
                ApiResponse.Empty -> emit(Resource.Error("Empty Response"))
            }
        }
    }

    override fun detailProduct(idMerch: String): Flow<Resource<DetailProductResponse>> = flow {
        emit(Resource.Loading())
        eventRemoteDataSource.getDetailProduct(idMerch).collect { apiResponse ->
            when(apiResponse) {
                is ApiResponse.Success -> emit(Resource.Success(apiResponse.data))
                is ApiResponse.Error -> emit(Resource.Error(apiResponse.errorMessage))
                ApiResponse.Empty -> emit(Resource.Error("Empty Response"))
            }
        }
    }

    override fun listProduct(): Flow<Resource<ProductResponse>> = flow {
        emit(Resource.Loading())
        eventRemoteDataSource.getProduct().collect { apiResponse ->
            when(apiResponse) {
                is ApiResponse.Success -> emit(Resource.Success(apiResponse.data))
                is ApiResponse.Error -> emit(Resource.Error(apiResponse.errorMessage))
                ApiResponse.Empty -> emit(Resource.Error("Empty Response"))
            }
        }
    }

    override suspend fun getListVideo(token: String): ListVideoResponse {
        return eventRemoteDataSource.getListVideo(token)
    }

    override suspend fun getDetailVideo(token: String, idEvent: String): DetailVideoResponse {
        return eventRemoteDataSource.getDetailVideo(token, idEvent)
    }
}