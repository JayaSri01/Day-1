package com.example.employeeapplication.integration;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static  org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.employeeapplication.entity.Employee;
import com.example.employeeapplication.repo.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@AutoConfigureMockMvc
@SpringBootTest
public class EmployeeControllerITest {

	@Autowired
	private MockMvc mockmvc;

	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	private EmployeeRepository employeerepository;
	
	@BeforeEach
	void setup() {
		employeerepository.deleteAll();
	}
	@Test
	public void employeeAddTest() throws Exception {
		
		Employee employee=Employee.builder()
				.id(1)
				.firstname("jaya")
				.lastname("sri")
				.role("analyst")
				.build();
		
	
		ResultActions response=mockmvc.perform(post("/save")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(employee)));
		
		response.andExpect(status().isOk()).andExpect(jsonPath("$.id", is(employee.getId())))
		   .andExpect(jsonPath("$.firstname", is(employee.getFirstname())))
		   .andExpect(jsonPath("$.lastname", is(employee.getLastname())))
		   .andExpect(jsonPath("$.role", is(employee.getRole())));
	}
	
	@Test
    public void getAllTest()throws Exception {
    	List<Employee> list= new ArrayList<>();
       list.add(Employee.builder().id(3).firstname("bhavani").lastname("sri").role("Sranalyst").build());
       list.add(Employee.builder().id(4).firstname("veni").lastname("sri").role("analyst").build());
       employeerepository.saveAll(list);
//       given(employeeservice.getAllEmp()).willReturn(list);
       ResultActions response=mockmvc.perform(get("/getall"));
       
       response.andExpect(status().isOk())
       .andDo(print())
       .andExpect( jsonPath("$.size()", is(list.size())));

    }
	@Test
    public void getEmpById()throws Exception  {
    	
    	 
    	 Employee employee=Employee.builder()
 				.id(1)
 				.firstname("jaya")
 				.lastname("sri")
 				.role("analyst")
 				.build();
    	 
    	employeerepository.save(employee);
    	 
    	 
    	ResultActions response=mockmvc.perform(get("/get/{id}",employee.getId()));
    	
    	response.andExpect(status().isOk())
    	.andDo(print())
    	.andExpect(MockMvcResultMatchers.jsonPath("$.id", is(employee.getId())))
	    .andExpect(MockMvcResultMatchers.jsonPath("$.firstname", is(employee.getFirstname())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.lastname", is(employee.getLastname())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.role", is(employee.getRole())));
	
    }

	@Test
    public void updateEmployeeTest()throws Exception {
    	
    	
    	Employee saveemployee=Employee.builder()
    			.id(4)
    			.firstname("jaswik")
    			.lastname("Tej")
    			.role("Sranalyst")
    			.build();
    	employeerepository.save(saveemployee);

 // Perform the update request
    	ResultActions response=mockmvc.perform(MockMvcRequestBuilders.put("/update/{id}", saveemployee.getId())
         .contentType(MediaType.APPLICATION_JSON)
         .content(mapper.writeValueAsString(saveemployee)));
    	
    	
    	response.andExpect(MockMvcResultMatchers.status().isOk());
 
    } 
	
	@Test
    public void deleteTest()throws Exception {
		 Employee employee=Employee.builder()
	 				.id(1)
	 				.firstname("jaya")
	 				.lastname("sri")
	 				.role("analyst")
	 				.build();
	    	 
	    	employeerepository.save(employee);
    	
    	ResultActions response=mockmvc.perform(delete("/delete/{id}",employee.getId()));
    	response.andExpect(status().isOk())
    	        .andDo(print());
    	
    }
}
