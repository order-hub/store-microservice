package org.orderhub.st.store.domain

import jakarta.persistence.*
import org.orderhub.st.inventory.domain.Inventory
import org.orderhub.st.stock.domain.Stock
import java.time.LocalDateTime

@Entity
@Table(name = "store")
class Store(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, length = 100)
    var name: String,

    @Column(nullable = false, length = 20)
    var phoneNumber: String,

    @Column(nullable = false)
    var memberId: Long,

    @OneToOne(mappedBy = "store", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    var inventory: Inventory = Inventory(),

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: StoreStatus = StoreStatus.PREPARING,

    @Column(nullable = false, updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now(),

    ) {

    init {
        inventory.assignStore(this)
    }

    @PrePersist
    fun persist() {
        createdAt = LocalDateTime.now()
    }

    @PreUpdate
    fun preUpdate() {
        updatedAt = LocalDateTime.now()
    }

    fun updateStore(name: String, phoneNumber: String, memberId: Long, status: StoreStatus) {
        this.name = name
        this.phoneNumber = phoneNumber
        this.memberId = memberId
        this.status = status
    }

    fun deleteStore() {
        status = StoreStatus.CLOSED;
    }
}
