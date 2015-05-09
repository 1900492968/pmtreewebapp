package cn.edu.scnu.pmtreewebapp.controller;

import java.io.IOException;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@EnableAutoConfiguration
@Controller
@RequestMapping(value = "/pmtree")
public class PMTreeController {

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String newTree() throws IOException {
		return "new_pmtree";
	}
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String rangeQuery() throws IOException {
		return "new_pmtree";
	}
}
