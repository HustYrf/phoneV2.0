package hust.phone.service.impl;

import hust.phone.mapper.pojo.UserExample;
import hust.phone.utils.DateKit;
import hust.phone.utils.UUID;
import hust.phone.utils.pojo.PhoneUtils;
import hust.phone.utils.pojo.TipException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hust.phone.mapper.mapper.UserMapper;
import hust.phone.mapper.pojo.User;
import hust.phone.service.interFace.UserService;

import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class UserServiceimpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User getUserById(int id) {
        User user = userMapper.selectByPrimaryKey(id);
        return user;
    }

    @Override
    public User login(String username, String password) {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            throw new TipException("用户名、密码和身份不能为空");
        }
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andNameEqualTo(username);
        //criteria.andRoleEqualTo(identity);
        
        int count = userMapper.countByExample(example);
        
        if (count < 1) {
            throw new TipException("没有该用户");
        }
        String pwd = PhoneUtils.MD5encode(username + password);
        criteria.andPasswordEqualTo(pwd);
        List<User> userList = userMapper.selectByExample(example);
        if (userList.size() != 1) {
            throw new TipException("用户名密码错误或您没有操作权限");
        }
        return userList.get(0);
    }

    @Override
    public boolean register(String username, String password) {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password) ) {
            throw new TipException("用户名、密码不能为空");
        }
        int userCount = userMapper.selectByUserName(username);
        if(userCount==1){
            throw new TipException("该用户已经存在");
        }
        User user = new User();
        user.setName(username);
        user.setPassword(PhoneUtils.MD5encode(username+password));
        //user.setRole(role);
        user.setCreatetime(DateKit.getNowTime());
        //String description = role.equals("1")?"起飞员":"降落员";
        //user.setDescripte(description);
        //user.setUserid(UUID.UU32());
        int count = userMapper.insertSelective(user);
        if(count == 1)
        	return true;
        else
        	return false;
    }

	@Override
	public int getTaskNumByUser(User user) {
		int tasknum = userMapper.getTaskNumByUser(user);
		return tasknum;
	}

	@Override
	public String getNameByUserId(Integer usercreator) {
		return userMapper.getNameByUserId(usercreator);
	}

	@Override
	public boolean updateByUser(User user) {
		if(userMapper.updateByPrimaryKeySelective(user)==1) {
			return true;
		}else {
			return false;
		}
			
	}
}
