package com.example.demo.presentation.presenter;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Client {
    private String name;
    private String lastName;
}
