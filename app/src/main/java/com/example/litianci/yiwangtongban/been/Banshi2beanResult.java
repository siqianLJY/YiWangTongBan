package com.example.litianci.yiwangtongban.been;

import java.util.List;

/**
 * Created by litianci on 2019/5/7.
 */

public class Banshi2beanResult {

    /**
     * code : 0
     * msg : ok
     * data : [{"id":2,"name":"独生子女父母年老时一次性奖励","dept_code":"wjb","dept_person":"dingyunfei","dept_name":"wjb","type1":31,"type2":11,"type3":null,"type4":null,"number":"TZSX0002","time_limit":"每年10月\u2014\u201411月办理","handle_time":"2019-04-20","telphone":"010-8046233","address":"北京市顺义区天竺镇政务服务中心","mode":"0","is_post":"0","is_others":"0","howto":"","is_pay":"0","pay_info":"","result":"千元奖励金","feedback":"每年11月底办结，奖励直接汇款到申请人农商银行存折或卡","run1st":"1","population":"","process":"\\public\\uploads\\20190420\\93b25968b6087d2f44d9545d9980ffa6.png","basis":"《北京市人口与计划生育条例》《北京市计划生育委员会 北京市人事局 北京市财政局 北京市劳动和社会保障局关于落实<北京市人口与计划生育条例>规定的有关奖励问题的通知》（京计生委字【2003】112号）、《北京市卫生健康委员会关于在办理计划生育事项中取消生育情况证明的通知》京卫办【2018】61号","process_flag":"2","is_delete":"0"},{"id":5,"name":"《北京市独生子女父母光荣证》","dept_code":"wjb","dept_person":"dingyunfei","dept_name":"wjb","type1":31,"type2":11,"type3":null,"type4":null,"number":"TZSX0005","time_limit":"当场办结","handle_time":"2019-04-20","telphone":"010-8046233","address":"北京市顺义区天竺镇政务服务中心","mode":"0","is_post":"0","is_others":"0","howto":"如何办理描述如何办理描述如何办理描述如何办理描述如何办理描述如何办理描述如何办理描述如何办理描述如何办理描述如何办理描述如何办理描述如何办理描述如何办理描述如何办理描述如何办理描述如何办理描述如何办理描述如何办理描述如何办理描述如何办理描述如何办理描述如何办理描述如何办理描述如何办理描述如何办理描述如何办理描述如何办理描述如何办理描述如何办理描述如何办理描述如何办理描述如何办理描述如何办理描述如何办理描述如何办理描述如何办理描述如何办理描述","is_pay":"0","pay_info":"《北京市人口与计划生育条例》、北京市人口和计划生育委员会关于印发《关于办理<独生子女父母光荣证>的规定》的通知 京人口发【2005】17号、北京市卫生和计划生育委员会关于《独生子女父母光荣证》有关问题的通知 京卫家庭【2016】6号、《北京市卫生健康委员会关于在办理计划生育事项中取消生育情况证明的通知》京卫办【2018】61号","result":"","feedback":"","run1st":"1","population":"","process":"\\public\\uploads\\20190420\\8f4298e6d62a27854d3c9153319a689e.png","basis":"","process_flag":"0","is_delete":"0"}]
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
         * id : 2
         * name : 独生子女父母年老时一次性奖励
         * dept_code : wjb
         * dept_person : dingyunfei
         * dept_name : wjb
         * type1 : 31
         * type2 : 11
         * type3 : null
         * type4 : null
         * number : TZSX0002
         * time_limit : 每年10月——11月办理
         * handle_time : 2019-04-20
         * telphone : 010-8046233
         * address : 北京市顺义区天竺镇政务服务中心
         * mode : 0
         * is_post : 0
         * is_others : 0
         * howto :
         * is_pay : 0
         * pay_info :
         * result : 千元奖励金
         * feedback : 每年11月底办结，奖励直接汇款到申请人农商银行存折或卡
         * run1st : 1
         * population :
         * basis : 《北京市人口与计划生育条例》《北京市计划生育委员会 北京市人事局 北京市财政局 北京市劳动和社会保障局关于落实<北京市人口与计划生育条例>规定的有关奖励问题的通知》（京计生委字【2003】112号）、《北京市卫生健康委员会关于在办理计划生育事项中取消生育情况证明的通知》京卫办【2018】61号
         * process_flag : 2
         * is_delete : 0
         */

        private int id;
        private String name;
        private String dept_code;
        private String dept_person;
        private String dept_name;
        private int type1;
        private int type2;
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
        private String process;
        private String basis;
        private String process_flag;
        private String is_delete;

        public int getId() {
            return id;
        }

        public void setId(int id) {
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

        public int getType1() {
            return type1;
        }

        public void setType1(int type1) {
            this.type1 = type1;
        }

        public int getType2() {
            return type2;
        }

        public void setType2(int type2) {
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

        public String getProcess() {
            return process;
        }

        public void setProcess(String process) {
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
