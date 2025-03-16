    package org.orderhub.st.store.service.impl

    import org.orderhub.st.store.domain.Store
    import org.orderhub.st.store.dto.request.StoreCreateRequest
    import org.orderhub.st.store.dto.request.StoreUpdateRequest
    import org.orderhub.st.store.dto.response.StoreResponse
    import org.orderhub.st.store.repository.StoreRepository
    import org.orderhub.st.store.service.StoreService
    import org.springframework.stereotype.Service
    import org.springframework.transaction.annotation.Transactional

    @Service
    @Transactional(readOnly = true)
    class StoreServiceImpl (
        private val storeRepository: StoreRepository
    ) : StoreService {

        override fun createStore(request: StoreCreateRequest): StoreResponse {
            val store = Store(
                name = request.name,
                phoneNumber = request.phoneNumber,
                memberId = request.memberId,
                status = request.status
            )
            return storeRepository.save(store).toResponse()
        }

        override fun updateStore(id: Long, request: StoreUpdateRequest): StoreResponse {
            val store = getStoreEntityById(id);

            store.name = request.name
            store.phoneNumber = request.phoneNumber
            store.memberId = request.memberId
            store.status = request.status
            return storeRepository.save(store).toResponse()
        }

        override fun getAllStores(): List<StoreResponse> {
            return storeRepository.findAll().map { it.toResponse() }
        }

        fun getStoreEntityById(id: Long): Store {
            return storeRepository.findById(id).orElseThrow{
                throw IllegalArgumentException("Store with id $id not found")
            }
        }

        override fun getStoreById(id: Long): StoreResponse? {
            return storeRepository.findById(id).map { it.toResponse() }.orElse(null)
        }

        override fun deleteStore(id: Long) {
            getStoreEntityById(id).deleteStore();
        }

        private fun Store.toResponse(): StoreResponse {
            return StoreResponse(
                id = this.id!!,
                name = this.name,
                phoneNumber = this.phoneNumber,
                memberId = this.memberId,
                status = this.status,
                createdAt = this.createdAt,
                updatedAt = this.updatedAt
            )
        }

    }