package our.devdays.clip.service;


import our.devdays.clip.entity.User;

public interface UserService {
    void signup(User user);
    boolean login(String userId, String password);

}
