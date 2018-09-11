package hust.phone.mapper.mapper;

import java.util.List;

import hust.phone.mapper.pojo.Route;

public interface RouteMapper {
	List<Route> selectALLRoute();

	int insert(Route route);

	List<Route> getRouteByNameAndType(String name, int type);

	List<Route> selectRoute(String name, int type);

	List<Route> getRouteByType(int type);
}
