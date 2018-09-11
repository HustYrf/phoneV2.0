package hust.phone.web.controller.admin;

import hust.phone.constant.WebConst;
import hust.phone.mapper.pojo.User;
import hust.phone.service.interFace.UserService;
import hust.phone.utils.pojo.JsonView;
import hust.phone.utils.pojo.MapCache;
import hust.phone.utils.pojo.PhoneUtils;
import hust.phone.utils.pojo.TipException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.TileObserver;
import java.io.IOException;

@Controller(value = "AdminIndexController")
public class AdminIndexController {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(AdminIndexController.class);
    private MapCache cache = MapCache.single();
    @Resource
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginIndex() {
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String doLogin(@RequestParam String username, @RequestParam String password,
                          @RequestParam(required = false) String remeber_me, HttpServletRequest request, HttpServletResponse response) {
        Integer error_count = cache.get("login_error_count");
        try {
            User user = userService.login(username, password);
            request.getSession().setAttribute(WebConst.LOGIN_SESSION_KEY, user);
            if (StringUtils.isNotBlank(remeber_me)) {
                PhoneUtils.setCookie(response, user.getId());
            }
        } catch (Exception e) {
            error_count = null == error_count ? 1 : error_count + 1;
            if (error_count > 3) {
                return JsonView.render(1, "您输入密码已经错误超过3次，请10分钟后尝试");
            }
            cache.set("login_error_count", error_count, 10 * 60);
            String msg = "登录失败";
            if (e instanceof TipException) {
                msg = e.getMessage();
            } else {
                LOGGER.error(msg, e);
            }
            return JsonView.render(1, msg);
        }
        return JsonView.render("");
    }

    @RequestMapping(value = "admin/register", method = RequestMethod.GET)
    public String getRegister() {
        return "register";
    }

    @RequestMapping(value = "admin/register", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String doRegister(@RequestParam String username, @RequestParam String password
            , @RequestParam(required = false) String agree_me) {
        try {
            if (StringUtils.isBlank(agree_me)) {
                throw new TipException("请同意注册条款");
            }
            boolean flag = userService.register(username, password);
            if (flag == false){
                return JsonView.render(1,"注册失败，请重新注册");
            }
        } catch (Exception e) {
            String msg = "注册失败";
            if(e instanceof TipException){
                msg = e.getMessage();
            }else{
                LOGGER.error(msg,e);
            }
            return JsonView.render(1,msg);
        }
        return JsonView.render(0,"注册成功,请返回登陆页面登陆");
    }


    @RequestMapping(value = "logout",method = RequestMethod.GET)
    public void logout(HttpSession session, HttpServletResponse response, HttpServletRequest request) {
        session.removeAttribute(WebConst.LOGIN_SESSION_KEY);
        Cookie cookie = new Cookie(WebConst.USER_IN_COOKIE, "");
        cookie.setValue(null);
        cookie.setMaxAge(0);// 立即销毁cookie
        cookie.setPath("/");
        response.addCookie(cookie);
        try {
            response.sendRedirect("login");
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("注销失败", e);
        }
    }
}
