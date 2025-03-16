package org.orderhub.st.store.repository

import org.orderhub.st.store.domain.Store
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StoreRepository : JpaRepository<Store, Long>