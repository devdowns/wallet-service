package com.devdowns.walletservice.domain.configuration;

import javax.sql.DataSource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;

@Component
public class DatabaseLoader implements CommandLineRunner {

  private final DataSource dataSource;

  public DatabaseLoader(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public void run(String... args) throws Exception {
    Resource resource = new ClassPathResource("data.sql");
    try (var connection = dataSource.getConnection()) {
      ScriptUtils.executeSqlScript(connection, resource);
      System.out.println("Data.sql script executed successfully");
    } catch (Exception e) {
      throw new RuntimeException("Failed to execute data.sql script", e);
    }
  }
}
