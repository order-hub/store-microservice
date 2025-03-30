package org.orderhub.st.inventory.service.listener

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.orderhub.st.inventory.dto.request.InventoryDeductRequest
import org.orderhub.st.inventory.dto.request.InventoryItemRequest
import org.orderhub.st.inventory.service.InventoryService

@ExtendWith(MockitoExtension::class)
class InventoryDeductListenerTest {

 private val inventoryService: InventoryService = mock()
 private val listener = InventoryDeductListener(inventoryService)

 @Test
 fun `should call inventoryService to deduct inventory`() {
  // Given
  val request = InventoryDeductRequest(
   storeId = 1L,
   items = listOf(
    InventoryItemRequest(itemId = 101L, quantity = 2),
    InventoryItemRequest(itemId = 102L, quantity = 3)
   )
  )

  // When
  listener.handleInventoryDeduct(request)

  // Then
  verify(inventoryService, times(1)).deductInventory(request)
  verifyNoMoreInteractions(inventoryService)
 }
}
