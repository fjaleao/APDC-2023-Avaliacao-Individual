package pt.unl.fct.di.apdc.firstwebapp.resources;

import java.util.logging.Logger;

import org.apache.commons.codec.digest.DigestUtils;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.Transaction;

import pt.unl.fct.di.apdc.firstwebapp.util.AuthToken;

public class TokenResource {
    
    public TokenResource() {}
    
    public static boolean isTokenValid(Logger LOG, AuthToken givenToken, Key tokenKey, Datastore datastore, Transaction txn) {
        
        boolean valid = false;
        Entity datastoreToken = txn.get(tokenKey);

        if (datastoreToken == null) {
            LOG.warning("No token provided.");

        } else if (givenToken.getTokenID().equals(datastoreToken.getString(AuthToken.ID))) {
            LOG.severe("Token ID not valid.");
            txn.delete(tokenKey);

        } else if (!DigestUtils.sha512Hex(givenToken.getVerification()).equals(datastoreToken.getString(AuthToken.VERIFICATION))) {
            LOG.severe("Token compromised.");
            txn.delete(tokenKey);

        } else if (givenToken.isExpired()) {
            LOG.warning("Token is expired.");
            txn.delete(tokenKey);

        } else valid = true;

        LOG.info("Token valid!");
        return valid;
        
    }

}
