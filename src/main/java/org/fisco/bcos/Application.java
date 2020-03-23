package org.fisco.bcos;

import org.fisco.bcos.bac.Bac;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class Application {
    public static void main(String[] args) {
        //    SpringApplication.run(Application.class, args);
        String method = args[0];
        Bac bac = new Bac();
        if (method.equalsIgnoreCase("init")) {
            bac.initAssetContract(args[1]);
        } else if (method.equalsIgnoreCase("getBalance")) {
            bac.getBalance(args[1], args[2]);
        } else {
            System.out.println("method not found");
        }
    }
}
