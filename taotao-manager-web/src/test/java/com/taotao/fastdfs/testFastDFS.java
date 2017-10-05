package com.taotao.fastdfs;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;

import com.taotao.utils.FastDFSClient;

public class testFastDFS {
	
	@Test
	public void testFastDFSUpload() throws FileNotFoundException, IOException, MyException {
		//创建一个配置文件fast_dfs.conf，配置文件的内容就是指定TrackerServer的地址
		//加载配置文件
		ClientGlobal.init("C:/develop/eclipse/workspace/taotao/taotao-manager-web/src/main/resources/resource/fast_dfs.conf"); //转义也可以
		//创建一个trackerClient对象
		TrackerClient trackerClient = new TrackerClient() ;
		//通过trackerClient对象获得TrackerServer对象
		TrackerServer trackerServer = trackerClient.getConnection() ;
		//创建一个StorageServer对象，null就可以了
		StorageServer storageServer = null ;
		//创建一个storageClient对象，两个参数TrackerServer和StorageServer
		StorageClient storageClient = new StorageClient(trackerServer, storageServer) ;
		//使用storageClient上传文件
		//http://192.168.25.133/group1/M00/00/00/wKgZhVmelJmABQwcAAWjmNySZrk398.jpg
		String[] strings = storageClient.upload_file("C:/Users/Leo/Pictures/2.jpg", "jpg", null) ; 
		for (String string : strings) {
			System.out.println(string);
		}
	}
	
	@Test
	public void testUtils() throws Exception {
		FastDFSClient client = new FastDFSClient("C:/develop/eclipse/workspace/taotao/taotao-manager-web/src/main/resources/resource/fast_dfs.conf") ; 
		String string = client.uploadFile("C:/Users/Leo/Pictures/小班长.jpg") ; //group1/M00/00/00/wKgZhVmemF6AKaRZAAaMa_HZPr4802.jpg
		System.out.println(string);
	}
}
