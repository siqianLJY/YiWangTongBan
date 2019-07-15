package com.example.litianci.yiwangtongban.cominterface;


import com.example.litianci.yiwangtongban.cominterface.otg.OtgInterface;
import com.example.litianci.yiwangtongban.cominterface.serial.SerialInterface;

public class ReaderInterfaceFactory {
	
	public static final int READER_BLUETOOTH=0;
	public static final int READER_HANDSET_718D=1;//718d handset
	public static final int READER_SERIAL=2;
	public static final int READER_USB=3;
	public static final int READER_USB_2303SERIAL=4;
	public static final int READER_S8=5;
	public static final String READER_INTERFACE[]={"蓝牙","手持机718D","串口","USB OTG","USB2303转串口","S8手持机"};
	
	private static  int WHICH_READER=READER_USB;

	public static void setWhichReader(int whichReader){
        if(whichReader>=0)
		    WHICH_READER=whichReader;
	}

    public static int getWhichReader(){
        return WHICH_READER;
    }
	
	public static ReaderInterface create(){
		switch (WHICH_READER) {
            case READER_BLUETOOTH:
               // return new BluetoothInterface();
            case READER_HANDSET_718D:
                //return new Handset718DInterface();
            case READER_SERIAL:
                return new SerialInterface();
            case READER_USB:
                return new OtgInterface();
            case READER_USB_2303SERIAL:
				//return new USB2303Interface();
			case READER_S8:
				//return new S8Interface();
		default:
			break;
		}
		return new OtgInterface();
	}
}
