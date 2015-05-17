package cn.edu.scnu.pmtreewebapp.controller;

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

import cn.edu.scnu.pmtreewebapp.model.RoadNetwork;
import cn.edu.scnu.pmtreewebapp.model.RoadNetworkGraph;
import cn.edu.scnu.pmtreewebapp.model.RoadNetworkGraphs;

import com.MBR;
import com.fasterxml.jackson.databind.ObjectMapper;

@EnableAutoConfiguration
@Controller
public class RoadNetworkController {

	@RequestMapping(value = "/rngraph/{graphId}/query", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String trajectoryQuery(@PathVariable("graphId") String graphId,
//			@MatrixVariable(defaultValue = "-1", value="d1", required = false) int d1,
//			@MatrixVariable(defaultValue = "-1", required = false) int d2,
//			@MatrixVariable(defaultValue = "-1", required = false) int t1,
//			@MatrixVariable(defaultValue = "-1", required = false) int t2, 
			@RequestParam(value="d1", defaultValue = "-1", required=false) int d1,
			@RequestParam(value="d2", defaultValue = "-1", required=false) int d2, 
			@RequestParam(value="t1", defaultValue = "-1", required=false) int t1,
			@RequestParam(value="t2", defaultValue = "-1", required=false) int t2, Model model) {

		return "trajectoryquery";
	}
	

	@RequestMapping(value = "/rngraph/{graphId}/queryresult", method = {
			RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String trajectoryQueryResult(@PathVariable("graphId") String graphId,
			@RequestParam(value="d1", defaultValue = "-1", required=false) int d1,
			@RequestParam(value="d2", defaultValue = "-1", required=false) int d2, 
			@RequestParam(value="t1", defaultValue = "-1", required=false) int t1,
			@RequestParam(value="t2", defaultValue = "-1", required=false) int t2, Model model) {
		
		model.addAttribute("graphId", graphId);		
		RoadNetworkGraph rng = RoadNetworkGraphs
				.findRoadNetworkGraphById(graphId);
		model.addAttribute("result", Collections.EMPTY_LIST);
		if (d1 == -1 || d2 == -1 || t1 == -1 || t2 == -1) {
			model.addAttribute("result", Collections.EMPTY_LIST);
			String json = "[]";
			json = "{\"total\":"+0+",\"smbr\":{\"d1\":"+d1+",\"d2\":"+d2+",\"t1\":"+t1+",\"t2\":"+t2+"},\"rows\":"+json+"}";
			return json;
		} else {
			try {
				List<MBR> mbrs = new ArrayList<MBR>();
				for (RoadNetwork rn : rng.getRoadNetworks()) {
					List<MBR> sub = rn.getPMTree().rangeQuery(d1, t1, d2, t2);
					mbrs.addAll(sub);
				}
				MBRVO searchvo = new MBRVO();
				searchvo.d1 = d1;
				searchvo.d2 = d2;
				searchvo.t1 = t1;
				searchvo.t2 = t2;
				ResultVO resultvo = converToResultVO(searchvo, mbrs, false);
				ObjectMapper objMapper = new ObjectMapper();
				String json = objMapper.writeValueAsString(resultvo);
				
				return json;
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("保存pmtree失败!"+e.getLocalizedMessage());
			}
		}
	}
	
	@RequestMapping(value = "/rngraph/{graphId}/queryresultgraphic", method = {
			RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String trajectoryQueryGraphicResult(@PathVariable("graphId") String graphId,
			@RequestParam(value="d1", defaultValue = "-1", required=false) int d1,
			@RequestParam(value="d2", defaultValue = "-1", required=false) int d2, 
			@RequestParam(value="t1", defaultValue = "-1", required=false) int t1,
			@RequestParam(value="t2", defaultValue = "-1", required=false) int t2, Model model) {
		
		model.addAttribute("graphId", graphId);		
		RoadNetworkGraph rng = RoadNetworkGraphs
				.findRoadNetworkGraphById(graphId);
		model.addAttribute("result", Collections.EMPTY_LIST);
		if (d1 == -1 || d2 == -1 || t1 == -1 || t2 == -1) {
			model.addAttribute("result", Collections.EMPTY_LIST);
			String json = "[]";
			json = "{\"total\":"+0+",\"smbr\":{\"d1\":"+d1+",\"d2\":"+d2+",\"t1\":"+t1+",\"t2\":"+t2+"},\"rows\":"+json+"}";
			return json;
		} else {
			try {
				List<MBR> mbrs = new ArrayList<MBR>();
				for (RoadNetwork rn : rng.getRoadNetworks()) {
					List<MBR> sub = rn.getPMTree().rangeQuery(d1, t1, d2, t2);
					mbrs.addAll(sub);
				}
				MBRVO searchvo = new MBRVO();
				searchvo.d1 = d1;
				searchvo.d2 = d2;
				searchvo.t1 = t1;
				searchvo.t2 = t2;
//				List<MBRVO> mbrvos = convertFromMBRToMBRVO(mbrs);
//				Collections.sort(mbrvos);
//				model.addAttribute("result", mbrvos);
				ResultVO resultvo = converToResultVO(searchvo, mbrs, false);
				ObjectMapper objMapper = new ObjectMapper();
				String json = objMapper.writeValueAsString(resultvo);
				
//				json = "{\"total\":"+mbrs.size()+",\"smbr\":{\"d1\":"+d1+",\"d2\":"+d2+",\"t1\":"+t1+",\"t2\":"+t2+"}, \"rows\":"+json+"}";
				return json;
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("保存pmtree失败!"+e.getLocalizedMessage());
			}
		}
	}
	
	private static final class ResultVO {
		public int total;
		public MBRVO smbr;
		public double max;
		
		public List<MBRVO> rows;
	}
	
	private static final class MBRVO implements Comparable {
//		public int index;
		public int objId;
		public double d1,d2,t1,t2;
		@Override
		public int compareTo(Object o) {
			MBRVO oo = (MBRVO) o;
			if (objId < oo.objId) return -1;
			if (objId == oo.objId) return 0;
			else return 1;
		}
	}
	
	private static final ResultVO converToResultVO(MBRVO searchvo, List<MBR> mbrs, boolean ifGraphic) {
		
		ResultVO resultvo = new ResultVO();
		List<MBRVO> mbrvos = new ArrayList<MBRVO>(mbrs.size());
		resultvo.total = mbrs.size();
		resultvo.rows = mbrvos;
		if (ifGraphic) {
			double max = maxValue(searchvo);
			for (MBR m: mbrs) {
				double tempmax = maxValue(m);
				if (max < tempmax) {
					max = tempmax;
				}
			}
	
			MBRVO smbr = new MBRVO();
			smbr.d1 = makeFactor(searchvo.d1, max);
			smbr.d2 = makeFactor(searchvo.d2,max);
			smbr.t1 = makeFactor(searchvo.t1,max); 
			smbr.t2 = makeFactor(searchvo.t2,max);
			resultvo.smbr = smbr;
			
			for (MBR m: mbrs) {
				MBRVO vo = new MBRVO();
				vo.objId = m.p1.id;
				vo.d1 = makeFactor(m.p1.d, max);
				vo.d2 = makeFactor(m.p2.d,max);
				vo.t1 = makeFactor(m.p1.t,max); 
				vo.t2 = makeFactor(m.p2.t,max);
				mbrvos.add(vo);
			}
			resultvo.max = max;
		} else {
			resultvo.smbr = searchvo;	
			for (MBR m: mbrs) {
				MBRVO vo = new MBRVO();
				vo.objId = m.p1.id;
				vo.d1 = m.p1.d;
				vo.d2 = m.p2.d;
				vo.t1 = m.p1.t;
				vo.t2 = m.p2.t;
				mbrvos.add(vo);
			}
		}
		return resultvo;
	}
	
	private static final double makeFactor(double origin, double max) {
		if (max<=100) return origin;
		return (origin*100)/max;
	}
	
	private static final double maxValue(MBR vo) {
		double max = 0;
		if (vo.p1.d > max) {
			max = vo.p1.d;
		}
		if (vo.p2.d > max) {
			max = vo.p2.d;
		}
		if (vo.p1.t > max) {
			max = vo.p1.t;
		}
		if (vo.p2.t > max) {
			max = vo.p2.t;
		}
		return max;
	}
	private static final double maxValue(MBRVO vo) {
		double max = 0;
		if (vo.d1 > max) {
			max = vo.d1;
		}
		if (vo.d2 > max) {
			max = vo.d2;
		}
		if (vo.t1 > max) {
			max = vo.t1;
		}
		if (vo.t2 > max) {
			max = vo.t2;
		}
		return max;
	}
//	private static final List<MBRVO> convertFromMBRToMBRVO(List<MBR> mbrs) {
//		return mbrvos;
//	}
}
