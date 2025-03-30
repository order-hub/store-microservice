package org.orderhub.st.inventory.repository

import org.orderhub.st.inventory.domain.Inventory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface InventoryRepository : JpaRepository<Inventory, String> {
    @Query("""
        SELECT DISTINCT i 
        FROM Inventory i 
        JOIN FETCH i.stocks s 
        WHERE s.productId = :productId
    """)
    fun findAllWithStocksByProductId(@Param("productId") productId: Long): List<Inventory>

    fun findByStoreId(@Param("storeId") storeId: Long): Inventory
}