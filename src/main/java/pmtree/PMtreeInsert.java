package pmtree;

import java.util.ArrayList;
import java.util.List;

import com.MBR;

public class PMtreeInsert {
	public static void InsertUpdate(List<List<MBR>> pmtree, MBR insertMBR)
	{
		int length = pmtree.size();
		int i = 0;
		int firstLocation,lastLocation;
		while(i<length)
		{
			List<MBR> tempList = pmtree.get(i);
			int lobLength = tempList.size();
			if(lobLength == 0)
				System.out.println("god");
			if(tempList.get(tempList.size()-1).a <= insertMBR.a){			//Lob的最后一个结点起始<=v，v不是加入Lob尾，就是不能加入本分支
				if(tempList.get(tempList.size()-1).b >= insertMBR.b){		//(1)Lob的最后一个终止结点时间>=v，即包含v，那么v成为尾结点:ok!!!
					tempList.add(insertMBR);
					i = length + 1;
				}else{					   									//(2)Lob的最后一个终止时间<v，即v不会插入本分支
					i++;
					continue;
				}
			}else{															//Lob的最后一个起始时间>v
				if(tempList.get(0).b <= insertMBR.b){						//Lob的第一个终止时间<=v
					if(tempList.get(0).a >= insertMBR.a){					//(3)Lob的第一个起始时间>=v，即被v包含，那么v成为头结点:ok!!!
						tempList.add(0, insertMBR);
						i = length + 1;
					}else{													//(4)Lob的第一个起始时间<v,即v不会插入本分支
						i++;
						continue;
					}
				}else{														//Lob的最后一个起始时间>v,第一个终止时间>v
					if(tempList.get(0).a > insertMBR.a){					//(5)Lob的第一个起始时间>v,v所在分支应该在当前分支的前一条，新建分支frontList
						List<MBR> frontList = new ArrayList<MBR>();
						frontList.add(insertMBR);
						
						if(tempList.get(tempList.size()-1).b > insertMBR.b){	//(5)':插入点在LOB左下方
							pmtree.add(i,frontList);
							i = length + 1;
						}
						else{												//(5)'':当前LOB部分被截取,firstLocation寻找被包含截取的第一个结点
							firstLocation = 1;
							while(tempList.get(firstLocation).a < insertMBR.a 
									|| tempList.get(firstLocation).b > insertMBR.b)
							{
								firstLocation++;
							}
							List<MBR> behindList = new ArrayList<MBR>();
							behindList.addAll(tempList.subList(firstLocation, lobLength-1));
							frontList.addAll(behindList);
							tempList.removeAll(behindList);
					
							behindList = tempList;
							tempList = frontList;
							//frontList = behindList;
							/*
							for(int j = firstLocation; j < lobLength; j++)
							{
								frontList.add(tempList.get(j));
							}
							for(int j = firstLocation; j < lobLength; j++)
							{
								tempList.remove(j);
							}*/
							
							InsertLinkUpdate(pmtree,i+1,behindList);
							i = length + 1;
						}
					}else{										//Lob的最后一个起始时间>v，Lob的第一个包含v，j记录第一个不包含v的结点
						int j = 0;
						while(j < length
								&& tempList.get(j).a <= insertMBR.a 
								&& tempList.get(j).b >= insertMBR.b
								)
						{
							j++;
						}
						
						if(j == lobLength-1){								//LOB上每一个结点都包含v（实际上不会出现）
							tempList.add(insertMBR);
							i = length + 1;
							continue;
						}else{												//记录第一个不包含v的结点，编号为j
							if( tempList.get(j).b > insertMBR.b ){			//(6)j的终止时间>v，新建一条分支
								List<MBR> behindList = new ArrayList<MBR>();
								
								if(tempList.get(lobLength-1).b > insertMBR.b){	//(6)-1 Lob的最后一个在OUR(v), Lob的第一个包含v,v为当前Lob最后一个
									firstLocation = j;
									behindList.addAll(tempList.subList(firstLocation, lobLength-1));
									tempList.removeAll(behindList);
									tempList.add(insertMBR);
									/*
									while(firstLocation < lobLength)
									{
										behindList.add(tempList.get(firstLocation));
										firstLocation++;
									}
									firstLocation = j;
									while(firstLocation < lobLength)
									{
										firstLocation++;
										tempList.remove(firstLocation);	
									}*/

								}
								else{									//(6)-2 Lob的最后一个在DR(v),j是第一个不包含v的结点
									firstLocation = j;
									while(firstLocation<lobLength)          //firstLocation记录第一个被v包含的结点，则从j到firstLocation之间的放到behindList
									{
										if(tempList.get(firstLocation).a >= insertMBR.a
												&& tempList.get(firstLocation).b <= insertMBR.b)
											break;
										else
											firstLocation++;
									}
									behindList.addAll(tempList.subList(j, firstLocation-1));
									tempList.removeAll(behindList);
									/*
									for(int z = j;z<=firstLocation;z++)
									{
										behindList.add(tempList.get(z));
									}
									for(int z = j;z<=firstLocation;z++)
									{
										tempList.remove(j);									//每次删除同一位置，后面提前
									}*/
									tempList.add(j,insertMBR);
								}
								InsertLinkUpdate(pmtree,i+1,behindList);
								i = length + 1;
								
							}else{								//j的终止时间<=v，即t在ODL(v)或DR(v)
								if(tempList.get(j).a >= insertMBR.a)		//(7)j属于DR（v） : ok!!!!
								{
									tempList.add(j,insertMBR);
									i = length + 1;
								} 
								else{							//(7)'t属于ODL(v): ok!!!
									i++;
									continue;
								}
							}
						}
					}
				}
			}
		}
		if(i < (length + 1))
		{
			List<MBR> behindList = new ArrayList<MBR>();
			behindList.add(insertMBR);
			pmtree.add(behindList);
		}
	}
	
