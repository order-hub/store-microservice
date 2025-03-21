package org.orderhub.st.inventory.service.listener

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.orderhub.st.inventory.service.InventoryService
import org.orderhub.st.stock.dto.request.ProductUpdateRequest
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class ProductUpdateEventListener(
    private val inventoryService: InventoryService,
    private val objectMapper: ObjectMapper
) {
    private val log = LoggerFactory.getLogger(this::class.java)

    @KafkaListener(topics = ["product-updated"], groupId = "product-update-consumer")
    fun handleProductUpdate(message: String, record: ConsumerRecord<String, String>) {
        log.info("Received product update event: $message")
        try {
            val request = objectMapper.readValue(message, ProductUpdateRequest::class.java)

            inventoryService.updateProductInfoAcrossInventories(request)
            log.info("Inventory updated for productId=${request.productId}")

        } catch (e: Exception) {
            log.error("Failed to process product update event", e)
        }

    }
}