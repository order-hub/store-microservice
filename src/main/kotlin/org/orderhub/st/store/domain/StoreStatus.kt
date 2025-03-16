package org.orderhub.st.store.domain

enum class StoreStatus(val description: String) {
    OPEN("운영 중"),
    CLOSED("폐쇄"),
    PREPARING("준비 중"),
    SUSPENDED("일시 정지")
}