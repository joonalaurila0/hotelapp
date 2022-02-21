package io.hotely.customer.entities;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("mariadb")
public class VaultCredentials {

  private String username;
  private String password;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    String Newline = System.getProperty("line.separator");

    result.append(this.getClass().getName() + " Object {" + Newline);
    result.append(" MariaDB Username: " + username + Newline);
    result.append(" MariaDB Password: " +  password + Newline);
    result.append("}");

    return result.toString();
  }

}