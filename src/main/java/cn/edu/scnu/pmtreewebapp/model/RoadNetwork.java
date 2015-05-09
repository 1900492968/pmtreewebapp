package cn.edu.scnu.pmtreewebapp.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.MBR;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RoadNetwork {
	private String id;
	private String name;
	private String desc;
	private String pmtreeFileName;
	
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
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
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
		result = prime * result + ((desc == null) ? 0 : desc.hashCode());
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
		if (desc == null) {
			if (other.desc != null)
				return false;
		} else if (!desc.equals(other.desc))
			return false;
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
	
	public static final void savePMTree(String fileName, List<List<MBR>> pm_tree) throws IOException {
		ObjectMapper objMapper = new ObjectMapper();
		objMapper.writeValue(new File(fileName), pm_tree);
	}
	
	public static final  List<List<MBR>> loadPMTree(String fileName) throws IOException {
		ObjectMapper objMapper = new ObjectMapper();
		List<List<MBR>> pmTree = objMapper.readValue(new File(fileName), new TypeReference<List<List<MBR>>>() { });
		return pmTree;
	}
}
