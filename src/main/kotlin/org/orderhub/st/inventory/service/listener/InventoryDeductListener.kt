package org.orderhub.st.inventory.service.listener

import org.orderhub.st.inventory.dto.request.InventoryDeductRequest
import org.orderhub.st.inventory.service.InventoryService
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class InventoryDeductListener(
    private val inventoryService: InventoryService
) {

    @KafkaListener(topics = ["inventory-deduct"], groupId = "store-service")
    fun handleInventoryDeduct(request: InventoryDeductRequest) {
        inventoryService.deductInventory(request)
    }
}