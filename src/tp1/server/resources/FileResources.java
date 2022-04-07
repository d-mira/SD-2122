package tp1.server.resources;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import tp1.api.FileInfo;
import tp1.api.service.rest.RestFiles;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class FileResources implements RestFiles {

    private static Logger Log = Logger.getLogger(FileResources.class.getName());

    //Not sure if we should actually write a file or just store the data
    // to simulate a filesystem
    //Assuming the second option:
    private Map<String, byte[]> filesystem = new HashMap<>();

    public FileResources(){
    }

    @Override
    public void writeFile(String fileId, byte[] data, String token) {

        //Validity of parameters
        if(fileId == null || data == null || token == null){
            Log.info("Invalid data.");
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        //Validate token, how?
        /* if(!token.isValid()){
        *   Log.info("Session expired.")
        *   throw new WebApplicationException(Response.Status.FORBIDDEN);
        * }
        * */

        filesystem.put(fileId, data);
    }

    @Override
    public void deleteFile(String fileId, String token) {

        if(fileId == null || token == null){
            Log.info("Invalid data.");
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        //Validate token, how?
        /* if(!token.isValid()){
         *   Log.info("Session expired.")
         *   throw new WebApplicationException(Response.Status.FORBIDDEN);
         * }
         * */

        if(filesystem.get(fileId) == null){
            Log.info("File does not exist.");
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        filesystem.remove(fileId);
    }

    @Override
    public byte[] getFile(String fileId, String token) {

        if(fileId == null || token == null){
            Log.info("Invalid data.");
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        //Validate token, how?
        /* if(!token.isValid()){
         *   Log.info("Session expired.")
         *   throw new WebApplicationException(Response.Status.FORBIDDEN);
         * }
         * */

        byte[] file = filesystem.get(fileId);

        if(file == null){
            Log.info("File does not exist.");
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        return file;
    }
}
