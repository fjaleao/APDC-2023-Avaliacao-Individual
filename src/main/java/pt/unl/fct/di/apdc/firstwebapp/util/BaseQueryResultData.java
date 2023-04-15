package pt.unl.fct.di.apdc.firstwebapp.util;

public class BaseQueryResultData {

    private String username;
    private String name;
    private String email;

    public BaseQueryResultData() {
        
    }
    
    public BaseQueryResultData(String username, String name, String email) {
        this.username = username;
        this.name = name;
        this.email = email;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }
    
}
