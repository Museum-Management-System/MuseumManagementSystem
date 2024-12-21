package org.example.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GuestUserDAO {
    private Connection connection;
    public GuestUserDAO(Connection connection) {
        this.connection = connection;
    }
}
