package cn.edu.scnu.pmtreewebapp.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.edu.scnu.pmtreewebapp.ExcelUtils;
import cn.edu.scnu.pmtreewebapp.model.NetworkPoint;
import cn.edu.scnu.pmtreewebapp.model.RoadNetwork;
import cn.edu.scnu.pmtreewebapp.model.RoadNetworkGraph;
import cn.edu.scnu.pmtreewebapp.model.RoadNetworkGraphs;

import com.fasterxml.jackson.databind.ObjectMapper;

@EnableAutoConfiguration
@Controller
@RequestMapping(value = "/rngraph")
public class RoadNetworkGraphController {

	@RequestMapping(value = "/new", method = { RequestMethod.PUT,
			RequestMethod.POST })
	@ResponseBody
	public String newGraph(String id, String name, String desc,
			@RequestParam(required=false) MultipartFile roadnetworkfile) {
		try {
			id = parameter_trim(id);
			name = parameter_trim(name);
			desc = parameter_trim(desc);
			RoadNetworkGraph rng = null;
			if ("".equals(id)) {
				//新建
				//id 既是  网络图名称
				id = name;
				rng = new RoadNetworkGraph(id, name, desc);
				ExcelUtils.processGraphFromExcel(rng, roadnetworkfile);
				RoadNetworkGraphs.saveRoadNetworkGraphs(rng);
			} else {
				//修改
				rng = RoadNetworkGraphs.findRoadNetworkGraphById(id);
				RoadNetworkGraph graph2 = RoadNetworkGraphs.findRoadNetworkGraphByName(name);
				if (graph2 != null && !graph2.getId().equals(rng.getId())) {
					throw new RuntimeException("修改失败！已经存在名称为："+name+"的网络图！");
				}
				id = name;
				rng.setId(id);
				rng.setName(name);
				rng.setDesc(desc);
				ExcelUtils.processGraphFromExcel(rng, roadnetworkfile); 
				RoadNetworkGraphs.saveRoadNetworkGraphs(rng);
			} 

			String json = "{\"success\": true, \"name\":\"" + name + "\"}";
			return json;
		} catch (Exception e) {
			String json = "{\"success\": false, \"message\":\""
					+ e.getLocalizedMessage() + "\"}";
			return json;
		}
	}

	private static String parameter_trim(String p) {
		if (p == null)
			return "";
		else
			return p.trim();
	}

	@RequestMapping(value = "/upateinfo", method = { RequestMethod.PUT,
			RequestMethod.POST })
	@ResponseBody
	public String updateGraphBasicInfo(@RequestParam(value="networkroadform_id") String id, @RequestParam(value="networkroadform_name")String name, @RequestParam(value="networkroadform_desc") String desc) {
		try {
			id = parameter_trim(id);
			name = parameter_trim(name);
			desc = parameter_trim(desc);
			RoadNetworkGraph rng = null;
			if ("".equals(id)) {
				throw new RuntimeException("修改失败！要修改的路网图标识不能为空！");
			} else {
				//修改
				rng = RoadNetworkGraphs.findRoadNetworkGraphById(id);
				RoadNetworkGraph graph2 = RoadNetworkGraphs.findRoadNetworkGraphByName(name);
				if (graph2 != null && !graph2.getId().equals(rng.getId())) {
					throw new RuntimeException("修改失败！已经存在名称为："+name+"的网络图！");
				}
				id = name;
				rng.setId(id);
				rng.setName(name);
				rng.setDesc(desc);
				RoadNetworkGraphs.updateRoadNetworkGraphs(rng);
			} 

			String json = "{\"success\": true, \"name\":\"" + name + "\"}";
			return json;
		} catch (Exception e) {
			String json = "{\"success\": false, \"message\":\""
					+ e.getLocalizedMessage() + "\"}";
			return json;
		}
	}

	@RequestMapping(value = "/{id}/remove", method = { RequestMethod.DELETE,
			RequestMethod.PUT, RequestMethod.POST })
	@ResponseBody
	public String removeNetworkGraph(@PathVariable("id") String id) {
		try {
			RoadNetworkGraphs.removeRoadNetworkGraph(id);
			String json = "{\"success\": true, \"id\":\"" + id + "\"}";
			return json;
		} catch (IOException e) {
			String json = "{\"success\": false, \"message\":\""
					+ e.getLocalizedMessage() + "\"}";
			return json;
		}
	}

