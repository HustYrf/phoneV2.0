package hust.phone.mapper.mapper;

import java.util.List;

import hust.phone.mapper.pojo.User_has_GroupKey;


public interface User_has_GroupKeyMapper {

	 List<User_has_GroupKey> getAllGroupByUserId(int UserId);
}
