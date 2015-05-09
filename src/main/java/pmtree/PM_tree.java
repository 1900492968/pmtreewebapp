package pmtree;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import com.MBR;
import com.PointData;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import create_data.CreateData;
import create_data.Write_Read;

public class PM_tree {
	
    private static String writer = "";
    private static String path = "D:/resource/PM_tree.txt";		//文件路径
    private static String save_path = "D:/resource/PM_treeGenerateData.txt";
    private static String save_mbr_path = "D:/resource/PM_treeGenerateDataMBR.txt";
    private static String WindowQuery_path = "D:/resource/QueryMBR_Range.txt";
    private static String PointQuery_path = "D:/resource/QueryMBR_Point.txt";
    private static String last_point_path = "D:/resource/PM_treeGenerateDataPoint.txt";
    private static String insertPoint_path = "D:/resource/PM_treeGenerateInsertData.txt";
    
    static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");	//定义时间格式，到毫秒级
    
	public static void main(String[] args) {
	
		while(true)
		{
			System.out.println("*********选项********");
			System.out.println("1.新建PM_tree");
			System.out.println("2.区域查询");
			System.out.println("3.点查询");
			System.out.println("4.插入");
			System.out.println("5.生成查询MBR");
			System.out.println("6.生成插入点");
			System.out.print("选择操作：");
			
			Scanner in = new Scanner(System.in);
			int option =in.nextInt();
			long startTime,endTime,totalTime;		//程序时间			
			List<List<MBR>> pm_tree = null;
			HashMap<Integer,PointData> PointDataMap = null;//new HashMap<Integer,PointData>();
			int lastID = 0;			
	    	switch (option)
	    	{
	    	case 1:
	    		startTime=System.currentTimeMillis();
	    		CreateData.GenerateData2(save_path);
	    		endTime=System.currentTimeMillis();
	    		totalTime=endTime-startTime;
	    		System.out.println("生成数据花费时间: totalTime="+totalTime+"ms");
	    		System.gc();
	    		
	    		startTime=System.currentTimeMillis();
	    		//读入生成数据
	    		MBR MBR_data[] = Write_Read.ReadtoMBR(save_mbr_path);
	    		endTime=System.currentTimeMillis();
	    		totalTime=endTime-startTime;
	    		System.out.println("读入: totalTime="+totalTime+"ms");
			
	    		startTime=System.currentTimeMillis();
				Arrays.sort(MBR_data);
				endTime=System.currentTimeMillis();
				totalTime=endTime-startTime;
				System.out.println("排序时间: totalTime="+totalTime+"ms");
			
				int length = MBR_data.length;			
				List<MBR> MBR_data_pointer = new ArrayList<MBR>();
				int key = 0;
				while(key < length)
				{
					MBR_data_pointer.add(MBR_data[key]);
					//System.out.println(key+":("+MBR_data[key].p1.id+","+MBR_data[key].a+","+MBR_data[key].b+")");
					key++;
				}		
				
				startTime=System.currentTimeMillis();
				pm_tree = generateLop(MBR_data_pointer,MBR_data.length);
				endTime=System.currentTimeMillis();
				totalTime=endTime-startTime;
				System.out.println("建立: totalTime="+totalTime+"ms");
				
				startTime=System.currentTimeMillis();
				Write_Read.toWrite(writer, path, 0);				//PM_Tree写入文件
				
//				ObjectMapper xmlMapper = new XmlMapper();
//				ObjectMapper objMapper = new ObjectMapper();
//				try {
//					xmlMapper.writeValue(new File("pm_tree.xml"), pm_tree);
//					objMapper.writeValue(new File("pm_tree.json"), pm_tree);
//					List<List<MBR>> mbrs = objMapper.readValue(new File("pm_tree.json"), new TypeReference<List<List<MBR>>>() { });
//					objMapper.writeValue(new File("pm_tree2.json"), pm_tree);
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				
				endTime=System.currentTimeMillis();
				totalTime=endTime-startTime;
				System.out.println("写入: totalTime="+totalTime+"ms");
				//记录ID最新位置
				PointDataMap = new HashMap<Integer,PointData>();
				PointData Point_data[] = Write_Read.toReadPoint(last_point_path);
				length = Point_data.length;
				key = 0;
				lastID = Point_data[length-1].id;
				
				while(key < length-1)
				{
					PointDataMap.put(Point_data[key].id, Point_data[key]);
					key++;
					
				}
				
				Point_data = null;
				MBR_data = null;
				MBR_data_pointer = null;
		    	break;
		    case 2:
		    	long MemoryS, MemoryE, MemoryTotal;
		    	MemoryS = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		    	startTime=System.currentTimeMillis();
		    	pm_tree = Write_Read.rebuild_PMtree(path);
		    	endTime=System.currentTimeMillis();
				totalTime=endTime-startTime;
				System.out.println("重建: totalTime="+totalTime+"ms");
				if(pm_tree == null)
		    		break;
				MemoryE = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
				MemoryTotal = MemoryE - MemoryS;
				System.out.println("内存消耗: MemoryTotal="+MemoryTotal+"byte");
				
		    	String queryStr = Write_Read.toRead(WindowQuery_path);
		    	String []query_temp_data = queryStr.split(" ");//new String[str.split(" ").length];
		    	int query_length = query_temp_data.length;
                if (query_length % 4 != 0) 
                   	break;
                int j = 0;
                startTime=0; endTime=0; totalTime=0;
                while(j < query_length)
                {
                    MBR query = new MBR(0,Integer.valueOf(query_temp_data[j]),Integer.valueOf(query_temp_data[j+1]),Integer.valueOf(query_temp_data[j+2]),Integer.valueOf(query_temp_data[j+3]));
    				startTime = System.currentTimeMillis();
    				List<MBR> query_result = PMtreeQuery.ToqueryIntersect(pm_tree, query);
    				List<MBR> final_result = PMtreeQuery.GetResult(query_result, query);
    				endTime = System.currentTimeMillis();
    				totalTime = totalTime + (endTime-startTime);
    				j = j+4;
    				/*
    				for(MBR l:final_result)
    			    {
    			    	System.out.println(l.p1.id+" "+l.a+" "+l.b);
    			    }
    			    */
    			}
                System.out.println("MBR查询时间: totalTime="+totalTime+"ms");

				break;
				/*
		    	System.out.println("请输入查询区间(格式为:d1 t1 d2 t2)");
		    	int a[] = new int[4];
		    	for(int z=0; z<4; z++)
		    	{
		    		in = new Scanner(System.in);
		    		a[z]  =in.nextInt();	
		    	}
		    	MBR query = new MBR(0,a[0],a[1],a[2],a[3]);
		    	*/
		    	//22 2 77 444
		    	//List<MBR> query_result = PMtreeQuery.Toquery(pm_tree, query);
		    	//List<MBR> query_result = PMtreeQuery.ToqueryIntersect(pm_tree, query);
		    	/*System.out.println("query:"+query.a+" "+query.b);*/
		    	/*for(MBR l:query_result)
		    	{
		    		System.out.println(l.p1.id+" "+l.a+" "+l.b);
		    	}*/
		    	//List<MBR> final_result = PMtreeQuery.GetResult(query_result, query);
		    	/*
				for(MBR l:final_result)
		    	{
		    		System.out.println(l.p1.id);
		    	}
				break;
				*/
		    case 3:
		    	startTime=System.currentTimeMillis();
		    	pm_tree = Write_Read.rebuild_PMtree(path);
		    	endTime=System.currentTimeMillis();
				totalTime=endTime-startTime;
				System.out.println("重建: totalTime="+totalTime+"ms");
		    	
				if(pm_tree == null)
		    		break;
				
				String PointQueryStr = Write_Read.toRead(PointQuery_path);
		    	String []PointQuery_temp_data = PointQueryStr.split(" ");//new String[str.split(" ").length];
		    	int PointQuery_length = PointQuery_temp_data.length;
                if (PointQuery_length % 2 != 0) 
                   	break;
                int Pointj = 0;
                startTime=0; endTime=0; totalTime=0;
                while(Pointj < PointQuery_length)
                {
                    MBR Pointquery = new MBR(0,Integer.valueOf(PointQuery_temp_data[Pointj]),Integer.valueOf(PointQuery_temp_data[Pointj+1]),Integer.valueOf(PointQuery_temp_data[Pointj]),Integer.valueOf(PointQuery_temp_data[Pointj+1]));
    				startTime = System.currentTimeMillis();
    				List<MBR> Pointquery_result = PMtreeQuery.ToqueryPointIntersect(pm_tree, Pointquery);
    		    	List<PointData> Pointfinal_result = PMtreeQuery.GetPointResult(Pointquery_result, Pointquery);
    				endTime = System.currentTimeMillis();
    				totalTime = totalTime + (endTime-startTime);
    				Pointj = Pointj+2;
    				/*
    				for(PointData l:Pointfinal_result)
		    		{
		    			System.out.println(l.id+" "+l.d+" "+l.t);
		    		}
    			    */
    			}
                System.out.println("Point查询时间: totalTime="+totalTime+"ms");				
				/*											
		    	System.out.println("请输入查询区间(格式为:d1 t1 d2 t2)");
		    	int b[] = new int[2];
		    	for(int z=0; z<2; z++)
		    	{
		    		in = new Scanner(System.in);
		    		b[z]  =in.nextInt();	
		    	}
		    	MBR Pointquery = new MBR(0,b[0],b[1],b[0],b[1]);
		    	
		    	List<MBR> Pointquery_result = PMtreeQuery.ToqueryPointIntersect(pm_tree, Pointquery);
		    	List<PointData> Pointfinal_result = PMtreeQuery.GetPointResult(Pointquery_result, Pointquery);
		    	
		    	for(PointData l:Pointfinal_result)
		    	{
		    		System.out.println(l.id+" "+l.d+" "+l.t);
		    	}
				*/
		    	
		    	break;
		    case 4:
		    	startTime=System.currentTimeMillis();
		    	pm_tree = Write_Read.rebuild_PMtree(path);
		    	endTime=System.currentTimeMillis();
				totalTime=endTime-startTime;
				System.out.println("重建: totalTime="+totalTime+"ms");
				if(pm_tree == null)
		    		break;
				
				//记录ID最新位置
				if (PointDataMap == null)
				{
					PointDataMap = new HashMap<Integer,PointData>();
					PointData Point_data2[] = Write_Read.toReadPoint(last_point_path);
					length = Point_data2.length;
					key = 0;
					lastID = Point_data2[length-1].id;
					while(key < length-1)
					{
						PointDataMap.put(Point_data2[key].id, Point_data2[key]);
						key++;
					}
					Point_data = null;
				}
				
				String insertPointStr = Write_Read.toRead(insertPoint_path);
		    	String []insertPoint_temp_data = insertPointStr.split(" ");//new String[str.split(" ").length];
		    	int insertPoint_length = insertPoint_temp_data.length;
                if (insertPoint_length % 3 != 0) 
                   	break;
                int PointN = 0;
                int insertPointID;
                startTime=0; endTime=0; totalTime=0;
                while(PointN < insertPoint_length)
                {
                	insertPointID = Integer.valueOf(insertPoint_temp_data[PointN]);
                	PointData tempPoint = PointDataMap.get(insertPointID);
                	MBR insertMBR;
                	if (tempPoint != null)
                	{
                		insertMBR = new MBR(insertPointID,tempPoint.d,tempPoint.t,Integer.valueOf(insertPoint_temp_data[PointN+1]),Integer.valueOf(insertPoint_temp_data[PointN+2]));
                	}
                	else
                	{
                		insertMBR = new MBR(insertPointID,Integer.valueOf(insertPoint_temp_data[PointN+1]),Integer.valueOf(insertPoint_temp_data[PointN+2]),Integer.valueOf(insertPoint_temp_data[PointN+1]),Integer.valueOf(insertPoint_temp_data[PointN+2]));
                	}
                	startTime=System.currentTimeMillis();
    		    	PMtreeInsert.InsertUpdate(pm_tree, insertMBR);
    		    	endTime=System.currentTimeMillis();
    				totalTime=(endTime-startTime) + totalTime;                	
                	PointN = PointN+3;
    			}	
				
				
				/*
				System.out.println("请输入插入点id(id<="+lastID+")");
				int ID;
				in = new Scanner(System.in);
				ID = in.nextInt();	
				PointData tempPoint = PointDataMap.get(ID);
				
				System.out.println("请输入插入点(格式为:d(d>"+tempPoint.d+") t(t>"+tempPoint.t+")");
				
		    	int c[] = new int[2];
		    	for(int z=0; z<2; z++)
		    	{
		    		in = new Scanner(System.in);
		    		c[z]  =in.nextInt();		    		
		    	}
		    	MBR insertMBR = new MBR(ID,tempPoint.d,tempPoint.t,c[0],c[1]);
		    
		    	
		    	startTime=System.currentTimeMillis();
		    	PMtreeInsert.InsertUpdate(pm_tree, insertMBR);
		    	endTime=System.currentTimeMillis();
				totalTime=endTime-startTime;
				*/
				
				System.out.println("插入更新: totalTime="+totalTime+"ms");
								
				
						
				//重写最新位置记录
				
		    	break;
		    case 5:
		    	
		    	startTime=System.currentTimeMillis();
	    		CreateData.GenerateQueryData(WindowQuery_path);
	    		endTime=System.currentTimeMillis();
	    		totalTime=endTime-startTime;
	    		System.out.println("生成查询MBR: totalTime="+totalTime+"ms");
		    	break;
		    	
		    case 6:
				
				//记录ID最新位置
				if (PointDataMap == null)
				{
					PointDataMap = new HashMap<Integer,PointData>();
					PointData Point_data2[] = Write_Read.toReadPoint(last_point_path);
					length = Point_data2.length;
					key = 0;
					lastID = Point_data2[length-1].id;
					while(key < length-1)
					{
						PointDataMap.put(Point_data2[key].id, Point_data2[key]);
						key++;
					}
					Point_data = null;
				}
				
				System.out.println("请输入插入点的个数：");
				int InsertNum;
				InsertNum = in.nextInt();
				
				for(int n=0; n<InsertNum; n++)
				{
					Random rnd = new Random();
					int oid;
					int p[] = new int[2];
					
					oid = rnd.nextInt(lastID);
					PointData oidPoint = PointDataMap.get(oid);					
					int a[] = new int[2];
					a[0] = rnd.nextInt(50);
					a[1] = rnd.nextInt(50);
					
					p[0] = oidPoint.d + a[0];
					p[1] = oidPoint.t + a[1];
					
					/*
					oid = rnd.nextInt(lastID) + lastID;
					p[0] = rnd.nextInt(1000);
					p[1] = rnd.nextInt(1000);
					*/
					String str = oid + " "+ p[0] + " "+ p[1] + " ";
					Write_Read.toWrite(str, insertPoint_path, 1);
				}
				
				
		
		    	
		    	
		    	break;
		    	
		    default:
		    	System.out.println("*********程序结束********");
		    	System.exit(0);
		        break;		
		    }
		}		
	}
	
