package hust.phone.web.controller.vo;

import java.util.ArrayList;


import hust.phone.mapper.pojo.FlyingPath;
import hust.phone.utils.DateKit;
import hust.phone.utils.LineUtil;

public class FlyingPathVO {

	private Integer id;

	private Integer airportZ;

	private Integer airportA;

	private String name;

	private ArrayList<ArrayList<Double>> pathdata;

	private String description;

	private String createtime;

	private String updatetime;

//	private ArrayList<Double> heightdata;
	
//	private ArrayList<Integer> typedata;
//	
//	private ArrayList<Float> paramonedata;
//	
//	private ArrayList<Float> paramtwodata;

	public FlyingPathVO(FlyingPath flyingPath) {

		this.id = flyingPath.getId();
		
//		if (flyingPath.getHeightdata() != null) {
//			this.heightdata = LineUtil.stringpointToList(flyingPath.getHeightdata());
//		}
//		if (flyingPath.getPointtype() != null) {
//			this.typedata = LineUtil.stringpointToIntList(flyingPath.getPointtype());
//		}
//		if (flyingPath.getParamone() != null) {
//			this.paramonedata = LineUtil.stringpointToFloatList(flyingPath.getParamone());
//		}
//		if (flyingPath.getParamtwo() != null) {
//			this.paramtwodata = LineUtil.stringpointToFloatList(flyingPath.getParamtwo());
//		}
		if (flyingPath.getDescription() != null) {
			this.description = flyingPath.getDescription();
		}
		if (flyingPath.getCreatetime() != null) {
			this.createtime = DateKit.dateFormat(flyingPath.getCreatetime(), "yyyy/MM/dd HH:mm:ss");
		}
		if (flyingPath.getUpdatetime() != null) {
			this.updatetime = DateKit.dateFormat(flyingPath.getUpdatetime(), "yyyy/MM/dd HH:mm:ss");
		}
		//对类型为178和206的数据需要进行处理，不作为航线落图的数据，但是仍需要作为航线数据进行下发。
		if (flyingPath.getPathdata() != null && flyingPath.getPointtype()!= null) {

			this.pathdata = new ArrayList<ArrayList<Double>>();
			String sub=flyingPath.getPathdata().substring(11, flyingPath.getPathdata().length()-1);
			String slist[]=sub.split(",");
			String pointType[] = flyingPath.getPointtype().split(",");
			for(int i=0;i<slist.length;i++)
			{
				if (pointType[i].equals("22") ||pointType[i].equals("20") || pointType[i].equals("178") || pointType[i].equals("206") || pointType[i].equals("189") || pointType[i].equals("208")) {
                    continue;
                }
				ArrayList<Double> point=new ArrayList<Double>();
				/*
                 * 还可以根据纬度来判断航线是在哪个区域（南宁，桂林，武汉..），
                 * 再加或减不同的数值
                 */
				
				//经度纠偏
                point.add(Double.parseDouble(slist[i].split(" ")[0]) + 0.000637);
                //纬度纠偏
                point.add(Double.parseDouble(slist[i].split(" ")[1]) - 0.001596);
				this.pathdata.add(point);
			}
		}else if(flyingPath.getPathdata() != null && flyingPath.getPointtype()== null){
			//如果航点类型为空，则全部都显示
			this.pathdata = new ArrayList<ArrayList<Double>>();
			String sub=flyingPath.getPathdata().substring(11, flyingPath.getPathdata().length()-1);
			String slist[]=sub.split(",");
			for(int i=0;i<slist.length;i++) {
				ArrayList<Double> point=new ArrayList<Double>();
				point.add(Double.parseDouble(slist[i].split(" ")[0]) + 0.000637);
	            point.add(Double.parseDouble(slist[i].split(" ")[1]) - 0.001596);
				this.pathdata.add(point);
			}

		}

	}

//	public ArrayList<Integer> getTypedata() {
//		return typedata;
//	}
//
//	public void setTypedata(ArrayList<Integer> typedata) {
//		this.typedata = typedata;
//	}
//
//	public ArrayList<Float> getParamonedata() {
//		return paramonedata;
//	}
//
//	public void setParamonedata(ArrayList<Float> paramonedata) {
//		this.paramonedata = paramonedata;
//	}
//
//	public ArrayList<Float> getParamtwodata() {
//		return paramtwodata;
//	}
//
//	public void setParamtwodata(ArrayList<Float> paramtwodata) {
//		this.paramtwodata = paramtwodata;
//	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAirportZ() {
		return airportZ;
	}

	public void setAirportZ(Integer airportZ) {
		this.airportZ = airportZ;
	}

	public Integer getAirportA() {
		return airportA;
	}

	public void setAirportA(Integer airportA) {
		this.airportA = airportA;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<ArrayList<Double>> getPathdata() {
		return pathdata;
	}

	public void setPathdata(ArrayList<ArrayList<Double>> pathdata) {
		this.pathdata = pathdata;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}

//	public ArrayList<Double> getHeightdata() {
//		return heightdata;
//	}
//
//	public void setHeightdata(ArrayList<Double> heightdata) {
//		this.heightdata = heightdata;
//	}

}
