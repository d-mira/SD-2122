package tp1.server.resources;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import tp1.api.Discovery;
import tp1.api.FileInfo;
import tp1.api.User;
import tp1.api.service.rest.RestDirectory;
import tp1.api.service.rest.RestUsers;
import tp1.clients.RestFilesClient;
import tp1.clients.RestUsersClient;
import tp1.server.FilesServer;
import tp1.server.UsersServer;

import java.net.URI;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class DirectoryResources implements RestDirectory {

    private static Logger Log = Logger.getLogger(DirectoryResources.class.getName());

    private Map<String, FileInfo> fileInfos = new ConcurrentHashMap<>();

    public DirectoryResources(){
        Discovery.getInstance().listener();
    }

    @Override
    public FileInfo writeFile(String filename, byte[] data, String userId, String password) {

        if(filename == null || data == null || userId == null || password == null){
            Log.info("Invalid data.");
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        String fileURL = String.format("/%s/%s", userId, filename);

        FileInfo info = new FileInfo(userId, filename, fileURL, new HashSet<>());

        //What should the token be??
        String token = "";

        URI usersURI = Discovery.getInstance().knownUrisOf(UsersServer.SERVICE)[0];
        //We might need to try multiple fileServers don't yet know
        URI[] filesURIs = Discovery.getInstance().knownUrisOf(FilesServer.SERVICE);

        User user = new RestUsersClient(usersURI).getUser(userId, password);

        if(user == null){
            Log.info("User does not exist.");
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        new RestFilesClient(filesURIs[0]).writeFile(filename, data, token);

        fileInfos.put(filename, info);

        return info;
    }

    @Override
    public void deleteFile(String filename, String userId, String password) {
        if(filename == null || userId == null || password == null){
            Log.info("Invalid data.");
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        URI usersURI = Discovery.getInstance().knownUrisOf(UsersServer.SERVICE)[0];
        //We might need to try multiple fileServers don't yet know
        URI[] filesURIs = Discovery.getInstance().knownUrisOf(FilesServer.SERVICE);
        User user = new RestUsersClient(usersURI).getUser(userId, password);

        String token = "";

        if(user == null){
            Log.info("User does not exist.");
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        FileInfo info = fileInfos.get(filename);

        if(info == null) {
            Log.info("File does not exist.");
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        if(!info.getSharedWith().contains(userId) || !info.getOwner().equals(userId)){
            Log.info("Not allowed.");
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        }

        new RestFilesClient(filesURIs[0]).deleteFile(filename, token);

        fileInfos.remove(filename);
    }

    @Override
    public void shareFile(String filename, String userId, String userIdShare, String password) {
        if(filename == null || userId == null || userIdShare == null || password == null){
            Log.info("Invalid data.");
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        URI usersURI = Discovery.getInstance().knownUrisOf(UsersServer.SERVICE)[0];
        User owner = new RestUsersClient(usersURI).getUser(userId, password);

        if(owner == null){
            Log.info("User does not exist.");
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        FileInfo info = fileInfos.get(filename);

        if(info == null) {
            Log.info("File does not exist.");
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        if(!info.getSharedWith().contains(userId) || !info.getOwner().equals(userId)){
            Log.info("Not allowed.");
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        }

        info.getSharedWith().add(userIdShare);
    }

    @Override
    public void unshareFile(String filename, String userId, String userIdShare, String password) {
        if(filename == null || userId == null || userIdShare == null || password == null){
            Log.info("Invalid data.");
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        URI usersURI = Discovery.getInstance().knownUrisOf(UsersServer.SERVICE)[0];
        User owner = new RestUsersClient(usersURI).getUser(userId, password);

        if(owner == null){
            Log.info("User does not exist.");
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        FileInfo info = fileInfos.get(filename);

        if(info == null) {
            Log.info("File does not exist.");
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        if(!info.getSharedWith().contains(userId) || !info.getOwner().equals(userId)){
            Log.info("Not allowed.");
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        }

        info.getSharedWith().remove(userIdShare);
    }

    @Override
    public byte[] getFile(String filename, String userId, String accUserId, String password) {
        if(filename == null || userId == null || accUserId == null || password == null){
            Log.info("Invalid data.");
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        URI usersURI = Discovery.getInstance().knownUrisOf(UsersServer.SERVICE)[0];
        User user = new RestUsersClient(usersURI).getUser(accUserId, password);

        if(user == null){
            Log.info("User does not exist.");
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        FileInfo info = fileInfos.get(filename);

        if(info == null) {
            Log.info("File does not exist.");
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        if(!info.getSharedWith().contains(accUserId)){
            Log.info("Not allowed.");
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        }

        throw new WebApplicationException(
                Response.temporaryRedirect(
                        URI.create(String.format("%s/%s/%s", usersURI.toString(), userId, filename))
                ).build()
        );
    }

    @Override
    public List<FileInfo> lsFile(String userId, String password) {
        if(userId == null || password == null){
            Log.info("Invalid data.");
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        URI usersURI = Discovery.getInstance().knownUrisOf(UsersServer.SERVICE)[0];
        User user = new RestUsersClient(usersURI).getUser(userId, password);

        if(user == null){
            Log.info("User does not exist.");
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        List<FileInfo> userFiles = Collections.synchronizedList(new ArrayList<>());
        for (FileInfo info : fileInfos.values()) {
            if(info.getOwner().equals(user.getUserId()) ||
                    info.getSharedWith().contains(user.getUserId())){
                    userFiles.add(info);
            }
        }

        return userFiles;
    }
}
