package org.orderhub.st.store.domain

import jakarta.persistence.*
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: StoreStatus = StoreStatus.PREPARING,

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

    fun deleteStore() {
        status = StoreStatus.CLOSED;
    }
}
