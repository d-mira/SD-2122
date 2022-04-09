package tp1.clients;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import tp1.api.User;
import tp1.api.service.rest.RestFiles;
import tp1.clients.RestClient;

import java.net.URI;

public class RestFilesClient extends RestClient implements RestFiles {

    final WebTarget target;

    public RestFilesClient(URI serverURI){
        super(serverURI);
        target = client.target(serverURI).path(RestFiles.PATH);
    }

    @Override
    public void writeFile(String fileId, byte[] data, String token) {
        super.reTry( () -> {
            return clt_writeFile(fileId, data, token);
        } );
    }

    @Override
    public void deleteFile(String fileId, String token) {
        super.reTry( () -> {
            return clt_deleteFile(fileId, token);
        } );
    }

    @Override
    public byte[] getFile(String fileId, String token) {
        return super.reTry( () -> clt_getFile(fileId, token) );
    }

    //Private Methods

    private String clt_writeFile(String fileId, byte[] data, String token){
            Response r = target
                    .path(fileId)
                    .queryParam(token).request()
                    .accept(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(data, MediaType.APPLICATION_OCTET_STREAM));

        if( r.getStatus() == Response.Status.OK.getStatusCode() && r.hasEntity() )
            return "";
        else
            System.out.println("Error, HTTP error status: " + r.getStatus() );

        return "";
    }

    private String clt_deleteFile(String fileId, String token){
        Response r = target
                .path(fileId)
                .queryParam(token).request()
                .accept(MediaType.APPLICATION_JSON)
                .delete();

        if( r.getStatus() == Response.Status.OK.getStatusCode() && r.hasEntity() )
            return "";
        else
            System.out.println("Error, HTTP error status: " + r.getStatus() );

        return "";
    }

    private byte[] clt_getFile(String fileId, String token){
        Response r = target
                .path(fileId)
                .queryParam(token).request()
                .accept(MediaType.APPLICATION_OCTET_STREAM)
                .get();

        if( r.getStatus() == Response.Status.OK.getStatusCode() && r.hasEntity() )
            return r.readEntity(byte[].class);
        else
            System.out.println("Error, HTTP error status: " + r.getStatus() );

        return null;
    }
}
