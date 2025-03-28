package org.orderhub.st.inventory.service.impl

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.orderhub.st.inventory.domain.Inventory
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
}