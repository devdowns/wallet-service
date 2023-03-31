package com.devdowns.walletservice.domain.configuration;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.sql.DataSource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.FileSystemResource;
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
    Path resourcePath = Paths.get("src", "main", "resources", "data.sql");
    File resourceFile = resourcePath.toFile();
    try (var connection = dataSource.getConnection()) {
      var resource = new FileSystemResource(resourceFile);
      ScriptUtils.executeSqlScript(connection, resource);
      System.out.println("Data.sql script executed successfully");
    } catch (Exception e) {
      throw new RuntimeException("Failed to execute data.sql script", e);
    }
  }
}
