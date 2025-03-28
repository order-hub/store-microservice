package org.orderhub.st.stock.domain

import jakarta.persistence.*
import org.orderhub.st.inventory.domain.Inventory
import org.orderhub.st.store.domain.Store
import java.time.LocalDateTime

@Entity
@Table(name = "stock")
class Stock(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_id")
    val inventory: Inventory,

    @Column(nullable = false, unique = true)
    val productId: Long,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var quantity: Long,

    @Column(nullable = false)
    var price: String,

    // product code, status
    var productCode: String? = null,

    @Enumerated(EnumType.STRING)
    var productStatus: SaleStatus? = null,

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

    fun updateQuantity(changeQuantity: Long){
        this.quantity += changeQuantity;
    }

}