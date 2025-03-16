package org.orderhub.st.store.dto.request

import org.orderhub.st.store.domain.StoreStatus

data class StoreUpdateRequest(
    val name: String,
    val phoneNumber: String,
    val memberId: Long,
    val status: StoreStatus
)