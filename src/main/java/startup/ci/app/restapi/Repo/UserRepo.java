package startup.ci.app.restapi.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import startup.ci.app.restapi.Model.User;

public interface UserRepo extends JpaRepository<User, Long> {

}
