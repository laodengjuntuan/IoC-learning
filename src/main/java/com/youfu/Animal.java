package com.youfu;

import lombok.Data;

@Component
@Data
public class Animal {
    @Value("dog")
    String type;
}
