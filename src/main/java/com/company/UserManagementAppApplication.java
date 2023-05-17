package com.company;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserManagementAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserManagementAppApplication.class, args);
    }

}

class Test {
    public Test(){

    }
    protected void testmethod() {

    }
}

class Test2 extends Test {
//    Test2(){
//        //super();
//    }

    @Override
    public final void testmethod(){

    }
}
