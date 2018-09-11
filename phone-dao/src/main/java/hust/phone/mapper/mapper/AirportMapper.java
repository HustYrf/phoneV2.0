package hust.phone.mapper.mapper;

import java.util.List;

import hust.phone.mapper.pojo.Airport;

public interface AirportMapper {

	List<Airport> getAllAirport();
	
	int insertAirport(Airport airport);
	
}
