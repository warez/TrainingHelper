package it.traininghelper.valueobject;

import com.google.appengine.api.users.User;

/**
 * Created by warez on 03/06/15.
 */
public class UserInfo {

    private String userId;
    private String email;
    private String nickName;
    private final boolean isLogged;
    private final String url;

    public UserInfo(User user, String url) {

        this.isLogged = user != null;
        if(user != null) {
            this.email = user.getEmail();
            this.nickName = user.getNickname();
            this.userId = user.getUserId();
        }
        this.url = url;

    }

    public String getEmail() {
        return email;
    }

    public String getNickName() {
        return nickName;
    }

    public boolean isLogged() {
        return isLogged;
    }

    public String getUrl() {
        return url;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
