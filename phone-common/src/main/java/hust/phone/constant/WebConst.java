package hust.phone.constant;

import org.springframework.stereotype.Component;

@Component
public class WebConst {
    public static String LOGIN_SESSION_KEY = "login_user";

    public static final String USER_IN_COOKIE = "S_L_ID";
    /**
     * 成功返回
     */
    public static String SUCCESS_RESULT = "SUCCESS";
    /**
     * aes加密加盐
     */
    public static String AES_SALT = "0123456789abcdef";
}
