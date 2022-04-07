package tp1.server.resources;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import tp1.api.FileInfo;
import tp1.api.User;
import tp1.api.service.rest.RestDirectory;
import tp1.clients.RestUsersClient;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;

public class DirectoryResources implements RestDirectory {

    private static Logger Log = Logger.getLogger(DirectoryResources.class.getName());

    private UserResources userResources = new UserResources();
    private FileResources fileResources = new FileResources();

    public DirectoryResources(){
    }

    @Override
    public FileInfo writeFile(String filename, byte[] data, String userId, String password) {

        if(filename == null || data == null || userId == null || password == null){
            Log.info("Invalid data.");
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        String fileURL = String.format("/%s/%s", userId, filename);

        FileInfo fileInfo = new FileInfo(userId, filename, fileURL, new HashSet<>());

        //What should the token be??
        String token = "";

        fileResources.writeFile(filename, data, token);

        return fileInfo;
    }

    @Override
    public void deleteFile(String filename, String userId, String password) {

    }

    @Override
    public void shareFile(String filename, String userId, String userIdShare, String password) {

    }

    @Override
    public void unshareFile(String filename, String userId, String userIdShare, String password) {

    }

    @Override
    public byte[] getFile(String filename, String userId, String accUserId, String password) {
        return new byte[0];
    }

    @Override
    public List<FileInfo> lsFile(String userId, String password) {
        return null;
    }
}
