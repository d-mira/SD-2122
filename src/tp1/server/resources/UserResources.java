package tp1.server.resources;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;
import tp1.api.User;
import tp1.api.service.rest.RestUsers;

import java.util.*;
import java.util.logging.Logger;

public class UserResources implements RestUsers {

    private static final Map<String, User> users = new HashMap<>();

    private static Logger Log = Logger.getLogger(UserResources.class.getName());

    public UserResources(){
    }

    @Override
    public String createUser(User user) {
        Log.info("create user: " + user);

        //Check if the user has valid data
        if(user.getUserId() == null || user.getPassword() == null){
            Log.info("User object invalid.");
            throw new WebApplicationException(Status.BAD_REQUEST);
        }

        //Check if the user already exists
        User u = users.get(user.getUserId());

        if(u != null){
            Log.info("User already exists.");
            throw new WebApplicationException(Status.CONFLICT);
        }

        //Add user to the map of users
        users.put(user.getUserId(), user);
        return user.getUserId();
    }

    @Override
    public User getUser(String userId, String password) {
        //Check if the data is valid
        if(userId == null || password == null){
            Log.info("Invalid data.");
            throw new WebApplicationException(Status.BAD_REQUEST);
        }

        //Check if the user exists
        User user = users.get(userId);
        if(user == null){
            Log.info("User does not exist.");
            throw new WebApplicationException(Status.NOT_FOUND);
        }

        //Check if inputted password matches user password
        if(!user.getPassword().equals(password)){
            Log.info("Username and/or password are incorrect.");
            throw new WebApplicationException(Status.FORBIDDEN);
        }

        return user;
    }

    @Override
    public User updateUser(String userId, String password, User user) {
        //Check if data is valid
        if(userId == null || password == null){
            Log.info("Invalid data.");
            throw new WebApplicationException(Status.BAD_REQUEST);
        }

        if(user.getUserId() == null || user.getPassword() == null){
            Log.info("Invalid data.");
            throw new WebApplicationException(Status.BAD_REQUEST);
        }

        //Check if user exists
        User u = users.get(userId);
        if(u == null){
            Log.info("User does not exist.");
            throw new WebApplicationException(Status.NOT_FOUND);
        }

        //Check if inputted data matches user data
        if(!u.getPassword().equals(password) || !user.getUserId().equals(userId)){
            Log.info("Username and/or password are incorrect.");
            throw new WebApplicationException(Status.FORBIDDEN);
        }

        //Change user data by replacing the entry in the users map
        users.replace(userId, user);

        return users.get(userId);
    }

    @Override
    public User deleteUser(String userId, String password) {
        //Check if data is valid
        if(userId == null || password == null){
            Log.info("Invalid data.");
            throw new WebApplicationException(Status.BAD_REQUEST);
        }

        //Check if user exists
        User user = users.get(userId);
        if(user == null){
            Log.info("User does not exist.");
            throw new WebApplicationException(Status.NOT_FOUND);
        }

        //Check if inputted data matches user data,
        // this is necessary because only the user can delete its account
        if(!userId.equals(user.getUserId()) || !password.equals(user.getPassword())){
            Log.info("Username and/or password are incorrect.");
            throw new WebApplicationException(Status.FORBIDDEN);
        }
        return user;
    }

    @Override
    public List<User> searchUsers(String pattern) {
        //Check if parameter is valid
        if (pattern == null) {
            Log.info("Invalid data.");
            throw new WebApplicationException(Status.BAD_REQUEST);
        }

        Log.info("searchUsers : pattern = " + pattern);
        String pat = pattern.toLowerCase();

        List<User> matchingUsers = new LinkedList<>();

        //Added functionality to search by username or name
        for (User user : users.values()) {
            String name = user.getFullName().toLowerCase();
            String username = user.getUserId().toLowerCase();
            if (name.contains(pat) || username.contains(pat)) {
                User userHidePw = new User(
                        user.getUserId(), user.getFullName(),
                        user.getEmail(), "");
                matchingUsers.add(userHidePw);
            }
        }

        return matchingUsers;
    }
}
