package pt.unl.fct.di.apdc.firstwebapp.resources;

import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.codec.digest.DigestUtils;

import com.google.cloud.Timestamp;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.Transaction;
// import com.google.gson.Gson;

import pt.unl.fct.di.apdc.firstwebapp.util.UserData;


@Path("/register")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class RegisterResource {
    
    public RegisterResource() {}

	private static final Logger LOG = Logger.getLogger(LoginResource.class.getName());

    private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

    // private final Gson g = new Gson();


    @POST
    // @Path("/v1")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerUserV1(UserData data) {

        String username = data.getUsername();

        LOG.fine("Registering a new user: " + username);

        if (!data.isValid())
            return Response.status(Status.BAD_REQUEST).entity("Missing or wrong parameter").build();

        Key userKey = datastore.newKeyFactory().setKind("User").newKey(username);

        Entity user = Entity.newBuilder(userKey)
                .set("user_pwd", DigestUtils.sha512Hex(data.getPassword()))
                .set("user_creation_time", Timestamp.now())
                .build();

        datastore.put(user);
        LOG.info(String.format("User %s registered!", username));
        
        return Response.ok("{}").build();
    }

    @POST
    @Path("/v2")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerUserV2(UserData data) {

        String username = data.getUsername();

        LOG.fine("Registering a new user: " + username);

        if (!data.isValid())
            return Response.status(Status.BAD_REQUEST).entity("Missing or wrong parameter").build();

        Transaction txn = datastore.newTransaction();
        try{
            Key userKey = datastore.newKeyFactory().setKind("User").newKey(username);
            Entity user = txn.get(userKey);
            if(user != null) {
                txn.rollback();
                return Response.status(Status.BAD_REQUEST).entity("User already exists.").build();
            } else {
                user = Entity.newBuilder(userKey)
                        .set("user_name", username)
                        .set("user_pwd", DigestUtils.sha512Hex(data.getPassword()))
                        .set("user_email", data.getEmail())
                        .set("user_creation_time", Timestamp.now())
                        .build();
                txn.add(user);
                LOG.info(String.format("User %s registered!", username));
                txn.commit();
                return Response.ok("{}").build();
            }

        } finally {
            if (txn.isActive())
                txn.rollback();
        }
    }

}
