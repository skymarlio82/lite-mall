
package org.linlinjava.litemall.wx.model.wxpay;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.linlinjava.litemall.wx.config.MyWxPayProperties;

public class MyWxPayConfig extends WXPayConfig {

	private MyWxPayProperties props = null;
    private byte[] certData = null;

    public MyWxPayConfig(MyWxPayProperties props) throws Exception {
    	this.props = props;
        String certPath = props.getKeypath();
        File file = new File(certPath);
        InputStream certStream = new FileInputStream(file);
        this.certData = new byte[(int) file.length()];
        certStream.read(this.certData);
        certStream.close();
    }

    public String getAppID() {
        return this.props.getAppid();
    }

    public String getMchID() {
        return this.props.getMchid();
    }

    public String getKey() {
        return this.props.getMchkey();
    }

    public InputStream getCertStream() {
        ByteArrayInputStream certBis = new ByteArrayInputStream(this.certData);
        return certBis;
    }

    public int getHttpConnectTimeoutMs() {
        return 8000;
    }

    public int getHttpReadTimeoutMs() {
        return 10000;
    }

    @Override
    IWXPayDomain getWXPayDomain() {
        return WXPayDomainSimpleImpl.instance();
    }
}