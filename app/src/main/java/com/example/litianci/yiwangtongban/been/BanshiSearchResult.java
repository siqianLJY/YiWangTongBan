package com.example.litianci.yiwangtongban.been;

import java.util.List;

/**
 * Created by litianci on 2019/6/10.
 */

public class BanshiSearchResult {

    /**
     * code : 0
     * msg : ok
     * data : [{"id":"23","name":"白内障复明手术","dept_code":"cl","dept_person":"tianweiping","dept_name":"残联","type1":"22","type2":"12","type3":null,"type4":null,"number":"TZSX0023","time_limit":"5个工作日","handle_time":"周一到周五9:00-17:00","telphone":"010-80462326","address":"北京市顺义区天竺镇政务服务中心","mode":"0","is_post":"0","is_others":"0","howto":"网上申请后请携带村（居）委会开具的贫困证明（原件）、身份证原件、户口本原件至北京市顺义区天竺镇政务服务中心窗口办理。","is_pay":"0","pay_info":"","result":"","feedback":"","run1st":"1","population":"","process":null,"basis":"顺残字（2017)2号 关于印发顺义区白内障患者复明手术补贴实施方案的通知","process_flag":"1","is_delete":"0"}]
     * count : 0
     */

    private int code;
    private String msg;
    private int count;
    private List<DataBean> data;

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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 23
         * name : 白内障复明手术
         * dept_code : cl
         * dept_person : tianweiping
         * dept_name : 残联
         * type1 : 22
         * type2 : 12
         * type3 : null
         * type4 : null
         * number : TZSX0023
         * time_limit : 5个工作日
         * handle_time : 周一到周五9:00-17:00
         * telphone : 010-80462326
         * address : 北京市顺义区天竺镇政务服务中心
         * mode : 0
         * is_post : 0
         * is_others : 0
         * howto : 网上申请后请携带村（居）委会开具的贫困证明（原件）、身份证原件、户口本原件至北京市顺义区天竺镇政务服务中心窗口办理。
         * is_pay : 0
         * pay_info :
         * result :
         * feedback :
         * run1st : 1
         * population :
         * process : null
         * basis : 顺残字（2017)2号 关于印发顺义区白内障患者复明手术补贴实施方案的通知
         * process_flag : 1
         * is_delete : 0
         */

        private String id;
        private String name;
        private String dept_code;
        private String dept_person;
        private String dept_name;
        private String type1;
        private String type2;
        private Object type3;
        private Object type4;
        private String number;
        private String time_limit;
        private String handle_time;
        private String telphone;
        private String address;
        private String mode;
        private String is_post;
        private String is_others;
        private String howto;
        private String is_pay;
        private String pay_info;
        private String result;
        private String feedback;
        private String run1st;
        private String population;
        private Object process;
        private String basis;
        private String process_flag;
        private String is_delete;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDept_code() {
            return dept_code;
        }

        public void setDept_code(String dept_code) {
            this.dept_code = dept_code;
        }

        public String getDept_person() {
            return dept_person;
        }

        public void setDept_person(String dept_person) {
            this.dept_person = dept_person;
        }

        public String getDept_name() {
            return dept_name;
        }

        public void setDept_name(String dept_name) {
            this.dept_name = dept_name;
        }

        public String getType1() {
            return type1;
        }

        public void setType1(String type1) {
            this.type1 = type1;
        }

        public String getType2() {
            return type2;
        }

        public void setType2(String type2) {
            this.type2 = type2;
        }

        public Object getType3() {
            return type3;
        }

        public void setType3(Object type3) {
            this.type3 = type3;
        }

        public Object getType4() {
            return type4;
        }

        public void setType4(Object type4) {
            this.type4 = type4;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getTime_limit() {
            return time_limit;
        }

        public void setTime_limit(String time_limit) {
            this.time_limit = time_limit;
        }

        public String getHandle_time() {
            return handle_time;
        }

        public void setHandle_time(String handle_time) {
            this.handle_time = handle_time;
        }

        public String getTelphone() {
            return telphone;
        }

        public void setTelphone(String telphone) {
            this.telphone = telphone;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }

        public String getIs_post() {
            return is_post;
        }

        public void setIs_post(String is_post) {
            this.is_post = is_post;
        }

        public String getIs_others() {
            return is_others;
        }

        public void setIs_others(String is_others) {
            this.is_others = is_others;
        }

        public String getHowto() {
            return howto;
        }

        public void setHowto(String howto) {
            this.howto = howto;
        }

        public String getIs_pay() {
            return is_pay;
        }

        public void setIs_pay(String is_pay) {
            this.is_pay = is_pay;
        }

        public String getPay_info() {
            return pay_info;
        }

        public void setPay_info(String pay_info) {
            this.pay_info = pay_info;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public String getFeedback() {
            return feedback;
        }

        public void setFeedback(String feedback) {
            this.feedback = feedback;
        }

        public String getRun1st() {
            return run1st;
        }

        public void setRun1st(String run1st) {
            this.run1st = run1st;
        }

        public String getPopulation() {
            return population;
        }

        public void setPopulation(String population) {
            this.population = population;
        }

        public Object getProcess() {
            return process;
        }

        public void setProcess(Object process) {
            this.process = process;
        }

        public String getBasis() {
            return basis;
        }

        public void setBasis(String basis) {
            this.basis = basis;
        }

        public String getProcess_flag() {
            return process_flag;
        }

        public void setProcess_flag(String process_flag) {
            this.process_flag = process_flag;
        }

        public String getIs_delete() {
            return is_delete;
        }

        public void setIs_delete(String is_delete) {
            this.is_delete = is_delete;
        }
    }
}
