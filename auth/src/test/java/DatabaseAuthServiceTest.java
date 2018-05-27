import com.geekcloud.auth.AuthService;
import com.geekcloud.auth.DatabaseAuthService;
import com.geekcloud.auth.exceptions.AuthServiceException;
import com.geekcloud.auth.exceptions.DatabaseConnectionException;
import com.geekcloud.auth.exceptions.UserAlreadyExistsException;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;

public class DatabaseAuthServiceTest {

    private static AuthService service;

    @BeforeClass
    public static void startService () {
        service = new DatabaseAuthService();
        try {
            service.start();
        } catch (AuthServiceException e) {
            e.printStackTrace();
        }
        System.out.println(Arrays.deepToString(service.listRegisteredUsers().toArray()));
    }

    @Test
    public void getNickByLoginPass () {
        Assert.assertEquals("Rick", service.getNickByLoginPass("login1", "pass1"));
    }

    @Test (expected = UserAlreadyExistsException.class)
    public void exceptionWhenAddingAlreadyExistentUser () throws UserAlreadyExistsException, DatabaseConnectionException {
        service.registerNewUser("login1", "pass doesn't matter", "nick doesn't matter");
    }

    @Test
    public void addAndDeleteUser () {
        try {
            service.registerNewUser("login4", "pass4", "Jerry");
            Assert.assertEquals("Jerry", service.getNickByLoginPass("login4", "pass4"));
            System.out.println(Arrays.deepToString(service.listRegisteredUsers().toArray()));
            service.deleteUser("login4", "pass4");
            Assert.assertTrue(service.isUserNameVacant("login4"));
            System.out.println(Arrays.deepToString(service.listRegisteredUsers().toArray()));
        } catch (AuthServiceException e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void stopService () {
        service.stop();
    }

}
