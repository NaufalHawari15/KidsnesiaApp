package com.naufal.kidsnesia.main_features.domain.usecase

import com.naufal.kidsnesia.auth.data.Resource
import com.naufal.kidsnesia.main_features.data.source.remote.response.DetailProductResponse
import com.naufal.kidsnesia.main_features.data.source.remote.response.DetailVideoResponse
import com.naufal.kidsnesia.main_features.data.source.remote.response.EventResponse
import com.naufal.kidsnesia.main_features.data.source.remote.response.ListVideoResponse
import com.naufal.kidsnesia.main_features.data.source.remote.response.ProductResponse
import com.naufal.kidsnesia.main_features.domain.repository.IEventRepository
import kotlinx.coroutines.flow.Flow

class EventInteractor (private val eventRepository: IEventRepository) : EventUseCase {
    override fun listEvent() = eventRepository.listEvent()

    override fun detailEvent(idEvent: String) = eventRepository.detailEvent(idEvent)

    override fun detailProduct(idMerch: String) = eventRepository.detailProduct(idMerch)

    override fun listProduct() = eventRepository.listProduct()

    override suspend fun getListVideo(token: String): ListVideoResponse {
        return eventRepository.getListVideo(token)
    }

    override suspend fun getDetailVideo(token: String, idEvent: String): DetailVideoResponse {
        return eventRepository.getDetailVideo(token, idEvent)
    }
}