package ecom.service;

import ecom.domain.Address;
import ecom.domain.Customer;
import ecom.domain.User;
import ecom.dto.CustomerDto;
import ecom.repository.CustomerRepository;
import ecom.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

@Service
@Transactional
public class CustomerService {

    private Logger logger = Logger.getLogger(CustomerService.class.getName());

    private CustomerRepository customerRepository;
    private UserRepository userRepository;
    private AddressService addressService;
    private ProductService productService;

    public CustomerService(CustomerRepository customerRepository, UserRepository userRepository, AddressService addressService, ProductService productService) {
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
        this.addressService = addressService;
        this.productService = productService;
    }

    public List<Customer> list() {
        return customerRepository.list();
    }

    public Customer get(Long id) {
        return customerRepository.get(id);
    }

    public boolean create(Customer customer){
        return customerRepository.create(customer);
    }

    public boolean create(CustomerDto customerDto) {

        User user = new User();
        user.setUsername(customerDto.getUsername());
        user.setFullName(customerDto.getFullName());
        user.setPassword(customerDto.getPassword());
        user.setEnabled(true);
        user.setCreatedOn(LocalDate.now());
        userRepository.create(user);


        Customer customer = new Customer();
        customer.setGender(customerDto.getGender());
        customer.setDateOfBirth(customerDto.getDateOfBirth());
        customer.setUser(user);
        customerRepository.create(customer);

        Address address = new Address();
        address.setAddress(customerDto.getAddress());
        address.setAddressType(customerDto.getAddressType());
        address.setCustomer(customer);
        addressService.create(address);

        productService.create(customerDto.getProduct());


        return true;
    }

    public boolean update(Customer customer) {
        return customerRepository.update(customer);
    }

    public boolean delete(Long id) {
        return customerRepository.delete(get(id));
    }
}
