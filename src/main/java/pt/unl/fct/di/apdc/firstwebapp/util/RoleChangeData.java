package pt.unl.fct.di.apdc.firstwebapp.util;

public class RoleChangeData extends UserData{

    private String newRole;

    public RoleChangeData() {

    }

    public RoleChangeData(TokenData token, String targetUsername, String newRole) {
        super(token, targetUsername);
        this.newRole = newRole;
    }

    /**
     * @return the newRole
     */
    public String getNewRole() {
        return newRole;
    }
    
}
