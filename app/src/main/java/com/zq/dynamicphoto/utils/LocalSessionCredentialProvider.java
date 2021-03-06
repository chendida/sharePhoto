package com.zq.dynamicphoto.utils;

import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.qcloud.core.auth.BasicLifecycleCredentialProvider;
import com.tencent.qcloud.core.auth.QCloudLifecycleCredentials;
import com.tencent.qcloud.core.auth.SessionQCloudCredentials;

/**
 * Created by Administrator on 2018/3/7.
 */

public class LocalSessionCredentialProvider extends BasicLifecycleCredentialProvider {
    private String tempSecretId;
    private String tempSecretKey;
    private String sessionToken;
    private long expiredTime;

    public LocalSessionCredentialProvider(String tempSecretId, String tempSecretKey, String sessionToken, long expiredTime) {
        this.tempSecretId = tempSecretId;
        this.tempSecretKey = tempSecretKey;
        this.sessionToken = sessionToken;
        this.expiredTime = expiredTime;
    }

    /**
     返回 SessionQCloudCredential
     */
    @Override
    public QCloudLifecycleCredentials fetchNewCredentials() throws CosXmlClientException {
        return new SessionQCloudCredentials(tempSecretId, tempSecretKey, sessionToken, expiredTime);
    }
}
