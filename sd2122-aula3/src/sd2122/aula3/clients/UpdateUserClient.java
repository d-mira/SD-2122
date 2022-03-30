package sd2122.aula3.clients;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import sd2122.aula3.api.User;
import util.Debug;

public class UpdateUserClient {

    private static Logger Log = Logger.getLogger(CreateUserClient.class.getName());

    static {
        System.setProperty("java.net.preferIPv4Stack", "true");
    }

    public static void main(String[] args) throws IOException{
        Debug.setLogLevel(Level.FINE, Debug.SD2122);

        if(args.length != 6){
            System.err.println("Use: java sd2122.aula3.clients.UpdateUserClient url userId oldPwd fullName email password");
            return;
        }

        String serverUrl = args[0];
        String userId = args[1];
        String oldpwd = args[2];
        String fullName = args[3];
        String email = args[4];
        String password = args[5];

        User user = new User(userId, fullName, email, password);

        Log.info("Sending request to server...");

        var result = new RestUsersClient(URI.create(serverUrl)).updateUser(userId, oldpwd, user);
        System.out.println("Result: " + result);
    }
}
