package com.synjones.idcard;

/**
 * Created by zhaodianbo on 2017-5-23.
 */
public class ForeignerIDCard {
    private String  EngName=""; 	//英文姓名       
    private String  Sex="";   		//性别
    private String  IDCardNo=""; 	//永久居留证号码
    private String  Nation=""; 	//国籍或所在地区代码 GB/T 2659-200
    private String  CHNName=""; 	//中文姓名
    private String  UserLifeBegin=""; //证件签发日期
    private String  UserLifeEnd="";	//证件终止日期
    private String  Born="";		//出生日期
    private String  CertVol="";	//证件版本号
    private String  GrantDep=""; 	//当次申请受理机关代码
    private String  CertType=""; 	//证件类型标识，大写字母“I”
    private String  reserved=""; 	//保留

    public ForeignerIDCard(){

    }

    public void parse(byte baseData[],int offset){
        EngName=getStringFromData(baseData,offset,120);
        Sex=getStringFromData(baseData,offset+120,2);
        IDCardNo=getStringFromData(baseData,offset+122,30);
        Nation=getStringFromData(baseData,offset+152,6);
        CHNName=getStringFromData(baseData,offset+158,30);
        UserLifeBegin=getStringFromData(baseData,offset+188,16);
        UserLifeEnd=getStringFromData(baseData,offset+204,16);
        Born=getStringFromData(baseData,offset+220,16);
        CertVol=getStringFromData(baseData,offset+236,4);
        GrantDep=getStringFromData(baseData,offset+240,8);
        CertType=getStringFromData(baseData,offset+248,2);
        reserved=getStringFromData(baseData,offset+250,6);
    }


    private String getStringFromData(byte baseData[],int offset,int stringLen){
        try {
            byte[] tmpBuffer = new byte[stringLen];
            System.arraycopy(baseData, offset, tmpBuffer, 0, tmpBuffer.length);
            return new String(tmpBuffer, "UTF-16LE").trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public String getUserLifBebinWithPoint(){

        return dateInsertPoint(UserLifeBegin);
    }
    public String getUserLifEndWithPoint(){

        return dateInsertPoint(UserLifeEnd);
    }

    public String getBirthdayWithSeparator(String separator){
        return dateInsertSeparator(Born, separator);
    }
    private String dateInsertPoint(String date){
        return dateInsertSeparator(date, ".");
    }

    public static String dateInsertSeparator(String date,String separator){
        try {
            Integer.parseInt(date);
        } catch (Exception e) {
            return date;
        }
        if(date.length()!=8)return date;
        StringBuilder sb=new StringBuilder();
        sb.append(date.substring(0,4));
        sb.append(separator);
        sb.append(date.substring(4,6));
        sb.append(separator);
        sb.append(date.substring(6,8));
        return sb.toString();
    }
    public String getYearOfBirth(){
        return getSubInfo(Born, 0, 4);
    }

    public String getMonthOfBirth(){
        return getSubInfo(Born, 4, 6);
    }

    public String getDayOfBirth(){
        return getSubInfo(Born, 6, 8);
    }

    private String getSubInfo(String info,int start,int end){
        try {
            return info.substring(start, end);
        } catch (Exception e) {
            return "";
        }
    }

    public String getEngName() {
        return EngName;
    }

    public void setEngName(String engName) {
        EngName = engName;
    }

    public String getSex() {
        if (Sex.equalsIgnoreCase("1"))
            return "男";
        else
            return "女";
    }

    public String getSexCode(){
        return Sex;
    }

    public void setSex(String sex) {
        Sex = sex;
    }

    public String getIDCardNo() {
        return IDCardNo;
    }

    public void setIDCardNo(String IDCardNo) {
        this.IDCardNo = IDCardNo;
    }

    public String getNation() {
        return Nation;
    }

    public void setNation(String nation) {
        Nation = nation;
    }

    public String getCHNName() {
        return CHNName;
    }

    public void setCHNName(String CHNName) {
        this.CHNName = CHNName;
    }

    public String getUserLifeBegin() {
        return UserLifeBegin;
    }

    public void setUserLifeBegin(String userLifeBegin) {
        UserLifeBegin = userLifeBegin;
    }

    public String getUserLifeEnd() {
        return UserLifeEnd;
    }

    public void setUserLifeEnd(String userLifeEnd) {
        UserLifeEnd = userLifeEnd;
    }

    public String getBorn() {
        return Born;
    }

    public void setBorn(String born) {
        Born = born;
    }

    public String getCertVol() {
        return CertVol;
    }

    public void setCertVol(String certVol) {
        CertVol = certVol;
    }

    public String getGrantDep() {
        return GrantDep;
    }

    public void setGrantDep(String grantDep) {
        GrantDep = grantDep;
    }

    public String getCertType() {
        return CertType;
    }

    public void setCertType(String certType) {
        CertType = certType;
    }

    public String getReserved() {
        return reserved;
    }

    public void setReserved(String reserved) {
        this.reserved = reserved;
    }
}
