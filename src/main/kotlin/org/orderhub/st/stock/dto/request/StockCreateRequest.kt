package org.orderhub.st.stock.dto.request

data class StockCreateRequest(
    val productId: Long,
    val quantity: Long,
    val price: String,
    val name: String,
)
