package org.orderhub.st.inventory.service.impl

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.orderhub.st.inventory.domain.Inventory
import org.orderhub.st.inventory.dto.request.InventoryDeductRequest
import org.orderhub.st.inventory.dto.request.InventoryItemRequest
import org.orderhub.st.inventory.repository.InventoryRepository
import org.orderhub.st.stock.dto.request.ProductUpdateRequest

@ExtendWith(MockitoExtension::class)
class InventoryServiceImplTest {

 private val inventoryRepository: InventoryRepository = mock()
 private val service = InventoryServiceImpl(inventoryRepository)

 @Test
 fun `should update product info across all inventories`() {
  // Given
  val request = ProductUpdateRequest(
   productId = 123L,
   name = "New Product Name",
   price = "29.99"
  )

  val mockInventory1 = mock(Inventory::class.java)
  val mockInventory2 = mock(Inventory::class.java)

  `when`(inventoryRepository.findAllWithStocksByProductId(anyLong()))
   .thenReturn(listOf(mockInventory1, mockInventory2))

  // When
  service.updateProductInfoAcrossInventories(request)

  // Then
  verify(inventoryRepository).findAllWithStocksByProductId(request.productId)

  verify(mockInventory1).updateStockInfo(
   productId = request.productId,
   newName = request.name,
   newPrice = request.price
  )

  verify(mockInventory2).updateStockInfo(
   productId = request.productId,
   newName = request.name,
   newPrice = request.price
  )
 }

 @Test
 fun `should handle no inventories found`() {
  // Given
  val request = ProductUpdateRequest(
   productId = 456L,
   name = "Non-existent Product",
   price = "0.00"
  )

  `when`(inventoryRepository.findAllWithStocksByProductId(anyLong()))
   .thenReturn(emptyList())

  // When
  service.updateProductInfoAcrossInventories(request)

  // Then
  verify(inventoryRepository).findAllWithStocksByProductId(request.productId)
  verifyNoMoreInteractions(inventoryRepository)
 }

 @Test
 fun `should deduct stocks for given inventory`() {
  // Given
  val storeId = 1L
  val items = listOf(
   InventoryItemRequest(itemId = 101L, quantity = 2),
   InventoryItemRequest(itemId = 102L, quantity = 3)
  )
  val request = InventoryDeductRequest(storeId = storeId, items = items)

  val mockInventory = mock(Inventory::class.java)

  `when`(inventoryRepository.findByStoreId(storeId)).thenReturn(mockInventory)

  // When
  service.deductInventory(request)

  // Then
  verify(inventoryRepository).findByStoreId(storeId)
  verify(mockInventory).deductStocks(items)
 }

 @Test
 fun `should throw exception when inventory not found`() {
  // Given
  val storeId = 999L
  val request = InventoryDeductRequest(
   storeId = storeId,
   items = listOf(InventoryItemRequest(itemId = 1L, quantity = 5))
  )

  `when`(inventoryRepository.findByStoreId(storeId)).thenReturn(null)

  // When / Then
  assertThrows<IllegalArgumentException> {
   service.deductInventory(request)
  }.also {
   assert(it.message!!.contains("storeId=$storeId"))
  }

  verify(inventoryRepository).findByStoreId(storeId)
  verifyNoMoreInteractions(inventoryRepository)
 }
}