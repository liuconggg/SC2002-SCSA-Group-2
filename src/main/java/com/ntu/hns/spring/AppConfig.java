package com.ntu.hns.spring;

import com.ntu.hns.enums.Environment;
import java.io.Console;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import org.springframework.context.annotation.*;

@Configuration
@ComponentScan(basePackages = "com.ntu.hns")
public class AppConfig {

  @Bean
  public Scanner scanner() {
    return new Scanner(System.in);
  }

  @Bean
  public Console console() {
    return System.console();
  }

  @Bean
  public Environment environment() {
    return Environment.DEV;
  }

  @Bean
  public DateTimeFormatter dateTimeFormatter() {
    return DateTimeFormatter.ofPattern("dd/MM/yyyy");
  }
}
