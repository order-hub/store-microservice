package org.orderhub.st.store.dto.response

import org.orderhub.st.store.domain.StoreStatus
import java.time.LocalDateTime

data class StoreResponse(
    val id: Long,
    val name: String,
    val phoneNumber: String,
    val memberId: Long,
    val status: StoreStatus,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)
