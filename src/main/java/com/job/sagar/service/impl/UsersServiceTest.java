import com.job.sagar.model.Users;
import com.job.sagar.repository.UsersRepository;
import com.job.sagar.service.impl.UsersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsersServiceTest {

    private UsersService usersService;

    @Mock
    private UsersRepository usersRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usersService = new UsersService(usersRepository);
    }

    @Test
    void testGetAllUsers() {
        // Arrange
        List<Users> usersList = new ArrayList<>();
        usersList.add(new Users(1L, "John"));
        usersList.add(new Users(2L, "Jane"));

        when(usersRepository.findAll()).thenReturn(usersList);

        // Act
        List<UsersDataObject> result = usersService.getAllUsers();

        // Assert
        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getName());
        assertEquals("Jane", result.get(1).getName());
    }

    @Test
    void testGetUserById() {
        // Arrange
        Long userId = 1L;
        Users user = new Users(userId, "John");

        when(usersRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        Optional<Users> result = usersService.getUserById(userId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(userId, result.get().getId());
        assertEquals("John", result.get().getName());
    }

    @Test
    void testCreateUser() {
        // Arrange
        UsersDataObject userDataObject = new UsersDataObject();
        userDataObject.setName("John");

        Users users = new Users();
        BeanUtils.copyProperties(userDataObject, users);

        when(usersRepository.save(users)).thenReturn(users);

        // Act
        UsersDataObject result = usersService.createUser(userDataObject);

        // Assert
        assertEquals("John", result.getName());
    }

    @Test
    void testUpdateUser() {
        // Arrange
        Long userId = 1L;
        UsersDataObject userDataObject = new UsersDataObject();
        userDataObject.setName("Jane");

        Users existingUser = new Users(userId, "John");

        when(usersRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(usersRepository.save(existingUser)).thenReturn(existingUser);

        // Act
        Users result = usersService.updateUser(userId, userDataObject);

        // Assert
        assertEquals(userId, result.getId());
        assertEquals("Jane", result.getName());
    }

    @Test
    void testDeleteUser() {
        // Arrange
        Long userId = 1L;

        // Act
        usersService.deleteUser(userId);

        // Assert
        verify(usersRepository, times(1)).deleteById(userId);
    }
}