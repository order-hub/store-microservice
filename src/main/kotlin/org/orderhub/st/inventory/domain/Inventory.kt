package org.orderhub.st.inventory.domain

import jakarta.persistence.*
import org.orderhub.st.inventory.dto.request.InventoryItemRequest
import org.orderhub.st.stock.domain.Stock
import org.orderhub.st.stock.dto.request.StockCreateRequest
import org.orderhub.st.store.domain.Store
import java.time.LocalDateTime

@Entity
@Table(name = "inventory")
class Inventory(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    var store: Store? = null,

    @OneToMany(mappedBy = "inventory", cascade = [CascadeType.ALL], orphanRemoval = true)
    var stocks: MutableList<Stock> = mutableListOf(),

    @Column(nullable = false, updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now(),
) {

    @PrePersist
    fun persist() {
        createdAt = LocalDateTime.now()
    }

    @PreUpdate
    fun preUpdate() {
        updatedAt = LocalDateTime.now()
    }

    fun assignStore(store: Store) {
        this.store = store
    }

    fun addStock(request: StockCreateRequest) {
        val existingStock = stocks.find { it.productId == request.productId }

        if (existingStock != null) {
            existingStock.updateQuantity(request.quantity)
            return
        }

        val newStock = Stock(
            inventory = this,
            productId = request.productId,
            quantity = request.quantity,
            price = request.price,
            name = request.name,
        )
        stocks.add(newStock)
    }

    fun updateStockInfo(productId: Long, newName: String, newPrice: String) {
        val target = stocks.find { it.productId == productId }
        if (target != null) {
            target.name = newName
            target.price = newPrice
        }
    }

    fun deductStocks(itemRequests: List<InventoryItemRequest>) {
        val stockMap = stocks.associateBy { it.productId }

        itemRequests.forEach { item ->
            val stock = stockMap[item.itemId]
                ?: throw IllegalArgumentException("No stock found for productId=${item.itemId}")

            stock.updateQuantity(item.quantity.toLong())
        }
    }

}