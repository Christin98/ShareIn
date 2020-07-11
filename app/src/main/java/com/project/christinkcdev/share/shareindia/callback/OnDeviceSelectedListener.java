package com.project.christinkcdev.share.shareindia.callback;

import com.project.christinkcdev.share.shareindia.models.NetworkDevice;

import java.util.List;

public interface OnDeviceSelectedListener {
    void onDeviceSelected(NetworkDevice.Connection connection, List<NetworkDevice.Connection> availableInterfaces);
}
