package com.project.christinkcdev.share.shareindia.ui;

import com.genonbeta.android.framework.util.Interrupter;

public interface UITask {
    void updateTaskStarted(final Interrupter interrupter);

    void updateTaskStopped();
}
