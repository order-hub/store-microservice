package org.orderhub.st.inventory.service

import org.orderhub.st.inventory.dto.request.InventoryDeductRequest
import org.orderhub.st.stock.dto.request.ProductUpdateRequest

interface InventoryService {
    fun updateProductInfoAcrossInventories(request: ProductUpdateRequest)
    fun deductInventory(request: InventoryDeductRequest)
}