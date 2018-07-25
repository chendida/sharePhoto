package com.zq.dynamicphoto.view;

import com.tencent.imsdk.TIMGroupMemberInfo;
import com.tencent.imsdk.ext.group.TIMGroupCacheInfo;

import java.util.List;

/**
 * Created by Administrator on 2018/7/25.
 */

public interface GroupListener {
    void onMemberJoin(String s, List<TIMGroupMemberInfo> list, String nick);

    void onMemberQuit(String s, List<String> list, String nick);

    void onMemberUpdate(String s, List<TIMGroupMemberInfo> list);

    void onGroupDelete(String s);

    void onGroupUpdate(TIMGroupCacheInfo timGroupCacheInfo);

    void onGroupAdd(TIMGroupCacheInfo timGroupCacheInfo);
}
