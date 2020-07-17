package com.project.christinkcdev.share.sharein.callback;

import com.project.christinkcdev.share.sharein.models.NetworkDevice;

import java.util.List;

public interface OnDeviceSelectedListener {
    void onDeviceSelected(NetworkDevice.Connection connection, List<NetworkDevice.Connection> availableInterfaces);
}
