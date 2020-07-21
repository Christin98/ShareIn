package com.project.christinkcdev.share.sharein.ui.callback;

import com.project.christinkcdev.share.sharein.models.NetworkDevice;

public interface NetworkDeviceSelectedListener {
    boolean onNetworkDeviceSelected(NetworkDevice networkDevice, NetworkDevice.Connection connection);

    boolean isListenerEffective();
}
