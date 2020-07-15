package com.project.christinkcdev.share.shareindia.ui.callback;

import com.project.christinkcdev.share.shareindia.models.NetworkDevice;

public interface NetworkDeviceSelectedListener {
    boolean onNetworkDeviceSelected(NetworkDevice networkDevice, NetworkDevice.Connection connection);

    boolean isListenerEffective();
}
