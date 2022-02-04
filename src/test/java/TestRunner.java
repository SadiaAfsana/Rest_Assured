import org.apache.commons.configuration.ConfigurationException;
import org.testng.annotations.Test;

import java.io.IOException;

public class TestRunner {

    Customer customer;

    @Test
    public void doLogin() throws ConfigurationException, IOException {
        customer = new Customer();
        customer.callingCustomerLoginAPI();
    }

    @Test (priority = 1)
    public void getCustomerList() throws IOException {
        customer = new Customer();
        customer.callingCustomerListAPI();
    }

    @Test (priority = 2)
    public void searchCustomer() throws IOException {
        customer = new Customer();
        customer.callingSearchCustomerAPI();
    }

    @Test (priority = 3)
    public void generateCustomerInfo() throws IOException, ConfigurationException {
        customer = new Customer();
        customer.generateCustomer();
    }

    @Test (priority = 4)
    public void createRandomCustomer() throws IOException {
        customer = new Customer();
        customer.createCustomer();
    }

    @Test (priority = 5)
    public void deleteCustomer() throws IOException {
        customer = new Customer();
        customer.deleteCustomer();
    }

    @Test (priority = 6)
    public void updateCustomer() throws IOException {
        customer = new Customer();
        customer.updateCustomer();
    }

}
