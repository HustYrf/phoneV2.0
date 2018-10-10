package hust.phone.service.interFace;

import hust.phone.mapper.pojo.User;

public interface UserService {
	
    User getUserById(int id);

    User login(String username, String password);

    boolean register(String username, String password);

	int getTaskNumByUser(User loginUser);

	String getNameByUserId(Integer usercreator);

	boolean updateByUser(User user);

}
