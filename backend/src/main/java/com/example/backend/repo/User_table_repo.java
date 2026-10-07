package com.example.backend.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.backend.models.User;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Repository
public class User_table_repo {
    
    @Autowired
    private JdbcTemplate jdbc;

    private static final Logger logger =LoggerFactory.getLogger(User_table_repo.class);


    public boolean  Createuser(String name,String mail,String hashed_pass){
        String sql="insert into users (name,mail,pass) values (?,?,?)";
        try {
            int rows = jdbc.update(sql, name, mail, hashed_pass);

            return rows == 1;
        } catch (DuplicateKeyException e) {
            logger.debug("Enter already registered mail {}",mail);
            throw e;
        }
    }


    public boolean IsUserExist(String mail) {
        String sql = "SELECT EXISTS(SELECT 1 FROM users WHERE mail = ?)";

        return Boolean.TRUE.equals(jdbc.queryForObject(sql, Boolean.class, mail));
    }

    public User GetUserByMail(String mail) {
        String sql = "SELECT id, name, mail, pass FROM users WHERE mail = ?";

        try {
            User user = jdbc.queryForObject(
                sql,
                (rs, rowNum) -> new User(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("mail"),
                    rs.getString("pass")
                ),
                mail
            );

            logger.debug("User found with email: {}", mail);
            return user;

        } catch (EmptyResultDataAccessException e) {
            logger.debug("No user found with email: {}", mail);
            return null;
        }
    }

    public User GetUserById(long id) {
        String sql = "SELECT id, name, mail, pass FROM users WHERE id = ?";

        try {
            return jdbc.queryForObject(
                sql,
                (rs, rowNum) -> new User(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("mail"),
                    rs.getString("pass")
                ),
                id
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
