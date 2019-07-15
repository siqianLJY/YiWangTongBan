package com.synjones.idcard;

import android.graphics.Bitmap;

/**
 * 身份证数据对象
 * @ClassName IDCard
 * @author zhaodianbo@Synjones
 * @date 2013-11-13 上午11:37:27
 *
 */
public class IDCard {
	private String name="";
	private String sex="";
	private String nation="";
	private String birthday="";
	private String address="";
	private String idcardno="";
	private String grantdept="";
	private String userlifebegin="";
	private String userlifeend="";
	private String appendAddress="";
	private String fpName="";
	private String readTime="";
	private byte[] wlt;
	private byte[] fp;
	private byte[] fp1;
	private byte[] fp2;
	private String fpName1="";
	private String fpName2="";
	private String passID="";
	private String issuesTimes="";
	static public byte SW1, SW2, SW3;
	public String strCommandPeroid, strDataPeroid;
	public int decodeResult;
	private Bitmap bmp;
	boolean isBlack=false;
	boolean isWhite=false;

	public static final int CARDTYPE_NORMAL=0;      //普通居民身份证
	public static final int CARDTYPE_FOREIGNER=1;   //外国人永久居住证
	public static final int CARDTYPE_HMT =2;        //港澳台居民居住证

	private ForeignerIDCard foreignerIDCard;

	public ForeignerIDCard getForeignerIDCard() {
		return foreignerIDCard;
	}

	public void setForeignerIDCard(ForeignerIDCard foreignerIDCard) {
		this.foreignerIDCard = foreignerIDCard;
	}

	public int getCardType() {//card type 0=身份证 1=外国人居住证
		return cardType;
	}

	public void setCardType(int cardType) {
		this.cardType = cardType;
	}

	private int cardType=0;

	static final private String[] nations = {
			"解码错",			// 00
			"汉",			// 01
			"蒙古",			// 02
			"回",			// 03
			"藏",			// 04
			"维吾尔",			// 05
			"苗",			// 06
			"彝",			// 07
			"壮",			// 08
			"布依",			// 09
			"朝鲜",			// 10
			"满",			// 11
			"侗",			// 12
			"瑶",			// 13
			"白",			// 14
			"土家",			// 15
			"哈尼",			// 16
			"哈萨克",			// 17
			"傣",			// 18
			"黎",			// 19
			"傈僳",			// 20
			"佤",			// 21
			"畲",			// 22
			"高山",			// 23
			"拉祜",			// 24
			"水",			// 25
			"东乡",			// 26
			"纳西",			// 27
			"景颇",			// 28
			"柯尔克孜",		// 29
			"土",			// 30
			"达斡尔",			// 31
			"仫佬",			// 32
			"羌",			// 33
			"布朗",			// 34
			"撒拉",			// 35
			"毛南",			// 36
			"仡佬",			// 37
			"锡伯",			// 38
			"阿昌",			// 39
			"普米",			// 40
			"塔吉克",			// 41
			"怒",			// 42
			"乌孜别克",		// 43
			"俄罗斯",			// 44
			"鄂温克",			// 45
			"德昴",			// 46
			"保安",			// 47
			"裕固",			// 48
			"京",			// 49
			"塔塔尔",			// 50
			"独龙",			// 51
			"鄂伦春",			// 52
			"赫哲",			// 53
			"门巴",			// 54
			"珞巴",			// 55
			"基诺",			// 56
			"编码错",			// 57
			"其他",			// 97
			"外国血统"			// 98
	};

	public final static String NORMAL_FP_NAME[]={"右手拇指","右手食指","右手中指","右手环指","右手小指",
			"左手拇指","左手食指","左手中指","左手环指","左手小指"};
	public final static String OTHER_FP_NAME[]={"右手不正确指位","左手不正确指位","其他不正确指位"};

