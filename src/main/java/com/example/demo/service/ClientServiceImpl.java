package com.example.demo.service;

import com.example.demo.api.ClientApi;
import com.example.demo.presentation.presenter.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import retrofit2.Response;

import java.io.IOException;
import java.net.SocketException;
import java.util.logging.Logger;

@Service
public class ClientServiceImpl {

    public static Logger logger = Logger.getLogger(ClientServiceImpl.class.getName());

    @Autowired
    private ClientApi clientApi;

    public Client getClient() throws IOException {
        try {
            Response<Client> clientResponse = clientApi.getClientApi().execute();
            if (!clientResponse.isSuccessful()) {
                logger.info("Error");
                throw new SocketException(clientResponse.message());
            }
            logger.info("Exito");
            return clientResponse.body();
        } catch (IOException e) {
            throw e;
        }
    }

}
