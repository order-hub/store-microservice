package org.orderhub.st.store.service.impl

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.orderhub.st.store.domain.Store
import org.orderhub.st.store.domain.StoreStatus
import org.orderhub.st.store.dto.request.StoreCreateRequest
import org.orderhub.st.store.dto.request.StoreUpdateRequest
import org.orderhub.st.store.dto.response.StoreResponse
import org.orderhub.st.store.repository.StoreRepository
import java.time.LocalDateTime
import java.util.*

@ExtendWith(MockitoExtension::class)
class StoreServiceImplTest{

 @Mock
 private lateinit var storeRepository: StoreRepository

 private lateinit var storeService: StoreServiceImpl

 @BeforeEach
 fun setUp() {
  storeService = StoreServiceImpl(storeRepository)
 }

 @Test
 fun `createStore should save and return store response`() {
  val request = StoreCreateRequest(
   name = "Test Store",
   phoneNumber = "010-1234-5678",
   memberId = 1L,
   status = StoreStatus.PREPARING
  )

  val now = LocalDateTime.now()
  val savedStore = Store(
   id = 1L,
   name = request.name,
   phoneNumber = request.phoneNumber,
   memberId = request.memberId,
   status = request.status,
   createdAt = now,
   updatedAt = now
  )

  `when`(storeRepository.save(any(Store::class.java))).thenReturn(savedStore)

  val response: StoreResponse = storeService.createStore(request)

  verify(storeRepository, times(1)).save(any(Store::class.java))
  assertThat(response.id).isEqualTo(1L)
  assertThat(response.name).isEqualTo("Test Store")
  assertThat(response.phoneNumber).isEqualTo("010-1234-5678")
  assertThat(response.memberId).isEqualTo(1L)
  assertThat(response.status).isEqualTo(StoreStatus.PREPARING)
 }

 @Test
 fun `updateStore should modify existing store and return updated response`() {
  val request = StoreUpdateRequest(
   name = "Updated Name",
   phoneNumber = "010-9999-8888",
   memberId = 2L,
   status = StoreStatus.OPEN
  )

  val now = LocalDateTime.now()
  val originalStore = Store(
   id = 10L,
   name = "Original Name",
   phoneNumber = "010-1234-5678",
   memberId = 1L,
   status = StoreStatus.PREPARING,
   createdAt = now.minusDays(1),
   updatedAt = now.minusDays(1)
  )

  val updatedStore = originalStore.apply {
   name = request.name
   phoneNumber = request.phoneNumber
   memberId = request.memberId
   status = request.status
   updatedAt = now
  }

  `when`(storeRepository.findById(eq(10L))).thenReturn(Optional.of(originalStore))
  `when`(storeRepository.save(any(Store::class.java))).thenReturn(updatedStore)

  val response = storeService.updateStore(10L, request)

  verify(storeRepository, times(1)).findById(eq(10L))
  verify(storeRepository, times(1)).save(any(Store::class.java))
  assertThat(response.name).isEqualTo("Updated Name")
  assertThat(response.phoneNumber).isEqualTo("010-9999-8888")
  assertThat(response.memberId).isEqualTo(2L)
  assertThat(response.status).isEqualTo(StoreStatus.OPEN)
  assertThat(response.updatedAt).isEqualTo(now)
 }

 @Test
 fun `getAllStores should return list of store responses`() {
  val now = LocalDateTime.now()
  val storeList = listOf(
   Store(id = 1L, name = "Store1", phoneNumber = "010-1111-2222", memberId = 1L, status = StoreStatus.OPEN, createdAt = now, updatedAt = now),
   Store(id = 2L, name = "Store2", phoneNumber = "010-3333-4444", memberId = 2L, status = StoreStatus.PREPARING, createdAt = now, updatedAt = now)
  )

  `when`(storeRepository.findAll()).thenReturn(storeList)

  val responses = storeService.getAllStores()

  verify(storeRepository, times(1)).findAll()
  assertThat(responses).hasSize(2)
  assertThat(responses[0].name).isEqualTo("Store1")
  assertThat(responses[1].name).isEqualTo("Store2")
 }

 @Test
 fun `getStoreById should return store response if exists`() {
  val now = LocalDateTime.now()
  val store = Store(
   id = 100L,
   name = "Some Store",
   phoneNumber = "010-7777-9999",
   memberId = 10L,
   status = StoreStatus.OPEN,
   createdAt = now,
   updatedAt = now
  )

  `when`(storeRepository.findById(eq(100L))).thenReturn(Optional.of(store))

  val response = storeService.getStoreById(100L)

  verify(storeRepository, times(1)).findById(eq(100L))
  assertThat(response).isNotNull
  assertThat(response!!.name).isEqualTo("Some Store")
 }

 @Test
 fun `getStoreById should return null if store does not exist`() {
  `when`(storeRepository.findById(eq(999L))).thenReturn(Optional.empty())

  val response = storeService.getStoreById(999L)

  verify(storeRepository, times(1)).findById(eq(999L))
  assertThat(response).isNull()
 }

 @Test
 fun `deleteStore should change store status to CLOSED`() {
  val store = Store(
   id = 200L,
   name = "Deletable Store",
   phoneNumber = "010-1111-1111",
   memberId = 1L,
   status = StoreStatus.OPEN
  )

  `when`(storeRepository.findById(eq(200L))).thenReturn(Optional.of(store))

  storeService.deleteStore(200L)

  verify(storeRepository, times(1)).findById(eq(200L))
  assertThat(store.status).isEqualTo(StoreStatus.CLOSED)
 }
}
