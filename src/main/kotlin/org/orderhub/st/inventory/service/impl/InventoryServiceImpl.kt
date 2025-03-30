package org.orderhub.st.inventory.service.impl

import org.orderhub.st.inventory.dto.request.InventoryDeductRequest
import org.orderhub.st.inventory.repository.InventoryRepository
import org.orderhub.st.inventory.service.InventoryService
import org.orderhub.st.stock.dto.request.ProductUpdateRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class InventoryServiceImpl(
    private val inventoryRepository: InventoryRepository
) : InventoryService {

    @Transactional
    override fun updateProductInfoAcrossInventories(request: ProductUpdateRequest) {
        val inventories = inventoryRepository.findAllWithStocksByProductId(request.productId)

        for (inventory in inventories) {
            inventory.updateStockInfo(
                productId = request.productId,
                newName = request.name,
                newPrice = request.price
            )
        }
    }

    @Transactional
    override fun deductInventory(request: InventoryDeductRequest) {
        val inventory = inventoryRepository.findByStoreId(request.storeId)
            ?: throw IllegalArgumentException("Inventory not found for storeId=${request.storeId}")

        inventory.deductStocks(request.items)
    }
}