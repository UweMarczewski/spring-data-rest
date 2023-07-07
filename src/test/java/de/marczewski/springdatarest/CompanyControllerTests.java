package de.marczewski.springdatarest;

import de.marczewski.springdatarest.domain.Company;
import de.marczewski.springdatarest.repository.CompanyRepository;
import de.marczewski.springdatarest.repository.EmployeeRepository;
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
public class CompanyControllerTests extends BaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    public void returnRepositoryIndex() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.companies").exists())
                .andExpect(jsonPath("$._links.employees").exists())
                .andExpect(jsonPath("$._links.profile").exists())  // <-- Why Profile?
                .andExpect(jsonPath("$._links.users").doesNotExist());
    }

    @Test
    public void createCompany() throws Exception {

        final String postCompanyJson = """
                {
                    "name": "Test Company"
                }
                """;
        final JSONObject postCompanyObj = new JSONObject(postCompanyJson);

        // post new company
        MvcResult result = mockMvc.perform(post("/companies")
                        .content(postCompanyJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("companies/")))
                .andReturn();

        // get company location from header
        final String location = result.getResponse().getHeader("Location");

        assertNotNull(location);

        // get company from location
        result = mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(postCompanyObj.getString("name")))
                .andReturn();

        assertNotNull(result.getResponse().getContentAsString());

        // get company id from response _links.self.href
        final JSONObject getCompanyObj = new JSONObject(result.getResponse().getContentAsString());
        final String companySelfLink = getCompanyObj.getJSONObject("_links")
                .getJSONObject("self").get("href").toString();
        final Long companyId = Long.parseLong(
                companySelfLink.split("/")[companySelfLink.split("/").length - 1]);

        // get company from repository
        Optional<Company> dbCompany = companyRepository.findById(companyId);

        // assert database company equals post company
        assertTrue(dbCompany.isPresent());
        assertEquals(postCompanyObj.getString("name"), dbCompany.get().getName());
    }
}
