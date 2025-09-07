package com.example.RentCar.service;

import com.example.RentCar.entity.Customer;
import com.example.RentCar.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // 📌 Lấy tất cả khách hàng
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    // 📌 Lấy khách hàng theo ID
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("❌ Không tìm thấy khách hàng với ID = " + id));
    }

    // 📌 Thêm hoặc cập nhật khách hàng
    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    // 📌 Xóa khách hàng
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }
}
