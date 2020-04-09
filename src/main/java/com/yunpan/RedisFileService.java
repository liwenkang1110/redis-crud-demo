package com.yunpan;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import redis.clients.jedis.Jedis;

public class RedisFileService {
	
	private Jedis client = null;
	
	{
		try {
			client = new Jedis(new URI("redis://127.0.0.1:6379/5"));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	

    
    public void createAccount(String account,String password)throws Exception{
    	client.hset("CLOUD_USER", account, password);
    	
    	CloudFile rootdir = new CloudFile(account,false);
    	String welcomefile = "welcome.txt";
    	CloudFile welc = new CloudFile(account + "/"+welcomefile,true);
    	client.sadd(rootdir.getPath().getBytes(), CloudUtil.objectToBytes(welc));
    	client.set(welc.getPathKey().getBytes(), ("welcome! "+account).getBytes());
    }
    
    public boolean login(String account,String password){
    	String pass = client.hget("CLOUD_USER", account);
    	if(pass.equals(password)){
    		return true;
    	}else{
    		return false;
    	}
    }
    
    public List<CloudFile> readDir(String dir)throws Exception{
    	CloudFile dirpath = new CloudFile(dir,false);
    	byte[] keybytes = dirpath.getPath().getBytes();    	
    	List<CloudFile> list = new ArrayList<>();
    	long len = client.scard(keybytes);
    	if(len > 0){    		
    		Set<byte[]> fs = client.smembers(keybytes);
    		for(byte[] f : fs){
    			list.add((CloudFile)CloudUtil.byteToObject(f));
    		}
    	}
    	return list;
    }
    
    
    public void addDir(String parent, String dir)throws Exception{
    	CloudFile added = exists(parent,dir);
    	if(added == null){
    		CloudFile parentdir = new CloudFile(parent,false);
    		CloudFile childdir = new CloudFile(parent+"/"+dir,false);
    		client.sadd(parentdir.getPath().getBytes(), CloudUtil.objectToBytes(childdir));
    	}

    	
    }
    
    public void addFile(String parent, String filepath)throws Exception{
    	CloudFile parentdir = new CloudFile(parent,false);
    	
    	String fileName = filepath.substring(filepath.lastIndexOf("\\")+1);
    	CloudFile childdir = new CloudFile(parent+"/"+fileName,true);
    	CloudFile added = exists(parent,fileName);
    	if(added == null){
    		client.sadd(parentdir.getPath().getBytes(), CloudUtil.objectToBytes(childdir));
    		client.set(childdir.getPathKey().getBytes(), CloudUtil.fileToBytes(new File(filepath)));
    	}
    	
    }
    
    public void getFile(String path,String dest)throws Exception{
    	CloudFile filepath = new CloudFile(path,true);
    	CloudUtil.bytesToFile(client.get(filepath.getPathKey().getBytes()), new File(dest));
    }
    


    public void delete(String parent,String path)throws Exception{
    	String currentPath = parent+"/"+path;
    	boolean isFile = !client.exists(currentPath.getBytes());
    	CloudFile current = new CloudFile(currentPath,isFile);
    	if(isFile){
    		client.del(current.getPathKey().getBytes());    		
    	}else{
    		List<CloudFile> fs = readDir(currentPath);
    		for(CloudFile cf :fs){
    			String name = cf.getPath().substring(cf.getPath().lastIndexOf("/")+1);
    			delete(currentPath, name);
    		}    		
    	}
    	
    	CloudFile deleted = exists(parent,path);
    	if(deleted != null){
    		client.srem(parent.getBytes(), CloudUtil.objectToBytes(deleted));
    	}
    	
    	
    }
    
    private CloudFile exists(String parent, String name)throws Exception{
    	List<CloudFile> fs = readDir(parent);
    	String currentPath = parent+"/"+name;
    	for(CloudFile cf :fs){
    		if(cf.getPath().equals(currentPath)){
    			return cf;
    		}
    	}
    	return null;
    }
    
    public void listDir(String dir)throws Exception{
    	List<CloudFile> fs = readDir(dir);
    	for(CloudFile cf :fs){
    		System.out.println(cf.getPath());
    	} 

    }
}