	public IDCard() {
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getNation() {
		return nation;
	}
	public String getNationName(String nation) {
		int nationcode = Integer.parseInt(nation);
		if (nationcode>=1 && nationcode<=56) {
			this.nation = nations[nationcode];
		} else if (nationcode == 97) {
			this.nation = "其他";
		} else if (nationcode == 98) {
			this.nation = "外国血统";
		} else {
			this.nation = "编码错";
		}

		return this.nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public String getBirthday() {
		return birthday;
	}

	public String getYearOfBirth(){
		return getSubInfo(birthday, 0, 4);
	}

	public String getMonthOfBirth(){
		return getSubInfo(birthday, 4, 6);
	}

	public String getDayOfBirth(){
		return getSubInfo(birthday, 6, 8);
	}

	private String getSubInfo(String info,int start,int end){
		try {
			return info.substring(start, end);
		} catch (Exception e) {
			return "";
		}
	}

	public boolean isBlack() {
		return isBlack;
	}

	public boolean isWhite() {
		return isWhite;
	}

	public void setWhite(boolean white) {
		isWhite = white;
	}

	public void setIsBlack(boolean isBlack) {
		this.isBlack = isBlack;
	}

	public String getReadTime() {
		return readTime;
	}

	public void setReadTime(String readTime) {
		this.readTime = readTime;
	}



	public String getBirthdayWithSeparator(String separator){
		return dateInsertSeparator(birthday, separator);
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getIDCardNo() {
		return idcardno;
	}
	public void setIDCardNo(String idcardno) {
		this.idcardno = idcardno;
	}
	public String getGrantDept() {
		return grantdept;
	}
	public void setGrantDept(String grantdept) {
		this.grantdept = grantdept;
	}
	public String getUserLifeBegin() {
		return this.userlifebegin;
	}
	public String getUserLifBebinWithPoint(){

		return dateInsertPoint(userlifebegin);
	}

	public void setUserLifeBegin(String userlifebegin) {
		this.userlifebegin = userlifebegin;
	}
	public String getUserLifeEnd() {
		return this.userlifeend;
	}

	public String getUserLifEndWithPoint(){

		return dateInsertPoint(userlifeend);
	}
	public void setUserLifeEnd(String userlifeend) {
		this.userlifeend = userlifeend;
	}

	public String getAppendAddress(){
		return this.appendAddress;
	}

	public void setPhoto(Bitmap b){
		bmp=b;
	}
	public Bitmap getPhoto(){
		return bmp;
	}

	public void setAppendAddress(String appendAddress){
		this.appendAddress=appendAddress;
	}
	public byte[] getWlt() {
		return this.wlt;
	}
	public void setWlt(byte[] wlt) {
		this.wlt = wlt;
	}

	public String getPassID() {
		return passID;
	}

	public void setPassID(String passID) {
		this.passID = passID;
	}

	public String getIssuesTimes() {
		return issuesTimes;
	}

	public void setIssuesTimes(String issuesTimes) {
		this.issuesTimes = issuesTimes;
	}

	public void setFp(byte[] fp){
		//this.fp=fp;
		fp1=null;
		fp2=null;
		if(fp==null||fp.length==0) return;
		if(fp.length==1024){
			fp1=new byte[512];
			fp2=new byte[512];
			System.arraycopy(fp, 0, fp1, 0, 512);
			System.arraycopy(fp, 512, fp2, 0, 512);
			fpName1=getWhichFp(getWhichFpByte(fp1));
			fpName2=getWhichFp(getWhichFpByte(fp2));
		}
		else if(fp.length==512){
			fp1=new byte[512];
			System.arraycopy(fp, 0, fp1, 0, 512);
			fpName1=getWhichFp(getWhichFpByte(fp1));
			fp2=null;
		}
	}

	public void setFp(byte[]fpByte1,byte[]fpByte2){
		if(fpByte1!=null&&fpByte1.length>=512){
			fp1=new byte[512];
			System.arraycopy(fpByte1, 0, fp1, 0, 512);
			fpName1=getWhichFp(getWhichFpByte(fp1));
		}else
			fp1=null;

		if(fpByte2!=null &&fpByte2.length>=512){
			fp2=new byte[512];
			System.arraycopy(fpByte2, 0, fp2, 0, 512);
			fpName2=getWhichFp(getWhichFpByte(fp2));
		}else
			fp2=null;
	}
	/**
	 * 获取指位名称 
	 * @Title getFpName
	 * @param which 1or2
	 * @return
	 */
	public String  getFpName(int which){
		if(which==1){
			return fpName1;
		}else if(which==2){
			return fpName2;
		}
		return fpName1;
	}

	public int getFpCount(){
		int count=0;
		if(fp1!=null)count++;
		if(fp2!=null)count++;
		return count;
	}
	/**
	 * 获取指纹模板
	 * @Title getFp
	 * @param which 1or2
	 * @return
	 */
	public byte[] getFp(int which){
		if(which==1){
			return fp1;
		}else if(which==2){
			return fp2;
		}
		return fp1;
	}
	@Deprecated
	public String  getFpName(){
		return fpName;
	}

	/**
	 * 获取指纹名称描述
	 * @Title getFpDescription
	 * @return
	 */
	public String getFpDescription(){
		if(getFpCount()==0) return "无指纹";
		String ret="指位1:"+getFpName(1)+ getFpRegisterStatus(1)+",质量:"+ getFpQuality(1);
		if(fp2!=null) ret+=", 指位2:"+getFpName(2)+ getFpRegisterStatus(2)+",质量:"+ getFpQuality(2);
		return ret;
	}
	/**
	 * 获取指定指纹的职位代码
	 * @Title getFpIndexCode
	 * @param which
	 * @return
	 */
	public int getFpIndexCode(int which){
		if(which==1){
			return getWhichFpByte(fp1)&0xff;
		}else if(which==2){
			return getWhichFpByte(fp2)&0xff;
		}
		return getWhichFpByte(fp1)&0xff;
	}

	public int getFpQuality(int which){
		int ret=0;
		if(which==1){
			if(fp1!=null&&fp1.length>7)
				ret= fp1[6]&0xff;
		}else if(which==2){
			if(fp2!=null&&fp2.length>7)
				ret= fp2[6]&0xff;
		}
		if(ret>0 && ret<=100)
			return ret;

		return 0;
	}

	public String getFpRegisterStatus(int which){
		int result=0;
		if(which==1){
			if(fp1!=null&&fp1.length>5)
				result= fp1[4]&0xff;
		}else if(which==2){
			if(fp2!=null&&fp2.length>5)
				result= fp2[4]&0xff;
		}
		switch (result){
			case 1:
				return "注册成功";
			case 2:
				return "注册失败";
			case 3:
				return "未注册";
			case 9:
				return "未知";
		}
		return "注册结果错误";
	}


	@Deprecated
	public byte[] getFp(){
		return this.fp;
	}
	public static String getWhichFp(byte whichFpByte){

		int which=whichFpByte&0xff;
		int index=0;
		if(which<=0x14 && which>=0xb){
			index=which-0xb;
			return NORMAL_FP_NAME[index]/*+"("+which+")"*/;
		}
		if(which<=0x63 && which>=0x61){
			index=which-0x61;
			return OTHER_FP_NAME[index]/*+"("+which+")"*/;
		}

		return "无指纹";
	}
	public byte getWhichFpByte(byte fp[]){
		if(fp!=null && fp.length>6)
			return fp[5];
		return -1;
	}

	public static int getCodeFromWhichStr(String which){
		if(which==null) return -1;
		int index=0;
		for(; index< NORMAL_FP_NAME.length; index++){
			if(NORMAL_FP_NAME[index].equals(which))
				return index+0x0b;
		}

		for(index=0; index< OTHER_FP_NAME.length; index++){
			if(OTHER_FP_NAME[index].equals(which))
				return index+0x61;
		}
		return -2;
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
}
