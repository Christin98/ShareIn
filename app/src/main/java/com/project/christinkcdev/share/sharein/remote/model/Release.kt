package com.project.christinkcdev.share.sharein.remote.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Release(
    @field:SerializedName("tag_name") val tag: String,
    @field:SerializedName("name") val name: String,
    @field:SerializedName("body") val changelog: String,
    @field:SerializedName("prerelease") val prerelase: Boolean,
    @field:SerializedName("published_at") val publishDate: String,
    @field:SerializedName("html_url") val url: String,
    @field:SerializedName("assets") val assets: Array<Asset>?,
) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Release

        if (tag != other.tag) return false
        if (name != other.name) return false
        if (changelog != other.changelog) return false
        if (prerelase != other.prerelase) return false
        if (publishDate != other.publishDate) return false
        if (url != other.url) return false
        if (assets != null) {
            if (other.assets == null) return false
            if (!assets.contentEquals(other.assets)) return false
        } else if (other.assets != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = tag.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + changelog.hashCode()
        result = 31 * result + prerelase.hashCode()
        result = 31 * result + publishDate.hashCode()
        result = 31 * result + url.hashCode()
        result = 31 * result + (assets?.contentHashCode() ?: 0)
        return result
    }
}

@Parcelize
data class Asset(
    @field:SerializedName("name") val name: String,
    @field:SerializedName("url") val url: String,
) : Parcelable