package pt.unl.fct.di.apdc.firstwebapp.util;

public abstract class AbstractChangeData {
    
    private AuthToken token;

    public AbstractChangeData() {

    }

    /**
     * @param token
     */
    public AbstractChangeData(AuthToken token) {
        this.token = token;
    }

    /**
     * @return the token
     */
    public AuthToken getToken() {
        return token;
    }

}
