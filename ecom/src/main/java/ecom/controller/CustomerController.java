package ecom.controller;

import ecom.domain.Customer;
import ecom.domain.Product;
import ecom.domain.User;
import ecom.dto.CustomerDto;
import ecom.service.CustomerService;
import ecom.service.ProductService;
import ecom.service.UserService;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.jws.WebParam;
import javax.validation.Valid;
import java.beans.PropertyEditorSupport;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/customers")
public class CustomerController {

    private CustomerService customerService;
    private ProductService productService;

    public CustomerController(CustomerService customerService, ProductService productService) {
        this.customerService = customerService;
        this.productService = productService;
    }


    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        webDataBinder.registerCustomEditor(String.class, stringTrimmerEditor);

        webDataBinder.registerCustomEditor(Product.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException
            {
                Product product = productService.get(Long.parseLong(text));
                setValue(product);
            }
        });

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        webDataBinder.registerCustomEditor(LocalDate.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException
            {
                LocalDate localDate = LocalDate.parse(text, dateFormatter);
                setValue(localDate);
            }
        });
    }

    @RequestMapping("/create-dto")
    public String createDto(Model model){
        model.addAttribute("customerDto", new CustomerDto());
        model.addAttribute("products", productService.list());
        return "customer/create-dto";
    }

    @RequestMapping("/store-dto")
    public String storeDto(@Valid @ModelAttribute("customerDto") CustomerDto customerDto, Model model, BindingResult bindingResult) throws  SQLException{
        if(bindingResult.hasErrors()){
            return "customer/create-dto";
        }

        customerService.create(customerDto);
        return "redirect:/users/list";
    }

    @RequestMapping("/list")
    public String list(Model model) {
        model.addAttribute("customers", customerService.list());
        return "customer/list";
    }

    @RequestMapping("/create")
    public String create(Model model) {
        model.addAttribute("customer", new User());
        return "customer/create";
    }



    @RequestMapping("/store")
    public String store(@Valid @ModelAttribute("customer") Customer customer, BindingResult bindingResult) throws SQLException {
        if (bindingResult.hasErrors()) {
            return "customer/create";
        }
        customerService.create(customer);
        return "redirect:/customers/list";
    }

    @RequestMapping("/edit")
    public String edit(@RequestParam("customerId") Long customerId, Model model) throws SQLException {
        model.addAttribute("customer", customerService.get(customerId));
        return "customer/edit";
    }

    @RequestMapping("/update")
    public String update(@Valid @ModelAttribute("customer") Customer customer, BindingResult bindingResult) throws SQLException {
        if (bindingResult.hasErrors()) {
            return "customer/edit";
        }
        customerService.update(customer);
        return "redirect:/customers/list";
    }

    @RequestMapping("/delete")
    public String delete(@RequestParam("customerId") Long customerId) {
        customerService.delete(customerId);
        return "redirect:/customers/list";
    }
}
