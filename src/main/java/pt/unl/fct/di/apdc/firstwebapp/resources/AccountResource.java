package pt.unl.fct.di.apdc.firstwebapp.resources;

import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.codec.digest.DigestUtils;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.Transaction;

import pt.unl.fct.di.apdc.firstwebapp.util.PasswordChangeData;
import pt.unl.fct.di.apdc.firstwebapp.util.RegisterData;
import pt.unl.fct.di.apdc.firstwebapp.util.TokenData;

@Path("/account")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class AccountResource {
	
	/**
	 * Logger
	 */
	private static final Logger LOG = Logger.getLogger(LoginResource.class.getName());

	private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    // private final KeyFactory userKeyFactory = datastore.newKeyFactory().setKind("User");
    // private final KeyFactory tokenKeyFactory = datastore.newKeyFactory().setKind("Token");
	
	public AccountResource() {}

	// TODO logout, showToken, changePassword

	@DELETE
	@Path("/logout/")
	public Response logOut(TokenData token) {
		Transaction txn = datastore.newTransaction();
		try {
            Key datastoreTokenKey = datastore.newKeyFactory().setKind("Token").newKey(token.getUsername());
			Entity datastoreToken = txn.get(datastoreTokenKey);
			if (datastoreToken == null) {
				txn.rollback();
				LOG.severe("Token not found.");
				return Response.status(Status.BAD_REQUEST).build();
			}
			txn.delete(datastoreTokenKey);
			LOG.info("User: " + token.getUsername() + " logged out successfuly.");
			txn.commit();
			return Response.ok("{}").build();
		} catch (Exception e) {
			txn.rollback();
			LOG.severe(e.getMessage());
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();

        } finally {
			if (txn.isActive()) {
				txn.rollback();
				return Response.status(Status.INTERNAL_SERVER_ERROR).build();

			}
			
		}
	}

	@PUT
	@Path("/token/")
	public Response showToken(TokenData token) {
		Transaction txn = datastore.newTransaction();
		try {
            Key datastoreTokenKey = datastore.newKeyFactory().setKind("Token").newKey(token.getUsername());
			Entity datastoreToken = txn.get(datastoreTokenKey);
            if (!TokenResource.isTokenValid(LOG, token, datastoreToken)) {
                txn.commit();
                return Response.status(Status.FORBIDDEN).build();
            }
			LOG.info("User: " + token.getUsername() + " retrieved token successfuly.");
			txn.commit();
			return Response.ok("{}").build();
		} catch (Exception e) {
			txn.rollback();
			LOG.severe(e.getMessage());
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();

        } finally {
			if (txn.isActive()) {
				txn.rollback();
				return Response.status(Status.INTERNAL_SERVER_ERROR).build();

			}
			
		}

	}

	@PUT
	@Path("/change_password/")
	public Response changePassword(PasswordChangeData data) {
        Transaction txn = datastore.newTransaction();
        String username = data.getToken().getUsername();
        TokenData token = data.getToken();
        try {
            Key datastoreTokenKey = datastore.newKeyFactory().setKind("Token").newKey(token.getUsername());
			Entity datastoreToken = txn.get(datastoreTokenKey);
            if (!TokenResource.isTokenValid(LOG, token, datastoreToken)) {
                txn.commit();
                return Response.status(Status.FORBIDDEN).build();
            }

			Key userKey = datastore.newKeyFactory().setKind("User").newKey(username);
            Entity user = txn.get(userKey);

            Entity updatedUser = Entity.newBuilder(userKey)
                        .set(RegisterData.USERNAME, username)
                        .set(RegisterData.NAME, user.getString(RegisterData.NAME))
                        .set(RegisterData.EMAIL, user.getString(RegisterData.EMAIL))
                        .set(RegisterData.PASSWORD, DigestUtils.sha512Hex(data.getNewPassword()))
                        .set(RegisterData.CREATION_TIME, user.getString(RegisterData.NAME))
                        .set(RegisterData.TYPE, user.getString(RegisterData.TYPE))
                        .set(RegisterData.STATE, user.getBoolean(RegisterData.STATE))
                        .set(RegisterData.VISIBILITY, user.getBoolean(RegisterData.VISIBILITY))
                        .set(RegisterData.MOBILE, user.getString(RegisterData.MOBILE))
                        .set(RegisterData.PHONE, user.getString(RegisterData.PHONE))
                        .set(RegisterData.OCCUPATION, user.getString(RegisterData.OCCUPATION))
                        .set(RegisterData.WORK_ADDRESS, user.getString(RegisterData.WORK_ADDRESS))
                        .set(RegisterData.ADDRESS, user.getString(RegisterData.ADDRESS))
                        .set(RegisterData.SECOND_ADDRESS, user.getString(RegisterData.SECOND_ADDRESS))
                        .set(RegisterData.POST_CODE, user.getString(RegisterData.POST_CODE))
                        .set(RegisterData.NIF, user.getString(RegisterData.NIF))
                        .build();

            txn.put(updatedUser);
            LOG.info("Password changed successfully!");
            txn.commit();
            return Response.ok("{}").build();

        } catch (Exception e) {
			txn.rollback();
			LOG.severe(e.getMessage());
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();

        } finally {
			if (txn.isActive()) {
				txn.rollback();
				return Response.status(Status.INTERNAL_SERVER_ERROR).build();

			}
			
		}
		
	}
    
}
