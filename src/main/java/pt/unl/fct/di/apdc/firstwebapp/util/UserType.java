package pt.unl.fct.di.apdc.firstwebapp.util;

/**
 * @author f.leao
 * 
 * define o privilégio para autorização de execução de operações na aplicação
 */
public enum UserType {

    /**
     * 
     * <h1>Superuser</h1>
     * They may:<p>
     * > Change any registered user`s <b>ROLE</b>;<p>
     * > Toggle any registered user`s {@code status};<p>
     * > Remove any user, no matter their <b>ROLE</b>;<p>
     * > List any registered users` <b>attributes</b> and their {@code status};<p>
     * > Modify GS, GBO or USER users` <b>attributes</b>, except their {@code username};
     * 
    */
    SU("SU"),
    
    /**
     * <pre>
     * 
     * <code>System Manager</code> may:
     * > Change USERs to GBOs;
     * > Toggle GBO and GA users` status;
     * > Remove USER, GBO and GA users;
     * > List USER and GBO users` <b>attributes</b> and their status, no matter their VISIBILITY or status;
     * > Modify GS, GBO or USER user`s <b>attributes</b>, except their username;
     * 
     * </pre>
    */
    GS("GS"),
    GA("GA"),
    GBO("GBO"),
    USER("USER");

    public final String type;

    private UserType(String type) {
        this.type = type;
    }

    public static UserType toType(String type) {
        for (UserType t : UserType.values())
            if (t.type.equals(type))
                return t;
        return null;
    }
}
