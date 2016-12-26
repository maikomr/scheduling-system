package org.mk.scheduling;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mk.scheduling.domain.Student;
import org.mk.scheduling.repositories.ClassRepository;
import org.mk.scheduling.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.hateoas.MediaTypes;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by maiko on 24/12/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class ApiDocumentation {

    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation)).build();
    }

    @After
    public void tearDown() {
        this.studentRepository.deleteAll();
        this.classRepository.deleteAll();
    }

    @Test
    public void index() throws Exception {
        this.mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andDo(document("index",
                        links(
                                linkWithRel("students").description("The <<resources-students,Students resource>>"),
                                linkWithRel("classes").description("The <<resources-classes,Classes resource>>"),
                                linkWithRel("profile").description("The ALPS profile for the service")),
                        responseFields(
                                fieldWithPath("_links").description("<<resources-index-links,Links>> to other resources"))));

    }

    @Test
    public void studentsList() throws Exception {
        createStudent("Robb", "Stark");
        createStudent("Jon", "Snow");
        createStudent("Bran", "Stark");

        this.mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andDo(document("students-list",
                        links(
                                linkWithRel("self").description("Canonical link for this resource"),
                                linkWithRel("profile").description("The ALPS profile for this resource"),
                                linkWithRel("search").description("Link to the search resource")),
                        responseFields(
                                fieldWithPath("_embedded.students").description("An array of <<resources-student, Student resources>>"),
                                fieldWithPath("_links").description("<<resources-classes-list-links, Links>> to other resources"),
                                fieldWithPath("page").description("Contains pagination info"),
                                fieldWithPath("page.size").description("Number of students on this page"),
                                fieldWithPath("page.size").description("Number of students on this collection"),
                                fieldWithPath("page.size").description("Number of pages on this collection"),
                                fieldWithPath("page.size").description("Page number"))));
    }

    @Test
    public void studentCreate() throws Exception {
        Map<String, String> aClass = new HashMap<String, String>();
        aClass.put("code", "physics-101");
        aClass.put("title", "Physics 101");
        aClass.put("description", "Description of Physics 101");

        String classLocation = this.mockMvc
                .perform(
                        post("/classes").contentType(MediaTypes.HAL_JSON).content(
                                this.objectMapper.writeValueAsString(aClass)))
                .andExpect(status().isCreated()).andReturn().getResponse()
                .getHeader("Location");

        Map<String, Object> student = new HashMap<String, Object>();
        student.put("firstName", "Jon");
        student.put("lastName", "Snow");
        student.put("classes", Arrays.asList(classLocation));

        this.mockMvc.perform(
                post("/students").contentType(MediaTypes.HAL_JSON).content(
                        this.objectMapper.writeValueAsString(student))).andExpect(
                status().isCreated())
                .andDo(document("student-create",
                        requestFields(
                                fieldWithPath("firstName").description("The student's first name"),
                                fieldWithPath("lastName").description("The student's last name"),
                                fieldWithPath("classes").description("An array of class resource URIs"))));
    }

    @Test
    public void studentGet() throws Exception {
        Map<String, String> aClass = new HashMap<String, String>();
        aClass.put("code", "physics-101");
        aClass.put("title", "Physics 101");
        aClass.put("description", "Description of Physics 101");

        String classLocation = this.mockMvc
                .perform(
                        post("/classes").contentType(MediaTypes.HAL_JSON).content(
                                this.objectMapper.writeValueAsString(aClass)))
                .andExpect(status().isCreated()).andReturn().getResponse()
                .getHeader("Location");

        Map<String, Object> student = new HashMap<String, Object>();
        student.put("firstName", "Jon");
        student.put("lastName", "Snow");
        student.put("classes", Arrays.asList(classLocation));

        String studentLocation = this.mockMvc
                .perform(
                        post("/students").contentType(MediaTypes.HAL_JSON).content(
                                this.objectMapper.writeValueAsString(student)))
                .andExpect(status().isCreated()).andReturn().getResponse()
                .getHeader("Location");

        this.mockMvc.perform(get(studentLocation))
                .andExpect(status().isOk())
                .andExpect(jsonPath("firstName", is(student.get("firstName"))))
                .andExpect(jsonPath("lastName", is(student.get("lastName"))))
                .andExpect(jsonPath("_links.self.href", is(studentLocation)))
                .andExpect(jsonPath("_links.classes", is(notNullValue())))
                .andDo(print())
                .andDo(document("student-get",
                        links(
                                linkWithRel("self").description("Canonical link for this <<resources-student, student>>"),
                                linkWithRel("student").description("This <<resources-student, student>>"),
                                linkWithRel("classes").description("This student's classes")),
                        responseFields(
                                fieldWithPath("id").description("The student's unique identifier"),
                                fieldWithPath("firstName").description("The student's first name"),
                                fieldWithPath("lastName").description("The student's last name"),
                                fieldWithPath("_links").description("<<resources-student-links,Links>> to other resources"),
                                fieldWithPath("createdBy").description("Who created this student"),
                                fieldWithPath("createdDate").description("The date in which this student was created"),
                                fieldWithPath("modifiedBy").description("Who modified this student"),
                                fieldWithPath("modifiedDate").description("The date in which this student was modified"))));
    }

    @Test
    public void studentUpdate() throws Exception {
        Map<String, Object> student = new HashMap<String, Object>();
        student.put("firstName", "Jon");
        student.put("lastName", "Snow");

        String studentLocation = this.mockMvc
                .perform(
                        post("/students").contentType(MediaTypes.HAL_JSON).content(
                                this.objectMapper.writeValueAsString(student)))
                .andExpect(status().isCreated()).andReturn().getResponse()
                .getHeader("Location");

        this.mockMvc.perform(get(studentLocation)).andExpect(status().isOk())
                .andExpect(jsonPath("firstName", is(student.get("firstName"))))
                .andExpect(jsonPath("lastName", is(student.get("lastName"))))
                .andExpect(jsonPath("_links.self.href", is(studentLocation)))
                .andExpect(jsonPath("_links.classes", is(notNullValue())));

        Map<String, String> aClass = new HashMap<String, String>();
        aClass.put("code", "physics-101");
        aClass.put("title", "Physics 101");
        aClass.put("description", "Description of Physics 101");

        String classLocation = this.mockMvc
                .perform(
                        post("/classes").contentType(MediaTypes.HAL_JSON).content(
                                this.objectMapper.writeValueAsString(aClass)))
                .andExpect(status().isCreated()).andReturn().getResponse()
                .getHeader("Location");

        Map<String, Object> studentUpdate = new HashMap<String, Object>();
        studentUpdate.put("classes", Arrays.asList(classLocation));

        this.mockMvc.perform(
                patch(studentLocation).contentType(MediaTypes.HAL_JSON).content(
                        this.objectMapper.writeValueAsString(studentUpdate)))
                .andExpect(status().isNoContent())
                .andDo(document("student-update"));
    }

    @Test
    public void classesList() throws Exception {
        createClass("physics-1O1", "Physics 101", "Description of Physics 101");
        createClass("calculus-1O1", "Calculus 101", "Description of Calculus 101");
        createClass("algebra-1O1", "Algebra 101", "Description of Algebra 101");

        this.mockMvc.perform(get("/classes"))
                .andExpect(status().isOk())
                .andDo(document("classes-list",
                        links(
                                linkWithRel("self").description("Canonical link for this resource"),
                                linkWithRel("profile").description("The ALPS profile for this resource"),
                                linkWithRel("search").description("Link to the search resource")),
                        responseFields(
                                fieldWithPath("_embedded.classes").description("An array of <<resources-class, Class resources>>"),
                                fieldWithPath("_links").description("<<resources-classes-list-links, Links>> to other resources"),
                                fieldWithPath("page").description("Contains pagination info"),
                                fieldWithPath("page.size").description("Number of classes on this page"),
                                fieldWithPath("page.size").description("Number of classes on this collection"),
                                fieldWithPath("page.size").description("Number of pages on this collection"),
                                fieldWithPath("page.size").description("Page number"))));
    }

    @Test
    public void classCreate() throws Exception {
        Map<String, String> aClass = new HashMap<String, String>();
        aClass.put("code", "physics-101");
        aClass.put("title", "Physics 101");
        aClass.put("description", "Description of Physics 101");

        this.mockMvc.perform(
                post("/classes").contentType(MediaTypes.HAL_JSON).content(
                        this.objectMapper.writeValueAsString(aClass)))
                .andExpect(status().isCreated())
                .andDo(document("class-create",
                        requestFields(
                                fieldWithPath("code").description("The unique code for this class"),
                                fieldWithPath("title").description("The title of the class"),
                                fieldWithPath("description").description("The description of the class"))));
    }

    @Test
    public void classGet() throws Exception {
        Map<String, String> aClass = new HashMap<String, String>();
        aClass.put("code", "physics-101");
        aClass.put("title", "Physics 101");
        aClass.put("description", "Description of Physics 101");

        String classLocation = this.mockMvc
                .perform(
                        post("/classes").contentType(MediaTypes.HAL_JSON).content(
                                this.objectMapper.writeValueAsString(aClass)))
                .andExpect(status().isCreated()).andReturn().getResponse()
                .getHeader("Location");

        this.mockMvc.perform(get(classLocation))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code", is(aClass.get("code"))))
                .andExpect(jsonPath("title", is(aClass.get("title"))))
                .andExpect(jsonPath("description", is(aClass.get("description"))))
                .andDo(document("class-get",
                        links(
                                linkWithRel("self").description("Canonical link for this <<resources-class, tag>>"),
                                linkWithRel("class").description("This <<resources-class, class>>"),
                                linkWithRel("students").description("The <<resources-tagged-classes,students>> enrolled in this class")),
                        responseFields(
                                fieldWithPath("code").description("The unique code for this class"),
                                fieldWithPath("title").description("The title of the class"),
                                fieldWithPath("description").description("The description of the class"),
                                fieldWithPath("_links").description("<<resources-tag-links,Links>> to other resources"),
                                fieldWithPath("createdBy").description("Who created this class"),
                                fieldWithPath("createdDate").description("The date in which this class was created"),
                                fieldWithPath("modifiedBy").description("Who modified this class"),
                                fieldWithPath("modifiedDate").description("The date in which this class was modified"))));
    }

    @Test
    public void classUpdate() throws Exception {
        Map<String, String> aClass = new HashMap<String, String>();
        aClass.put("code", "physics-101");
        aClass.put("title", "Physics 101");
        aClass.put("description", "Description of Physics 101");

        String classLocation = this.mockMvc
                .perform(
                        post("/classes").contentType(MediaTypes.HAL_JSON).content(
                                this.objectMapper.writeValueAsString(aClass)))
                .andExpect(status().isCreated()).andReturn().getResponse()
                .getHeader("Location");

        Map<String, Object> classUpdate = new HashMap<String, Object>();
        aClass.put("code", "algebra-101");
        aClass.put("title", "Algebra 101");
        aClass.put("description", "Description of Algebra 101");

        this.mockMvc.perform(
                patch(classLocation).contentType(MediaTypes.HAL_JSON).content(
                        this.objectMapper.writeValueAsString(classUpdate)))
                .andExpect(status().isNoContent())
                .andDo(document("class-update"));
    }

    private void createStudent(String firstName, String lastName) {
        Student student = new Student();
        student.setFirstName(firstName);
        student.setLastName(lastName);

        this.studentRepository.save(student);
    }

    private void createClass(String code, String title, String description) {
        org.mk.scheduling.domain.Class aClass = new org.mk.scheduling.domain.Class();
        aClass.setCode(code);
        aClass.setTitle(title);
        aClass.setDescription(description);
        this.classRepository.save(aClass);
    }
}