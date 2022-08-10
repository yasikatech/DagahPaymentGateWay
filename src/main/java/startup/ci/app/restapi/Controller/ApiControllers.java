package startup.ci.app.restapi.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import startup.ci.app.restapi.Model.Merchants;
import startup.ci.app.restapi.Model.TransactionProcessor;
import startup.ci.app.restapi.Model.User;
import startup.ci.app.restapi.Repo.MerchantsRepo;
import startup.ci.app.restapi.Repo.TransactionProcessorRepo;
import startup.ci.app.restapi.Repo.UserRepo;
import startup.ci.app.restapi.Util.Util;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
public class ApiControllers {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private TransactionProcessorRepo transactionProcessorRepo;

    @Autowired
    private MerchantsRepo merchantsRepo;


    //// Setting global variables for transaction processing
    private UUID transactionReference;
    private Date transactionDate;
    private Util util;


    @GetMapping(value = "/")
    public String getPage() {

        return "Welcome";
    }

    @GetMapping(value = "/users")
    public List<User> getUsers() {

        return userRepo.findAll();
    }

    @PostMapping(value = "/save")
    public String saveUser(@RequestBody User user) {
        userRepo.save(user);
        return "Record(s) successfully saved ....";
    }

    @PutMapping(value = "update/{id}")
    public String updateUser(@PathVariable long id, @RequestBody User user) {

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
    public String deleteUser(@PathVariable long id) {

        User deleteUser = userRepo.findById(id).get();
        userRepo.delete(deleteUser);
        return "Deleted id is " + id;
    }


    //Create Merchants
    @PostMapping(value = "/createMerchant")
    public String merchant(@RequestBody Merchants merchant) {
        //Auto generating merchant ID
        String uniqueID = UUID.randomUUID().toString();

        merchant.setMerchantId("DGT" + uniqueID);

        merchantsRepo.save(merchant);

        return "Merchants successfully created";
    }


    //Fetch Merchants
    @GetMapping(value = "/fetchMerchants")
    public List<Merchants> merchants() {
        return merchantsRepo.findAll();
    }


    ///Transaction posting
    @PostMapping(value = "/postTransaction")
    public String postTransaction(@RequestBody TransactionProcessor transactionProcessor) {

        //Automatically generating the user reference number
        String uniqueID = UUID.randomUUID().toString();

        //Automatically assigning timestamp of the transaction
        Date transactionDate = Util.getTodayDate();

        //Checking for the payment method used to determine the final API to call
        if (transactionProcessor.getTransactionType().equalsIgnoreCase("Mobile")) {
            //Make an API call to the telco involved for the transaction MTN, Airtel or others
            if (transactionProcessor.getTelcoName() != null && transactionProcessor.getTelcoName().equalsIgnoreCase("MTN")) {
                /// Route the transaction to MTN for processing
                transactionProcessor.setTransactionType("Mobile");
                transactionProcessor.setTelcoName("MTN");
            } else if (transactionProcessor.getTelcoName() != null && transactionProcessor.getTelcoName().equalsIgnoreCase("Airtel")) {
                /// Route the transaction to Airtel
                transactionProcessor.setTransactionType("Mobile");
                transactionProcessor.setTelcoName("Airtel");
            } else if (transactionProcessor.getTelcoName() != null && transactionProcessor.getTelcoName().equalsIgnoreCase("Orange")) {
                // Route the transaction to Orange
                transactionProcessor.setTransactionType("Mobile");
                transactionProcessor.setTelcoName("Orange");
            } else {
                // Telco does not exist in the list
                transactionProcessor.setTransactionType("Telco does not exist in the list");
            }

            transactionProcessor.setTransactionType("Momo");


        } else if (transactionProcessor.getTransactionType() != null && transactionProcessor.getTransactionType().equalsIgnoreCase("Card")) {
            // Make an API call to the Card service
            transactionProcessor.setTransactionType("Card");
            transactionProcessor.setTelcoName("");


        } else if (transactionProcessor.getTransactionType() != null && transactionProcessor.getTransactionType().equalsIgnoreCase("Account")) {
            //Calling Ecobank API

            String uri = "";
            RestTemplate restTemplate = new RestTemplate();
            String result = restTemplate.getForObject(uri, String.class);
            //return result;

            //Make an API call to Ecobank payment API
            transactionProcessor.setTransactionType("Account");
            transactionProcessor.setTelcoName("");


        } else {
            transactionProcessor.setTransactionType("Transaction type unavailable");
        }

        transactionProcessor.setTransactionDate(transactionDate);
        transactionProcessor.setTransactionReference(uniqueID);
        transactionProcessorRepo.save(transactionProcessor);
        return "Transaction successfully posted";
    }

    //Fetch Transactions
    @GetMapping(value = "/fetchTransactions")
    public List<TransactionProcessor> fetchTransactions() {
        return transactionProcessorRepo.findAll();
    }

}
