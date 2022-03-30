package tp1.clients.user;

import tp1.api.User;
import tp1.util.Debug;

import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UpdateUserClient {

    private static Logger Log = Logger.getLogger(CreateUserClient.class.getName());

    static {
        System.setProperty("java.net.preferIPv4Stack", "true");
    }

    public static void main(String[] args){

        Debug.setLogLevel(Level.FINE, Debug.SD2122);

        //  At the moment I'm
        if(args.length != 6){
            System.err.println("Invalid Opertaion.");
            System.err.println("Use: java tp1.clients.user.DeleteUserClient url userId password newPwd email fullName");
            return;
        }

        String serverUrl = args[0];
        String userId = args[1];
        String password = args[2];
        String newPwd = args[3];
        String email = args[4];
        String fullName = args[5];

        User user = new User(userId, fullName, email, newPwd);

        System.out.println("Sending request to server...");

        new RestUsersClient(URI.create(serverUrl)).updateUser(userId, password, user);

    }
}
