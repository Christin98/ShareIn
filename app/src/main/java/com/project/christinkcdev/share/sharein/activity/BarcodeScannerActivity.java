package com.project.christinkcdev.share.sharein.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;

import com.project.christinkcdev.share.sharein.R;
import com.project.christinkcdev.share.sharein.app.Activity;
import com.project.christinkcdev.share.sharein.fragment.BarcodeConnectFragment;
import com.project.christinkcdev.share.sharein.models.NetworkDevice;
import com.project.christinkcdev.share.sharein.ui.callback.NetworkDeviceSelectedListener;

public class BarcodeScannerActivity extends Activity {

//    public static final String
//            EXTRA_DEVICE = "extraDevice",
//            EXTRA_CONNECTION = "extraConnectionAdapter";


    public static final String EXTRA_DEVICE_ID = "extraDeviceId";
    public static final String EXTRA_CONNECTION_ADAPTER = "extraConnectionAdapter";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setResult(RESULT_CANCELED);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        BarcodeConnectFragment fragment = (BarcodeConnectFragment) getSupportFragmentManager().findFragmentById(R.id.barcodeScannerFragment);

        if (fragment != null)
            fragment.setDeviceSelectedListener(new NetworkDeviceSelectedListener()
            {
                @Override
                public boolean onNetworkDeviceSelected(NetworkDevice networkDevice, NetworkDevice.Connection connection)
                {
                    setResult(RESULT_OK, new Intent()
                            .putExtra(EXTRA_DEVICE_ID, networkDevice.deviceId)
                            .putExtra(EXTRA_CONNECTION_ADAPTER, connection.adapterName));
                    finish();

                    return true;
                }

                @Override
                public boolean isListenerEffective()
                {
                    return true;
                }
            });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (android.R.id.home == item.getItemId())
            onBackPressed();
        else
            return super.onOptionsItemSelected(item);

        return true;
    }
}