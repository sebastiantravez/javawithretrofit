package com.example.demo.controller;

import com.example.demo.presentation.presenter.Client;
import com.example.demo.service.ClientServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.SocketTimeoutException;

@RestController
public class ClientController {

    @Autowired
    private ClientServiceImpl clientService;

    @GetMapping("/test")
    public Client getClient() throws IOException {
        return clientService.getClient();
    }
}
