package our.devdays.clip.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import our.devdays.clip.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findByUserId(String userId);
}
