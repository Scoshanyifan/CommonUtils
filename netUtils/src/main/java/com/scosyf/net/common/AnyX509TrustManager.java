/**
 * Copyright (c) 2016, MicroAnts. All rights reserved.
 */
package com.scosyf.net.common;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/**
 * https连接用证书信任管理器。
 * <p>
 * 本信任管理器信任任何证书，各类型的https应用需要继承本类来定制各自的证书信任管理器。
 * 
 * @author <a href="mailto:shiban@microants.cn">石斑</a>
 * @version 1.0.0 2016年12月18日
 * @since 1.0.0
 */
public class AnyX509TrustManager implements X509TrustManager {

    /*
     * (non-Javadoc)
     * @see javax.net.ssl.X509TrustManager#checkClientTrusted(java.security.cert.X509Certificate[], java.lang.String)
     */
    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see javax.net.ssl.X509TrustManager#checkServerTrusted(java.security.cert.X509Certificate[], java.lang.String)
     */
    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see javax.net.ssl.X509TrustManager#getAcceptedIssuers()
     */
    @Override
    public X509Certificate[] getAcceptedIssuers() {
        // TODO Auto-generated method stub
        return null;
    }

}
