package com.taoato.item.freemarker;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class TestFreemaker {
	
	@Test
	public void testFreemaker() throws IOException, TemplateException {
		//创建一个configuration对象
		Configuration configuration = new Configuration(Configuration.getVersion()) ;
		//设置模版所在目录
		configuration.setDirectoryForTemplateLoading(new File("C:/develop/eclipse/workspace/taotao/taotao-item-web/src/main/resources/ftl"));
		//设置模版字符集
		configuration.setDefaultEncoding("utf-8");
		//加载模版文件
//		Template template = configuration.getTemplate("hello.ftl") ;
		Template template = configuration.getTemplate("first.html") ;
		//创建一个数据集
		Map data = new HashMap<>() ;
		data.put("title", "学生信息管理系统") ;
		data.put("stu", new Student(1, "小马哥", 20, "重庆山城")) ;
		List<Student> studentList = new ArrayList<>() ;
		studentList.add(new Student(1, "小马哥", 20, "重庆山城")) ;
		studentList.add(new Student(2, "小马哥1", 20, "重庆山城")) ;
		studentList.add(new Student(3, "小马哥2", 20, "重庆山城")) ;
		studentList.add(new Student(4, "小马哥3", 20, "重庆山城")) ;
		studentList.add(new Student(5, "小马哥4", 20, "重庆山城")) ;
		studentList.add(new Student(6, "小马哥5", 20, "重庆山城")) ;
		data.put("studentList", studentList) ;
		data.put("date", new Date()) ;
		data.put("mytest", 4234) ;
		data.put("hello", "hello world!") ;
		//设置模版输出的目录以及文件名
		FileWriter writer = new FileWriter(new File("c:/temp/freemaker/first.html")) ;
		//生成文件
		template.process(data, writer);
		//关闭流
		writer.close();
	}
	
}
