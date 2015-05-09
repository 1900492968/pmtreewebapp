package com;

public class PointData implements Comparable{
	//id d t
	public int id;
	public int d;
	public int t;

	public PointData(){
	}
	
	public PointData(int id,int d,int t){
		this.id = id;
		this.d  = d;
		this.t  = t;
	}
	
	//return string
	public String toData(PointData a)
	{
		int id = a.id;
		int d = a.d;
		int t = a.t;

		String ss = Integer.toString(id) + " " + 
		Integer.toString(d) + " " + 
		Integer.toString(t) + " ";
		return ss;
	}
	
	//和toData功能相似，只是这个将调用者的信息String化
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("(").append(id).append(",").append(d).append(";").append(t).append(")");
		//String ss = sb.toString();
		return sb.toString();
	}
	
	public int compareTo(Object o) {
		 PointData specify = ( PointData)o;
		
		if(this.d == specify.d){
			if(this.t == specify.t){
			return 0;
			}
			else if(this.t < specify.t){
				return -1;
			}
				
		}
		
		if(this.d < specify.d){
			return -1;
		}
		
		return 1;
	}	
}
