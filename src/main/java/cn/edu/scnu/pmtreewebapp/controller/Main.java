package cn.edu.scnu.pmtreewebapp.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.MultipartProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.edu.scnu.pmtreewebapp.model.RoadNetworkGraph;
import cn.edu.scnu.pmtreewebapp.model.RoadNetworkGraphs;

@EnableAutoConfiguration
@Controller
@RequestMapping(value = "/")
public class Main {

	@Autowired
	MultipartProperties multipartProperties;

	@RequestMapping(method = RequestMethod.GET)
	public String mainFrame(Model model) throws IOException {
		multipartProperties.setMaxFileSize("-1");
		List<RoadNetworkGraph> graphs = RoadNetworkGraphs
				.getAllRoadNetworkGraph();
		model.addAttribute("graphs", graphs);
		return "main";
	}

	@RequestMapping(value = "/roadnetwork_template", method = RequestMethod.GET)
	// @ResponseBody
	public void downloadTemplateFile(HttpSession hs, HttpServletResponse response, OutputStream os) throws IOException {
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		InputStream is = null;
//		String ctxPath = hs.getServletContext()  
//		        .getRealPath("/");  

//		String downLoadPath = ctxPath+"/static/roadnetwork_template.xls"; 
//		is = hs.getServletContext().getResourceAsStream("/roadnetwork_template.xls");
//		is = hs.getServletContext().getResourceAsStream("/static/roadnetwork_template.xls");
		File file = new File("./roadnetwork_template.xls");
//		if (file.exists()) {
//			System.out.println("aaaaaaaaaaaaaaaaa");
//		}
	    long fileLength = file.length();  
	    //设置文件输出类型
	    response.setContentType("application/octet-stream");  
	    response.setHeader("Content-disposition", "attachment; filename=roadnetwork_template.xls"); 
	    //设置输出长度
	    response.setHeader("Content-Length", String.valueOf(fileLength));  
//		file = new File("./static/roadnetwork_template.xls");
//		if (file.exists()) {
//			System.out.println("aaaaaaaaaaaaaaaaabbbbbbbbbbbbbbbbbbbbb");
//		}
		try {
			is = new FileInputStream(file);
			bis = new BufferedInputStream(is);
			bos = new BufferedOutputStream(os);
			byte[] buff = new byte[2048];
			int bytesRead;
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
		} finally {
			if (bis != null)
				is.close();
			if (bos != null)
				bos.close();
		}
	}

	// public static void () {}
}
