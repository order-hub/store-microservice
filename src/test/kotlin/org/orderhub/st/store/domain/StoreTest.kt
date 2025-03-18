package org.orderhub.st.store.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class StoreTest {

 @Test
 fun `updateStore method test`() {
  val store = Store(
   name = "Original Store",
   phoneNumber = "010-1234-5678",
   memberId = 1L
  )
  val oldUpdatedAt = store.updatedAt

  store.updateStore(
   name = "Updated Store",
   phoneNumber = "010-9876-5432",
   memberId = 2L,
   status = StoreStatus.OPEN
  )
  // @PreUpdate는 JPA가 실제 DB 업데이트 시점에 호출하지만,
  // 여기서는 순수 단위 테스트이므로 수동 호출합니다.
  store.preUpdate()

  assertThat(store.name).isEqualTo("Updated Store")
  assertThat(store.phoneNumber).isEqualTo("010-9876-5432")
  assertThat(store.memberId).isEqualTo(2L)
  assertThat(store.status).isEqualTo(StoreStatus.OPEN)
  assertThat(store.updatedAt).isAfter(oldUpdatedAt)
 }

 @Test
 fun `deleteStore method test`() {
  val store = Store(
   name = "Test Store",
   phoneNumber = "010-1234-5678",
   memberId = 1L,
   status = StoreStatus.OPEN
  )
  store.deleteStore()
  assertThat(store.status).isEqualTo(StoreStatus.CLOSED)
 }

 @Test
 fun `persist method test`() {
  val store = Store(
   name = "Test Store",
   phoneNumber = "010-1234-5678",
   memberId = 1L
  )
  val initialCreatedAt = store.createdAt
  val initialUpdatedAt = store.updatedAt

  // @PrePersist 역시 DB에 처음 저장될 때 호출되지만, 여기서는 수동으로 호출합니다.
  store.persist()

  // 실제로 JPA 환경에서라면 createdAt과 updatedAt 둘 다 저장 직전 시점으로 설정됩니다.
  // 단위 테스트에서는 값이 잘 변경되었는지만 확인합니다.
  assertThat(store.createdAt).isAfter(initialCreatedAt)
  assertThat(store.updatedAt).isEqualTo(initialUpdatedAt) // persist() 안에서는 updatedAt 변경 없음
 }

 @Test
 fun `constructor test`() {
  val now = LocalDateTime.now()
  val store = Store(
   name = "Constructed Store",
   phoneNumber = "010-0000-0000",
   memberId = 1L,
   status = StoreStatus.PREPARING,
   createdAt = now,
   updatedAt = now
  )

  assertThat(store.id).isNull()
  assertThat(store.name).isEqualTo("Constructed Store")
  assertThat(store.phoneNumber).isEqualTo("010-0000-0000")
  assertThat(store.memberId).isEqualTo(1L)
  assertThat(store.status).isEqualTo(StoreStatus.PREPARING)
  assertThat(store.createdAt).isEqualTo(now)
  assertThat(store.updatedAt).isEqualTo(now)
 }
}
