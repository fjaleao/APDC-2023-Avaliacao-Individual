package pt.unl.fct.di.apdc.firstwebapp.resources;

import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
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

import pt.unl.fct.di.apdc.firstwebapp.util.RegisterData;
import pt.unl.fct.di.apdc.firstwebapp.util.UserType;


@Path("/register")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class RegisterResource {
    
    public RegisterResource() {}

	private static final Logger LOG = Logger.getLogger(LoginResource.class.getName());

    private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

    @PUT
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Response registerUser(RegisterData data) {

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
                        .set(RegisterData.USERNAME, username)
                        .set(RegisterData.NAME, data.getName())
                        .set(RegisterData.EMAIL, data.getEmail())
                        .set(RegisterData.PASSWORD, DigestUtils.sha512Hex(data.getPassword()))
                        .set(RegisterData.CREATION_TIME, Timestamp.now())
                        .set(RegisterData.TYPE, UserType.USER.type)
                        .set(RegisterData.STATE, false)
                        .set(RegisterData.VISIBILITY, data.isVisible())
                        .set(RegisterData.MOBILE, data.getMobilePhoneNumber())
                        .set(RegisterData.PHONE, data.getPhoneNumber())
                        .set(RegisterData.OCCUPATION, data.getOccupation())
                        .set(RegisterData.WORK_ADDRESS, data.getWorkAddress())
                        .set(RegisterData.ADDRESS, data.getAddress())
                        .set(RegisterData.SECOND_ADDRESS, data.getSecondAddress())
                        .set(RegisterData.POST_CODE, data.getPostCode())
                        .set(RegisterData.NIF, data.getNif())
                        .build();
                txn.add(user);
                LOG.info(String.format("User %s registered!", username));
                txn.commit();
                return Response.ok("{}").build();
            }

        } catch (Exception e) {
			txn.rollback();
			LOG.severe(e.getMessage());
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();

        } finally {
            if (txn.isActive())
                txn.rollback();
                
        }
    }

}
