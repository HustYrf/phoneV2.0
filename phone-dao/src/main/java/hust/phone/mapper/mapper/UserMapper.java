package hust.phone.mapper.mapper;

import hust.phone.mapper.pojo.User;
import hust.phone.mapper.pojo.UserExample;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface UserMapper {
	
    int countByExample(UserExample example);

    int deleteByExample(UserExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    List<User> selectByExample(UserExample example);

    User selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") User record, @Param("example") UserExample example);

    int updateByExample(@Param("record") User record, @Param("example") UserExample example);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    //Id自增
    int insertSelectiveWithIdInc(User record);

    //根据该用户名查出用户的数量
    int selectByUserName(String name);

//    int selectByUserNameAndRole(@Param("username") String username, @Param("role") String role);

    int selectUserCount();

//    int selectCountWithRole(@Param("Role") String searchUserStatus);

//    List<User> selectUserByRole(@Param("page") TailPage<User> page, @Param("role") String searchUserStatus);

	int userAddTasknum(User user);
	
	int userReduceTasknum(User user);

	int getTaskNumByUser(User user);

	String getNameByUserId(Integer id);
}