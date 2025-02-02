package app.quantun.summay;

import org.springframework.boot.SpringApplication;

public class TestSummayApplication {

    public static void main(String[] args) {
        SpringApplication.from(SummaryApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
