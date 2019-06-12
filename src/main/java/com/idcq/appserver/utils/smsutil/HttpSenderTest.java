package com.idcq.appserver.utils.smsutil;


public class HttpSenderTest extends Thread{
    int count = 10003;
//    String url = "http://222.73.117.158/msg/";// 应用地址
//    String account = "jiekou-clcs-10";// 账号
//    String pswd = "Tch666777";// 密码
//    String[] mobiles = new String[]{"18657981038", "15810403739", "18566232008", "15012942229", "18617161369"};
//    //{ "18657981038", "15810403739", "13926507073", "13510120404", "18566232008"};
////  String mobile = "15810403739";// 手机号码，多个号码使用","分割
//    boolean needstatus = true;// 是否需要状态报告，需要true，不需要false
//    String product = null;// 产品ID
//    String extno = null;// 扩展码
    
//    public void run() { 
//        try {
//            while (true)
//            {
//                for (int i=0; i<5; i++)
//                {
//                    for (String mobile : mobiles)
//                    {
////                        String code = mobile.substring(mobile.length()-4, mobile.length());
//                        StringBuffer sb  = new StringBuffer();
//                        sb.append("您正在进行一点传奇注册，动态验证码：").append(i).append(count).append("，请勿向任何人提供您收到的验证码");
////                      String msg = "您正在进行一点传奇注册，动态验证码：123456，请勿向任何人提供您收到的验证码";// 短信内容
//                        String returnString = HttpSender.batchSend(url, account, pswd, mobile, sb.toString(), needstatus, product, extno);
//                        System.out.println(returnString);
//                    }
//                }
//                count++;
//                System.out.println("发送次数："+count);
//                Thread.sleep(60000);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    } 
    
	public static void main(String[] args) throws Exception {
	    String url = "http://222.73.117.158/msg/";// 应用地址
	    String account = "sjyinhew";// 账号
	    String pswd = "Sjyinhew123";// 密码
//	    String[] mobiles = new String[]{"18657981038", "15810403739", "18566232008", "15012942229", "18617161369"};
	    //{ "18657981038", "15810403739", "13926507073", "13510120404", "18566232008"};
	//  String mobile = "15810403739";// 手机号码，多个号码使用","分割
	    boolean needstatus = true;// 是否需要状态报告，需要true，不需要false
	    String product = null;// 产品ID
	    String extno = null;// 扩展码
//	    Thread t1 = new HttpSenderTest(); 
//        t1.start(); 
	    StringBuffer sb  = new StringBuffer();
        sb.append("您正在进行一点传奇注册，动态验证码：").append("788990").append("，请勿向任何人提供您收到的验证码");
	    String returnString = HttpSender.batchSend(url, account, pswd, "13692101942", sb.toString(), needstatus, product, extno);
	    String[] a =  returnString.split("\n");
	    System.out.println(a[0]);
	}
	
}
