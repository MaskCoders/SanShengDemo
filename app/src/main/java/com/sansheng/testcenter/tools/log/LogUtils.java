package com.sansheng.testcenter.tools.log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hua on 1/5/16.
 */
public class LogUtils {
    public LogUtils(){

    }
    public static final void main(String[] args){
        com.sansheng.testcenter.tools.log.LogUtils log =new com.sansheng.testcenter.tools.log.LogUtils();
        File file = log.getLastFile();
        System.out.println(file.getName());
    }
    public static final String PATH = "/sdcard/sanshenglog/";

    /**
     * 日志文件名格式yyyy-MM-dd-hh-mm-ss;
     * @return
     */
    private File getLastFile(){
        File dir = new File(PATH);
        try{

            if(!dir.exists()){
                dir.mkdir();
            }
            File[] files = dir.listFiles();
            File tmp = null;
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
            String filename = df.format(new Date())+".log";
            if(files.length == 0){
                tmp = new File(PATH+filename);
                tmp.createNewFile();
                return  tmp;
            }else{
                long maxTimestmp = 0;
                for(File f:files){
                    try{
                        Date date=df .parse(f.getName());
                        long time = date.getTime();
                        if(time > maxTimestmp){
                            maxTimestmp = time;
                            tmp = f;
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                if(tmp.length()/1024/1024 >= 1){
                    tmp =  new File(PATH+filename);
                }
                tmp.createNewFile();
                return tmp;
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public void saveLog(String info){
        try {
            System.out.println("by hua ====>  "+info);
            FileWriter writer = new FileWriter(getLastFile(), true);
            writer.write(info+"\n");
            writer.flush();
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
