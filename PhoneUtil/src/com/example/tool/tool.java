package com.example.tool;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
 
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
 
/**
 * ��ȡ�ֻ���Ϣ������
 * @author linin
 */
public class tool {
 
    private static tool instance;
 
    private TelephonyManager tm;
    private Activity act;
 
    private tool(Activity act){
        tm = (TelephonyManager) act.getSystemService(Context.TELEPHONY_SERVICE);
        this.act = act;
    }
 
    public static tool getInstance(Activity act){
        if (instance==null) {
            instance = new tool(act);
        }else if(instance.act!=act){
            instance = new tool(act);
        }
        return instance;
    }
 
    /**�Ƿ��ڷ���ģʽ */
    @SuppressWarnings("deprecation")
	public boolean isAirModeOpen() {
        return (Settings.System.getInt(act.getContentResolver(),
                Settings.System.AIRPLANE_MODE_ON, 0) == 1 ? true : false);
    }
 
    /**��ȡ�ֻ�����*/
    public String getPhoneNumber(){
        return tm==null?null:tm.getLine1Number();
    }
 
    /**��ȡ�������ͣ���ʱ�ò�����*/
    public int getNetWorkType(){
        return tm==null?0:tm.getNetworkType();
    }
 
    /**��ȡ�ֻ�sim�������кţ�IMSI��*/
    public String getIMSI(){
        return tm==null?null:tm.getSubscriberId();
    }
 
    /**��ȡ�ֻ�IMEI*/
    public String getIMEI(){
        return tm==null?null:tm.getDeviceId();
    }
 
    /**��ȡ�ֻ��ͺ�*/
    public static String getModel(){
        return android.os.Build.MODEL;
    }
 
    /**��ȡ�ֻ�Ʒ��*/
    public static String getBrand(){
        return android.os.Build.BRAND;
    }
 
    /**��ȡ�ֻ�ϵͳ�汾*/
    public static String getVersion(){
        return android.os.Build.VERSION.RELEASE;
    }
 
    /**����ֻ�ϵͳ���ڴ�*/
    public String getTotalMemory() {  
        String str1 = "/proc/meminfo";// ϵͳ�ڴ���Ϣ�ļ�   
        String str2;  
        String[] arrayOfString;  
        long initial_memory = 0;  
 
        try {  
            FileReader localFileReader = new FileReader(str1);  
            BufferedReader localBufferedReader = new BufferedReader(  
                    localFileReader, 8192);  
            str2 = localBufferedReader.readLine();// ��ȡmeminfo��һ�У�ϵͳ���ڴ��С   
 
            arrayOfString = str2.split("\\s+");  
 
            initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// ���ϵͳ���ڴ棬��λ��KB������1024ת��ΪByte   
            localBufferedReader.close();  
 
        } catch (IOException e) {  
        }  
        return Formatter.formatFileSize(act, initial_memory);// Byteת��ΪKB����MB���ڴ��С���   
    }
    /**��ȡ�ֻ��Ƿ����Զ�����*/
    public  boolean isAutoBrightness(ContentResolver aContentResolver) {    
		boolean automicBrightness = false;    
		try{        
			automicBrightness = Settings.System.getInt(aContentResolver,                
			Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;   
		 } 
		catch(SettingNotFoundException e) 
		{       
			e.printStackTrace();  
		 }    
		return automicBrightness;
	} 
    
    /**��ȡ�ֻ���Ļ��*/
    public int getScreenWidth(){
    	DisplayMetrics dm = new DisplayMetrics();
    	act.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }
 
    /**��ȡ�ֻ����߿�*/
    public int getScreenHeight(){
    	DisplayMetrics dm = new DisplayMetrics();
    	act.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }
     
    /**��ȡӦ�ð���*/
    public String getPackageName(){
        return act.getPackageName();
    }
 
    /** 
     * ��ȡ�ֻ�MAC��ַ 
     * ֻ���ֻ�����wifi���ܻ�ȡ��mac��ַ 
     */ 
    public String getMacAddress(){  
        String result = "";  
        WifiManager wifiManager = (WifiManager) act.getSystemService(Context.WIFI_SERVICE);  
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();  
        result = wifiInfo.getMacAddress();  
        return result;  
    }  
 
    /** 
     * ��ȡ�ֻ�CPU��Ϣ //1-cpu�ͺ�  //2-cpuƵ��  
     */ 
    public String[] getCpuInfo() {  
        String str1 = "/proc/cpuinfo";  
        String str2 = "";  
        String[] cpuInfo = {"", ""};  //1-cpu�ͺ�  //2-cpuƵ��  
        String[] arrayOfString;  
        try {  
            FileReader fr = new FileReader(str1);  
            BufferedReader localBufferedReader = new BufferedReader(fr, 8192);  
            str2 = localBufferedReader.readLine();  
            arrayOfString = str2.split("\\s+");  
            for (int i = 2; i < arrayOfString.length; i++) {  
                cpuInfo[0] = cpuInfo[0] + arrayOfString[i] + " ";  
            }  
            str2 = localBufferedReader.readLine();  
            arrayOfString = str2.split("\\s+");  
            cpuInfo[1] += arrayOfString[2];  
            localBufferedReader.close();  
        } catch (IOException e) {
        }
        return cpuInfo;  
    }
     
    /**��ȡApplication�е�meta-data����*/
    public String getMetaData(String name){
        String result = "";
        try {
            ApplicationInfo appInfo = act.getPackageManager()
                    .getApplicationInfo(getPackageName(),PackageManager.GET_META_DATA);
            result = appInfo.metaData.getString(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
 
}
