package ControllerHelper;

import Model.Admin;
import Model.Agent;
import Model.Customer;
import Model.UserCredentials;

public class GetUserCredentialsFactory {

    public UserCredentials getUserCredentials(String type, String email, String password) {
        if (type == null) {
            return null;
        }
        // use object Factory pattern
        if (type.equalsIgnoreCase("admin")) {
            return new Admin(email, password);
        } else if (type.equalsIgnoreCase("agent")) {
            return new Agent(email, password);
        } else if (type.equalsIgnoreCase("customer")) {
            return new Customer(email, password);
        }
        return null;
    }

}
