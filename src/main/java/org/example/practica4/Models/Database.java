package org.example.practica4.Models;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

public class Database {
    public static String host = "mongodb://localhost:27017";

    public static MongoDatabase conexion () {
        MongoDatabase database = null;
        MongoClient mongoClient = MongoClients.create(host);
        try  {

            database = mongoClient.getDatabase(("JoaquimJulian"));

        }catch (Exception error){
            System.out.println(error.getMessage());
        }
        return database;
    }
}
