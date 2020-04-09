package com.yunpan;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.net.URI;

import redis.clients.jedis.Jedis;

public class Test {
	
	
	
    public static void main( String[] args )throws Exception{
    	//set
    	
    	RedisFileService rfs = new RedisFileService();
    	String account = "mycloud";
//    	rfs.createAccount(account,"123456");
//  
//    	rfs.addFile(account, "E:\\yangjinfeng\\teachingworkspace\\redis\\test.txt");
//    	
//    	rfs.addDir(account, "dir2");
//    	rfs.addDir(account, "dir1");
//    	
//    	rfs.addFile(account+"/dir2", "E:\\yangjinfeng\\teachingworkspace\\redis\\test2.txt");
//    	rfs.addFile(account+"/dir2", "E:\\yangjinfeng\\teachingworkspace\\redis\\test.txt");
//    	rfs.addFile(account+"/dir2", "E:\\yangjinfeng\\teachingworkspace\\redis\\test2.txt");
//    	rfs.addFile(account+"/dir1", "E:\\yangjinfeng\\teachingworkspace\\redis\\test2.txt");
    	
//    	rfs.delete(account+"/dir2", "test2.txt");
    	
    	rfs.delete(account, "dir1");
    	
    	rfs.listDir(account);
    	System.out.println();
    	rfs.listDir(account+"/dir1");
    	System.out.println();
    	rfs.listDir(account+"/dir2");
    	System.out.println();
    }
    


}
