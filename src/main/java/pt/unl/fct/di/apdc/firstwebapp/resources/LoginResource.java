package pt.unl.fct.di.apdc.firstwebapp.resources;

import java.util.Random;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
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
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.Transaction;
import com.google.gson.Gson;

import pt.unl.fct.di.apdc.firstwebapp.util.AuthToken;
import pt.unl.fct.di.apdc.firstwebapp.util.LoginData;
import pt.unl.fct.di.apdc.firstwebapp.util.RegisterData;
@Path("/login")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class LoginResource {
	
	/**
	 * Logger
	 */
	private static final Logger LOG = Logger.getLogger(LoginResource.class.getName());
	
	private final Gson g = new Gson();

	private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
	private final KeyFactory userKeyFactory = datastore.newKeyFactory();
	
	public LoginResource() {}

	@PUT
	@Path("/")
	public Response loginUserV3(LoginData data) {
		
		LOG.fine("Attempt to login user: " + data.getUsername());

		Key userKey = userKeyFactory.newKey(data.getUsername());

		Transaction txn = datastore.newTransaction();
		try {
			Entity user = txn.get(userKey);
			if (user != null) {
				if(!user.getBoolean(RegisterData.STATE)) {
					txn.rollback();
					LOG.warning("User must be activated to log-in.");
					return Response.status(Status.BAD_REQUEST).build();
				}
				String hashedPwd = user.getString("user_pwd");
				if (hashedPwd.equals(DigestUtils.sha512Hex(data.getPassword()))) {
					String tokenVerification = Long.toString(new Random().nextLong());
					AuthToken token = new AuthToken(data.getUsername(), tokenVerification);

					Key tknKey = datastore.newKeyFactory().setKind("Token").newKey(data.getUsername());
					Entity tkn = Entity.newBuilder(tknKey)
								.set(AuthToken.USERNAME, token.getUsername())
								.set(AuthToken.ID, token.getTokenID())
								.set(AuthToken.CREATION_TIME, token.getCreationDate())
								.set(AuthToken.EXPIRATION_TIME, token.getExpirationDate())
								.set(AuthToken.VERIFICATION, DigestUtils.sha512Hex(tokenVerification))
								.build();

					txn.add(tkn);
					LOG.info("User'" + data.getUsername() + "' logged in successfully.");
					txn.commit();
					return Response.ok(g.toJson(token)).build();

				} else {
					LOG.warning("Wrong password for username: " + data.getUsername());
					return Response.status(Status.FORBIDDEN).build();

				}
			} else {
				LOG.warning("Failed login attempt for username: " + data.getUsername());
				return Response.status(Status.FORBIDDEN).build();
				
			}

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
	
	// @POST
	// @Path("/vleitao")
	// public Response loginUserV1(RegisterData data) {

	// 	String username = data.getId();
		
	// 	LOG.fine("Attempt to login user:" + data.getId());
		
	// 	if(data.getId().equals("jleitao") && data.getPassword().equals("password")) {
	// 		AuthToken at = new AuthToken(username);
	// 		return Response.ok(g.toJson(at)).build();			
	// 	}
		
	// 	return Response.status(Status.FORBIDDEN).entity("Incorrect username or password.").build();
	// }
	
	// @GET
	// @Path("/{username}")
	// public Response checkUsernameAvailable(@PathParam("username") String username) {
		
	// 	// boolean result = !(username.trim().equals("jleitao"));
	// 	// return Response.ok().entity(g.toJson(result)).build();
		
	// 	if (username.trim().equals("jleitao"))
	// 		return Response.ok().entity(g.toJson(false)).build();
	// 	else
	// 		return Response.ok().entity(g.toJson(true)).build();
		
	// }

	// @POST
	// @Path("/v1")
	// public Response loginUserV1(LoginData data) {
		
	// 	LOG.fine("Attempt to login user: " + data.getUsername());

	// 	Key userKey = userKeyFactory.newKey(data.getUsername());
	// 	Entity user = datastore.get(userKey);
	// 	if (user != null) {
	// 		String hashedPwd = user.getString("user_pwd");
	// 		if (hashedPwd.equals(DigestUtils.sha512Hex(data.getPassword()))) {
	// 			AuthToken token = new AuthToken(data.getUsername());
	// 			LOG.info("User'" + data.getUsername() + "' logged in successfully.");
	// 			return Response.ok(g.toJson(token)).build();
	// 		} else {
	// 			LOG.warning("Wrong password for username: " + data.getUsername());
	// 			return Response.status(Status.FORBIDDEN).build();
	// 		}
	// 	} else {
	// 		// Username does not exist
	// 		LOG.warning("Failed login attempt for username: " + data.getUsername());
	// 		return Response.status(Status.FORBIDDEN).build();
	// 	}

	// }

	// @POST
	// @Path("/v2")
	// public Response loginUserV2(LoginData data,
	// 		@Context HttpServletRequest request,
	// 		@Context HttpHeaders headers) {
		
	// 	LOG.fine("Attempt to login user: " + data.getUsername());

	// 	Key userKey = userKeyFactory.newKey(data.getUsername());

	// 	Key ctrsKey = datastore.newKeyFactory()
	// 			.addAncestors(PathElement.of("User", data.getUsername()))
	// 			.setKind("UserStats").newKey("counters");

	// 	Key logKey = datastore.allocateId(
	// 		datastore.newKeyFactory()
	// 		.addAncestors(PathElement.of("User", data.getUsername()))
	// 		.setKind("UserLog").newKey()
	// 	);

	// 	Transaction txn = datastore.newTransaction();
	// 	try {
	// 		Entity user = txn.get(userKey);
	// 		if (user == null) {
	// 			LOG.warning("Failed login attempt for username: " + data.getUsername());
	// 			return Response.status(Status.FORBIDDEN).build();
	// 		}

	// 		Entity stats = txn.get(ctrsKey);
	// 		if (stats == null) {
	// 			stats = Entity.newBuilder(ctrsKey)
	// 					.set("user_stats_logins", 0L)
	// 					.set("user_stats_failed", 0L)
	// 					.set("user_first_login", Timestamp.now())
	// 					.set("user_last_login", Timestamp.now())
	// 					.build();
	// 		}

	// 		String hashedPwd = (String) user.getString("user_pwd");
	// 		if (hashedPwd.equals(DigestUtils.sha512Hex(data.getPassword()))) {
	// 			// Construct the logs
	// 			Entity log = Entity.newBuilder(logKey)
	// 					.set("user_login_ip", request.getRemoteAddr())
	// 					.set("user_login_host", request.getRemoteHost())
	// 					.set("user_login_latlon",
	// 						// Does not index this property value
	// 						StringValue.newBuilder(headers.getHeaderString("X-AppEngine-CityLatLong"))
	// 							.setExcludeFromIndexes(true).build()
	// 						)
	// 					.set("user_login_city", headers.getHeaderString("X-AppEngine-City"))
	// 					.set("user_login_country", headers.getHeaderString("X-AppEngine-Country"))
	// 					.set("user_login_time", Timestamp.now())
	// 					.build();
	// 			// Get the user statistics and updates them
	// 			// Copying information everytime a user logs in is not a good solution
	// 			Entity ustats = Entity.newBuilder(ctrsKey)
	// 					.set("user_stats_logins", 1L + stats.getLong("user_stats_logins"))
	// 					.set("user_stats_failed", 0L)
	// 					.set("user_first_login", stats.getTimestamp("user_first_login"))
	// 					.set("user_last_login", Timestamp.now())
	// 					.build();

	// 			txn.put(log, ustats);
	// 			txn.commit();

	// 			AuthToken token = new AuthToken(data.getUsername());
	// 			LOG.info("User'" + data.getUsername() + "' logged in successfully.");
	// 			return Response.ok(g.toJson(token)).build();
	// 		} else {
	// 			// Copying here is even worse. Propose a better solution!
	// 			Entity ustats = Entity.newBuilder(ctrsKey)
	// 					.set("user_stats_logins", stats.getLong("user_stats_logins"))
	// 					.set("user_stats_failed", 1L + stats.getLong("user_stats_failed"))
	// 					.set("user_first_login", stats.getTimestamp("user_first_login"))
	// 					.set("user_last_login", stats.getTimestamp("user_last_login"))
	// 					.set("user_last_attempt", Timestamp.now())
	// 					.build();
	// 			txn.put(ustats);
	// 			txn.commit();
	// 			LOG.warning("Wrong password for username: " + data.getUsername());
	// 			return Response.status(Status.FORBIDDEN).build();
	// 		}
	// 	} catch (Exception e) {
	// 		txn.rollback();
	// 		LOG.severe(e.getMessage());
	// 		return Response.status(Status.INTERNAL_SERVER_ERROR).build();
	// 	} finally {
	// 		if (txn.isActive()) {
	// 			txn.rollback();
	// 			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
	// 		}
	// 	}

	// }
}