package cn.edu.scnu.pmtreewebapp.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
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

	@RequestMapping(method = RequestMethod.GET)
	public String mainFrame(Model model) throws IOException {
		List<RoadNetworkGraph> graphs = RoadNetworkGraphs.getAllRoadNetworkGraph();
		model.addAttribute("graphs", graphs);
		return "main";
	}
}
