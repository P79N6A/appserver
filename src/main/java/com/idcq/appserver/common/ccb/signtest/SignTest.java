package com.idcq.appserver.common.ccb.signtest;

import com.idcq.appserver.common.ccb.RSASig;


public class SignTest {
    public static void main(String []args){
        String strSrc;
        String strPubKey;
        String strPriKey;
        String strSign;
        
        strPubKey="30819d300d06092a864886f70d010101050003818b00308187028181009ba4951169c5deecf03a8ddb2fd934f53747c03a211f63bccc84773182bdd8f7159634705041087e4c9053df05326952a143e1aab5e8ba75ed891a91c2db484b66a064abba6605418944d8763814ff23c161101948ec9ef2dfac735b4bb7c7dac18fbf87157b424780eb7080a3e7c9e79dd4841e44a001edfe497b9e3d2181b9020111";
        strPriKey="30820277020100300d06092a864886f70d0101010500048202613082025d020100028181009ba4951169c5deecf03a8ddb2fd934f53747c03a211f63bccc84773182bdd8f7159634705041087e4c9053df05326952a143e1aab5e8ba75ed891a91c2db484b66a064abba6605418944d8763814ff23c161101948ec9ef2dfac735b4bb7c7dac18fbf87157b424780eb7080a3e7c9e79dd4841e44a001edfe497b9e3d2181b9020111028181008954fc004e452e1c5b7ef5a348563dc94ee4f4e7ff1bb25b4b0b783abea783345e575b7228b1da51529d772e31c311a342ffa90009eb7758fec4449ebafdb84126d1d2443dbcec07d9807638ef32cb91bf18eaaa46f6db84de5eba05edfe70ad029449a4cb4de7a95f5c903d6a3fa301f1cc0fe3e29ac72eeab68737f3b2f57d024100d428be0e1463c6b25cc493f23777135a9251b8092f3439c9604d61df8aadb958b947222fd60a489e5de44c379e806015edb0b15030a22cbc5e0ff693fd5bedcf024100bbce1eb6b55f5530f1bb7a437a0f0512f0153d0ada5c5b4ea57c3ea83bd89fe0166d5af1d07f153e83c05eae1585b113c03c8d989bb4d151c96aa78691fac1f7024100bb33020c6c5809ac6ff8bec6a9691113ae481adaed6a511b18bcbfc53e20d0b7b28a0f1b26454f2252d87f7c5ead81f53b236f46c180095ae9959d556714e0e3024100b0c1feca141d7d5b3ddda03f81f004c6879b84beeba237d18cb12be9a1bcd2b4c9d055984bc2e6d16cf14a0d416ec4c74b8449081a1397d48155526089647a51024100bcfe9b05b25578d5d96f80229e015aa58a0af5b0c0aa3ad695fe0d270c4818a737a7abc2f59cf1ea22c7155e06b7d26fba2594e29cb7fd02bd9b6e24b49e425a";
        strSrc="POSID=000000000&BRANCHID=110000000&ORDERID=19991101234&PAYMENT=500.00&CURCODE=01&REMARK1=19991101&REMARK2=北京商户&SUCCESS=Y&ACC_TYPE=11";
        RSASig rsa=new RSASig();
        rsa.setPrivateKey(strPriKey);
        strSign=rsa.generateSigature(strSrc);
        
        System.out.println(strSign);
        
        rsa.setPublicKey(strPubKey);
        if( rsa.verifySigature( strSign,strSrc) ){
            System.out.println("Sign OK");
        }
        else{
            System.out.println("Sign fail");
        }
        
        
        //修改了源串后再验签 注意比较POSID的编号
        strSrc="POSID=000000001&BRANCHID=110000000&ORDERID=19991101234&PAYMENT=500.00&CURCODE=01&REMARK1=19991101&REMARK2=北京商户&SUCCESS=Y&ACC_TYPE=11";
        if( rsa.verifySigature(strSign,strSrc) ){
            System.out.println("Sign OK");
        }
        else{
            System.out.println("Sign failed");
        }
        
        //修改签名串后再验签
        strSrc="POSID=000000000&BRANCHID=110000000&ORDERID=19991101234&PAYMENT=500.00&CURCODE=01&REMARK1=19991101&REMARK2=北京商户&SUCCESS=Y&ACC_TYPE=11";
        strSign='a'+strSign.substring(1,1)+strSign.substring(1,strSign.length());
        System.out.println(strSign);
        if( rsa.verifySigature( strSign,strSrc) ){
            System.out.println("Sign OK");
        }
        else{
            System.out.println("Sign failed");
        }
        
        //修改公钥后再验签 删除一位公钥
        //strPubKey="30819d300d06092a864886f70d010101050003818b00308187028181009ba4951169c5deecf03a8ddb2fd934f53747c03a211f63bccc84773182bdd8f7159634705041087e4c9053df05326952a143e1aab5e8ba75ed891a91c2db484b66a064abba6605418944d8763814ff23c161101948ec9ef2dfac735b4bb7c7dac18fbf87157b424780eb7080a3e7c9e79dd4841e44a001edfe497b9e3d2181b9020111";
        strPubKey="3019d300d06092a864886f70d010101050003818b00308187028181009ba4951169c5deecf03a8ddb2fd934f53747c03a211f63bccc84773182bdd8f7159634705041087e4c9053df05326952a143e1aab5e8ba75ed891a91c2db484b66a064abba6605418944d8763814ff23c161101948ec9ef2dfac735b4bb7c7dac18fbf87157b424780eb7080a3e7c9e79dd4841e44a001edfe497b9e3d2181b9020111";
        strSign=rsa.generateSigature( strSrc);
        rsa.setPublicKey( strPubKey);
        if( rsa.verifySigature( strSign,strSrc) ){
            System.out.println("Sign ok");
        }
        else{
            System.out.println("Sign failed");
        }
        
        //接下来是一些成功的签名验证
        //1
        strSrc="POSID=000000000&BRANCHID=330000000&ORDERID=2004010061&PAYMENT=0.01&CURCODE=01&REMARK1=&REMARK2=&SUCCESS=N";
        strSign="5bf88c409a13963286904e8954a4d825108f9b5bb60a8c8e5cfc05355fe4e247c777b521c7d68b8d51968285d51d1a0da0c5bd55e19268949a20dd7bd14f17422e41f3e6f7446d2136e10e796abc8b8a6f752bed5091374551d84d02f185aa3f9b516ac77ca319b06a8269389de6d7f677c619bfc0c89ccbcb125ae6dd7cc646";
        strPubKey="30819c300d06092a864886f70d010101050003818a003081860281807d1e98e9c10625239ad9116488accf18a95125c83f5ac52f055be47614087b1bc55f1d475ddb0516b6339f7c2a8fd4def86519087cc6ecd8ea4657a5cef26d84890d00772d216e95d0aba1ea9fd39fb02202c82b71333b104e715da5de65be4cf5b83e3c0ba459777fe83a39485f145fccc94b471981348db5beab735c5889f1020111";
        rsa.setPublicKey(strPubKey);
        if( rsa.verifySigature( strSign,strSrc) ){
            System.out.println("Sign OK");
        }
        else{
            System.out.println("Sign failed");
        }
        
        //2
        strSrc="POSID=000000000&BRANCHID=110000000&ORDERID=20041031&PAYMENT=0.01&CURCODE=01&REMARK1=ccb&REMARK2=test&SUCCESS=Y";
        strSign="43680d00f5097caae18b7af3fc936cc79feb621fb166e25affbb52721e2c5c1e656f030dff46e6f0298ef82cf2fd10b6cef34fb2aa270716c30708aeb1abf0520418449614562e891cd5aede8f83b1dd65f76cc81ad5aabfd4aba409da3523ef8e82a7d19055dbb6d9241171893bf282bf64f239677ecd84abbe55fd855f48f3";
        strPubKey="30819f300d06092a864886f70d010101050003818d0030818902818100b466e3a0fa097b57a1bc63c1fd5d97d4ef8d270d538a5aee3d1061f579f02a19cf1543701d94d81f46ce56adb84dca440a7e8f5af40538bb7a88efaf9991ead0fabc63d48fd1f12de658229e30e38ccbd9a631ec9c2d95b8590ea1a01d0931221e062544023a1ed2eb7050853fe56bf8cfd0f18243192d38855a36a87badba790203010001";
        rsa.setPublicKey(strPubKey);
        if( rsa.verifySigature( strSign,strSrc) ){
            System.out.println("Sign OK");
        }
        else{
            System.out.println("Sign failed");
        }
        
        //3
        strSrc="POSID=000000000&BRANCHID=110000000&ORDERID=20041031&PAYMENT=0.01&CURCODE=01&REMARK1=ccb&REMARK2=test&SUCCESS=Y";
        strSign="3183a60f887937846008f4ecfea725af5d65ecaefebea828459193343df7d0943f0fa9e44a298cc9a8e335bece72f8bfce8da3975e21fe4ce4d6c96894d5428e05e896b7da03f7519551b8a09bf1286ea48975b3cd49978eefbb628cc98f4f064feb898518dfb783acdd25eb6f5507fc00c16d1ae69d801a8cb970c4b7e0959b";
        strPubKey="30819f300d06092a864886f70d010101050003818d0030818902818100d0e57a2ebbc82801980de2ad7101c67dc137432bb6ced45882b8d41cbfec7519ae8bf18b2584ae460d7d437aec069ec907935e4b39c72a6291e43a6a88c3405565357dc23c46b7072e6e50b1da4cd9cfdec616cb6ad43f0b013040307973d63b889e78fdd1389714adec663acefe5c974e513a063ba9acb96f590139b0fc571b0203010001";
        rsa.setPublicKey(strPubKey);
        if( rsa.verifySigature( strSign,strSrc) ){
            System.out.println("Sign OK");
        }
        else{
            System.out.println("Sign failed");
        }
        
        //接下来是一些错误的验签
        //1
        strSrc="POSID=000000000&BRANCHID=330000000&ORDERID=120040915091516271800047&PAYMENT=0.01&CURCODE=01&REMARK1=&REMARK2=&SUCCESS=Y";
        strSign="2732e323aa1d4f460bed516a79944001fbad2c93bbdb0d22c71066f4a69528c8699da27c13893bfcefea14dabb6f3cfa93e1414e8782124c99ffc8b059aad5f7f543993f28d262147b5206aea8d2b72aa34a256a4a5bb9c90c9aa8d2897eae90b581e7e6091fcfbb7f4885711b75b95ba7982f7519dc166e20d8b8294e4af2ad";
        strPubKey="30819c300d06092a864886f70d010101050003818a003081860281807e4caba7c0ff9f593bb03ac8e64fcc76ebdf728b3b54493c3f62c7c94e8663d8505da39b08b00df4320c5a49d54c7774044fcc42937a0fb6a3706f724f872fe5f998cc48eb20875902b4b935e14df77b7aeb9224a5cf2db765b20fe56d8f4d5a9e03ab7943a41a179f8240e5311b3957971921fe9ccb9c24c828e99f91cc33f1020111";
        rsa.setPublicKey(strPubKey);
        if( rsa.verifySigature( strSign,strSrc) ){
            System.out.println("Sign OK");
        }
        else{
            System.out.println("Sign failed");
        }
    }
}