	public static List<List<MBR>> generateLop(List<MBR> MBR_data, int length) {
		int count = 0;
		//System.out.println("MBR元素个数:"+length+","+MBR_data.size());
		if (MBR_data == null||length==0) {
			return null;
		}
		List<List<MBR>> result = new ArrayList<List<MBR>>();
		List<MBR> tempSubList = null;
		
		MBR temp = null;
		MBR finder = null;
		
		//i表示已经进入Lop的元素个数
		for (int i = 0; i < length;) {
			temp = MBR_data.get(0);
			i++;
			tempSubList = new ArrayList<MBR>();
			tempSubList.add(temp);
			MBR_data.remove(0);
			
			int num = 0;
			String str_temp = "";
			
			//str_temp = str_temp + temp.p1.id + " " + temp.a + " " + temp.b + " ";
			str_temp = str_temp + temp.p1.id + " " +
				       + temp.p1.d + " " +
					   + temp.p1.t + " " +
					   + temp.p2.d + " " +
					   + temp.p2.t + " ";
			num++;
			
			//最后一个元素单独组成LOP的情况
			if(i == length){
				writer = writer + Integer.toString(num) + " " + str_temp;	
				count = count + num;
				result.add(tempSubList);
			}
			
			int key = MBR_data.size();
			for (int j = 0; j < key; j++) {
				finder = MBR_data.get(j);			
				if(finder.a >= temp.a && finder.b<=temp.b){
					tempSubList.add(finder);
					//str_temp = str_temp + finder.p1.id + " " + finder.a + " " +finder.b + " ";
					str_temp = str_temp + finder.p1.id + " " +
				       + finder.p1.d + " " +
					   + finder.p1.t + " " +
					   + finder.p2.d + " " +
					   + finder.p2.t + " ";
			        num++;
			        
					i++;
					temp = finder;
					MBR_data.remove(j);
					key--;
					j--;
				}
			}		
			writer = writer + Integer.toString(num) + " " + str_temp;	
			count = count + num;
			result.add(tempSubList);
		}
		//System.out.println("LOP元素个数:"+count);
		return result;
	}
}