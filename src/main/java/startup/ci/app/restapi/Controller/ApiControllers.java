package startup.ci.app.restapi.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import startup.ci.app.restapi.Model.TransactionProcessor;
import startup.ci.app.restapi.Model.User;
import startup.ci.app.restapi.Repo.TransactionProcessorRepo;
import startup.ci.app.restapi.Repo.UserRepo;
import startup.ci.app.restapi.Util.Util;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
public class ApiControllers {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private TransactionProcessorRepo transactionProcessorRepo;


    private Util util;



    //// Setting global variables for transaction processing
    private UUID transactionReference;
    private Date transactionDate;

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


    ///Transaction posting
    @PostMapping(value = "/postTransaction")
    public String postTransaction(@RequestBody TransactionProcessor transactionProcessor){

        //Automatically generating the user reference number
        String uniqueID = UUID.randomUUID().toString();



        //Automatically assigning timestamp of the transaction
        Date transactionDate = Util.getTodayDate();


        transactionProcessor.setTransactionDate(transactionDate);
        transactionProcessor.setTransactionReference(uniqueID);
        transactionProcessorRepo.save(transactionProcessor);
        return "Transaction successfully posted";
    }
}
