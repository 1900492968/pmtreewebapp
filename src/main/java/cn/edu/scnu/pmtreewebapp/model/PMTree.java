package cn.edu.scnu.pmtreewebapp.model;

import java.util.List;

import pmtree.PMtreeInsert;
import pmtree.PMtreeQuery;

import com.MBR;
import com.PointData;

public class PMTree {
	private List<List<MBR>> pm_tree;

	public PMTree(List<List<MBR>> pt) {
		this.pm_tree = pt;
	}
	
	public void insertMBR(String objId, int d1, int t1, int d2, int t2) {
		MBR insertMBR = new MBR(Integer.valueOf(objId), d1, t1, d2, t2);

		PMtreeInsert.InsertUpdate(pm_tree, insertMBR);
	}
	
	public List<MBR> rangeQuery(int d1, int t1, int d2, int t2) {
		MBR query = new MBR(0,d1, t1, d2, t2);
		
		List<MBR> query_result = PMtreeQuery.ToqueryIntersect(pm_tree, query);
		List<MBR> final_result = PMtreeQuery.GetResult(query_result, query);
		return final_result;
	}
	
	public List<PointData> pointQuery(int d1, int t1, int d2, int t2) {
		MBR Pointquery = new MBR(0,d1, t1, d2, t2);

		List<MBR> Pointquery_result = PMtreeQuery.ToqueryPointIntersect(pm_tree, Pointquery);
    	List<PointData> Pointfinal_result = PMtreeQuery.GetPointResult(Pointquery_result, Pointquery);
    	return Pointfinal_result;
	}
}
