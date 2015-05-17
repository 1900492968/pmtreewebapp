package cn.edu.scnu.pmtreewebapp;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import cn.edu.scnu.pmtreewebapp.model.NetworkPoint;
import cn.edu.scnu.pmtreewebapp.model.RoadNetwork;
import cn.edu.scnu.pmtreewebapp.model.RoadNetworkGraph;

public class RoadNetworkGraphTest {
	public void setUp() {

		RoadNetwork r1 = new RoadNetwork();
//		r1.setDesc("desc");
		r1.setId("id");
		r1.setName("name");
		List<NetworkPoint> jointPoints = new ArrayList<NetworkPoint>();
		r1.setJointPoints(jointPoints);
		for (int i = 0 ; i < 10; i++) {
			NetworkPoint np1 = new NetworkPoint(i, i);
			jointPoints.add(np1);
		}
		List<RoadNetwork> rs = new ArrayList<RoadNetwork>();
		rs.add(r1);

		RoadNetworkGraph rg = RoadNetworkGraph.newRoadNetworkGraph("id", "id", "desc");
		rg.setRoadNetworks(rs);
		try {
			rg.ToXMLFile("test.xml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
/**
	@Test
	public void testToXMLFile() {
		fail("Not yet implemented");
	}

	@Test
	public void testToXMLString() {
		fail("Not yet implemented");
	}

	@Test
	public void testToJSONString() {
		fail("Not yet implemented");
	}

	@Test
	public void testReadFromXML() {
		fail("Not yet implemented");
	}
*/
}
