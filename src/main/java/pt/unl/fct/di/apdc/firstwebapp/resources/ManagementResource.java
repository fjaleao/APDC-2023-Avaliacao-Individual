package pt.unl.fct.di.apdc.firstwebapp.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;
import com.google.cloud.datastore.Transaction;
import com.google.gson.Gson;

import pt.unl.fct.di.apdc.firstwebapp.util.AttributeChangeData;
import pt.unl.fct.di.apdc.firstwebapp.util.AuthToken;
import pt.unl.fct.di.apdc.firstwebapp.util.CompleteQueryResultData;
import pt.unl.fct.di.apdc.firstwebapp.util.RegisterData;
import pt.unl.fct.di.apdc.firstwebapp.util.RoleChangeData;
import pt.unl.fct.di.apdc.firstwebapp.util.UserData;
import pt.unl.fct.di.apdc.firstwebapp.util.UserType;

@Path("/manage")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class ManagementResource {
	
	/**
	 * Logger
	 */
	private static final Logger LOG = Logger.getLogger(LoginResource.class.getName());
	
	private final Gson g = new Gson();

	private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
	private final KeyFactory userKeyFactory = datastore.newKeyFactory().setKind("User");
    private final KeyFactory tokenKeyFactory = datastore.newKeyFactory().setKind("Token");
	
	public ManagementResource() {}

    @PUT
    @Path("/roles/")
    public Response changeRole(RoleChangeData data) {
        Transaction txn = datastore.newTransaction();
        String targetUsername = data.getTargetUsername();
        AuthToken managerGivenToken = data.getToken();
        try {
            Key managerDatastoreTokenKey = tokenKeyFactory.newKey(managerGivenToken.getUsername());
            if (!TokenResource.isTokenValid(LOG, managerGivenToken, managerDatastoreTokenKey, datastore, txn)) {
                txn.commit();
                return Response.status(Status.FORBIDDEN).build();
            }

            Key targetKey = userKeyFactory.newKey(targetUsername);
            Entity target = txn.get(targetKey);
            if (target == null) {
                txn.rollback();
                LOG.warning("Target does not exist.");
                return Response.status(Status.BAD_REQUEST).build();
            }

            Key managerKey = userKeyFactory.newKey(managerGivenToken.getUsername());
            Entity manager = txn.get(managerKey);
            UserType managerType = UserType.toType(manager.getString(RegisterData.TYPE));
            String targetType = target.getString(RegisterData.TYPE);
            switch(managerType) {
                case SU:
                    if (managerGivenToken.getUsername().equals(targetUsername)) {
                        txn.rollback();
                        LOG.warning("SU: Forbidden role change attempted.");
                        return Response.status(Status.FORBIDDEN).build();
                    }
                    break;
                case GS:
                    if (!(targetType.equals(UserType.USER.type) || data.getNewRole().equals(UserType.GBO.type))) {
                        txn.rollback();
                        LOG.warning("GS: Forbidden role change attempted.");
                        return Response.status(Status.FORBIDDEN).build();
                    }
                    break;
                case GA:
                    txn.rollback();
                    LOG.warning("GA: Forbidden role change attempted.");
                    return Response.status(Status.FORBIDDEN).build();
                case GBO:
                    txn.rollback();
                    LOG.warning("GBO: Forbidden role change attempted.");
                    return Response.status(Status.FORBIDDEN).build();
                case USER:
                    txn.rollback();
                    LOG.warning("USER: Forbidden role change attempted.");
                    return Response.status(Status.FORBIDDEN).build();
                default:
                    throw new Exception("Could not resolve manager type.");
            }

            Entity updatedTarget = Entity.newBuilder(targetKey)
                        .set(RegisterData.USERNAME, targetUsername)
                        .set(RegisterData.NAME, target.getString(RegisterData.NAME))
                        .set(RegisterData.EMAIL, target.getString(RegisterData.EMAIL))
                        .set(RegisterData.PASSWORD, target.getString(RegisterData.PASSWORD))
                        .set(RegisterData.CREATION_TIME, target.getString(RegisterData.NAME))
                        .set(RegisterData.TYPE, data.getNewRole())
                        .set(RegisterData.STATE, target.getBoolean(RegisterData.STATE))
                        .set(RegisterData.VISIBILITY, target.getBoolean(RegisterData.VISIBILITY))
                        .set(RegisterData.MOBILE, target.getString(RegisterData.MOBILE))
                        .set(RegisterData.PHONE, target.getString(RegisterData.PHONE))
                        .set(RegisterData.OCCUPATION, target.getString(RegisterData.OCCUPATION))
                        .set(RegisterData.WORK_ADDRESS, target.getString(RegisterData.WORK_ADDRESS))
                        .set(RegisterData.ADDRESS, target.getString(RegisterData.ADDRESS))
                        .set(RegisterData.SECOND_ADDRESS, target.getString(RegisterData.SECOND_ADDRESS))
                        .set(RegisterData.POST_CODE, target.getString(RegisterData.POST_CODE))
                        .set(RegisterData.NIF, target.getString(RegisterData.NIF))
                        .build();

            txn.add(updatedTarget);
            LOG.info(String.format("User %s updated successfully!", targetUsername));
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
    @Path("/state/")
    public Response changeState(UserData data) {
        Transaction txn = datastore.newTransaction();
        String targetUsername = data.getTargetUsername();
        AuthToken managerGivenToken = data.getToken();
        try {
            Key managerDatastoreTokenKey = tokenKeyFactory.newKey(managerGivenToken.getUsername());
            if (!TokenResource.isTokenValid(LOG, managerGivenToken, managerDatastoreTokenKey, datastore, txn)) {
                txn.commit();
                return Response.status(Status.FORBIDDEN).build();
            }

            Key targetKey = userKeyFactory.newKey(targetUsername);
            Entity target = txn.get(targetKey);
            if (target == null) {
                txn.rollback();
                LOG.warning("Target does not exist.");
                return Response.status(Status.BAD_REQUEST).build();
            }

            Key managerKey = userKeyFactory.newKey(managerGivenToken.getUsername());
            Entity manager = txn.get(managerKey);
            UserType managerType = UserType.toType(manager.getString(RegisterData.TYPE));
            String targetType = target.getString(RegisterData.TYPE);
            switch(managerType) {
                case SU:
                    if (managerGivenToken.getUsername().equals(targetUsername)) {
                        txn.rollback();
                        LOG.warning("SU: Forbidden state change attempted.");
                        return Response.status(Status.FORBIDDEN).build();
                    }
                    break;
                case GS:
                    if (!(targetType.equals(UserType.GA.type) || targetType.equals(UserType.GBO.type))) {
                        txn.rollback();
                        LOG.warning("GS: Forbidden state change attempted.");
                        return Response.status(Status.FORBIDDEN).build();
                    }
                    break;
                case GA:
                if (!(targetType.equals(UserType.GBO.type) || targetType.equals(UserType.USER.type))) {
                    txn.rollback();
                    LOG.warning("GA: Forbidden state change attempted.");
                    return Response.status(Status.FORBIDDEN).build();
                }
                case GBO:
                if (!targetType.equals(UserType.USER.type)) {
                    txn.rollback();
                    LOG.warning("GBO: Forbidden state change attempted.");
                    return Response.status(Status.FORBIDDEN).build();
                }
                case USER:
                if (!managerGivenToken.getUsername().equals(targetUsername)) {
                    txn.rollback();
                    LOG.warning("USER: Forbidden state change attempted.");
                    return Response.status(Status.FORBIDDEN).build();
                }
                default:
                    throw new Exception("Could not resolve manager type.");
            }

            boolean newState;

            Entity updatedTarget = Entity.newBuilder(targetKey)
                        .set(RegisterData.USERNAME, targetUsername)
                        .set(RegisterData.NAME, target.getString(RegisterData.NAME))
                        .set(RegisterData.EMAIL, target.getString(RegisterData.EMAIL))
                        .set(RegisterData.PASSWORD, target.getString(RegisterData.PASSWORD))
                        .set(RegisterData.CREATION_TIME, target.getString(RegisterData.NAME))
                        .set(RegisterData.TYPE, target.getString(RegisterData.TYPE))
                        .set(RegisterData.STATE, newState = !target.getBoolean(RegisterData.STATE))
                        .set(RegisterData.VISIBILITY, target.getBoolean(RegisterData.VISIBILITY))
                        .set(RegisterData.MOBILE, target.getString(RegisterData.MOBILE))
                        .set(RegisterData.PHONE, target.getString(RegisterData.PHONE))
                        .set(RegisterData.OCCUPATION, target.getString(RegisterData.OCCUPATION))
                        .set(RegisterData.WORK_ADDRESS, target.getString(RegisterData.WORK_ADDRESS))
                        .set(RegisterData.ADDRESS, target.getString(RegisterData.ADDRESS))
                        .set(RegisterData.SECOND_ADDRESS, target.getString(RegisterData.SECOND_ADDRESS))
                        .set(RegisterData.POST_CODE, target.getString(RegisterData.POST_CODE))
                        .set(RegisterData.NIF, target.getString(RegisterData.NIF))
                        .build();

            txn.add(updatedTarget);
            LOG.info(String.format("User %s has been %s!", targetUsername, newState ? "ACTIVATED" : "DEACTIVATED"));
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
    @Path("/remove/")
    public Response removeUser(AttributeChangeData data) {
        Transaction txn = datastore.newTransaction();
        String targetUsername = data.getTargetUsername();
        AuthToken managerGivenToken = data.getToken();
        try {
            Key managerDatastoreTokenKey = tokenKeyFactory.newKey(managerGivenToken.getUsername());
            if (!TokenResource.isTokenValid(LOG, managerGivenToken, managerDatastoreTokenKey, datastore, txn)) {
                txn.commit();
                return Response.status(Status.FORBIDDEN).build();
            }

            Key targetKey = userKeyFactory.newKey(targetUsername);
            Entity target = txn.get(targetKey);
            if (target == null) {
                txn.rollback();
                LOG.warning("Target does not exist.");
                return Response.status(Status.BAD_REQUEST).build();
            }

            Key managerKey = userKeyFactory.newKey(managerGivenToken.getUsername());
            Entity manager = txn.get(managerKey);
            UserType managerType = UserType.toType(manager.getString(RegisterData.TYPE));
            String targetType = target.getString(RegisterData.TYPE);
            switch(managerType) {
                case SU:
                    if (managerGivenToken.getUsername().equals(targetUsername)) {
                        txn.rollback();
                        LOG.warning("SU: Forbidden remove attempted.");
                        return Response.status(Status.FORBIDDEN).build();
                    }
                    break;
                case GS:
                    if (!(targetType.equals(UserType.USER.type) || targetType.equals(UserType.GBO.type) || targetType.equals(UserType.GA.type))) {
                        txn.rollback();
                        LOG.warning("GS: Forbidden remove attempted.");
                        return Response.status(Status.FORBIDDEN).build();
                    }
                    break;
                case GA:
                    if (!(targetType.equals(UserType.USER.type) || targetType.equals(UserType.GBO.type))) {
                        txn.rollback();
                        LOG.warning("GA: Forbidden remove attempted.");
                        return Response.status(Status.FORBIDDEN).build();
                    }
                    break;
                case GBO:
                    if (!targetType.equals(UserType.USER.type)) {
                        txn.rollback();
                        LOG.warning("GBO: Forbidden remove attempted.");
                        return Response.status(Status.FORBIDDEN).build();
                    }
                    break;
                case USER:
                    if (!managerGivenToken.getUsername().equals(targetUsername)) {
                        txn.rollback();
                        LOG.warning("USER: Forbidden remove attempted.");
                        return Response.status(Status.FORBIDDEN).build();
                    }
                    break;
                default:
                    throw new Exception("Could not resolve manager type.");
            }

            txn.delete(targetKey);
            LOG.info("User " + targetUsername + " removed successfully!");
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
    @Path("/attributes/")
    public Response changeAttributes(AttributeChangeData data) {
        Transaction txn = datastore.newTransaction();
        String targetUsername = data.getTargetUsername();
        AuthToken managerGivenToken = data.getToken();
        try {
            Key managerDatastoreTokenKey = tokenKeyFactory.newKey(managerGivenToken.getUsername());
            if (!TokenResource.isTokenValid(LOG, managerGivenToken, managerDatastoreTokenKey, datastore, txn)) {
                txn.commit();
                return Response.status(Status.FORBIDDEN).build();
            }

            Key targetKey = userKeyFactory.newKey(targetUsername);
            Entity target = txn.get(targetKey);
            if (target == null) {
                txn.rollback();
                LOG.warning("Target does not exist.");
                return Response.status(Status.BAD_REQUEST).build();
            }

            Key managerKey = userKeyFactory.newKey(managerGivenToken.getUsername());
            Entity manager = txn.get(managerKey);
            UserType managerType = UserType.toType(manager.getString(RegisterData.TYPE));
            String targetType = target.getString(RegisterData.TYPE);
            switch(managerType) {
                case SU:
                    if (managerGivenToken.getUsername().equals(targetUsername)) {
                        txn.rollback();
                        LOG.warning("SU: Forbidden remove attempted.");
                        return Response.status(Status.FORBIDDEN).build();
                    }
                    break;
                case GS:
                    if (!(targetType.equals(UserType.USER.type) || targetType.equals(UserType.GBO.type) || targetType.equals(UserType.GA.type))) {
                        txn.rollback();
                        LOG.warning("GS: Forbidden remove attempted.");
                        return Response.status(Status.FORBIDDEN).build();
                    }
                    break;
                case GA:
                    if (!(targetType.equals(UserType.USER.type) || targetType.equals(UserType.GBO.type))) {
                        txn.rollback();
                        LOG.warning("GA: Forbidden remove attempted.");
                        return Response.status(Status.FORBIDDEN).build();
                    }
                    break;
                case GBO:
                    if (!targetType.equals(UserType.USER.type)) {
                        txn.rollback();
                        LOG.warning("GBO: Forbidden remove attempted.");
                        return Response.status(Status.FORBIDDEN).build();
                    }
                    break;
                case USER:
                    if (!(managerGivenToken.getUsername().equals(targetUsername) || data.getName().equals("") || data.getEmail().equals(""))) {
                        txn.rollback();
                        LOG.warning("USER: Forbidden remove attempted.");
                        return Response.status(Status.FORBIDDEN).build();
                    }
                    break;
                default:
                    throw new Exception("Could not resolve manager type.");
            }

            Entity updatedTarget = Entity.newBuilder(targetKey)
                        .set(RegisterData.USERNAME, targetUsername)
                        .set(RegisterData.NAME, target.getString(RegisterData.NAME))
                        .set(RegisterData.EMAIL, target.getString(RegisterData.EMAIL))
                        .set(RegisterData.PASSWORD, target.getString(RegisterData.PASSWORD))
                        .set(RegisterData.CREATION_TIME, target.getString(RegisterData.NAME))
                        .set(RegisterData.TYPE, target.getString(RegisterData.TYPE))
                        .set(RegisterData.STATE, target.getBoolean(RegisterData.STATE))
                        .set(RegisterData.VISIBILITY, data.isVisible())
                        .set(RegisterData.MOBILE, data.getMobilePhoneNumber().equals("") ? target.getString(RegisterData.MOBILE) : data.getMobilePhoneNumber())
                        .set(RegisterData.PHONE, data.getPhoneNumber().equals("") ? target.getString(RegisterData.PHONE) : data.getPhoneNumber())
                        .set(RegisterData.OCCUPATION, data.getOccupation().equals("") ? target.getString(RegisterData.OCCUPATION) : data.getOccupation())
                        .set(RegisterData.WORK_ADDRESS, data.getWorkAddress().equals("") ? target.getString(RegisterData.WORK_ADDRESS) : data.getWorkAddress())
                        .set(RegisterData.ADDRESS, data.getAddress().equals("") ? target.getString(RegisterData.ADDRESS) : data.getAddress())
                        .set(RegisterData.SECOND_ADDRESS, data.getSecondAddress().equals("") ? target.getString(RegisterData.SECOND_ADDRESS) : data.getSecondAddress())
                        .set(RegisterData.POST_CODE, data.getPostCode().equals("") ? target.getString(RegisterData.POST_CODE) : data.getPostCode())
                        .set(RegisterData.NIF, data.getNif().equals("") ? target.getString(RegisterData.NIF) : data.getNif())
                        .build();

            txn.add(updatedTarget);
            LOG.info(String.format("User %s updated successfully!", targetUsername));
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

    @GET
    @Path("/list_users/")
    public Response listUsers(AuthToken managerToken) {
        Transaction txn = datastore.newTransaction();
        try {
            Key managerDatastoreTokenKey = tokenKeyFactory.newKey(managerToken.getUsername());
            if (!TokenResource.isTokenValid(LOG, managerToken, managerDatastoreTokenKey, datastore, txn)) {
                txn.commit();
                return Response.status(Status.FORBIDDEN).build();
            }

            Key managerKey = userKeyFactory.newKey(managerToken.getUsername());
            Entity manager = txn.get(managerKey);
            UserType managerType = UserType.toType(manager.getString(RegisterData.TYPE));
            Query<Entity> query;
            QueryResults<Entity> users;
            List<CompleteQueryResultData> resultList = new ArrayList<>();
            switch(managerType) {
                case SU:
                    query = Query.newEntityQueryBuilder()
                        .setKind("User")
                        .build();

                    break;
                case GS:
                    query = Query.newEntityQueryBuilder()
                        .setKind("User")
                        .setFilter(
                            PropertyFilter.eq(RegisterData.TYPE, UserType.GBO.type)
                        )
                        .build();
                    users = txn.run(query);
                    users.forEachRemaining( user -> {
                        resultList.add(getCompleteQueryResultData(user));
                    });

                    query = Query.newEntityQueryBuilder()
                        .setKind("User")
                        .setFilter(
                            PropertyFilter.eq(RegisterData.TYPE, UserType.USER.type)
                        )
                        .build();
                    users = txn.run(query);
                    users.forEachRemaining( user -> {
                        resultList.add(getCompleteQueryResultData(user));
                    });

                    break;
                case GA:
                    txn.rollback();
                    LOG.warning("Behaviour for GA not defined.");
                    return Response.status(Status.NOT_IMPLEMENTED).build();
                case GBO:
                    query = Query.newEntityQueryBuilder()
                        .setKind("User")
                        .setFilter(
                            PropertyFilter.eq(RegisterData.TYPE, UserType.USER.type)
                        )
                        .build();
                    users = txn.run(query);
                    users.forEachRemaining( user -> {
                        resultList.add(getCompleteQueryResultData(user));
                    });
                case USER:
                    query = Query.newEntityQueryBuilder()
                        .setKind("User")
                        .build();
                    break;
                default:
                    throw new Exception("Could not resolve manager type.");
            }

            LOG.info("Listing successful!");
            txn.commit();
            return Response.ok(g.toJson(resultList)).build();

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

    private CompleteQueryResultData getCompleteQueryResultData(Entity user) {
        return new CompleteQueryResultData(user.getString(RegisterData.USERNAME),
                                        user.getString(RegisterData.NAME),
                                        user.getString(RegisterData.PASSWORD),
                                        user.getString(RegisterData.EMAIL),
                                        user.getTimestamp(RegisterData.CREATION_TIME),
                                        user.getString(RegisterData.TYPE),
                                        user.getBoolean(RegisterData.STATE),
                                        user.getBoolean(RegisterData.VISIBILITY),
                                        user.getString(RegisterData.MOBILE),
                                        user.getString(RegisterData.PHONE),
                                        user.getString(RegisterData.OCCUPATION),
                                        user.getString(RegisterData.WORK_ADDRESS),
                                        user.getString(RegisterData.ADDRESS),
                                        user.getString(RegisterData.SECOND_ADDRESS),
                                        user.getString(RegisterData.POST_CODE),
                                        user.getString(RegisterData.NIF));
    }
}
