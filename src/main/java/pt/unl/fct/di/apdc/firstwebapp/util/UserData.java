package pt.unl.fct.di.apdc.firstwebapp.util;

public class UserData extends AbstractChangeData {

    private String targetUsername;

    public UserData() {

    }

    
    public UserData(AuthToken token, String targetUsername) {
        super(token);
        this.targetUsername = targetUsername;
    }


    /**
     * @return the targetUsername
     */
    public String getTargetUsername() {
        return targetUsername;
    }
    
}
