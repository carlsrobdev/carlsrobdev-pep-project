package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    AccountService accountService;
    MessageService messageService;


    public SocialMediaController() {
        accountService = new AccountService();
        messageService = new MessageService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        app.post("/register", this::register);
        app.post("/login", this::login);
        app.post("/messages", this::newMessage);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    private void register(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        if(!account.getUsername().isEmpty() && account.password.length() > 4) {
            Account registerdAccount = accountService.registerAccount(account);
            if(registerdAccount != null) {
                context.json(mapper.writeValueAsString(registerdAccount));
            } else
                context.status(400);
        } else
            context.status(400);
    }

    private void login(Context context)throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account credentials = accountService.login(account);
            if(credentials != null) {
                context.json(mapper.writeValueAsString(credentials));
            } else
                context.status(401);
    }

    private void newMessage(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message messageInput = mapper.readValue(context.body(), Message.class);
        if(!messageInput.getMessage_text().isEmpty() && messageInput.getMessage_text().length() <= 255) {
            Account accountExist = accountService.getAccountById(messageInput.posted_by);
            if(accountExist != null) {
                Message message = messageService.createMessage(messageInput);
                if(message != null) {
                    context.json(mapper.writeValueAsString(message));
                } else {
                    context.status(400);
                }
            } else
                context.status(400);
        } else
            context.status(400);
    }


}