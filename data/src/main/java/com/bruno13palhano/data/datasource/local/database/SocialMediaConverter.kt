package com.bruno13palhano.data.datasource.local.database

import androidx.room.TypeConverter
import com.bruno13palhano.data.model.shared.SocialMedia
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal class SocialMediaConverter {
    @TypeConverter
    fun fromSocialMediaList(socialMediaList: List<SocialMedia>): String {
        return Json.encodeToString(socialMediaList)
    }

    @TypeConverter
    fun toSocialMediaList(socialMediaListString: String): List<SocialMedia> {
        return Json.decodeFromString(socialMediaListString)
    }
}
