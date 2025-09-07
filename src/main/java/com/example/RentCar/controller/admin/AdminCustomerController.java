package com.example.RentCar.controller.admin;

import com.example.RentCar.entity.Customer;
import com.example.RentCar.repository.CustomerRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/customers")
public class AdminCustomerController {

    private final CustomerRepository customerRepository;

    public AdminCustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @GetMapping
    public String index(Model model) {
        List<Customer> customers = customerRepository.findAll();
        model.addAttribute("customers", customers);
        return "admin/customers/index";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("customer", new Customer());
        model.addAttribute("mode", "create");
        return "admin/customers/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("customer") Customer customer,
                       BindingResult result,
                       RedirectAttributes ra) {
        if (result.hasErrors()) {
            return "admin/customers/form";
        }
        customer.setId(null); // ensure create
        customerRepository.save(customer);
        ra.addFlashAttribute("success", "Thêm khách hàng thành công");
        return "redirect:/admin/customers";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model, RedirectAttributes ra) {
        Optional<Customer> customer = customerRepository.findById(id);
        if (customer.isEmpty()) {
            ra.addFlashAttribute("error", "Không tìm thấy khách hàng");
            return "redirect:/admin/customers";
        }
        model.addAttribute("customer", customer.get());
        model.addAttribute("mode", "edit");
        return "admin/customers/form";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable Long id,
                         @ModelAttribute("customer") Customer form,
                         BindingResult result,
                         RedirectAttributes ra) {
        if (result.hasErrors()) {
            return "admin/customers/form";
        }
        Optional<Customer> existing = customerRepository.findById(id);
        if (existing.isEmpty()) {
            ra.addFlashAttribute("error", "Không tìm thấy khách hàng");
            return "redirect:/admin/customers";
        }
        Customer customer = existing.get();
        customer.setName(form.getName());
        customer.setEmail(form.getEmail());
        customer.setPhone(form.getPhone());
        customer.setAddress(form.getAddress());

        customerRepository.save(customer);
        ra.addFlashAttribute("success", "Cập nhật khách hàng thành công");
        return "redirect:/admin/customers";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
            ra.addFlashAttribute("success", "Đã xóa khách hàng");
        } else {
            ra.addFlashAttribute("error", "Khách hàng không tồn tại");
        }
        return "redirect:/admin/customers";
    }
}