import com.geekcloud.auth.AuthService;
import com.geekcloud.auth.SqliteAuthService;
import com.geekcloud.auth.exceptions.AuthServiceException;
import com.geekcloud.auth.exceptions.DatabaseConnectionException;
import com.geekcloud.auth.exceptions.UserAlreadyExistsException;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.Arrays;

public class SqliteAuthServiceTest {

    private static AuthService service;

    @BeforeClass
    public static void startService () {

        try {
            if(Files.notExists(Paths.get("../_cloud_repository")))
            Files.createDirectory(Paths.get("../_cloud_repository"));

            service = new SqliteAuthService("../_cloud_repository/users");
            service.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(Arrays.deepToString(service.listRegisteredUsers().toArray()));
    }

    @Test
    public void getNickByLoginPass () throws Exception {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        digest.update("pass1".getBytes());
        Assert.assertEquals("Rick",
                service.getNickByLoginPass("login1",
                new String (digest.digest())));
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
