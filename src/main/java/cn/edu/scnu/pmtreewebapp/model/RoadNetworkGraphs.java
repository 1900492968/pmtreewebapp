package cn.edu.scnu.pmtreewebapp.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.MBR;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;


@JsonIgnoreProperties(value={"graphsInstance", "GRAPHS_FILENAME"}) 
public final class RoadNetworkGraphs {

	private static final String GRAPHS_FILENAME = "graphxml/RoadNetworkGraphs.xml";
	
	private static RoadNetworkGraphs graphsInstance;

	private List<RoadNetworkGraph> graphs = new ArrayList<RoadNetworkGraph>();
//	
//	private static final Map<String, Map> cache = new HashMap<String, Map>();
	
	private RoadNetworkGraphs() {
//		 loadRoadNetworkGraphs();
	}

	public List<RoadNetworkGraph> getGraphs() {
		return graphs;
	}

	public static final List<RoadNetworkGraph> getAllRoadNetworkGraph()
			 {
		return graphsInstance.graphs;
	}
	
	public static final RoadNetworkGraph newRoadNetworkGraph(String id,
			String name, String desc) throws IOException {
		if (graphsInstance.hasGraph(id)) {
			throw new RuntimeException("id:" + id + ", 网络图已经存在，不能创建!");
		}
		RoadNetworkGraph graph = RoadNetworkGraph.newRoadNetworkGraph(id, name,
				desc);
		graphsInstance.graphs.add(graph);
		saveRoadNetworkGraphs();
		return graph;
	}

	public static final void removeRoadNetworkGraph(String id) throws IOException {
		for(RoadNetworkGraph graph : graphsInstance.graphs) {
			if (graph.getId().endsWith(id)) {
				graphsInstance.graphs.remove(graph);
				saveRoadNetworkGraphs();
				break;
			}
		}
	}
	
	public static final void updateRoadNetworkGraph(String id,
			String name, String desc) throws IOException {
		if (!graphsInstance.hasGraph(id)) {
			throw new RuntimeException("id:" + id + ", 网络图不存在!");
		} 
		
		for(RoadNetworkGraph graph : graphsInstance.graphs) {
			if (graph.getId().endsWith(id)) {
				graph.setName(name);
				graph.setDesc(desc);
				saveRoadNetworkGraphs();
				break;
			}
		}
	}

	public static final RoadNetworkGraph findRoadNetworkGraphById(String id) {
		for(RoadNetworkGraph graph : graphsInstance.graphs) {
			if (graph.getId().equals(id)) {
				return graph;
			}
		}
		return null;
	}
	
	public static final void saveRoadNetworkGraphs() throws IOException {
		ObjectMapper mapper = new XmlMapper();    
//		JavaType javaType = mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
//		mapper.configure(SerializationFeature.INDENT_OUTPUT,true);
		mapper.writeValue(assureFileExists(), graphsInstance);

//		final ObjectWriter w = mapper.writer();
//		w.with(SerializationFeature.INDENT_OUTPUT);
//		w.writeValue(assureFileExists(), graphsInstance);
	}

	public static final void loadRoadNetworkGraphs() {
		ObjectMapper xmlMapper = new XmlMapper();
		try {
			graphsInstance = xmlMapper.readValue(new File(GRAPHS_FILENAME),
					RoadNetworkGraphs.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (graphsInstance == null) {
			graphsInstance = new RoadNetworkGraphs();
			try {
				saveRoadNetworkGraphs();
			} catch (IOException e) {
				e.printStackTrace();
				new RuntimeException("路网图配置文件读取保存失败。");
			}
		}
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
	
	public static final void insertTracjectoryOneDimensionMBR(String graphId, String rnId, String objId, int d1, int t1, int d2, int t2) {
		
	}
	
	private static final File assureFileExists() throws IOException {
		File file =  new File(GRAPHS_FILENAME);
		if (!file.exists()) {
			file.createNewFile();
		}
		return file;
	}
	
	public static final boolean hasGraph(String id) {
		for (RoadNetworkGraph g : graphsInstance.graphs) {
			if (g.getId().equals(id)) {
				return true;
			}
		}
		return false;
	}

}