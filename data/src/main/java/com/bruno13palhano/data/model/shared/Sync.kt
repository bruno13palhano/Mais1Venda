package com.bruno13palhano.data.model.shared

/**
 * @param id the hardcoded id to represent a specific model/table type
 * @param name the name of the model/table
 * @param lastSyncTimestamp the timestamp of the last sync
 */
internal data class Sync(
    val id: Long,
    val name: String,
    val lastSyncTimestamp: String,
)
