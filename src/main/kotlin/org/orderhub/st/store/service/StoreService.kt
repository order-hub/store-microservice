package org.orderhub.st.store.service

import org.orderhub.st.store.dto.request.StoreCreateRequest
import org.orderhub.st.store.dto.request.StoreUpdateRequest
import org.orderhub.st.store.dto.response.StoreResponse

interface StoreService {
    fun createStore(request: StoreCreateRequest): StoreResponse
    fun updateStore(id: Long, request: StoreUpdateRequest): StoreResponse
    fun getAllStores(): List<StoreResponse>
    fun getStoreById(id: Long): StoreResponse?
    fun deleteStore(id: Long)
}