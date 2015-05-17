package cn.edu.scnu.pmtreewebapp.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.MBR;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RoadNetwork {
	private String id;
	private String name;
//	private String desc;
	private String pmtreeFileName;
	
	public String getPmtreeFileName() {
		return pmtreeFileName;
	}
	public void setPmtreeFileName(String pmtreeFileName) {
		this.pmtreeFileName = pmtreeFileName;
	}

	/**
	 * 路网链路连接点列表，第一个点是路网的起始点，最后一个点是路网的终点
	 */
	private List<NetworkPoint> jointPoints = new ArrayList<NetworkPoint>();
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
//	public String getDesc() {
//		return desc;
//	}
//	public void setDesc(String desc) {
//		this.desc = desc;
//	}
	public List<NetworkPoint> getJointPoints() {
		return jointPoints;
	}
	public void setJointPoints(List<NetworkPoint> jointPoints) {
		this.jointPoints = jointPoints;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((jointPoints == null) ? 0 : jointPoints.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RoadNetwork other = (RoadNetwork) obj;

		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (jointPoints == null) {
			if (other.jointPoints != null)
				return false;
		} else if (!jointPoints.equals(other.jointPoints))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}		
	
	@JsonIgnore
	private PMTree pmtree;
	
	public PMTree getPMTree() {
		if(pmtree == null) {
			List<List<MBR>> pt;
			try {
				pt = loadPMTree(this.pmtreeFileName);
			} catch (IOException e) {
				throw new RuntimeException("获取pmtree失败!");
			}
			
			pmtree = new PMTree(pt);
		}
		return pmtree;
	}
	public void savePMTree() {
//		savePMTree(this.pmtreeFileName, this.pmtree);
	}
	
	public static final void savePMTree(String fileName, List<List<MBR>> pm_tree) {
		if (pm_tree == null) {
			pm_tree = Collections.emptyList();
		}
		ObjectMapper objMapper = new ObjectMapper();
		try {
			objMapper.writeValue(new File(fileName), pm_tree);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("保存pmtree失败!");
		}
	}
	
	public static final  List<List<MBR>> loadPMTree(String fileName) throws IOException {
		ObjectMapper objMapper = new ObjectMapper();
		List<List<MBR>> pmTree = objMapper.readValue(new File(fileName), new TypeReference<List<List<MBR>>>() { });
		if (pmTree == null) {
			pmTree = Collections.emptyList();
		}
		return pmTree;
	}
	
	public static final String generatePMTreeFileName(String graphId, String rnid) {
		return "./graphxml/"+graphId+"_roadnetwork_"+rnid+"_pmtree.json";
	}
}
