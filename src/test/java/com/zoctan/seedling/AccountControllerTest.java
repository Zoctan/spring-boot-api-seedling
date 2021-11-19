package com.zoctan.seedling;

import com.zoctan.seedling.dto.AccountDTO;
import com.zoctan.seedling.dto.AccountLoginDTO;
import org.junit.jupiter.api.*;

import java.util.concurrent.TimeUnit;

/**
 * 账户接口测试
 *
 * @author Zoctan
 * @date 2018/11/29
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AccountControllerTest extends BaseControllerTest {

  private final String resource = "/account";

  @Test
  @Order(1)
  @Timeout(value = 5, unit = TimeUnit.SECONDS)
  public void register() throws Exception {
    final String targetUrl = this.resource;
    final AccountDTO account = new AccountDTO();
    account.setEmail("12345@qq.com");
    account.setName("xxxxx");
    account.setPassword("12345");
    this.post(targetUrl, account, null);
  }

  @Test
  @Order(2)
  @Timeout(value = 5, unit = TimeUnit.SECONDS)
  public void login() throws Exception {
    final String targetUrl = this.resource + "/token";
    final AccountLoginDTO accountLogin = new AccountLoginDTO();
    accountLogin.setName("xxxxx");
    accountLogin.setPassword("12345");
    this.post(targetUrl, accountLogin, null);
  }

  @Test
  @Order(3)
  @Timeout(value = 5, unit = TimeUnit.SECONDS)
  public void logout() throws Exception {
    final String targetUrl = this.resource + "/token";
    final AccountLoginDTO accountLogin = new AccountLoginDTO();
    accountLogin.setName("admin");
    accountLogin.setPassword("admin");
    // 先登录获取token
    final String token = (String) this.post(targetUrl, accountLogin, null).getData();
    this.delete(targetUrl, null, token);
  }

  @Test
  @Order(4)
  @Timeout(value = 5, unit = TimeUnit.SECONDS)
  @WithCustomUser(name = "user")
  public void update() throws Exception {
    final String targetUrl = this.resource;
    final AccountDTO accountDTO = new AccountDTO();
    accountDTO.setName("user");
    accountDTO.setEmail("xxxxx@qq.com");
    this.patch(targetUrl, accountDTO, null);
  }

  @Test
  @Order(5)
  @Timeout(value = 5, unit = TimeUnit.SECONDS)
  @WithCustomUser(name = "xxxxx")
  public void detail() throws Exception {
    final String targetUrl = this.resource + "/3";
    this.get(targetUrl, null, null);
  }

  @Test
  @Order(6)
  @Timeout(value = 5, unit = TimeUnit.SECONDS)
  @WithCustomUser(name = "user")
  public void list() throws Exception {
    final String targetUrl = this.resource + "?page=1&size=3";
    this.get(targetUrl, null, null);
  }

  @Test
  @Order(7)
  @Timeout(value = 5, unit = TimeUnit.SECONDS)
  @WithCustomUser(name = "admin")
  public void delete() throws Exception {
    final String targetUrl = this.resource + "/3";
    this.delete(targetUrl, null, null);
  }
}
