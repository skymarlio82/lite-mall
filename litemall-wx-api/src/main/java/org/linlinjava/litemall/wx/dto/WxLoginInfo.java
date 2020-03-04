package org.linlinjava.litemall.wx.dto;

public class WxLoginInfo {

    private String code = null;
    private UserInfo userInfo = null;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}