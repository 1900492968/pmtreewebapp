package create_data;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import com.PointData;

public class CreateData {

	/**
	 * @param args
	 */
	public static void GenerateData(String path) {
		
		System.out.print("要生成多少个数据：");
		Scanner in = new Scanner(System.in);
		int create_num = in.nextInt();
		System.out.print("要生成同一个对象多少个数据：");
		in = new Scanner(System.in);
		int obj_number = in.nextInt();
		
		StringBuffer str = new StringBuffer();
		int id = 0;
		
		PointData point[] = new PointData[create_num];
		int i = 0;
		while(i < create_num)
		{
			Random rnd = new Random();
			int obj_num = 0;
			while (obj_num<=0)
			{
				obj_num = rnd.nextInt(obj_number);
			}
			PointData temp_point[] = new PointData[obj_num];
			int j = 0;
			while(obj_num>0)
			{
				int a[] = new int[2];
				a[0] = rnd.nextInt(1000);
				a[1] = rnd.nextInt(1000);
				temp_point[j] = new PointData(id,a[0],a[1]);
				obj_num--;
				j++;
	    	//System.out.println("原始数据：["id+","+a[0] +","+ a[1] + "]");
			}
			Arrays.sort(temp_point);
			obj_num = temp_point.length;
			j = 0;
			while(obj_num > 0 && i < create_num)
			{
			  point[i] = temp_point[j];
			  save(point[i], path);
			  System.out.println("原始数据：["+point[i].id+","+point[i].d +","+ point[i].t + "]");
			  j++;
			  i++; 
			  obj_num--;
			}
			id++;
		}
		/*	
		try{
			FileOutputStream record_out = new FileOutputStream(path, true);
			DataOutputStream recordWriter = new DataOutputStream(record_out);
			recordWriter.writeBytes(str.toString());	
		}catch (Exception e){
			System.err.println("发生异常：" + e);
			e.printStackTrace();
		}*/
		//Write_Read.toWrite(str, path, 1);
		
	}
	
public static void GenerateData2(String path) {
	    String path_name[] = path.split(".txt");
		String mbr_path = path_name[0]+"MBR.txt";
		String Pointpath[] = path.split(".txt");
		String lastPointpath = Pointpath[0]+"Point.txt";
		
		System.out.print("要生成多少个数据：");
		Scanner in = new Scanner(System.in);
		int create_num = in.nextInt();
		System.out.print("同一个对象最多有多少个数据：");
		in = new Scanner(System.in);
		int obj_number = in.nextInt();
		int id = 0;
		int i = 0;
		Write_Read.toWrite("", path, 0);
		Write_Read.toWrite("", mbr_path, 0);
		Write_Read.toWrite("", lastPointpath, 0);
		
		while(i < create_num)
		{
			Random rnd = new Random();
			int obj_num = 0;
			while (obj_num<=0)
			{
				obj_num = rnd.nextInt(obj_number);
			}
			int j = 0;
			int d[] = new int[obj_num];
			int t[] = new int[obj_num];
			
			while(j<obj_num)
			{
				do{ //need to revise
					d[j] = rnd.nextInt(1000);
					t[j] = rnd.nextInt(1000);
				}while(d[j]==0 || t[j]==0);//need to revise
				j++;
	    	//System.out.println("原始数据：["+id+","+a[0] +","+ a[1] + "]");
			}
			Arrays.sort(d);
			Arrays.sort(t);
			
			PointData temp_point[] = new PointData[obj_num];
			j=0;
			while(j<obj_num)
			{
				//System.out.println("原始数据：["+id+","+d[j] +","+ t[j] + "]");
				temp_point[j] = new PointData(id,d[j],t[j]);	
				j++;
			}
			save(temp_point[obj_num - 1], lastPointpath);
			j = 0;
			if(obj_num == 1)
			{
				save_MBR(temp_point[0],temp_point[0],mbr_path);
				save(temp_point[0], path);
				i++;
			}
			else
			{
				save(temp_point[0], path);
				for(int l = 1; l < obj_num;l++){
					if(i < create_num)
					{
						save_MBR(temp_point[l-1],temp_point[l],mbr_path);
						save(temp_point[l], path);
						i++;
					}
				}
			}
			id++;
		}
	}
	
	public static void save(PointData point, String save_path)
	{
		String str = point.id + " "+ point.d + " "+ point.t + " " ;
		Write_Read.toWrite(str, save_path, 1);
	}
 
	public static void save_MBR(PointData point1,PointData point2, String save_path)
	{
		String str = point1.id + " " + point1.d + " "+ point1.t + " " + point2.d + " "+ point2.t + " ";
		Write_Read.toWrite(str, save_path, 1);
	}
	
	public static void GenerateQueryData(String path) {
		System.out.print("要生成多少查询mbr：");
		Scanner in = new Scanner(System.in);
		int query_num = in.nextInt();
		System.out.print("同一个查询mbr最大跨度为多少：");
		in = new Scanner(System.in);
		int MBR_span = in.nextInt();
		int i = 0;
		
		while(i < query_num)
		{
			Random rnd = new Random();			
			int d[] = new int[2];
			int t[] = new int[2];
			do{
			d[0] = rnd.nextInt(1000);
			d[1] = rnd.nextInt(1000);
			t[0] = rnd.nextInt(1000);
			t[1] = rnd.nextInt(1000);
			Arrays.sort(d);
			Arrays.sort(t);
			}while(d[1]-d[0] > MBR_span || t[1]-t[0] > MBR_span);
			String str = d[0] + " "+ t[0] + " "+ d[1] + " "+ t[1] + " ";
			Write_Read.toWrite(str, path, 1);
			i++;
		}
		
		
	}
	
}
