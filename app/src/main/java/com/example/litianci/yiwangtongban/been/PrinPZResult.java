package com.example.litianci.yiwangtongban.been;

/**
 * Created by litianci on 2019/6/14.
 */

public class PrinPZResult {

    /**
     * code : 0
     * msg :
     * data : {"id":"641","member_id":"38","matter_id":"23","status":"6","number":"TZ2019060641","is_window":"0","time":"1560232599","comment_time":"0","password":"812641"}
     * count : 0
     */

    private int code;
    private String msg;
    private DataBean data;
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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public static class DataBean {
        /**
         * id : 641
         * member_id : 38
         * matter_id : 23
         * status : 6
         * number : TZ2019060641
         * is_window : 0
         * time : 1560232599
         * comment_time : 0
         * password : 812641
         */

        private String id;
        private String member_id;
        private String matter_id;
        private String status;
        private String number;
        private String is_window;
        private String time;
        private String comment_time;
        private String password;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMember_id() {
            return member_id;
        }

        public void setMember_id(String member_id) {
            this.member_id = member_id;
        }

        public String getMatter_id() {
            return matter_id;
        }

        public void setMatter_id(String matter_id) {
            this.matter_id = matter_id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getIs_window() {
            return is_window;
        }

        public void setIs_window(String is_window) {
            this.is_window = is_window;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getComment_time() {
            return comment_time;
        }

        public void setComment_time(String comment_time) {
            this.comment_time = comment_time;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
