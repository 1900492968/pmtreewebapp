package create_data;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.MBR;
import com.PointData;


public class Write_Read {
    
	public static void toWrite(String str, String path, int flag){
		try{
			
			if(flag == 1){
			FileOutputStream record_out = new FileOutputStream(path, true);
			DataOutputStream recordWriter = new DataOutputStream(record_out);
			recordWriter.writeBytes(str);
			}
			else{
			FileOutputStream record_out = new FileOutputStream(path);
			DataOutputStream recordWriter = new DataOutputStream(record_out);
			recordWriter.writeBytes(str);
			}
			
		}catch (Exception e){
			System.err.println("发生异常：" + e);
			e.printStackTrace();
		}
	}
	
	public static String toRead(String path){
		String read = "";
		
		File filename = new File(path);
		
		try{
			
			StringBuffer address_array = new StringBuffer();
			int address_line = 0;
			
			FileReader f_in;
			f_in = new FileReader(filename);
			//读取数据，装入address数组中
			BufferedReader bufread = new BufferedReader(f_in);
			while((read = bufread.readLine()) != null) {
				address_array.append(read);
				address_array.append(" ");
				address_line++;
				}
			read = address_array.toString();
			bufread.close();
		}catch (Exception e){
			System.err.println("发生异常：" + e);
			e.printStackTrace();
		}
		
		return read;

	}	
	
	public static MBR[] ReadtoMBR(String path)
	{
		String read = "";
		File filename = new File(path);
		try{
			
			StringBuffer address_array = new StringBuffer();
			int address_line = 0;
			
			FileReader f_in;
			f_in = new FileReader(filename);
			//读取MBR数据，装入address数组中
			
			BufferedReader bufread = new BufferedReader(f_in);
			while((read = bufread.readLine()) != null) {
				address_array.append(read);
			    //address_array.append(" ");
				address_line++;
				}
			read = address_array.toString();
			bufread.close();
		}catch (Exception e){
			System.err.println("发生异常：" + e);
			e.printStackTrace();
		}
		
		int length = read.split(" ").length;	
		MBR temp_MBR[] = new MBR[length / 5];
		//System.out.println("MBR数目"+length / 5);
		toReadData(temp_MBR, read, length);
		
		return temp_MBR;
	}
	
	public static PointData[] toReadPoint(String lastPointPath){
		String read = "";
		File filename = new File(lastPointPath);
		try{
			
			StringBuffer address_array = new StringBuffer();
			int address_line = 0;
			
			FileReader f_in;
			f_in = new FileReader(filename);
			//读取MBR数据，装入address数组中
			
			BufferedReader bufread = new BufferedReader(f_in);
			while((read = bufread.readLine()) != null) {
				address_array.append(read);
			    //address_array.append(" ");
				address_line++;
				}
			read = address_array.toString();
			bufread.close();
		}catch (Exception e){
			System.err.println("发生异常：" + e);
			e.printStackTrace();
		}
		
		int length = read.split(" ").length;
		
		
		String []temp_data = new String[length];
		temp_data = read.split(" ");

		int i = 0;
		int j = 0;
		
		length = length;
		PointData tempPointArray[] = new PointData[length/3];
		while(i < length){
			tempPointArray[j] = new PointData(Integer.valueOf(temp_data[i]), Integer.valueOf(temp_data[i+1]), 
					Integer.valueOf(temp_data[i+2]));
			
			j++;
			i = i + 3;
		}
		return tempPointArray;
	}
	
	public static void toReadData(MBR temp_MBR[],String str,int length){
		
		String []temp_data = new String[length];
		temp_data = str.split(" ");
		int i = 0;
		int j = 0;
		
		while(i < length-1){
			temp_MBR[j] = new MBR(Integer.valueOf(temp_data[i]), Integer.valueOf(temp_data[i+1]), 
					Integer.valueOf(temp_data[i+2]),Integer.valueOf(temp_data[i+3]),
					Integer.valueOf(temp_data[i+4]));
			
			j++;
			i = i + 5;
		}
		
	}
	
	public static List<List<MBR>> rebuild_PMtree(String path) {
		String str = toRead(path);
		List<List<MBR>> result = new  ArrayList<List<MBR>>();
		
		
		int i = 0;
		String []temp_data = str.split(" ");//new String[str.split(" ").length];
		int length = temp_data.length;
		while(i < length-1)
		{
			 int j = 0;
			 int count = Integer.valueOf(temp_data[i]);
			 MBR[] temp_MBR = new MBR[count];
			 List<MBR> subList = new ArrayList<MBR>();
			 while(j < count)
			 {	 
				 temp_MBR[j] = new MBR(Integer.valueOf(temp_data[i+1]), Integer.valueOf(temp_data[i+2]), 
							Integer.valueOf(temp_data[i+3]),Integer.valueOf(temp_data[i+4]),
							Integer.valueOf(temp_data[i+5]));
				 subList.add(temp_MBR[j]);
				 i = i + 5;		 
				 j++;
			 }
			 result.add(subList); 
			 //System.out.println("Input : "+StringUtil.array2String(point)+ " "); 
			 i++;
		}
		 
		return result;
	
	}
	
	
}