	@RequestMapping(value = "/{id}/edit", method = { RequestMethod.GET,RequestMethod.POST })
	@ResponseBody
	public String initGrapEdit(@PathVariable("id") String id, Model model) {
		RoadNetworkGraph graph = RoadNetworkGraphs.findRoadNetworkGraphById(id);
		if (graph == null) {
			String json = "{success:true, message:\"不存在id为："+id+"的路网图\"}";
			return json;
		}
		String json = "{\"success\":true, \"message\":\"成功获取网络图基础信息！\", \"id\":\""+graph.getId()+"\", \"name\":\""+graph.getName()+"\",\"desc\":\""+graph.getDesc()+"\"}";
		return json;
//		return "opernetworkgraph";
	}
	
	@RequestMapping(value = "/{id}/view", method = { RequestMethod.GET,RequestMethod.POST })
	public String initGrapView(@PathVariable("id") String id, Model model) {
//		RoadNetworkGraph graph = RoadNetworkGraphs.findRoadNetworkGraphById(id);
//		if (graph == null) {
//			String json = "{success:true, message:\"不存在id为："+id+"的路网图\"}";
//			return json;
//		}
//		String json = "{\"success\":true, \"message\":\"成功获取网络图基础信息！\", \"id\":\""+graph.getId()+"\", \"name\":\""+graph.getName()+"\",\"desc\":\""+graph.getDesc()+"\"}";
//		return json;

		RoadNetworkGraph graph = RoadNetworkGraphs.findRoadNetworkGraphById(id);
		model.addAttribute("graphId", id);		
		model.addAttribute("graph", graph);		
		List<VO> vos = convertFromToVO(graph.getRoadNetworks());	
		model.addAttribute("vos", vos);	
		try {
			Collections.sort(vos);
			ObjectMapper objMapper = new ObjectMapper();
			String json = objMapper.writeValueAsString(vos);
			json = "{\"total\":"+vos.size()+", \"rows\":"+json+"}";
			model.addAttribute("vosJson", json);	
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("保存pmtree失败!"+e.getLocalizedMessage());
		}
		return "opernetworkgraph";
	}

	@RequestMapping(value = "/{id}/viewroadnetwork", method = { RequestMethod.GET,RequestMethod.POST })
	@ResponseBody
	public String initRoadnetworkView(@PathVariable("id") String id, Model model) {
		RoadNetworkGraph graph = RoadNetworkGraphs.findRoadNetworkGraphById(id);
		try {
			List<VO> vos = convertFromToVO(graph.getRoadNetworks());	
			Collections.sort(vos);
			ObjectMapper objMapper = new ObjectMapper();
			String json = objMapper.writeValueAsString(vos);
			json = "{\"total\":"+vos.size()+", \"rows\":"+json+"}";
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			String json = "[]";
			json = "{\"total\":"+0+", \"rows\":"+json+"}";
			return json;
		}
	}
	
	private static final class VO implements Comparable {
//		public int index;
		public int id;
		public String pointsStr="";
		
		@Override
		public int compareTo(Object o) {
			VO oo = (VO) o;
			if (id < oo.id) return -1;
			if (id == oo.id) return 0;
			else return 1;
		}
	}
	
	private static final List<VO> convertFromToVO(List<RoadNetwork> mbrs) {
		List<VO> mbrvos = new ArrayList<VO>(mbrs.size());

		for (RoadNetwork m: mbrs) {
			VO vo = new VO();
			vo.id = Integer.valueOf(m.getId());
			mbrvos.add(vo);
			List<NetworkPoint> ps = m.getJointPoints();
			if (ps.size() == 0) continue;
			for(int i = 0; i < ps.size()-1; i++) {
				NetworkPoint p = ps.get(i);
				int x = (int)p.getX();
				int y = (int)p.getY();
				String str = "("+x+", "+y+"), ";
				vo.pointsStr += str;
			}
			NetworkPoint p = ps.get(ps.size()-1);
			int x = (int)p.getX();
			int y = (int)p.getY();
			String str = "("+x+", "+y+")";
			vo.pointsStr += str;
		}
		return mbrvos;
	}
}
