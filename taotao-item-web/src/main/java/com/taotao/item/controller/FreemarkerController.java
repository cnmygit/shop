package com.taotao.item.controller;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Configuration;
import freemarker.template.Template;

@Controller
public class FreemarkerController {

	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer ;
	
	@RequestMapping("/gethtml")
	@ResponseBody
	public String getHtml() throws Exception {
		Configuration configuration = freeMarkerConfigurer.getConfiguration() ;
		Template template = configuration.getTemplate("hello.ftl") ;
		Map map = new HashMap<>() ;
		map.put("hello", "spring整合freemarker") ;
		FileWriter writer = new FileWriter(new File("C:\\temp\\freemaker\\out.html")) ;
		template.process(map, writer);
		writer.close();
		return "ok" ;
	}
}
