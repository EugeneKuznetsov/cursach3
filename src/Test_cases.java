import com.cardealer.controller.main.Main;
import com.cardealer.model.UserNote;
import com.cardealer.model.Users;
import com.cardealer.model.enums.Role;
import com.cardealer.repository.UserNoteRepo;
import com.cardealer.repository.UserRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class EmailContTest {

    @Autowired
    private EmailCont emailCont;

    @MockBean
    private JavaMailSender mailSender;

    @Test
    public void testSendEmail() {
        String to = "test@example.com";
        String subject = "Test Subject";
        String text = "Test Text";

        emailCont.sendEmail(to, subject, text);

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(messageCaptor.capture());

        SimpleMailMessage sentMessage = messageCaptor.getValue();
        assertEquals(to, sentMessage.getTo()[0]);
        assertEquals(subject, sentMessage.getSubject());
        assertEquals(text, sentMessage.getText());
    }

    @Test
    public void testSendOrderConfirmationEmail() {
        String to = "test@example.com";
        String expectedSubject = "Подтверждение заказа";
        String expectedText = "Уважаемый клиент,\n\n" +
                "Спасибо за покупку автомобиля!\n\n" +
                "В качестве благодарности за ваш заказ, мы рады предложить вам скидку 5% на следующую покупку автомобиля.\n\n" +
                "С уважением,\n" +
                "Команда Интернет-магазина";

        emailCont.sendOrderConfirmationEmail(to);

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(messageCaptor.capture());

        SimpleMailMessage sentMessage = messageCaptor.getValue();
        assertEquals(to, sentMessage.getTo()[0]);
        assertEquals(expectedSubject, sentMessage.getSubject());
        assertEquals(expectedText, sentMessage.getText());
    }
}

public class EmailRegContTest {

    @Autowired
    private EmailRegCont emailRegCont;

    @MockBean
    private JavaMailSender mailSender;

    @MockBean
    private Logger logger;

    @Test
    public void testSendEmail() {
        String to = "test@example.com";
        String subject = "Test Subject";
        String text = "Test Text";

        emailRegCont.sendEmail(to, subject, text);

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(messageCaptor.capture());

        SimpleMailMessage sentMessage = messageCaptor.getValue();
        assertEquals(to, sentMessage.getTo()[0]);
        assertEquals(subject, sentMessage.getSubject());
        assertEquals(text, sentMessage.getText());

        verify(logger, times(1)).info("Email sent to: {}", to);
    }

    @Test
    public void testSendRegistrationConfirmationEmail() {
        String to = "test@example.com";
        String expectedSubject = "Спасибо за регистрацию!";
        String expectedText = "Уважаемый клиент,\n\n" +
                "Спасибо за регистрацию в нашем интернет-магазине автомобилей!\n\n" +
                "Мы ценим ваш выбор и готовы предложить вам дополнительный год гарантии на ваш будущий автомобиль при его заказе в течение недели.\n\n" +
                "С уважением,\n" +
                "Команда Интернет-магазина";

        emailRegCont.sendRegistrationConfirmationEmail(to);

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(messageCaptor.capture());

        SimpleMailMessage sentMessage = messageCaptor.getValue();
        assertEquals(to, sentMessage.getTo()[0]);
        assertEquals(expectedSubject, sentMessage.getSubject());
        assertEquals(expectedText, sentMessage.getText());

        verify(logger, times(1)).info("Registration confirmation email sent to: {}", to);
    }
}