	public static void InsertLinkUpdate(List<List<MBR>> pmtree, int location, List<MBR> subList)
	{
		if(location > pmtree.size()-1)												//不存在Lob包含subList
		{
			pmtree.add(subList);
		}else{
			List<MBR> tempList = pmtree.get(location);
			int i,j,firstLocation,lastLocation,length;
			if(subList.size() != 0)
			{
				MBR firstSub = subList.get(0), lastSub = subList.get(subList.size()-1);
				length = tempList.size();
				i = 0;
			
				if(subList.get(subList.size()-1).a <= tempList.get(0).a				//subList 包含下一LOB
						&& subList.get(subList.size()-1).b >= tempList.get(0).b)          	
				{
					tempList.addAll(0, subList);
					/*
					for(i=subList.size()-1;i>0;i--)
					{
						tempList.add(0,subList.get(i));
					}*/
					subList = null;
				}else if(subList.get(0).a >= tempList.get(length-1).a						//subList 被下一LOB包含
						&& subList.get(0).b <= tempList.get(length-1).b)
				{
					tempList.addAll(subList);
					/*
					i=subList.size()-1;
					for(j=0;j<i;j++)
					{
						tempList.add(0,subList.get(j));
					}*/
					subList = null;
				}else if(subList.get(0).a < tempList.get(length-1).a						//subList和下一LOB互不包含
						&& subList.get(0).b < tempList.get(length-1).b){
					pmtree.add(location,subList);
					subList = null;
				}else{																		//下一条LOB与subList有被对方包含部分
					while(i < length)														//lastLocation定位最后一个包含subList的结点
					{
						if(tempList.get(i).a <= firstSub.a &&
								tempList.get(i).b >= firstSub.b)
						{
							i++;
						}
						else
							break;
					}
					
					lastLocation = i-1;
					
					i  = 0;
					while(i < length)														//firstLocation定位第一个被subList包含的结点
					{
						if(tempList.get(i).a < lastSub.a ||
								tempList.get(i).b > lastSub.b)
						{
							i++;
						}	
						else
							break;
					}
					firstLocation = i;
					
					List<MBR> behindList = new ArrayList<MBR>();
					if(lastLocation < 0 ){													//Lob没有包含subList的结点，但有被包含结点
						behindList.addAll(tempList.subList(0, firstLocation-1));
						tempList.removeAll(behindList);
						tempList.addAll(0, subList);
						InsertLinkUpdate(pmtree,location+1,behindList);
					}else if(firstLocation == length)										//Lob没有被subList包含的结点，但有包含它的结点
					{
						behindList.addAll(tempList.subList(lastLocation+1, length-1));
						tempList.removeAll(behindList);
						tempList.addAll(subList);
						InsertLinkUpdate(pmtree,location+1,behindList);
					}else{																	//Lob和subList互相有包含对方的结点
						if(lastLocation >= firstLocation-1)
						{
							tempList.addAll(lastLocation+1, subList);
						}
						else{
							behindList.addAll(tempList.subList(lastLocation+1, firstLocation-1));
							tempList.removeAll(behindList);
							tempList.addAll(lastLocation+1, subList);
							InsertLinkUpdate(pmtree,location+1,behindList);
						}
					}
				}
			}
		}
	}

}