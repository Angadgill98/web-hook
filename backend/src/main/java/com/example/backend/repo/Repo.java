package com.example.backend.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;



@Repository 
public class Repo {
    
    @Autowired 
    public User_table_repo user_repo;

    @Autowired 
    public Webhook_table_repo webhook_repo;

    @Autowired
    public Webhook_events_table_repo webhook_events_repo;
    
}
