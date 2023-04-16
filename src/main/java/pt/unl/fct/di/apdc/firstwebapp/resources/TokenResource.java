package pt.unl.fct.di.apdc.firstwebapp.resources;

import java.util.logging.Logger;

import org.apache.commons.codec.digest.DigestUtils;

import com.google.cloud.Timestamp;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;

import pt.unl.fct.di.apdc.firstwebapp.util.AuthToken;
import pt.unl.fct.di.apdc.firstwebapp.util.TokenData;

public class TokenResource {

    Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    
    // public TokenResource() {}    
    public static boolean isTokenValid(Logger LOG, TokenData givenToken, Entity datastoreToken) {
        
        boolean valid = false;
        if (!givenToken.getTokenID().equals(datastoreToken.getString(AuthToken.ID))) {
            LOG.severe("Token ID not valid.");

        } else if (!DigestUtils.sha512Hex(givenToken.getVerification()).equals(datastoreToken.getString(AuthToken.VERIFICATION))) {
            LOG.severe("Token compromised.");

        } else if (givenToken.getExpirationDate() < Timestamp.now().getSeconds()) {
            LOG.warning("Token is expired.");

        } else valid = true;

        LOG.info("Token valid!");
        return valid;
        
    }

}
