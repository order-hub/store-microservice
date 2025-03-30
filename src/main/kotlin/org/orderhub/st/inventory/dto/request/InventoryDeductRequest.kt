package org.orderhub.st.inventory.dto.request

data class InventoryDeductRequest(
    val storeId: Long,
    val items: List<InventoryItemRequest>
)
