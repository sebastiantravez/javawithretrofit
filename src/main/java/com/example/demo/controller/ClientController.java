package com.example.demo.controller;

import com.example.demo.presentation.presenter.Client;
import com.example.demo.service.ClientServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClientController {

    @Autowired
    private ClientServiceImpl clientService;

    @GetMapping("/test")
    public Client getClient() {
        return clientService.getClient();
    }
}
