package de.hssfds.bikeshop;

import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Set;


public class HelloController {

    @FXML
    private Label welcomeText;

    @FXML
    private ImageView bild1;

    @FXML
    private TextField tf_name;

    @FXML
    private TextField tf_akkukap;

    @FXML
    private TextField tf_drehmoment;

    @FXML
    private TextField tf_zustand;

    @FXML
    private TextField tf_preis;

    @FXML
    private TextField tf_email;

    @FXML
    private TextField tf_password;

    @FXML
    private TextField tf_token;

    @FXML
    private TextField tf_value;

    @FXML
    private TextField tf_key;

    @FXML
    private Label statusLabel;

    @FXML
    private int currentIndex = 0;

    ArrayList<String> meineBilder = new ArrayList<>();
    ArrayList<EBike> aktuelleBikes = new ArrayList<>();
    int i;

    public void initialize() {

        i = 0;

        try {
            getJpgPaths();
        } catch (Exception ex) {

            ex.printStackTrace();
        }

        EBike SloppyJoe = new EBike("SloppyJoe", 600.0, 250.0, 50.0, 50, 101);
        EBike EasyRider = new EBike("EasyRider", 1000.0, 500.0, 70.0, 100, 102);
        EBike Brutalist = new EBike("Brutalist", 2500.0, 1000, 120.0, 75, 103);

        aktuelleBikes.add(SloppyJoe);
        aktuelleBikes.add(EasyRider);
        aktuelleBikes.add(Brutalist);

        Image imgBuffer = new Image("file:" + meineBilder.getFirst()); // ab JDK21 muss "file:" vor dem Pfad stehe
        bild1.setImage(imgBuffer);
        infoBike(aktuelleBikes.get(i));

        tf_email.setText("test@test.de");
        tf_password.setText("test123");
    }


    public void getJpgPaths() throws URISyntaxException, NullPointerException {

        URL resourceUrl = getClass().getResource("/pics");
        if (resourceUrl == null) {
            throw new IllegalArgumentException("Resource folder 'pics' not " +
                    "found on the classpath");
        }
        File folder = new File(resourceUrl.toURI());
        File[] files = folder.listFiles();
        if (files == null) {
            throw new NullPointerException("Files-Object is Null!");
        }

        for (File file : files) {
            meineBilder.add(file.getPath());
        }
    }


    @FXML
    protected void save() {

        EBike currentBike = aktuelleBikes.get(i);

        currentBike.setPreis(Double.parseDouble(tf_preis.getText()));
        currentBike.setAkkukap(Double.parseDouble(tf_akkukap.getText()));
        currentBike.setDrehmoment(Double.parseDouble(tf_drehmoment.getText()));
        currentBike.setProduktname(tf_name.getText());
        currentBike.setZustand(Integer.parseInt(tf_zustand.getText()));
    }

    @FXML
    protected void bildVor() {

        i++;
        Image imgBuffer;


        if (i < meineBilder.size() - 1) {

            imgBuffer = new Image("file:" + meineBilder.get(i));
            bild1.setImage(imgBuffer);
            infoBike(aktuelleBikes.get(i));

        } else {

            i = meineBilder.size() - 1;
            imgBuffer = new Image("file:" + meineBilder.get(i)); // ab JDK21 muss "file:" vor dem Pfad stehen
            bild1.setImage(imgBuffer);
            infoBike(aktuelleBikes.get(i));
        }
    }

    @FXML
    protected void bildZuruck() {

        i--;
        Image imgBuffer;

        if (i > 0) {

            imgBuffer = new Image("file:" + meineBilder.get(i)); // ab JDK21 muss "file:" vor dem Pfad stehen
            bild1.setImage(imgBuffer);
            infoBike(aktuelleBikes.get(i));

        } else {

            i = 0;
            imgBuffer = new Image("file:" + meineBilder.get(i)); // ab JDK21 muss "file:" vor dem Pfad stehen
            bild1.setImage(imgBuffer);
            infoBike(aktuelleBikes.get(i));
        }
    }

    @FXML
    protected void infoBike(EBike aktuelleBikes) {

        tf_name.setText(aktuelleBikes.getProduktname());
        tf_akkukap.setText(String.valueOf(aktuelleBikes.getAkkukap()));
        tf_drehmoment.setText(String.valueOf(aktuelleBikes.getDrehmoment()));
        tf_preis.setText(String.valueOf(aktuelleBikes.getPreis()));
        tf_zustand.setText(String.valueOf(aktuelleBikes.getZustand()));
    }

    @FXML
    protected void setToken() {
        String email = tf_email.getText();
        String password = tf_password.getText();
        tf_token.setText(Firebasepusher.generateToken(email, password));
    }

    @FXML
    protected void saveJSONinDB() {

        ArrayList<String> bikeListeJSON = StringArrayToJSON(aktuelleBikes);
        for (int i = 0; i < bikeListeJSON.size(); i++) {

            Firebasepusher.pushJSONtoDB("bike" + i, bikeListeJSON.get(i), tf_token.getText());
        }
    }

    private ArrayList<String> StringArrayToJSON(ArrayList<EBike> aktuelleBikes) {

        ArrayList<String> bikeListeJSON = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        for(EBike bike:aktuelleBikes){
            try {
                bikeListeJSON.add(objectMapper.writeValueAsString(bike));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return bikeListeJSON;
    }

    @FXML
    protected void inDBschreiben() {
        Firebasepusher.pushToFirebase(tf_key.getText(), tf_value.getText(), tf_token.getText());
    }

    @FXML
    protected void ausDBlesen() {
        String[] response = Firebasepusher.getFromFirebase(tf_key.getText(), tf_token.getText());
        statusLabel.setText("Status Code: " + response[0] + " Response Body: " + response[1]);
    }

    @FXML
    protected void saveInFirebase() {

        save();

        // Konvertiere die Liste der Fahrrad-Objekte in eine Liste von JSON-Strings.
        ArrayList<String> aktuelleBikesJSON = StringArrayToJSON(aktuelleBikes);

        // Iteriere über alle JSON-Strings.
        for (int i = 0; i < aktuelleBikesJSON.size(); i++) {
            // Rufe die Methode pushJSONtoDB auf, um jedes JSON-Dokument in die Firebase-Datenbank zu speichern.
            // Als Parameter werden:
            // 1. Die ID des Fahrrads als String (vermutlich als Schlüssel in der DB)
            // 2. Der JSON-String, der das Fahrrad repräsentiert
            // 3. Ein Token (aus einem Textfeld tf_token), das vermutlich für Authentifizierungszwecke dient
            Firebasepusher.pushJSONtoDB(
                    String.valueOf(aktuelleBikes.get(i).getId()),
                    aktuelleBikesJSON.get(i),
                    tf_token.getText()
            );
        }
    }

    @FXML
    protected void loadFromFirebase() {

        String[] response = Firebasepusher.getFromFirebase("", tf_token.getText());
        String jsonAntwort = response[1];
        JSONparser(jsonAntwort);
    }

    private void JSONparser(String json) {

        ArrayList<EBike> aktuelleBikesJSONparser = new ArrayList<>();
        Map<Integer, EBike> fahrradMap = JsonFahrradParser.parse(json);
        Set<Integer> allKeys = fahrradMap.keySet();
        for(Integer bikeId : allKeys) {
            aktuelleBikesJSONparser.add(fahrradMap.get(bikeId));
        }

        aktuelleBikes = aktuelleBikesJSONparser;
        infoBike(aktuelleBikes.get(i));

    }

}





