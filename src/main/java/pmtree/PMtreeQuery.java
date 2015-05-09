package pmtree;
import java.util.ArrayList;
import java.util.List;

import javax.management.Query;

import com.MBR;

import com.PointData;

public class PMtreeQuery {
	
	//找出被查询点a、b包含的集合
	public static List<MBR> Toquery(List<List<MBR>> query_model, MBR Q){
		List<MBR> MBRList = new ArrayList<MBR>();
		int size = query_model.size();
		int i = 0;
		//System.out.println("长度"+size);
		while( i < size)
		{
			List<MBR> temp = query_model.get(i);
			int length = temp.size();
			int j = 0;
			//最大的区间包含
			if(query(temp.get(j),Q) ){
				for(j = 0; j < length; j++){
					MBRList.add(temp.get(j));
				}
				i++;
				continue;
			}
			j = length - 1;
			
			//最小的点都不被包含
			if(!query(temp.get(j),Q)){
				i++;
				continue;
			}
			//寻找PM_TREE中第一个被包含的点
			j = binary_search(temp, Q, 0 , length - 1);
			
			for(int k = j; k < length; k++){
				MBRList.add(temp.get(k));
			}
			i++;
			//System.out.println("区域x1：" + temp[j].x1);
		}
		return MBRList;
	}
	
	//找出与查询点a、b相交的集合
	public static List<MBR> ToqueryIntersect(List<List<MBR>> query_model, MBR Q){
		List<MBR> MBRList = new ArrayList<MBR>();
		int size = query_model.size();
		int i = 0;
		while( i < size)
		{
			List<MBR> temp = query_model.get(i);
			int length = temp.size();
			int j = 0;
			//最大的区间包含不相交，放弃分支
			if(!Intersect(temp.get(j),Q) ){
				i++;
				continue;
			}
			j = length - 1;
			//最小的区间都相交，整条分支都是
			if(Intersect(temp.get(j),Q)){
				for(j = 0; j < length; j++){
					MBRList.add(temp.get(j));
				}	
				i++;
				continue;
			}
								
			//寻找PM_TREE中第一个相交的区间
			j = Inter_binary_search(temp, Q, 0 , length - 1);
			
			for(int k = j; k < length - 1; k++){
				MBRList.add(temp.get(k));
			}
			i++;
		}
		return MBRList;
	}
	
	//查询相交
	public static Boolean Intersect(MBR data,MBR Query_data)
	{
		if(data.a >= Query_data.b||
				data.b <= Query_data.a)
			return false;
		else
			return true;
	}
	
	public static int Inter_binary_search(List<MBR>temp , MBR Q, int start, int end){
		if(start == end)	{	return start;	}
		int mid = (start + end) / 2;
		if(Intersect(temp.get(mid),Q)){
			return Inter_binary_search(temp, Q, start, mid);
		}
		else{
			return Inter_binary_search(temp, Q, mid+1, end);
		}
	}
	
	//从ToQuery方法得到集合中找出所有被查询点包含的区间
	public static List<MBR> GetResult(List<MBR> query_array, MBR Q){
		List<MBR> MBRList = new ArrayList<MBR>();	
		int size = query_array.size();
		int i = 0;	
		while(i < size)
		{	
			MBR temp = query_array.get(i);
			if(query_again(temp, Q)){
				MBRList.add(temp);
			}
			i++;
		}	
		return MBRList;
	}
	
	public static Boolean query_again(MBR data,MBR Query_data){
		if(data.p1.d <= Query_data.p2.d && 
			data.p1.t <= Query_data.p2.t &&
			data.p2.d >= Query_data.p1.d &&
			data.p2.t >= Query_data.p1.t)
		{
			return true;
		}
		else
			return false;
}
	
	//查询包含
	public static Boolean query(MBR data,MBR Query_data)
	{
		if(data.a >= Query_data.a && data.b <= Query_data.b)
			return true;
		else
			return false;
	}
	
	public static int binary_search(List<MBR>temp , MBR Q, int start, int end){
		int mid = (start + end) / 2;
		if(query(temp.get(mid),Q)){
			if(!query(temp.get(mid - 1),Q))	{	return mid;	}
			else	{	return binary_search(temp, Q, start, mid-1);	}
		}
		else{
			return binary_search(temp, Q, mid+1, end);
		}
	}
	

	//找出点查询与点a、b相交的集合
	public static List<MBR> ToqueryPointIntersect(List<List<MBR>> query_model, MBR Q){
		List<MBR> MBRList = new ArrayList<MBR>();
		int size = query_model.size();
		int i = 0;
		while( i < size)
		{
			List<MBR> temp = query_model.get(i);
			int length = temp.size();
			int j = 0;
			//最大的区间包含不相交，放弃分支
			if(!PointIntersect(temp.get(j),Q) ){
				i++;
				continue;
			}
			j = length - 1;
			//最小的区间都相交，整条分支都是
			if(PointIntersect(temp.get(j),Q)){
				for(j = 0; j < length; j++){
					MBRList.add(temp.get(j));
				}	
				i++;
				continue;
			}
								
			//寻找PM_TREE中第一个相交的区间
			j = PointInter_binary_search(temp, Q, 0 , length - 1);
			
			for(int k = j; k < length - 1; k++){
				MBRList.add(temp.get(k));
			}
			i++;
		}
		return MBRList;
	}
	
	//点查询的相交
	public static Boolean PointIntersect(MBR data,MBR Query_data)
	{
		if(data.a > Query_data.b||
				data.b < Query_data.a)
			return false;
		else
			return true;
	}
	
	public static int PointInter_binary_search(List<MBR>temp , MBR Q, int start, int end){
		if(start == end)	{	return start;	}
		int mid = (start + end) / 2;
		if(PointIntersect(temp.get(mid),Q)){
			return PointInter_binary_search(temp, Q, start, mid);
		}
		else{
			return PointInter_binary_search(temp, Q, mid+1, end);
		}
	}
	
	//从ToQuery方法得到集合中找出所有被查询点
	public static List<PointData> GetPointResult(List<MBR> query_array, MBR Q){
		List<PointData> PointDList = new ArrayList<PointData>();	
		int size = query_array.size();
		int i = 0;	
		while(i < size)
		{	
			MBR temp = query_array.get(i);
			if(temp.p1.d == Q.p2.d && 
					temp.p1.t == Q.p2.t )
			{
				PointData tempPoint = new PointData(temp.p1.id,temp.p1.d,temp.p1.t);
				PointDList.add(tempPoint);
			}
			else if( temp.p2.d == Q.p1.d &&
					temp.p2.t == Q.p1.t)
			{
				PointData tempPoint = new PointData(temp.p2.id,temp.p2.d,temp.p2.t);
				PointDList.add(tempPoint);
			}
			i++;
		}	
		return PointDList;
	}
	
}
