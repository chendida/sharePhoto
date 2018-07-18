package com.zq.dynamicphoto.ui.widge;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.KeyEvent;

/**
 * Created by Administrator on 2018/7/16.
 */

@RequiresApi(11)
@TargetApi(11)
class KeyEventCompatHoneycomb {
    public static int normalizeMetaState(int metaState) {
        return KeyEvent.normalizeMetaState(metaState);
    }

    public static boolean metaStateHasModifiers(int metaState, int modifiers) {
        return KeyEvent.metaStateHasModifiers(metaState, modifiers);
    }

    public static boolean metaStateHasNoModifiers(int metaState) {
        return KeyEvent.metaStateHasNoModifiers(metaState);
    }

    public static boolean isCtrlPressed(KeyEvent event) {
        return event.isCtrlPressed();
    }
}
