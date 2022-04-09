package tp1.clients;

import jakarta.ws.rs.client.WebTarget;
import tp1.api.FileInfo;
import tp1.api.service.rest.RestDirectory;
import tp1.api.service.rest.RestFiles;
import tp1.clients.RestClient;

import java.net.URI;
import java.util.List;

public class RestDirectoriesClient extends RestClient implements RestDirectory {

    final WebTarget target;

    RestDirectoriesClient(URI serverURI){
        super(serverURI);
        target = client.target(serverURI).path(RestFiles.PATH);
    }

    @Override
    public FileInfo writeFile(String filename, byte[] data, String userId, String password) {
        return null;
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

    //Private Methods
}
