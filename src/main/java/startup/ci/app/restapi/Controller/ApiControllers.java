package startup.ci.app.restapi.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import startup.ci.app.restapi.Model.User;
import startup.ci.app.restapi.Repo.UserRepo;

import java.util.List;

@RestController
public class ApiControllers {

    @Autowired
    private UserRepo userRepo;

    @GetMapping(value="/")
    public String getPage(){

        return "Welcome";
    }

    @GetMapping(value = "/users")
    public List<User> getUsers(){

        return userRepo.findAll();
    }

    @PostMapping(value = "/save")
    public String saveUser(@RequestBody User user){
        userRepo.save(user);
        return "Record(s) successfully saved ....";
    }

    @PutMapping(value = "update/{id}")
    public String updateUser(@PathVariable long id, @RequestBody User user){

        User updatedUser = userRepo.findById(id).get();
        updatedUser.setFirstName(user.getFirstName());
        updatedUser.setLastName(user.getLastName());
        updatedUser.setAge(user.getAge());
        updatedUser.setOccupation(user.getOccupation());
        userRepo.save(updatedUser);
        userRepo.save(user);
        return "Record(s) successfully updated ....";
    }


    @DeleteMapping(value = "delete/{id}")
    public String deleteUser(@PathVariable long id){

        User deleteUser = userRepo.findById(id).get();
        userRepo.delete(deleteUser);
        return "Deleted id is " + id;
    }
}
