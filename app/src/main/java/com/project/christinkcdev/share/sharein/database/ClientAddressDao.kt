package com.project.christinkcdev.share.sharein.database

import androidx.room.*
import com.project.christinkcdev.share.sharein.database.model.UClientAddress

@Dao
interface ClientAddressDao {
    @Query("SELECT * FROM clientAddress WHERE clientUid = :clientUid")
    suspend fun getAll(clientUid: String): List<UClientAddress>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(address: UClientAddress)
}