package com.naufal.kidsnesia.main_features.domain.usecase

import com.naufal.kidsnesia.auth.data.Resource
import com.naufal.kidsnesia.main_features.data.source.remote.response.DetailProductResponse
import com.naufal.kidsnesia.main_features.data.source.remote.response.EventResponse
import com.naufal.kidsnesia.main_features.data.source.remote.response.ProductResponse
import com.naufal.kidsnesia.main_features.domain.repository.IEventRepository
import kotlinx.coroutines.flow.Flow

class EventInteractor (private val eventRepository: IEventRepository) : EventUseCase {
    override fun listEvent() = eventRepository.listEvent()

    override fun detailEvent(idEvent: String) = eventRepository.detailEvent(idEvent)

    override fun detailProduct(idMerch: String) = eventRepository.detailProduct(idMerch)

    override fun listProduct() = eventRepository.listProduct()
}