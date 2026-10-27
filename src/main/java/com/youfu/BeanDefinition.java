package com.youfu;

import lombok.Data;

@Data
public class BeanDefinition {
    String name;
    Class<?> clazz;

    public BeanDefinition(String name, Class<?> clazz) {
        this.name = name;
        this.clazz = clazz;
    }
}
