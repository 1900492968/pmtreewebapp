package cn.edu.scnu.pmtreewebapp.controller;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.SessionStatus;

import cn.edu.scnu.pmtreewebapp.model.RoadNetworkGraph;
import cn.edu.scnu.pmtreewebapp.model.RoadNetworkGraphs;

import com.fasterxml.jackson.databind.ObjectMapper;

@EnableAutoConfiguration
@Controller
@RequestMapping(value = "/rngraph")
public class RoadNetworkGraphController {

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public String get() throws IOException {
		List<RoadNetworkGraph> graphs = RoadNetworkGraphs.getAllRoadNetworkGraph();
//		g.newRoadNetworkGraph(id, name, desc)
//		graphs.add(RoadNetworkGraph.newRoadNetworkGraph("1", "1", "1"));
//		graphs.add(RoadNetworkGraph.newRoadNetworkGraph("2", "2", "2"));
		ObjectMapper objMapper = new ObjectMapper();
		String json = objMapper.writeValueAsString(graphs);
		return json;
//		return "get_rngraph";
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String newInitGraph(Model model) {
		RoadNetworkGraph graph = new RoadNetworkGraph();
        model.addAttribute(graph);
        return "";
	}

	
	@RequestMapping(value = "/new", method = {RequestMethod.PUT, RequestMethod.POST})
	@ResponseBody
	public String newGraph(String name, String id, String desc, Model model) {
		try {
			name = name.trim();
			desc = desc.trim();
			RoadNetworkGraphs.newRoadNetworkGraph(name, name, desc);
			List<RoadNetworkGraph> graphs = RoadNetworkGraphs.getAllRoadNetworkGraph();
			ObjectMapper objMapper = new ObjectMapper();
			String json = objMapper.writeValueAsString(graphs);
			return json;
		} catch (Exception e) {
			String json = "{\"failure\": true, \"message\":\""+e.getLocalizedMessage()+"\"}";
			return json;
		}
//        model.addAttribute(graph);
//        return "main";
	}

    @RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
    public String initUpdateOwnerForm(@PathVariable("id") String id, Model model) {
//    	RoadNetworkGraphs.updateRoadNetworkGraph(id, name, desc);
//        model.addAttribute(owner);
        return "owners/createOrUpdateOwnerForm";
    }
    
    @RequestMapping(value = "{id}/edit", method = RequestMethod.PUT)
    public String processUpdate(@Valid RoadNetworkGraph graph, BindingResult result, SessionStatus status) {
        if (result.hasErrors()) {
            return "owners/createOrUpdateOwnerForm";
        } else {
            status.setComplete();
            return "redirect:/owners/{ownerId}";
        }
    }
}
