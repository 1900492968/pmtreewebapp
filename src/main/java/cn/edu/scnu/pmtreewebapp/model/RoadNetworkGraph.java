package cn.edu.scnu.pmtreewebapp.model;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class RoadNetworkGraph {
	private String id;
	private String name;
	private String desc;
	private List<RoadNetwork> roadNetworks = new ArrayList<RoadNetwork>();
	
	private RoadNetworkGraph(String id, String name, String desc) {
		super();
		this.id = id;
		this.name = name;
		this.desc = desc;
	}
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
	public List<RoadNetwork> getRoadNetworks() {
		return roadNetworks;
	}
	public void setRoadNetworks(List<RoadNetwork> roadNetworks) {
		this.roadNetworks = roadNetworks;
	}
	public RoadNetworkGraph() {
		super();
	}

	@JsonIgnore
    public boolean isNew() {
        return (this.id == null);
    }
    
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((desc == null) ? 0 : desc.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		RoadNetworkGraph other = (RoadNetworkGraph) obj;
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
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public void ToXMLFile(String filename) throws IOException {
		ObjectMapper xmlMapper = new XmlMapper();
		xmlMapper.writeValue(new File(filename), this);
	}
	
	public String ToXMLString() throws IOException {
		XmlMapper xmlMapper = new XmlMapper();        
		return xmlMapper.writeValueAsString(this);
	}
	
	public String ToJSONString() throws IOException {
		ObjectMapper objMapper = new ObjectMapper();
		String json = objMapper.writeValueAsString(this);
		return json;
	}
	
	public RoadNetworkGraph readFromXML(String xml) throws IOException {
		ObjectMapper xmlMapper = new XmlMapper();
		RoadNetworkGraph entry = xmlMapper.readValue(new File(xml), RoadNetworkGraph.class);
		return entry;
	}
	

	public static final RoadNetworkGraph newRoadNetworkGraph(String id, String name, String desc) {
		RoadNetworkGraph graph = new RoadNetworkGraph(id, name, desc);
		return graph;
	}
}
