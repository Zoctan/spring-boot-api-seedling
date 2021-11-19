import com.zoctan.seedling.Application;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * jasypt 用于加密配置文件 https://github.com/ulisesbocchio/jasypt-spring-boot
 *
 * @author Zoctan
 * @date 2018/05/27
 */
@SpringBootTest(classes = Application.class)
public class JasyptStringEncryptor {

  @Qualifier("myStringEncryptor")
  @Autowired
  private StringEncryptor stringEncryptor;

  @Test
  public void encode() throws Exception {
    final String mysql = this.stringEncryptor.encrypt("com.mysql.cj.jdbc.Driver");
    final String name = this.stringEncryptor.encrypt("root");
    final String password = this.stringEncryptor.encrypt("root");

    System.err.println("name = " + name);
    System.err.println("mysql = " + mysql);
    System.err.println("password = " + password);
  }
}
