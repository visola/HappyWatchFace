package com.github.visola.happywatchface;

import android.os.Handler;
import android.os.Message;

public class UpdateTimeHandler extends Handler {

    public static final int MSG_UPDATE_TIME = 0;

    private final HappyWatchFace.HappyWatchFaceEngine engine;

    public UpdateTimeHandler(HappyWatchFace.HappyWatchFaceEngine engine) {
        this.engine = engine;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_UPDATE_TIME:
                engine.invalidate();
                if (engine.shouldTimerBeRunning()) {
                    sendEmptyMessageDelayed(MSG_UPDATE_TIME, 500);
                }
                break;
        }
    }

}
