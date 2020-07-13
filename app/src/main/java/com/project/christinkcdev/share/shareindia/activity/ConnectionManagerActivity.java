package com.project.christinkcdev.share.shareindia.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.genonbeta.android.framework.util.Interrupter;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.project.christinkcdev.share.shareindia.R;
import com.project.christinkcdev.share.shareindia.database.AccessDatabase;
import com.project.christinkcdev.share.shareindia.models.NetworkDevice;
import com.project.christinkcdev.share.shareindia.service.CommunicationService;
import com.project.christinkcdev.share.shareindia.util.AppUtils;
import com.project.christinkcdev.share.shareindia.util.NetworkDeviceLoader;

public class ConnectionManagerActivity extends AppCompatActivity {

    public static final String ACTION_CHANGE_FRAGMENT = "com.genonbeta.intent.action.CONNECTION_MANAGER_CHANGE_FRAGMENT";
    public static final String EXTRA_FRAGMENT_ENUM = "extraFragmentEnum";
    public static final String EXTRA_DEVICE_ID = "extraDeviceId";
    public static final String EXTRA_CONNECTION_ADAPTER = "extraConnectionAdapter";
    public static final String EXTRA_REQUEST_TYPE = "extraRequestType";
    public static final String EXTRA_ACTIVITY_SUBTITLE = "extraActivitySubtitle";

//    private final IntentFilter mFilter = new IntentFilter();
//    private HotspotManagerFragment mHotspotManagerFragment;
//    private BarcodeConnectFragment mBarcodeConnectFragment;
//    private NetworkManagerFragment mNetworkManagerFragment;
//    private NetworkDeviceListFragment mDeviceListFragment;
//    private OptionsFragment mOptionsFragment;
//    private AppBarLayout mAppBarLayout;
//    private CollapsingToolbarLayout mToolbarLayout;
//    private ProgressBar mProgressBar;
//    private String mTitleProvided;
    private RequestType mRequestType = RequestType.RETURN_RESULT;

//    private final NetworkDeviceSelectedListener mDeviceSelectionListener = new NetworkDeviceSelectedListener()
//    {
//        @Override
//        public boolean onNetworkDeviceSelected(NetworkDevice networkDevice, NetworkDevice.Connection connection)
//        {
//            if (mRequestType.equals(RequestType.RETURN_RESULT)) {
//                setResult(RESULT_OK, new Intent()
//                        .putExtra(EXTRA_DEVICE_ID, networkDevice.deviceId)
//                        .putExtra(EXTRA_CONNECTION_ADAPTER, connection.adapterName));
//
//                finish();
//            } else {
//                ConnectionUtils connectionUtils = ConnectionUtils.getInstance(ConnectionManagerActivity.this);
//                UIConnectionUtils uiConnectionUtils = new UIConnectionUtils(connectionUtils, ConnectionManagerActivity.this);
//
//                UITask uiTask = new UITask()
//                {
//                    @Override
//                    public void updateTaskStarted(Interrupter interrupter)
//                    {
//                        mProgressBar.setVisibility(View.VISIBLE);
//                    }
//
//                    @Override
//                    public void updateTaskStopped()
//                    {
//                        mProgressBar.setVisibility(View.GONE);
//                    }
//                };
//
//                NetworkDeviceLoader.OnDeviceRegisteredListener registeredListener = new NetworkDeviceLoader.OnDeviceRegisteredListener()
//                {
//                    @Override
//                    public void onDeviceRegistered(AccessDatabase database, final NetworkDevice device, final NetworkDevice.Connection connection)
//                    {
//                        createSnackbar(R.string.mesg_completing).show();
//                    }
//                };
//
//                uiConnectionUtils.makeAcquaintance(ConnectionManagerActivity.this, uiTask,
//                        connection.ipAddress, -1, registeredListener);
//            }
//
//            return true;
//        }
//
//        @Override
//        public boolean isListenerEffective()
//        {
//            return true;
//        }
//    };
//
//    private final BroadcastReceiver mReceiver = new BroadcastReceiver()
//    {
//        @Override
//        public void onReceive(Context context, Intent intent)
//        {
//            if (ACTION_CHANGE_FRAGMENT.equals(intent.getAction())
//                    && intent.hasExtra(EXTRA_FRAGMENT_ENUM)) {
//                String fragmentEnum = intent.getStringExtra(EXTRA_FRAGMENT_ENUM);
//
//                try {
//                    AvailableFragment value = AvailableFragment.valueOf(fragmentEnum);
//
//                    if (AvailableFragment.EnterIpAddress.equals(value))
//                        showEnterIpAddressDialog();
//                    else
//                        setFragment(value);
//                } catch (Exception e) {
//                    // do nothing
//                }
//            } else if (mRequestType.equals(RequestType.RETURN_RESULT)) {
//                if (CommunicationService.ACTION_DEVICE_ACQUAINTANCE.equals(intent.getAction())
//                        && intent.hasExtra(CommunicationService.EXTRA_DEVICE_ID)
//                        && intent.hasExtra(CommunicationService.EXTRA_CONNECTION_ADAPTER_NAME)) {
//                    NetworkDevice device = new NetworkDevice(intent.getStringExtra(CommunicationService.EXTRA_DEVICE_ID));
//                    NetworkDevice.Connection connection = new NetworkDevice.Connection(device.deviceId, intent.getStringExtra(CommunicationService.EXTRA_CONNECTION_ADAPTER_NAME));
//
//                    try {
//                        AppUtils.getDatabase(ConnectionManagerActivity.this).reconstruct(device);
//                        AppUtils.getDatabase(ConnectionManagerActivity.this).reconstruct(connection);
//
//                        mDeviceSelectionListener.onNetworkDeviceSelected(device, connection);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            } else if (mRequestType.equals(RequestType.MAKE_ACQUAINTANCE)) {
//                if (CommunicationService.ACTION_INCOMING_TRANSFER_READY.equals(intent.getAction())
//                        && intent.hasExtra(CommunicationService.EXTRA_GROUP_ID)) {
//                    ViewTransferActivity.startInstance(ConnectionManagerActivity.this,
//                            intent.getLongExtra(CommunicationService.EXTRA_GROUP_ID, -1));
//                    finish();
//                }
//            }
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection_manager);
    }

    public enum RequestType
    {
        RETURN_RESULT,
        MAKE_ACQUAINTANCE
    }

}