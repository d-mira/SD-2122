package tp1.clients;

import jakarta.ws.rs.client.WebTarget;
import tp1.api.service.rest.RestFiles;
import tp1.clients.RestClient;

import java.net.URI;

public class RestFilesClient extends RestClient implements RestFiles {

    final WebTarget target;

    RestFilesClient(URI serverURI){
        super(serverURI);
        target = client.target(serverURI).path(RestFiles.PATH);
    }

    @Override
    public void writeFile(String fileId, byte[] data, String token) {
        super.reTry( () -> clt_writeFile(fileId, data, token) );
    }

    @Override
    public void deleteFile(String fileId, String token) {
        super.reTry( () -> clt_deleteFile(fileId, token) );
    }

    @Override
    public byte[] getFile(String fileId, String token) {
        return super.reTry( () -> clt_getFile(fileId, token) );
    }

    //Private Methods

    private void clt_writeFile(String fileId, byte[] data, String token){

    }

    private void clt_deleteFile(String fileId, String token){

    }

    private byte[] clt_getFile(String fileId, String token){
        return null;
    }
}
