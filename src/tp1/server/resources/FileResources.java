package tp1.server.resources;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import tp1.api.FileInfo;
import tp1.api.service.rest.RestFiles;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class FileResources implements RestFiles {

    private static Logger Log = Logger.getLogger(FileResources.class.getName());

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

        File file = new File(fileId);
        try(FileOutputStream fos = new FileOutputStream(file.getPath())){
            fos.write(data);
        }
        catch (Exception e){
            e.printStackTrace();
        }

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

        //Tries to delete the file
        if(!new File(fileId).delete()){
            Log.info("Operation Failed.");
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

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

        try(FileInputStream fis = new FileInputStream(fileId)){
            byte[] data = fis.readAllBytes();

            if(data == null){
                Log.info("File not found.");
                throw new WebApplicationException(Response.Status.NOT_FOUND);
            }

            return data;
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }
}
