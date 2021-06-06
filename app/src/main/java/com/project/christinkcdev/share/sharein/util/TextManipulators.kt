package com.project.christinkcdev.share.sharein.util

import android.content.Context
import androidx.collection.ArrayMap
import com.project.christinkcdev.share.sharein.R
import com.project.christinkcdev.share.sharein.config.AppConfig
import java.net.NetworkInterface

object TextManipulators {
    fun makeWebShareLink(context: Context, address: String?): String {
        return context.getString(R.string.mode_webShareAddress, address, AppConfig.SERVER_PORT_WEBSHARE)
    }

    fun getLetters(text: String = "?", length: Int): String {
        val breakAfter = length - 1
        val stringBuilder = StringBuilder()
        for (letter in text.split(" ".toRegex()).toTypedArray()) {
            if (stringBuilder.length > breakAfter) break
            if (letter.isEmpty()) continue
            stringBuilder.append(letter[0])
        }
        return stringBuilder.toString().toUpperCase()
    }

    fun String.toFriendlySsid() = this.replace("\"", "").let {
        if (it.startsWith(AppConfig.PREFIX_ACCESS_POINT))
            it.substring((AppConfig.PREFIX_ACCESS_POINT.length))
        else it
    }.replace("_", " ")

    fun toNetworkTitle(adapterName: String): Int {
        val unknownInterface = R.string.text_interfaceUnknown
        val associatedNames: MutableMap<String, Int> = ArrayMap()
        associatedNames["wlan"] = R.string.text_interfaceWireless
        associatedNames["p2p"] = R.string.text_interfaceWifiDirect
        associatedNames["bt-pan"] = R.string.text_interfaceBluetooth
        associatedNames["eth"] = R.string.text_interfaceEthernet
        associatedNames["tun"] = R.string.text_interfaceVPN
        associatedNames["unk"] = unknownInterface
        for (displayName in associatedNames.keys) if (adapterName.startsWith(displayName)) {
            return associatedNames[displayName] ?: unknownInterface
        }
        return -1
    }

    fun String.toNetworkTitle(context: Context): String {
        val adapterNameResource = toNetworkTitle(this)
        return if (adapterNameResource == -1) this else context.getString(adapterNameResource)
    }

    fun NetworkInterface.toNetworkTitle(context: Context): String {
        return this.displayName.toNetworkTitle(context)
    }
}