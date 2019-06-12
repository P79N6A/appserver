package com.idcq.appserver.service.file;

public class DownloadUtils
{      
    
    
    /**
     * 获得下载头的Content-Disposition,
     * 这里使用简单的传参拼凑。
     * @param seed
     * @param fileName
     * @param opt
     * @return
     */
    public static String getContentDisposition(String seed, String fileName){
        String opMode = null;
        if(seed == null){
            seed = "";
        }
        if("JPEG".equalsIgnoreCase(seed) || "JPG".equalsIgnoreCase(seed)){
            opMode = "inline";
        }else{
            opMode="attachment";
        }
        return opMode + ";filename=" + fileName;
    }
    
    /**
     * 获取得下载的contentType属性,这里简单处理(可能通过配置文件或者外部源来处理)
     * @param seed 直接传过来文件类型，如jpg...
     * @return
     */
    public static String getContentType(String seed){
        String re = null;
        if(seed == null){
            seed = "";
        }
        if("JPEG".equalsIgnoreCase(seed) || "JPG".equalsIgnoreCase(seed)){
            re = "image/jpeg;charset=UTF-8";
        }else{
            re = "application/octet-stream; charset=utf-8";
        }
        return re;
    }
}
