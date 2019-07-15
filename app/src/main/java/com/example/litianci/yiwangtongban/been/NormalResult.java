package com.example.litianci.yiwangtongban.been;

/**
 * Created by litianci on 2019/5/28.
 */

public class NormalResult {

    /**
     * code : 0
     * msg : 4
     * data :
     * count : 0
     */

    private int code;
    private String data;
    private String msg;
    private int count;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
