package Controller;

import java.util.List;

import Model.Account;
import Service.AccountService;

import Model.Message;
import Service.MessageService;

import io.javalin.Javalin;
import io.javalin.http.Context;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * 
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
    
        // ACCOUNT //////////////////////////
    
        app.get("/users", this::getAllUsersHandler);

        // Register Account
        app.post("/register", this::postAccountHandler);

        // Account Login
        app.post("/login", this::postLoginHandler);

        // MESSAGES ////////////////////////
        
        // **** Create ********
        app.post("/messages", this::addMessageHandler);

        // **** Retrieve ********

        // Get All Messages
        app.get("/messages", this::getAllMessagessHandler);

        // Get Message by Message ID
        app.get("/messages/{message_id}", this::getMessageByIDHandler);

        // **** Update ********
        
        // **** Delete ********

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void getAllUsersHandler(Context context) {
        List<Account> accounts = accountService.getAllAccounts();
        context.json(accounts);
    }

    private void postAccountHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper oMapper = new ObjectMapper();
        Account newAccount = oMapper.readValue(ctx.body(), Account.class);
        // username and password validation check
        if (newAccount.getUsername() == null || newAccount.getPassword() == null || newAccount.getUsername().length() < 1 || newAccount.getPassword().length() < 4 || accountService.checkAccountExist(newAccount.getUsername())) {
            // Invalid new account
            ctx.status(400);
        } else {
            Account addedAccount = accountService.insertAccount(newAccount);
            if (addedAccount != null ) {
                ctx.json(oMapper.writeValueAsString(addedAccount));
                // Account successfully inserted
                ctx.status(200);
            } else {
                // Account not inserted
                ctx.status(400);
            }
        }
    }

    private void postLoginHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper oMapper = new ObjectMapper();
        Account targetAccount = oMapper.readValue(ctx.body(), Account.class);
        // Find account according to username and password
        Account foundAccount = accountService.getAccount(targetAccount.getUsername(), targetAccount.getPassword());
        if (foundAccount != null ) {
            ctx.json(oMapper.writeValueAsString(foundAccount));
            // Successful Login
            ctx.status(200);
        } else {
            // Unauthorized Login
            ctx.status(401);
        }
    }

    // MESSAGES //////////////////////////////////////

    // **** Create ********

    /**
     * Insert a message
     * @param ctx
     */
    private void addMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper oMapper = new ObjectMapper();
        Message newMessage = oMapper.readValue(ctx.body(), Message.class);
        ctx.json(newMessage);
        Message postedMessage = messageService.addMessage(newMessage);
        if (postedMessage != null) {
            // Successfully inserted message
            ctx.json(postedMessage);
            ctx.status(200);
        } else {
            ctx.result("");
            ctx.status(400);
        }
        // ctx.result("In add message handler");
    }

    // **** Retrieve *******

    private void getAllMessagessHandler(Context ctx) {
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages);
    }

    private void getMessageByIDHandler(Context ctx) {
        int targetMessageID = Integer.parseInt(ctx.pathParam("message_id")) ;
        Message foundMessage = messageService.getMessage(targetMessageID);
        // ctx.result("Message ID received: " + ctx.pathParam("message_id"));
        if (foundMessage != null) {
            ctx.json(foundMessage);
        } else {
            ctx.result("");
        }
        ctx.status(200);
    }
}