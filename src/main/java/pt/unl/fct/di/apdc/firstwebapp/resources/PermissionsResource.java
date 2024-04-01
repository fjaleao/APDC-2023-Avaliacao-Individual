package pt.unl.fct.di.apdc.firstwebapp.resources;

public class PermissionsResource {

    private PermissionsResource instance;
    
    public PermissionsResource getInstance(){
        if (this.instance == null)
            this.instance = new PermissionsResource();
        return this.instance;
    }

    private PermissionsResource() {

    }

}
