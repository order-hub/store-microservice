package org.orderhub.st.stock.dto.request

data class ProductUpdateRequest(
    val productId: Long,
    val name: String,
    val price: String
)
