package org.example.practica4.Models;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.bson.Document;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;


import java.util.ArrayList;
import java.util.List;

public class Juego {
    @BsonId
    private ObjectId id;
    private StringProperty titulo;
    private StringProperty genero;
    private DoubleProperty precio;
    private StringProperty fechaLanzamiento;

    // Constructor
    public Juego(String titulo, String genero, double precio, String fechaLanzamiento) {
        this.titulo = new SimpleStringProperty(titulo);
        this.genero = new SimpleStringProperty(genero);
        this.precio = new SimpleDoubleProperty(precio);
        this.fechaLanzamiento = new SimpleStringProperty(fechaLanzamiento);
    }

    public Juego() {

    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }
    // Getters para las propiedades
    public StringProperty tituloProperty() {
        return titulo;
    }

    public StringProperty generoProperty() {
        return genero;
    }

    public DoubleProperty precioProperty() {
        return precio;
    }

    public StringProperty fechaLanzamientoProperty() {
        return fechaLanzamiento;
    }

    // Métodos getters tradicionales (si los necesitas)
    public String getTitulo() {
        return titulo.get();
    }

    public String getGenero() {
        return genero.get();
    }

    public double getPrecio() {
        return precio.get();
    }

    public String getFechaLanzamiento() {
        return fechaLanzamiento.get();
    }

    public static ObservableList<Juego> getJuegos() {
        ObservableList<Juego> juegos = FXCollections.observableArrayList();
        try {
            // Conectar a la base de datos
            MongoDatabase db = Database.conexion();
            MongoCollection<Document> coleccion = db.getCollection("juegos");

            // Obtener todos los documentos
            try (MongoCursor<Document> cursor = coleccion.find().iterator()) {
                while (cursor.hasNext()) {
                    Document doc = cursor.next();
                    String titulo = doc.getString("titulo");
                    String genero = doc.getString("genero");
                    double precio = doc.getDouble("precio");
                    String fechaLanzamiento = doc.getString("fecha_lanzamiento");

                    // Crear y agregar el juego a la lista
                    juegos.add(new Juego(titulo, genero, precio, fechaLanzamiento));
                }
            }
        } catch (Exception e) {
            System.out.println("Error al obtener juegos: " + e.getMessage());
        }

        return juegos;
    }

    public static void crearJuego(String titulo, String genero, Double precio, String fechaLanzamiento){
        try {
            MongoDatabase db = Database.conexion();
            MongoCollection<Document> coleccion = db.getCollection("juegos");

            // Crear el documento que representa un juego
            Document juego = new Document("titulo", titulo)
                    .append("genero", genero)
                    .append("precio", precio)
                    .append("fecha_lanzamiento", fechaLanzamiento);

            // Insertar el documento en la colección
            coleccion.insertOne(juego);
        }catch(Exception e) {
            System.out.println("Error al insertar el juego: " + e.getMessage());
        }
    }

    public static void editarJuego(String tituloAntiguo, String titulo, String genero, Double precio, String fechaLanzamiento) {
        try {
            // Obtén la conexión a la base de datos
            MongoDatabase db = Database.conexion();
            MongoCollection<Document> coleccion = db.getCollection("juegos");

            // Crear el filtro para encontrar el juego por su título antiguo
            Bson filtro = Filters.eq("titulo", tituloAntiguo);

            // Realizar la actualización para cambiar los campos
            coleccion.updateOne(filtro, Updates.combine(
                    Updates.set("titulo", titulo),
                    Updates.set("genero", genero),
                    Updates.set("precio", precio),
                    Updates.set("fechaLanzamiento", fechaLanzamiento)
            ));

            System.out.println("Juego actualizado exitosamente");

        } catch (Exception e) {
            System.out.println("Error al editar el juego: " + e.getMessage());
        }
    }

    public static void eliminarJuego(String titulo){
        MongoDatabase db = Database.conexion();
        MongoCollection<Document> coleccion = db.getCollection("juegos");

        Document filtro = new Document("titulo", titulo);
        // Eliminar el documento
        coleccion.deleteOne(filtro);
        System.out.println("Juego eliminado: " + titulo);
    }

    public static void eliminarGenero(String genero){
        MongoDatabase db = Database.conexion();
        MongoCollection<Document> coleccion = db.getCollection("juegos");

        Document filtro = new Document("genero", genero);
        // Eliminar el documento
        coleccion.deleteMany(filtro);

    }

    public static boolean compararTitulo(String titulo) {
        try {
            MongoDatabase db = Database.conexion();
            MongoCollection<Document> coleccion = db.getCollection("juegos");

            Document filtro = new Document("titulo", titulo);

            long count = coleccion.countDocuments(filtro);

            return count == 0;

        } catch (Exception e) {
            System.out.println("Error al comparar el titulo: " + e.getMessage());
            return false;
        }
    }

    public static ObservableList<String> obtenerGeneros() {
        ObservableList<String> generos = FXCollections.observableArrayList();

        try {
            // Conexión a la base de datos
            MongoDatabase db = Database.conexion();
            MongoCollection<Document> coleccion = db.getCollection("juegos");

            // Realizar la consulta para obtener los géneros distintos
            FindIterable<Document> documentos = coleccion.find();

            // Recorremos los documentos y extraemos el género de cada uno
            for (Document doc : documentos) {
                String genero = doc.getString("genero");

                // Si el género no está ya en la lista, lo agregamos
                if (genero != null && !genero.trim().isEmpty() && !generos.contains(genero)) {
                    generos.add(genero);
                }
            }
        } catch (Exception e) {
            System.out.println("Error al obtener géneros: " + e.getMessage());
        }

        return generos;
    }
}
