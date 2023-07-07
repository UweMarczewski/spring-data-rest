package de.marczewski.springdatarest;

import de.marczewski.springdatarest.domain.Company;
import de.marczewski.springdatarest.domain.Employee;
import de.marczewski.springdatarest.repository.CompanyRepository;
import de.marczewski.springdatarest.repository.EmployeeRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
public class EmployeeControllerTests extends BaseTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    @Transactional
    public void createEmployee() throws Exception {
        Company company = Company.builder()
                .name("Test Company")
                .build();
        // store company in database and retrieve entity from database with id
        company = companyRepository.save(company);

        final String companySelfLink = "http://localhost/companies/%d".formatted(company.getId());

        final String postEmployeeJson = """
                {
                    "firstName": "Test",
                    "lastName": "Employee",
                    "company": "%s"
                }
                """.formatted(companySelfLink);

        final JSONObject postObj = new JSONObject(postEmployeeJson);

        // post new employee
        MvcResult result = mockMvc.perform(post("/employees")
                        .content(postEmployeeJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("employees/")))
                .andReturn();

        // get employee location from header
        final String location = result.getResponse().getHeader("Location");

        assertNotNull(location);

        // get employee from location
        result = mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(postObj.getString("firstName")))
                .andExpect(jsonPath("$.lastName").value(postObj.getString("lastName")))
                .andExpect(jsonPath("$._links.company.href").exists())
                .andReturn();

        assertNotNull(result.getResponse().getContentAsString());

        // get employee id from response _links.self.href
        final JSONObject getObj = new JSONObject(result.getResponse().getContentAsString());
        final String employeeSelfLink = getObj.getJSONObject("_links")
                .getJSONObject("self").get("href").toString();
        final Long employeeId = Long.parseLong(
                employeeSelfLink.split("/")[employeeSelfLink.split("/").length - 1]);

        // get employee from repository
        Optional<Employee> dbEmployee = employeeRepository.findById(employeeId);

        // assert database employee equals post employee
        assertTrue(dbEmployee.isPresent());
        assertEquals(postObj.getString("firstName"), dbEmployee.get().getFirstName());
        assertEquals(postObj.getString("lastName"), dbEmployee.get().getLastName());
        assertEquals(dbEmployee.get().getCompany(), company);

    }

}
