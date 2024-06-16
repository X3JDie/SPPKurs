package ru.kata.spring.boot_security.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.models.Department;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.*;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {
    @Autowired
    private final RoleRepository roleRepository;
    @Autowired
    private final UserService userService;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final AttachmentRepository attachmentRepository;
    @Autowired
    private final ClientRepository clientRepository;
    @Autowired
    private final DepartmentRepository departmentRepository;
    @Autowired
    private final DocumentRepository documentRepository;
    @Autowired
    private final EmployeeRepository employeeRepository;
    @Autowired
    private final OperationRepository operationRepository;
    @Autowired
    private final OrganizationRepository organizationRepository;
    @Autowired
    private final RequestRepository requestRepository;

    public DataInitializer(RoleRepository roleRepository, UserService userService, UserRepository userRepository, AttachmentRepository attachmentRepository,
                           ClientRepository clientRepository, DepartmentRepository departmentRepository, DocumentRepository documentRepository,
                           EmployeeRepository employeeRepository, OperationRepository operationRepository, OrganizationRepository organizationRepository,
                           RequestRepository requestRepository) {
        this.roleRepository = roleRepository;
        this.userService = userService;
        this.userRepository = userRepository;
        this.attachmentRepository = attachmentRepository;
        this.clientRepository = clientRepository;
        this.departmentRepository = departmentRepository;
        this.documentRepository = documentRepository;
        this.employeeRepository = employeeRepository;
        this.operationRepository = operationRepository;
        this.organizationRepository = organizationRepository;
        this.requestRepository = requestRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        initializeRoles();
        initializeUsers();
        initializeDepartments();
    }

    private void initializeRoles() {
        roleRepository.save(new Role("ADMIN"));
        roleRepository.save(new Role("USER"));
        roleRepository.save(new Role("SECRETARY"));
        roleRepository.save(new Role("SALES"));
        roleRepository.save(new Role("FINANCE"));
    }

    private void initializeUsers() {

        Set<Role> adminRoles = new HashSet<>();
        Role adminRole = roleRepository.findRoleByName("ADMIN");
        if (adminRole != null) {
            adminRoles.add(adminRole);
            User adminUser = new User("Admin", "Admin", "admin@example.com", "adminpassword");
            adminUser.setRoles(adminRoles);
            userService.save(adminUser); // Здесь вызываем метод save() из UserService
        }

        Set<Role> financeRoles = new HashSet<>();
        Role financeRole = roleRepository.findRoleByName("FINANCE");
        if (financeRole != null) {
            financeRoles.add(financeRole);
            User financeUser = new User("Finance", "Finance", "finance@example.com", "financepassword");
            financeUser.setRoles(financeRoles);
            userService.save(financeUser);
        }

        Set<Role> salesRoles = new HashSet<>();
        Role saleRole = roleRepository.findRoleByName("SALES");
        if (saleRole != null) {
            salesRoles.add(saleRole);
            User salesUser = new User("SALES", "SALES", "sales@example.com", "salespassword");
            salesUser.setRoles(salesRoles);
            userService.save(salesUser);
        }

        Set<Role> secretaryRoles = new HashSet<>();
        Role secretaryRole = roleRepository.findRoleByName("SECRETARY");
        if (secretaryRole != null) {
            secretaryRoles.add(secretaryRole);
            User secretaryUser = new User("Secretary", "Secretary", "secretary@example.com", "secretarypassword");
            secretaryUser.setRoles(secretaryRoles);
            userService.save(secretaryUser); // Здесь также вызываем метод save() из UserService
        }

    }


    private void initializeDepartments() {
        Department department1 = new Department("Marketing");
        Department department2 = new Department("Finance");
        departmentRepository.save(department1);
        departmentRepository.save(department2);
    }


}
