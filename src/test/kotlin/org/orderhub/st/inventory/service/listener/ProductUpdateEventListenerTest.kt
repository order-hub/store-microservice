package org.orderhub.st.inventory.service.listener

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.kotlin.any
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.orderhub.st.inventory.service.InventoryService
import org.orderhub.st.stock.dto.request.ProductUpdateRequest

@ExtendWith(MockitoExtension::class)
class ProductUpdateEventListenerTest {

 @Mock
 private lateinit var inventoryService: InventoryService

 @Mock
 private lateinit var objectMapper: ObjectMapper

 @Mock
 private lateinit var consumerRecord: ConsumerRecord<String, String>

 private lateinit var listener: ProductUpdateEventListener

 @Test
 fun `should process valid product update message successfully`() {
  // Given
  listener = ProductUpdateEventListener(inventoryService, objectMapper)
  val validMessage = """{"productId":"123","name":"New Product"}"""
  val mockRequest = ProductUpdateRequest(123, "New Product", "1000")

  `when`(objectMapper.readValue(validMessage, ProductUpdateRequest::class.java))
   .thenReturn(mockRequest)

  // When
  listener.handleProductUpdate(validMessage, consumerRecord)

  // Then
  verify(inventoryService, times(1))
   .updateProductInfoAcrossInventories(mockRequest)
 }

 @Test
 fun `should handle invalid message format gracefully`() {
  // Given
  listener = ProductUpdateEventListener(inventoryService, objectMapper)
  val invalidMessage = "invalid_message"

  `when`(objectMapper.readValue(invalidMessage, ProductUpdateRequest::class.java))
   .thenThrow(JsonProcessingException::class.java)

  // When
  listener.handleProductUpdate(invalidMessage, consumerRecord)

  // Then
  verify(inventoryService, never())
   .updateProductInfoAcrossInventories(any())
 }

 @Test
 fun `should handle service exceptions gracefully`() {
  // Given
  listener = ProductUpdateEventListener(inventoryService, objectMapper)
  val validMessage = """{"productId":"123","name":"New Product"}"""
  val mockRequest = ProductUpdateRequest(123, "New Product", "1000")

  `when`(objectMapper.readValue(validMessage, ProductUpdateRequest::class.java))
   .thenReturn(mockRequest)
  doThrow(RuntimeException("Service failure"))
   .`when`(inventoryService)
   .updateProductInfoAcrossInventories(mockRequest)

  // When
  listener.handleProductUpdate(validMessage, consumerRecord)

  // Then
  verify(inventoryService, times(1))
   .updateProductInfoAcrossInventories(mockRequest)
 }
}