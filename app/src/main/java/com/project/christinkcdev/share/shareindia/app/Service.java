package com.project.christinkcdev.share.shareindia.app;

import android.content.SharedPreferences;

import com.project.christinkcdev.share.shareindia.database.AccessDatabase;
import com.project.christinkcdev.share.shareindia.util.AppUtils;
import com.project.christinkcdev.share.shareindia.util.NotificationUtils;

abstract public class Service extends android.app.Service {
    private NotificationUtils mNotificationUtils;

    public AccessDatabase getDatabase()
    {
        return AppUtils.getDatabase(this);
    }

    public SharedPreferences getDefaultPreferences()
    {
        return AppUtils.getDefaultPreferences(getApplicationContext());
    }

    public NotificationUtils getNotificationUtils()
    {
        if (mNotificationUtils == null)
            mNotificationUtils = new NotificationUtils(getApplicationContext(), getDatabase(), getDefaultPreferences());

        return mNotificationUtils;
    }
}
