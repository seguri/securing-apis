package com.application;

import com.application.api.ApplicationAPI;
import com.application.config.AppConfig;
import com.application.db.repository.PostgresTicketRepository;
import com.application.db.repository.PostgresUserRepository;
import com.application.domain.UserRepository;
import com.application.domain.service.ServiceImpl;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class App {
    public static void main(String[] args) {
        AppConfig appConfig = new AppConfig();

        ServiceImpl service = buildApp(appConfig);
        new ApplicationAPI(appConfig.getAPISecrets(), service).Start();
    }

    private static ServiceImpl buildApp(AppConfig appConfig) {
        DataSource ds = new HikariDataSource(new HikariConfig(appConfig.getDBConfigPath()));

        // repositories
        UserRepository userRepository = new PostgresUserRepository(ds);
        PostgresTicketRepository ticketRepository = new PostgresTicketRepository(ds);
        return new ServiceImpl(userRepository, ticketRepository, appConfig.getPhotoUploadDirectory());
    }
}
