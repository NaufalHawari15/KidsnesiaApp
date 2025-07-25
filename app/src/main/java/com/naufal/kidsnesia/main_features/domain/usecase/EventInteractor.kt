package com.naufal.kidsnesia.main_features.domain.usecase

import com.naufal.kidsnesia.auth.data.Resource
import com.naufal.kidsnesia.main_features.data.source.remote.response.CurrentMembershipResponse
import com.naufal.kidsnesia.main_features.data.source.remote.response.DetailNotaEventResponse
import com.naufal.kidsnesia.main_features.data.source.remote.response.DetailNotaMerchResponse
import com.naufal.kidsnesia.main_features.data.source.remote.response.DetailProductResponse
import com.naufal.kidsnesia.main_features.data.source.remote.response.DetailVideoResponse
import com.naufal.kidsnesia.main_features.data.source.remote.response.EventResponse
import com.naufal.kidsnesia.main_features.data.source.remote.response.ListVideoResponse
import com.naufal.kidsnesia.main_features.data.source.remote.response.NotaEventResponse
import com.naufal.kidsnesia.main_features.data.source.remote.response.NotaMerchResponse
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

    override suspend fun getMembership(token: String): CurrentMembershipResponse {
        return eventRepository.getMembership(token)
    }

    override suspend fun getNotaEvent(token: String): NotaEventResponse {
        return eventRepository.getNotaEvent(token)
    }

    override suspend fun getNotaMerch(token: String): NotaMerchResponse {
        return eventRepository.getNotaMerch(token)
    }

    override suspend fun getDetailNotaEvent(token: String, idPembelianEvent: String): DetailNotaEventResponse {
        return eventRepository.getDetailNotaEvent(token, idPembelianEvent)
    }

    override suspend fun getDetailNotaMerch(token: String, idPembelianMerch: String): DetailNotaMerchResponse {
        return eventRepository.getDetailNotaMerch(token, idPembelianMerch)
    }
}