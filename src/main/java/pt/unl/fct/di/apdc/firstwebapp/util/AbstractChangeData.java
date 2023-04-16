package pt.unl.fct.di.apdc.firstwebapp.util;

public abstract class AbstractChangeData {
    
    private TokenData token;

    public AbstractChangeData() {

    }

    /**
     * @param token
     */
    public AbstractChangeData(TokenData token) {
        this.token = token;
    }

    /**
     * @return the token
     */
    public TokenData getToken() {
        return token;
    }

}
