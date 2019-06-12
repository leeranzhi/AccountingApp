package com.leecode1988.accountingapp;

/**
 * author:LeeCode
 * create:2019/6/11 22:46
 */
public class ImageResponse {
    private int state;
    private String msg;
    private Api_res api_res;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Api_res getApi_res() {
        return api_res;
    }

    public void setApi_res(Api_res api_res) {
        this.api_res = api_res;
    }

    public class Api_res{
        private String code;
        private String img_url;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }
    }
}
