package com.youfu;

public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext container = new AnnotationConfigApplicationContext("com.youfu");

        try {
            Person person = (Person) container.getBean("person");
            System.out.println(person);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

    }
}