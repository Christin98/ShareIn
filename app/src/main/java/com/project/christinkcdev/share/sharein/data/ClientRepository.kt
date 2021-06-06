package com.project.christinkcdev.share.sharein.data

import androidx.lifecycle.LiveData
import com.project.christinkcdev.share.sharein.database.ClientAddressDao
import com.project.christinkcdev.share.sharein.database.ClientDao
import com.project.christinkcdev.share.sharein.database.model.UClient
import com.project.christinkcdev.share.sharein.database.model.UClientAddress
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClientRepository @Inject constructor(
    private val clientDao: ClientDao,
    private val clientAddressDao: ClientAddressDao,
) {
    suspend fun delete(client: UClient) = clientDao.delete(client)

    suspend fun get(uid: String): UClient? = clientDao.get(uid)

    fun getAll(): LiveData<List<UClient>> = clientDao.getAll()

    suspend fun getAddresses(clientUid: String): List<UClientAddress> = clientAddressDao.getAll(clientUid)

    suspend fun insert(client: UClient) = clientDao.insert(client)

    suspend fun insert(address: UClientAddress) = clientAddressDao.insert(address)

    suspend fun update(client: UClient) = clientDao.update(client)
}