public class IndexContTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testIndex1() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    public void testIndex2() throws Exception {
        mockMvc.perform(get("/index"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    public void testUndefined() throws Exception {
        mockMvc.perform(get("/undefined"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/index"));
    }

    @Test
    public void testContact() throws Exception {
        mockMvc.perform(get("/contact"))
                .andExpect(status().isOk())
                .andExpect(view().name("contact"));
    }

    @Test
    public void testAbout() throws Exception {
        mockMvc.perform(get("/about"))
                .andExpect(status().isOk())
                .andExpect(view().name("about"));
    }
}

public class LoginContTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testLogin() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }
}

public class ProductAppContTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductAppRepo productAppRepo;

    @Test
    public void testApps() throws Exception {
        mockMvc.perform(get("/apps"))
                .andExpect(status().isOk())
                .andExpect(view().name("apps"));
    }

    @Test
    public void testAppsByStatus() throws Exception {
        when(productAppRepo.findAllByStatus(ProductAppStatus.WAITING)).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/apps/WAITING"))
                .andExpect(status().isOk())
                .andExpect(view().name("apps"));
    }

    @Test
    public void testAccepted() throws Exception {
        when(productAppRepo.getReferenceById(1L)).thenReturn(new ProductApp());

        mockMvc.perform(get("/apps/1/accepted"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/apps/WAITING"));
    }

    @Test
    public void testDone() throws Exception {
        when(productAppRepo.getReferenceById(1L)).thenReturn(new ProductApp());

        mockMvc.perform(get("/apps/1/done"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/apps/ACCEPTED"));
    }
}

public class ProductContTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductRepo productRepo;

    @MockBean
    private ProductAppRepo productAppRepo;

    @Test
    public void testProducts() throws Exception {
        when(productRepo.findAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(MockMvcRequestBuilders.get("/products"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("products"));
    }

    @Test
    public void testSearch() throws Exception {
        when(productRepo.findAllByNameContaining("test")).thenReturn(new ArrayList<>());

        mockMvc.perform(MockMvcRequestBuilders.get("/products/search").param("name", "test"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("products"));
    }

    @Test
    public void testProduct() throws Exception {
        Product product = new Product();
        product.setId(1L);
        when(productRepo.getReferenceById(1L)).thenReturn(product);

        mockMvc.perform(MockMvcRequestBuilders.get("/products/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("product"));
    }

    @Test
    public void testAdd() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "filename.txt", "text/plain", "some xml".getBytes());
        MockMultipartFile[] photos = {new MockMultipartFile("photos", "photo1.jpg", "image/jpeg", "photo1".getBytes())};

        mockMvc.perform(MockMvcRequestBuilders.multipart("/products/add")
                        .file(file)
                        .file(photos[0])
                        .param("name", "Test Product")
                        .param("mileage", "100")
                        .param("weight", "2000")
                        .param("power", "300")
                        .param("warranty", "2")
                        .param("price", "50000")
                        .param("year", "2022")
                        .param("origin", "Test Origin")
                        .param("description", "Test Description")
                        .param("category", ProductCategory.SEDAN.toString())
                        .param("engine", ProductEngine.GASOLINE.toString()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/products/1"));
    }

    @Test
    public void testEdit() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "filename.txt", "text/plain", "some xml".getBytes());
        MockMultipartFile[] photos = {new MockMultipartFile("photos", "photo1.jpg", "image/jpeg", "photo1".getBytes())};

        mockMvc.perform(MockMvcRequestBuilders.multipart("/products/1/edit")
                        .file(file)
                        .file(photos[0])
                        .param("name", "Test Product")
                        .param("mileage", "100")
                        .param("weight", "2000")
                        .param("power", "300")
                        .param("warranty", "2")
                        .param("price", "50000")
                        .param("year", "2022")
                        .param("origin", "Test Origin")
                        .param("description", "Test Description")
                        .param("category", ProductCategory.SEDAN.toString())
                        .param("engine", ProductEngine.GASOLINE.toString()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/products/1"));
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/products/1/delete"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/products"));
    }
}

public class ProfileContTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepo userRepo;

    @Test
    public void testProfile() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/profile"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("profile"));
    }

    @Test
    public void testEdit() throws Exception {
        Users user = new Users();
        user.setId(1L);
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders.post("/profile/edit")
                        .param("fio", "Test User")
                        .param("age", "30")
                        .param("tel", "1234567890")
                        .param("email", "test@example.com"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/profile"));

    }

    @Test
    public void testPhoto() throws Exception {
        Users user = new Users();
        user.setId(1L);
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));

        MockMultipartFile file = new MockMultipartFile("photo", "filename.txt", "text/plain", "some xml".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/profile/photo")
                        .file(file))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/profile"));
    }

}

public class RegContTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepo userRepo;

    @Test
    public void testReg() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/reg"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("reg"));
    }

    @Test
    public void testRegUser() throws Exception {
        when(userRepo.findAll()).thenReturn(Collections.emptyList());
        when(userRepo.findByUsername("test")).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/reg")
                        .param("username", "test")
                        .param("password", "test"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/login"));
    }

    @Test
    public void testRegUserUserExists() throws Exception {
        when(userRepo.findAll()).thenReturn(Collections.singletonList(new Users("existingUser", "password", Role.USER)));
        when(userRepo.findByUsername("existingUser")).thenReturn(new Users("existingUser", "password", Role.USER));

        mockMvc.perform(MockMvcRequestBuilders.post("/reg")
                        .param("username", "existingUser")
                        .param("password", "password"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("reg"))
                .andExpect(MockMvcResultMatchers.model().attribute("message", "Пользователь с таким логином уже существует"));
    }

}

public void testStats() throws Exception {

    List<Product> products = new ArrayList<>();
    products.add(new Product("Product1", 100, 200, 300, 400, 500, 2022, "Origin1", "Description1", null, null));
    products.add(new Product("Product2", 200, 300, 400, 500, 600, 2021, "Origin2", "Description2", null, null));

    List<Users> users = new ArrayList<>();
    users.add(new Users("User1", 30, "1234567890", "user1@example.com", Role.USER));
    users.add(new Users("User2", 25, "0987654321", "user2@example.com", Role.USER));

    when(productAppRepo.findAllByStatus(ProductAppStatus.WAITING)).thenReturn(new ArrayList<>());
    when(productAppRepo.findAllByStatus(ProductAppStatus.ACCEPTED)).thenReturn(new ArrayList<>());
    when(productAppRepo.findAllByStatus(ProductAppStatus.DONE)).thenReturn(new ArrayList<>());
    when(productAppRepo.findAllByStatus(ProductAppStatus.REJECTED)).thenReturn(new ArrayList<>());
    when(productAppRepo.findAllByStatus(ProductAppStatus.CANCELED)).thenReturn(new ArrayList<>());

    when(productRepo.findAll()).thenReturn(products);

    when(userRepo.findAllByRole(Role.USER)).thenReturn(users);

    mockMvc.perform(MockMvcRequestBuilders.get("/stats"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name("stats"))
            .andExpect(MockMvcResultMatchers.model().attributeExists("statusesNames", "statusesValues",
                    "productsNames", "productsValues", "usersNames", "usersValues"));
}
}

public class UsersContTest extends Main {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepo userRepo;

    @MockBean
    private UserNoteRepo userNoteRepo;

    @Test
    public void testUsers() throws Exception {

        List<Users> users = new ArrayList<>();
        users.add(new Users("User1", 30, "1234567890", "user1@example.com", Role.USER));
        users.add(new Users("User2", 25, "0987654321", "user2@example.com", Role.USER));

        when(userRepo.findAll()).thenReturn(users);

        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("users"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("users", "roles"));
    }

    @Test
    public void testSearch() throws Exception {

        List<Users> users = new ArrayList<>();
        users.add(new Users("User1", 30, "1234567890", "user1@example.com", Role.USER));

        when(userRepo.findAllByRole(Role.USER)).thenReturn(users);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/search").param("username", "User1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("users"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("users", "roles", "username"));
    }

    @Test
    public void testUser() throws Exception {

        Users user = new Users("User1", 30, "1234567890", "user1@example.com", Role.USER);

        when(userRepo.getReferenceById(1L)).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("user"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("u"));
    }

}