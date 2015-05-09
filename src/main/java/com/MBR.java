package com;

public class MBR implements Comparable{
	//二价区间左下点坐标
	public PointData p1;
	public PointData p2;
	
	//二阶区间相点数
	public int a;
	public int b;

	public MBR(){
	}
	
	public MBR(PointData p1,PointData p2){
		this.p1 = p1;
		this.p2 = p2;
		
		this.a = p1.d+p1.t;
		this.b = p2.d+p2.t;
	}
	
	public MBR(int id,int d1, int t1, int d2, int t2){ 
		this.p1 = new PointData(id,d1,t1);
		this.p2 = new PointData(id,d2,t2);;
		
		this.a = p1.d+p1.t;
		this.b = p2.d+p2.t;
	}
	
	public String toData(PointData p11,PointData p22)
	{
		PointData p1 = p11;
		PointData p2 = p22;
		
		String ss = Integer.toString(p1.id) + " " +
		Integer.toString(p1.d) + " " + 
		Integer.toString(p1.t) + " " + 
		Integer.toString(p2.d) + " " + 
		Integer.toString(p2.t) + " "; 
		return ss;
		
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("("+p1.id+"：").append(p1.d).append(",").append(p1.t).append(";").append(p2.d).append(",").append(p2.t).append(")");
		//String ss = sb.toString();
		return sb.toString();
	}
	
	//返回1，this排后面，返回-1，this排前面
	public int compareTo(Object o) {
		MBR specify = (MBR)o;
		
		if(this.a == specify.a){
			if(this.b == specify.b){
			return 0;
			}
			else if(this.b < specify.b){
				return 1;
			}
				
		}
		
		if(this.a > specify.a){    
			return 1;
		}
		
		return -1;
	}	
}
