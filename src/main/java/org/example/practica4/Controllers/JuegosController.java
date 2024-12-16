package org.example.practica4.Controllers;

import com.mongodb.client.model.Filters;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.example.practica4.Models.Juego;

import java.net.URL;
import java.util.ResourceBundle;

public class JuegosController  {
    @FXML
    private TableView<Juego> juegos;
    @FXML
    private TableColumn<Juego, String> titulo;  // La columna para el título del juego
    @FXML
    private TableColumn<Juego, String> genero;  // La columna para el género del juego
    @FXML
    private TableColumn<Juego, Double> precio;  // La columna para el precio del juego
    @FXML
    private TableColumn<Juego, String> fecha_lanzamiento;
    @FXML
    private TextField tituloTexto;
    @FXML
    private TextField generoTexto;
    @FXML
    private TextField precioTexto;
    @FXML
    private TextField fechaLanzamiento;
    @FXML
    private Button crear;
    @FXML
    private Button editar;
    @FXML
    private Button eliminar;
    @FXML
    private Button eliminarGenero;
    @FXML
    private ComboBox<String> generosCombo;

    private Juego juegoSeleccionado;

    public void initialize() {
        crear.setOnMouseClicked(event -> crearJuego());
        editar.setOnMouseClicked(event -> editarJuego());
        eliminar.setOnMouseClicked(event -> eliminarJuego());
        eliminarGenero.setOnMouseClicked(event -> eliminarGenero());
        ObservableList<String> generos = Juego.obtenerGeneros();

        // Llenar el ComboBox con los géneros obtenidos
        generosCombo.setItems(generos);

        titulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));  // Asocia la columna con el atributo "titulo"
        genero.setCellValueFactory(new PropertyValueFactory<>("genero"));  // Asocia la columna con el atributo "genero"
        precio.setCellValueFactory(new PropertyValueFactory<>("precio"));  // Asocia la columna con el atributo "precio"
        fecha_lanzamiento.setCellValueFactory(new PropertyValueFactory<>("fechaLanzamiento"));  // Asocia la columna con el atributo "fechaLanzamiento"

        cargarJuegos();

        juegos.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                juegoSeleccionado = newValue;
                // Llenar los TextField con los datos del juego seleccionado
                tituloTexto.setText(newValue.getTitulo());
                generoTexto.setText(newValue.getGenero());
                precioTexto.setText(String.valueOf(newValue.getPrecio()));
                fechaLanzamiento.setText(newValue.getFechaLanzamiento());
            }
        });
    }

    public void cargarJuegos() {
        ObservableList<Juego> juegosData = Juego.getJuegos();  // Obtener todos los juegos desde la BD
        juegos.setItems(juegosData);  // Establecer los items de la TableView con la lista de juegos
    }

    public void crearJuego(){
        if (!tituloTexto.getText().trim().isEmpty()){
            String titulo = tituloTexto.getText();
            System.out.println(titulo);
            if (Juego.compararTitulo(titulo)){
                Double precio = 0.0;

                String genero = generoTexto.getText();
                if (!precioTexto.getText().trim().isEmpty()){
                    precio = Double.parseDouble(precioTexto.getText());
                }
                String fecha = fechaLanzamiento.getText();

                Juego.crearJuego(titulo, genero, precio, fecha);

                cargarJuegos();

                ObservableList<String> generos = Juego.obtenerGeneros();

                // Llenar el ComboBox con los géneros obtenidos
                generosCombo.setItems(generos);
            }else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Información");
                alert.setHeaderText(null);
                alert.setContentText("Ya existe un juego con este titulo");
                alert.showAndWait();
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Información");
            alert.setHeaderText(null);
            alert.setContentText("El campo 'Titulo' es obligatorio");
            alert.showAndWait();
        }

    }

    public void editarJuego(){
        if (juegoSeleccionado != null) {
            // Obtener los valores de los TextField
            String titulo = juegoSeleccionado.getTitulo().toString();
            String nuevoTitulo = tituloTexto.getText();
            String nuevoGenero = generoTexto.getText();
            Double nuevoPrecio = Double.parseDouble(precioTexto.getText());
            String nuevaFecha = fechaLanzamiento.getText();

            Juego.editarJuego(titulo, nuevoTitulo, nuevoGenero, nuevoPrecio, nuevaFecha);

            // Recargar la lista de juegos
            cargarJuegos();
            ObservableList<String> generos = Juego.obtenerGeneros();

            // Llenar el ComboBox con los géneros obtenidos
            generosCombo.setItems(generos);


            // Limpiar los campos después de editar
            tituloTexto.clear();
            generoTexto.clear();
            precioTexto.clear();
            fechaLanzamiento.clear();
        } else {
            // Si no hay un juego seleccionado, mostrar un mensaje de error
            System.out.println("Por favor, selecciona un juego para editar.");
        }
    }

    public void eliminarJuego(){
        if (juegoSeleccionado != null) {
            String titulo = juegoSeleccionado.getTitulo().toString();
            Juego.eliminarJuego(titulo);
            cargarJuegos();
        }
    }

    public void eliminarGenero(){
        if (generosCombo.getValue() != null){
            System.out.println(generosCombo.getValue().toString());
            Juego.eliminarGenero(generosCombo.getValue().toString());

            cargarJuegos();
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Información");
            alert.setHeaderText(null);
            alert.setContentText("No hay ningun genero seleccionado");
            alert.showAndWait();
        }
    }


}