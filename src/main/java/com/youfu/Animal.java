package com.youfu;

@Component
public class Animal {
    @Value("dog")
    String type;

    public Animal() {
    }

    public Animal(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Animal{" +
                "type='" + type + '\'' +
                '}';
    }
}
