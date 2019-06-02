package com.ra.janus.developersteam;


import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.ra.janus.developersteam")
public class DevTeamMain {

    public static void main(String[] args) {

        ApplicationContext context = new AnnotationConfigApplicationContext(DevTeamMain.class);
    }

}
