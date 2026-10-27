package com.youfu;

import lombok.Data;

@Component()
@Data
public class Person {
    @Value("xiaohua")
    String name;
    @Value("12")
    int age;
    @Autowired
    Animal animal;
}
