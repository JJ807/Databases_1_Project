package Filmoholik;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.sql.ResultSetMetaData;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

/**
 * Klasa opakowujaca metode odpowiadjaca za przejscie do ekranu glownego
 */
class PrzejdzStart {

  /**
   * Zmienia Scene na ekran glowny aplikacji
   * @param event klikniecie przycisku
   * @throws IOException wyjatek wyrzucany przez funkcje load() z klasy FXMLLoader w przypadku niepowodzenia
   */
  public void przejdzDoStartScreen(ActionEvent event) throws IOException {
    Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(getClass().getResource("StartScreen.fxml"));
    Controller controller = new Controller();
    controller.setUser(Controller.user);
    loader.setController(controller);
    Parent root = loader.load();
    Scene scene = new Scene(root);
    window.setScene(scene);
    window.sizeToScene();
    window.show();
  }
}

/**
 * Klasa reprezentujaca zalogowanego uzytkownika
 */
class User {

  /**
   * Flaga typu boolean informujaca o zalogowaniu - prawda lub falsz
   */
  private static boolean zalogowany = false;

  /**
   * Id uzytkownika
   */
  private final String userId;

  /**
   * Konstruktor
   * @param id id uzytkownika
   * @param flaga flaga z informacja czy uzytkownik zalogowany lub nie
   */
  User(String id, boolean flaga) {
    userId = id;
    zalogowany = flaga;
  }

  /**
   * Zwraca prawde lub falsz wynikajaca z faktu zalogowania lub niezalogowania uzytkownika
   * @return prawda lub falsz
   */
  public boolean getFlag() {
    return zalogowany;
  }

  /**
   * Zwraca id zalogowanego uzytkownika typu String przypisanego do klasy User
   * @return id uzytkownika
   */
  public String getId() {
    return userId;
  }
}

/**
 * Klasa - controller, obslugujaca glowny ekran aplikacji czyli StartScreen
 */
public class Controller implements Initializable {

  /**
   * Przycisk otwierajacy Scene z formularzem FormularzOsoba do dodania nowej osoby
   */
  @FXML
  public Button btnDodajOsobe;

  /**
   * Przycisk otwierajacy Scene z formularzem FormularzFilm do dodania nowego filmu
   */
  @FXML
  public Button btnDodajFilm;

  /**
   * Przycisk otwierajacy Scene Rejestracja z formularzem do dodania nowego uzytkownika
   */
  @FXML
  public Button btn_rejestracja;

  /**
   * Przycisk otwierajacy Scene Logowanie z formularzem do zalogowania
   */
  @FXML
  public Button btn_logowanie;

  /**
   * Pole tekstowe do wpisania imienia i nazwiska osoby ktora uzytkownik chce znalezc
   */
  @FXML
  private TextField szukajWBazieOsob;

  /**
   * VBox na wszystkie zwrocone wyniki z filmami
   */
  @FXML
  private VBox vboxOuterFilm;

  /**
   * VBox na wszystkie zwrocone wyniki z osobami
   */
  @FXML
  private VBox vboxOuterOsoba;

  /**
   * Pole tekstowe do wyszukania filmu
   */
  @FXML
  private TextField szukajWBazieFilmow;


  /**
   * Etykieta informujaca o liczbe wyswietlonych rekordow filmow
   */
  @FXML
  private Label liczbaRekordowFilm;

  /**
   * Etykieta informujaca o liczbe wyswietlonych rekordow osob
   */
  @FXML
  private Label liczbaRekordowOsoba;

  /**
   * Etykieta informujaca o liczbe zwroconych rekordow filmow
   */
  @FXML
  private Label liczbaZwroconychFilmow;

  /**
   * Etykieta informujaca o liczbe zwroconych rekordow osob
   */
  @FXML
  private Label liczbaZwroconychOsob;

  /**
   * ToggleButton reprezentujacy sortowanie alfabetyczne
   */
  @FXML
  private ToggleButton rbNazwa;

  /**
   * ToggleButton reprezentujacy sortowanie po roku produkcji
   */
  @FXML
  private ToggleButton rbRok;

  /**
   * ToggleButton reprezentujacy sortowanie po czasie trwania
   */
  @FXML
  private ToggleButton rbCzas;

  /**
   * ToggleButton reprezentujacy sortowanie po dacie wydania
   */
  @FXML
  private ToggleButton rbDataWydania;


  /**
   * RadioButtons dla osob filmu
   */
  @FXML
  private ToggleButton rbNazwisko, rbZawod, rbData;

  /**
   * API KEY potrzebne do ladowania plakatow z API themoviedb
   */
  String API_KEY = "5dfdcc4d42fada6e1ea91e9ee8fcc639";

  /**
   * Zmienna statyczna User przechowujaca stan zalogowania uzytkownika i jego id
   */
  public static User user;

  /**
   * Lista na referencje do przyciskow Dodaj Ocene utworzonych dla kazdego wyniku
   */
  private final ArrayList<Control> buttons = new ArrayList<>();

  /**
   * Lista na referencje do kontrolek ComboBox Ocena utworzonych dla kazdego wyniku
   */
  private static final ArrayList<ComboBox<String>> comboBoxButtons = new ArrayList<>();

  /**
   * Ustawienie uzytkownika na kazde zaladowanie Controllera
   *
   * @param u obiekt klasy User czyli uzytkownik o danym ID
   */
  public static void setUser(User u) {
    //System.out.println("Ustawiam uzytkownika.");
    user = new User(u.getId(), u.getFlag());
  }

  /**
   * Na kazde zaladowanie Controllera
   */
  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    //System.out.println("Funkcja initialize.");
    if (user.getFlag()) {
      enableButtons();
    } else {
      //System.out.println("Uzytkownik nie zostal zalogowany.");

    }
  }

  /**
   * Aktywowanie przyciskow na zalagowanie uzytkownika
   */
  public void enableButtons() {
    //System.out.println("Aktywowalem przyciski.");
    //aktywowanie przyciskow
    btnDodajFilm.setDisable(false);
    btnDodajOsobe.setDisable(false);

    //schowanie przycisku rejestracji i zmiana przycisku logowania
    btn_rejestracja.setVisible(false);
    btn_logowanie.setText("Wyloguj");
    btn_logowanie.setOnAction(new wylogujHandler());

    for (Control x : buttons) {
      x.setDisable(false);
    }

    for (ComboBox<String> x : comboBoxButtons) {
      x.setDisable(false);
    }
  }

  /**
   * Wypelnia gridpane odpowiednimi elementami z informacjami o filmie
   * @param grid obiekt typu GridPane przetrzymuajcy w sobie inne elementy layoutu
   * @param text napis dla danego elementu Label czyli etykiety - przykladowo opis filmu
   * @return obiekty typu TextFlow zawierajacy w sobie dana etykiete z tekstem
   */
  public TextFlow wypelnijGridPane(GridPane grid, String text) {
    TextFlow tfTemp = new TextFlow();
    tfTemp.setMaxWidth(400);
    tfTemp.setMinHeight(USE_COMPUTED_SIZE);
    tfTemp.setMinWidth(USE_COMPUTED_SIZE);
    tfTemp.setPrefHeight(USE_COMPUTED_SIZE);
    tfTemp.setPrefWidth(USE_COMPUTED_SIZE);
    tfTemp.setMinHeight(USE_COMPUTED_SIZE);

    Label temp = new Label();
    temp.setText(text);
    temp.setFont(Font.font("DejaVu Sans", FontWeight.BOLD, 12));
    temp.setTextFill(Color.WHITE);
    temp.setWrapText(true);
    temp.setMaxWidth(400);
    temp.setMinHeight(USE_COMPUTED_SIZE);
    temp.setMinWidth(USE_COMPUTED_SIZE);
    temp.setPrefHeight(USE_COMPUTED_SIZE);
    temp.setPrefWidth(USE_COMPUTED_SIZE);
    temp.setMinHeight(USE_COMPUTED_SIZE);
    //System.out.println("Stworzylem label z tekstem: " + text);

    tfTemp.getChildren().add(temp);
//    temp = null;

    return tfTemp;
  }


  /**
   * Wypelnia gridpane odpowiednimi elementami z informacjami o osobie
   * @param grid obiekt typu GridPane przetrzymuajcy w sobie inne elementy layoutu
   * @param text napis dla danego elementu Label czyli etykiety
   * @return obiekty typu TextFlow zawierajacy w sobie dana etykiete z tekstem
   */
  public TextFlow wypelnijGridPaneOsoba(GridPane grid, String text) {
    TextFlow tfTemp = new TextFlow();
    tfTemp.setMaxWidth(800);
    tfTemp.setMinHeight(USE_COMPUTED_SIZE);
    tfTemp.setMinWidth(USE_COMPUTED_SIZE);
    tfTemp.setPrefHeight(USE_COMPUTED_SIZE);
    tfTemp.setPrefWidth(USE_COMPUTED_SIZE);
    tfTemp.setMinHeight(USE_COMPUTED_SIZE);

    Label temp = new Label();
    temp.setMaxWidth(800);
    temp.setTextFill(Color.WHITE);
    temp.setText(text);
    temp.setFont(Font.font("DejaVu Sans", FontWeight.BOLD, 12));
    temp.setWrapText(true);
    temp.setMinHeight(USE_COMPUTED_SIZE);
    temp.setMinWidth(USE_COMPUTED_SIZE);
    temp.setPrefHeight(USE_COMPUTED_SIZE);
    temp.setPrefWidth(USE_COMPUTED_SIZE);
    temp.setMinHeight(USE_COMPUTED_SIZE);
    //System.out.println("Stworzylem label z tekstem: " + text);

    tfTemp.getChildren().add(temp);
    return tfTemp;
  }

  /**
   * Zwraca wyniki z filmami na podstawie wpisanego tekstu w polu tekstowym
   * @throws IOException
   */
  public void zwrocFilmNowe() throws IOException {

    Connection c = null;

    //garbage collector dla szybszego zwalniania pamieci
    System.gc();

    try {
      c = DriverManager.getConnection("jdbc:postgresql://dumbo.db.elephantsql.com/npbkqkcz",
          "npbkqkcz", "f85TbMJdfkZcgzX4uYuRMiq4OCrrUlyB");
    } catch (SQLException se) {
      //System.out.println("Brak polaczenia z baza danych, wydruk logu sledzenia i koniec.");
      se.printStackTrace();
      System.exit(1);
    }
    if (c != null) {
      //System.out.println("Polaczenie z baza danych OK ! ");
      try {

        String statement;

        //wyczyszczenie elementow glownego vboxa
        vboxOuterFilm.getChildren().clear();

        //wyczyszczenie listy przyciskow
        buttons.clear();

        //pobranie tekstu z textfield
        String res = szukajWBazieFilmow.getText();

        StringBuilder wholeTitle = new StringBuilder(res);
        if (res.equals("")) {
          c.close();
          throw new IllegalArgumentException();
        }

        if (res.contains(" ")) {

          String[] slowa = res.split(" ");
          StringBuilder ciag = new StringBuilder();

          for (int i = 0; i < slowa.length; ++i) {
            StringBuilder temp = new StringBuilder(slowa[i]);
            temp.setCharAt(0, '_');
            temp.append("%");
            ciag.append(temp);
            if (i != slowa.length - 1) {
              ciag.append(" ");
            }
          }
          statement =
              "SELECT * from " + "Film_info" + " where tytul like " + "'" + ciag + "'";
        } else {
          wholeTitle.setCharAt(0, '_');
          wholeTitle.append("%");
//          wholeTitle.setCharAt(res.length()-1, '%');
          statement =
              "SELECT * from " + "Film_info" + " where tytul like " + "'" + wholeTitle + "'";
        }

        // dodanie sortowania
        String kolumna;
        if (rbNazwa.isSelected()) {
          kolumna = "tytul";
          statement += " order by " + kolumna;
        } else if (rbRok.isSelected()) {
          kolumna = "rok";
          statement += " order by " + kolumna + ", tytul";
        } else if (rbCzas.isSelected()) {
          kolumna = "czas_trwania";
          statement += " order by " + kolumna + ", tytul";
        } else if (rbDataWydania.isSelected()) {
          kolumna = "data_wydania";
          statement += " order by " + kolumna + ", tytul";
        }

        //System.out.println("Zapytanie 1 :\n" + statement + "\n");

        PreparedStatement pst = c
            .prepareStatement(statement,
                ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = pst.executeQuery();

        //liczba rekordow dla filmow
        int counterFilm = 0;

        if (!rs.next()) {
          //System.out.println("Wchodze do ifa bo nic nie znalazlem");
          Label error = new Label();
          error.setFont(Font.font("DejaVu Sans", FontWeight.BOLD, 12));
          error.setWrapText(true);
          error.setTextFill(Color.WHITE);
          error.setText("Nie znaleziono takiego filmu.");
          vboxOuterFilm.getChildren().add(error);
          error = null;

        } else {

          //petla po wszystkich wynikach
          rs.previous();
          while (rs.next()) {
            //ustawienie max liczby rekordow
            if (counterFilm == 5) {
              break;
            }
            //inkrementacja liczby rekordow dla obiektu Label
            counterFilm++;

            ResultSetMetaData rsMetaData = rs.getMetaData();
            int liczbaKolumn = rsMetaData.getColumnCount();

            String[] filmInfo = {"Reżyser: ", "Gatunek: ", "Produkcja: ", "Premiera: ",
                "Czas trwania: ", "Wytwórnia: ", "Język: ", "Obsada: "};

            //VBOX1 - vbox dla kazdego setu z elementami
            VBox vbox1 = new VBox();
            vbox1.setPrefWidth(780);
            vbox1.setPrefHeight(200);
            vbox1.setSpacing(5);
            vbox1.setPadding(new Insets(5, 5, 5, 5));

            //HBOX na tytul - dla kazdego vboxa
            HBox hboxTytul = new HBox();
            hboxTytul.setPrefWidth(200);
            hboxTytul.setPrefHeight(100);
            hboxTytul.setAlignment(Pos.CENTER_LEFT);

            //HBOX na zawartosc - plakat i info - dla kazdego vboxa
            HBox hboxZawartosc = new HBox();
            hboxZawartosc.setSpacing(5);
            hboxZawartosc.setPrefHeight(270); //de facto wysokosc obrazka i tekstu na prawo od niego

            //VBOX dla Hboxa tytul
            VBox vboxTytul = new VBox();
            vboxTytul.setMinWidth(200);

            Label labelTytul = new Label();
            labelTytul.setTextFill(Color.WHITE);
            labelTytul.setFont(Font.font("DejaVu Sans", FontWeight.BOLD, 18));
            labelTytul.setWrapText(true);

            HBox hboxPodtytul = new HBox();
            hboxPodtytul.setAlignment(Pos.CENTER_LEFT);
            hboxPodtytul.setSpacing(5);

            //Gridpane
            GridPane gridPaneZawartosc = new GridPane();
            gridPaneZawartosc.setAlignment(Pos.CENTER_LEFT);
            gridPaneZawartosc.setPrefHeight(270);
            gridPaneZawartosc.setPrefWidth(400);
            gridPaneZawartosc.setMaxHeight(USE_COMPUTED_SIZE);
            gridPaneZawartosc.setMinHeight(USE_COMPUTED_SIZE);
            gridPaneZawartosc.setMaxWidth(USE_COMPUTED_SIZE);
            gridPaneZawartosc.setMinWidth(USE_COMPUTED_SIZE);

            RowConstraints firstRow = new RowConstraints();
            firstRow.setFillHeight(false);
            firstRow.setMinHeight(25);
            firstRow.setPercentHeight(-1);
            firstRow.setValignment(VPos.TOP);
            firstRow.setMaxHeight(USE_COMPUTED_SIZE);
            firstRow.setPrefHeight(USE_COMPUTED_SIZE);
            firstRow.setVgrow(Priority.SOMETIMES);

            RowConstraints row = new RowConstraints();
            row.setMinHeight(25);
            row.setPercentHeight(-1);
            row.setValignment(VPos.TOP);
            row.setMaxHeight(USE_COMPUTED_SIZE);
            row.setPrefHeight(USE_COMPUTED_SIZE);
            row.setVgrow(Priority.SOMETIMES);
            row.setFillHeight(false);

            ColumnConstraints col = new ColumnConstraints();
            col.setHgrow(Priority.NEVER);
            col.setFillWidth(false);
            col.setPercentWidth(-1);
            col.setHalignment(HPos.LEFT);
            col.setMaxWidth(USE_COMPUTED_SIZE);
            col.setMinWidth(USE_COMPUTED_SIZE);
            col.setPrefWidth(USE_COMPUTED_SIZE);

            gridPaneZawartosc.getRowConstraints()
                .addAll(firstRow, row, row, row, row, row, row, row, row);
            gridPaneZawartosc.getColumnConstraints().add(col);

            //zapisanie pierwszego wiersza danego filmu
            for (int i = 1; i <= liczbaKolumn - 2; i++) {
              //System.out.println("Nr iteracji: " + i);
              String dane_kolumny = rs.getString(rsMetaData.getColumnName(i));
              if (dane_kolumny == null) {
                dane_kolumny = "Brak";
              }
              if (i < 6) {
                switch (i) {
                  case 1: { //id_film
                    //ImageView
                    ImageView imageView = null;
                    //System.out.println("ID filmu: " + dane_kolumny);
                    try {
                      String przyklad =
                          "https://api.themoviedb.org/3/find/" + dane_kolumny + "?api_key="
                              + API_KEY + "&external_source=imdb_id";
                      URL url = new URL(przyklad);
                      HttpURLConnection con = (HttpURLConnection) url.openConnection();

                      StringBuffer response = null;
                      String USER_AGENT = "Mozilla/5.0";
                      con.setDoOutput(true);
                      con.setRequestMethod("GET");
                      con.setRequestProperty("User-Agent", USER_AGENT);
                      int responseCode = con.getResponseCode();
                      //System.out.println("GET Response Code :: " + responseCode);
                      if (responseCode == HttpURLConnection.HTTP_OK) { // success
                        BufferedReader in = new BufferedReader(new InputStreamReader(
                            con.getInputStream()));
                        String inputLine;
                        response = new StringBuffer();

                        while ((inputLine = in.readLine()) != null) {
                          response.append(inputLine);
                        }
                        in.close();
                        //System.out.println(response.toString());
                      } else {
                        //System.out.println("GET request not worked");
                      }

                      assert response != null;
                      JsonReader rdr = Json.createReader(new StringReader(response.toString()));
                      JsonObject obj = rdr.readObject();

                      JsonArray arr = obj
                          .getJsonArray("movie_results"); //tablica z jednym slownikiem
                      JsonObject last = arr.getJsonObject(0);
                      String poster_path = last.getString("poster_path");
                      //System.out.println("Sciezka do plakatu: " + poster_path);

                      String imgLink = "http://image.tmdb.org/t/p/original" + poster_path;
                      //System.out.println("Link z obrazkiem: " + imgLink);

                      imageView = new ImageView(imgLink);
                    } catch (Exception e) {
                      imageView = new ImageView(
                          getClass().getResource("resources/brakPlakatu.png").toExternalForm());
                    }

                    imageView.setFitHeight(270);
                    imageView.setFitWidth(200);
                    imageView.setPickOnBounds(true);
                    hboxZawartosc.getChildren().add(imageView); //dodanie ImageView do hbox'a
                    break;
                  }
                  case 2: { //tytul
                    labelTytul.setText(dane_kolumny);
                    TextFlow tfTytul = new TextFlow();
                    tfTytul.setPrefHeight(200);
                    tfTytul.setPrefWidth(200);
                    tfTytul.getChildren().add(labelTytul);
                    vboxTytul.getChildren().add(tfTytul);
                    break;
                  }
                  case 3: { //rok
                    Label tytulRok = new Label();
                    tytulRok.setTextFill(Color.WHITE);
                    tytulRok.setText(dane_kolumny);
                    tytulRok.setPadding(new Insets(0, 86, 0, 0));
                    tytulRok.setFont(Font.font("DejaVu Sans", 15));
                    tytulRok.setAlignment(Pos.CENTER_LEFT);
                    hboxPodtytul.getChildren().add(tytulRok);

                    ImageView gwiazda = new ImageView(
                        getClass().getResource("resources/gwiazda.png").toExternalForm());
                    gwiazda.setFitHeight(20);
                    gwiazda.setFitWidth(20);
                    hboxPodtytul.getChildren().add(gwiazda); //dodanie gwiazdy do hbox'a na podtytul
                    break;
                  }
                  case 4: { //ocena(srednia z ocen)
                    Label podtytulOcena = new Label();
                    podtytulOcena.setTextFill(Color.WHITE);
                    podtytulOcena.setText(dane_kolumny);
                    podtytulOcena.setFont(Font.font("DejaVu Sans", FontWeight.BOLD, 18));
                    hboxPodtytul.getChildren().add(podtytulOcena);
                    break;
                  }
                  case 5: { //liczba ocen
                    Label podtytulLiczbaOcen = new Label();
                    podtytulLiczbaOcen.setTextFill(Color.WHITE);
                    podtytulLiczbaOcen.setText("(" + dane_kolumny + " głosów)");
                    podtytulLiczbaOcen.setFont(Font.font("DejaVu Sans", 15));
                    hboxPodtytul.getChildren().add(podtytulLiczbaOcen);

                    vboxTytul.getChildren().add(hboxPodtytul);

                    hboxTytul.getChildren().add(vboxTytul);

                    vbox1.getChildren().add(hboxTytul);
                    break;
                  }
                  default:
                    //System.out.println("Nie ma takiego przypadku w konstrukcji switch.");
                    break;
                }
              } else {
                TextFlow tfTemp;
                if (i == 6) { //opis
                  tfTemp = wypelnijGridPane(gridPaneZawartosc, dane_kolumny);
                  GridPane.setConstraints(tfTemp, 0, 0);
                } else {
                  tfTemp = wypelnijGridPane(gridPaneZawartosc, filmInfo[i - 6] + dane_kolumny);
                  GridPane.setConstraints(tfTemp, 0, i - 5);
                }

                GridPane.setValignment(tfTemp, VPos.CENTER);
                GridPane.setHalignment(tfTemp, HPos.LEFT);

                gridPaneZawartosc.getChildren().add(tfTemp);
              }
            }

            //pobieramy info o tytule z drugiego zwrocengo wiersza
            String tytul = rs.getString(rsMetaData.getColumnName(2));
            String idFilm = rs.getString(rsMetaData.getColumnName(1));
            //przejscie z powrotem na poczatek zwroconych danych
            rs.previous();

            //ta petla sluzy do przechodzeniu po osobach czyli po wierszach dla ktorych wartosc tytul jest taka sama
            while (rs.next()) {

              //ustawiamy pomocnicze zmienne
              String tytulFilmu2 = rs.getString(rsMetaData.getColumnName(2));
              String osoba = rs.getString(rsMetaData.getColumnName(13));
              //pobieramy info o roli danej osoby z czternastego zwroconego wiersza
              String rola = rs.getString(rsMetaData.getColumnName(14));

              //sprawdzamy czy tytuly dla obu wierszy sa takie same
              try {
                if (tytul.equals(tytulFilmu2)) {
                  if (rola.equals("1") || rola.equals("2")) {
                    filmInfo[7] += "[" + osoba + "]" + " "; //obsada
                  } else if (rola.equals("3")) {
                    filmInfo[0] += osoba + "\t"; //rezyser
                  }
                } else {
                  rs.previous();
                  break;
                }
              } catch (NullPointerException e) {
                filmInfo[0] += "Brak informacji.";
                filmInfo[7] += "Brak informacji.";
              }
            }
            //dodaj rezyserow
            TextFlow rezyser = wypelnijGridPane(gridPaneZawartosc, filmInfo[0]);
            GridPane.setConstraints(rezyser, 0, 1);
            GridPane.setValignment(rezyser, VPos.CENTER);
            GridPane.setHalignment(rezyser, HPos.LEFT);

            //dodaj obsade
            TextFlow obsada = wypelnijGridPane(gridPaneZawartosc, filmInfo[7]);
            GridPane.setConstraints(obsada, 0, 8);
            GridPane.setValignment(obsada, VPos.CENTER);
            GridPane.setHalignment(obsada, HPos.LEFT);
            gridPaneZawartosc.getChildren().addAll(rezyser, obsada);

            hboxZawartosc.getChildren().add(gridPaneZawartosc);

            VBox vboxPrzyciski = new VBox();
            vboxPrzyciski.setSpacing(5);
            vboxPrzyciski.setPrefWidth(100);
            vboxPrzyciski.setPrefHeight(200);
            vboxPrzyciski.setAlignment(Pos.TOP_CENTER);

            ComboBox<String> ocena = new ComboBox<>();
            ocena.setId(idFilm);
            ocena.setStyle("-fx-background-radius:5; -fx-text-fill: black");

            for (int k = 1; k <= 10; ++k) {
              ocena.getItems().add(String.valueOf(k));
              //System.out.println(k);
            }
            ocena.setPromptText("Ocena");
            ocena.setPrefWidth(100);
            ocena.setMaxWidth(100);
            ocena.setDisable(true);
            comboBoxButtons.add(ocena);

            vboxPrzyciski.getChildren().add(ocena);

            Button btnDodajOcene = new Button();
            btnDodajOcene.setId(idFilm);
            btnDodajOcene.setStyle("-fx-background-radius:5");
            btnDodajOcene.setDisable(true);
            btnDodajOcene.setText("Dodaj ocenę");
            btnDodajOcene.setMinWidth(110);
            btnDodajOcene.setOnAction(new dodajOceneHandler());
            vboxPrzyciski.getChildren().add(btnDodajOcene);
            buttons.add(btnDodajOcene);

            hboxZawartosc.getChildren().add(vboxPrzyciski);

            vbox1.getChildren().add(hboxZawartosc);

            vboxOuterFilm.getChildren().add(vbox1);
            vboxOuterFilm.setSpacing(20);
            //System.out.println("####### <KONIEC> ###### - Dodalem ostatni rekord.\n");


          }
          //po zewnetrznym while'u zapisujemy liczbe rekordow
          liczbaRekordowFilm.setText("Liczba wyświetlonych rekordów: " + counterFilm);

          if (user.getFlag()) {
            enableButtons();
          }
        }

        rs.last();
        int nrOfRows = rs.getRow();
        liczbaZwroconychFilmow.setText("Liczba zwróconych rekordów: " + String.valueOf(nrOfRows));

        rs.close();
        pst.close();

        //zamkniecie polaczenia z baza
        c.close();

      } catch (IllegalArgumentException e) {
        Label error = new Label();
        error.setFont(Font.font("DejaVu Sans", FontWeight.BOLD, 12));
        error.setWrapText(true);
        error.setTextFill(Color.WHITE);
        error.setText("Nie znaleziono takiego filmu.");
        vboxOuterFilm.getChildren().add(error);
        error = null;
        //System.out.println("Nie znaleziono takiego filmu.");
      } catch (SQLException e) {
        //System.out.println("Blad podczas przetwarzania danych:" + e);
      }
    } else {
      //System.out.println("Brak polaczenia z baza, dalsza czesc aplikacji nie jest wykonywana.");
    }
  }

  /**
   * Handler dla przycisku Dodaj Ocene
   */
  private static class dodajOceneHandler implements EventHandler<ActionEvent> {

    /**
     * Znajduje wartosc przycisku o danym id, bedacym jednoczesnie id filmu
     * @param id id przycisku rowne id filmu
     * @return wybrana wartosc z listy czyli ocena
     */
    public String znajdzWartoscPrzycisku(String id) {
      String temp = "0";
      //System.out.println("Przeslane id: " + id);
      for (ComboBox<String> x : comboBoxButtons) {
        if (x.getId().equals(id)) {
          //System.out.println("Zaznaczono element o wartosci: " + x.getSelectionModel().getSelectedItem());
          temp = (String) x.getValue();
          //System.out.println("Znalazlem przycisk o id: " + x.getId() + " i wartosci: " + temp);
          break;
        }
      }
      comboBoxButtons.clear();
      return temp;
    }

    /**
     * Głowna metoda handlera obslugujaca przycisk dodania oceny
     * @param evt klikniecie przycisku
     */
    @Override
    public void handle(ActionEvent evt) {

      //pobranie jakie ID filmu i ocena nas interesuje
      String idFilmPrzycisk = ((Control) evt.getSource()).getId();

      //przygotowanie danych do wstawienia
      //System.out.println("Id filmu: " + idFilmPrzycisk);
      String oc = null;
      int ocena = 0, idUzytkownik = 0;
      boolean flag = false;
      try {
        idUzytkownik = Integer.parseInt(user.getId());
        oc = znajdzWartoscPrzycisku(idFilmPrzycisk);

        if (oc == null) {
          Alert alert = new Alert(AlertType.ERROR);
          alert.setTitle("Nieoczekiwany błąd!");
          Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
          stage.getIcons().add(
              new Image(this.getClass().getResource("resources/favicon.jpg").toString()));
          alert.setHeaderText(null);
          alert.setContentText("Z niewiadomego powodu, nie udało się pobrać wartości przycisku. Spróbuj ponownie.");
          alert.showAndWait();
          flag = true;

        }
        //System.out.println("Ocena przed castowaniem: " + oc);
        assert oc != null;
        ocena = Integer.parseInt(oc);
      } catch (Exception e) {
        //System.out.println("Nie udalo sie sparsowac Stringa z ocena.");
        //alert na wylogowanie
      }
      //System.out.println("Id uzytkownika: " + idUzytkownik);
      //System.out.println("Ocena: " + ocena);

      //polaczenie z baza
      Connection c = null;
      if (flag) {
      } else {

        try {
          c = DriverManager.getConnection("jdbc:postgresql://dumbo.db.elephantsql.com/npbkqkcz",
              "npbkqkcz", "f85TbMJdfkZcgzX4uYuRMiq4OCrrUlyB");
        } catch (SQLException se) {
          //System.out.println("Brak polaczenia z baza danych, wydruk logu sledzenia i koniec.");
          se.printStackTrace();
          System.exit(1);
        }

        if (c != null) {
          //System.out.println("Polaczenie z baza danych OK ! ");
          try {

            Statement st = c.createStatement();
            ResultSet rs = st
                .executeQuery("SELECT max(id_ocena) as \"id\" FROM \"Ocena\"");
            int lastID = 0;
            while (rs.next()) {
              lastID = rs.getInt("id");
              //System.out.println("Ostatnie ID: " + lastID);
            }
            rs.close();
            st.close();

            //Sprawdzenie czy uzytkownik dal juz ocene dla danego filmu
            String statement = " SELECT id_film, id_uzytkownik, id_ocena, ocena FROM \"Ocena\" where id_film=? and id_uzytkownik=?";
            //sprawdzenie czy film istnieje
            PreparedStatement lookFor = c.prepareStatement(statement,
                ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
            lookFor.setString(1, idFilmPrzycisk);
            lookFor.setInt(2, idUzytkownik);
            //System.out.println("Zapytanie po wstawieniu: " + lookFor.toString());
            ResultSet returnedData = lookFor.executeQuery();

            //Sytuacja 1: NIE znaleziono oceny dla danego filmu od danego uzytkownika
            if (!returnedData.next()) {

              //INSERT Ocena
              //System.out.println("Nie znalazlem oceny uzytkownika o ID:" + idUzytkownik + "i  idFilmu:"+ idFilmPrzycisk);
              PreparedStatement pst = c.prepareStatement(
                  "INSERT INTO \"Ocena\" VALUES (?,?,?,?)");
              pst.setString(1, idFilmPrzycisk);
              pst.setInt(2, idUzytkownik);
              pst.setInt(3, ocena);
              pst.setInt(4, lastID + 1);
              pst.executeUpdate();
              //System.out.println("Powinien wstawic do Oceny: " + idFilmPrzycisk + ", " + idUzytkownik + ", "+ ocena);
              pst.close();
            }

            //Sytuacja 2: ZNALEZIONO ocene danego uzytkownika i danego filmu w tabeli Ocena
            else {
              returnedData.previous();
              //System.out.println("Znalazlem juz ocene dla takiego id_filmu i id_uzytkownika.");
              if (returnedData.next()) {
                PreparedStatement pst = c.prepareStatement(
                    "update \"Ocena\" set ocena=? where id_film=? and id_uzytkownik=? and id_ocena=?");
                pst.setInt(1, ocena);
                pst.setString(2, idFilmPrzycisk);
                pst.setInt(3, idUzytkownik);
                int id_ocena = returnedData.getInt("id_ocena");
                pst.setInt(4, id_ocena);
                pst.executeUpdate();
                //System.out.println("Zrobilem UPDATE oceny w tabeli Ocena dla id_film: " + idFilmPrzycisk+ ", id_uzytkownik: " + idUzytkownik + ", id_ocena: " + id_ocena+ ", nowa ocena: " + ocena);
                pst.close();
              }
            }
            c.close();

            //alert na dodanie
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Dodawanie oceny");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(
                new Image(this.getClass().getResource("resources/favicon.jpg").toString()));
            alert.setHeaderText(null);
            alert.setContentText("Dodano ocenę do produkcji.");
            alert.showAndWait();
          } catch (SQLException e) {
            //System.out.println("Blad podczas przetwarzania danych:" + e);
          }
        } else {
          //System.out.println("Brak polaczenia z baza, dalsza czesc aplikacji nie jest wykonywana.");
        }
      }
    }
  }


  /**
   * Zwraca wyniki z osobami na podstawie wpisanego tekstu w polu tekstowym
   */
  public void zwrocOsobeNowe() {
    Connection c = null;

    //wyczyszczenie elementow glownego vboxa
    vboxOuterOsoba.getChildren().clear();

    try {
      c = DriverManager.getConnection("jdbc:postgresql://dumbo.db.elephantsql.com/npbkqkcz",
          "npbkqkcz", "f85TbMJdfkZcgzX4uYuRMiq4OCrrUlyB");
    } catch (SQLException se) {
      //System.out.println("Brak polaczenia z baza danych, wydruk logu sledzenia i koniec.");
      se.printStackTrace();
      System.exit(1);
    }
    if (c != null) {
      //System.out.println("Polaczenie z baza danych OK ! ");
      try {

        String statement;

        //wynikowy tekst dodany do TextArea
        StringBuilder opis_aktorow = new StringBuilder();

        //pobranie tekstu z textfield
        String wpisanaOsoba = szukajWBazieOsob.getText();

        if (wpisanaOsoba.equals("")) {
          c.close();
          throw new IllegalArgumentException();
        }

        //umozliwie wyszukiwania zarowno po malych jak i duzych literach oraz ze spacja w nazwie
        if (wpisanaOsoba.contains(" ")) {

          String[] slowa = wpisanaOsoba.split(" ");
          StringBuilder ciag = new StringBuilder();

          for (int i = 0; i < slowa.length; ++i) {
            StringBuilder temp = new StringBuilder(slowa[i]);
            temp.setCharAt(0, '_');
            temp.append("%");
            ciag.append(temp);
            if (i != slowa.length - 1) {
              ciag.append(" ");
            }
          }
          statement =
              "SELECT * from " + "Osoba_info" + " where imie_i_nazwisko like " + "'" + ciag
                  + "'";
        } else {
          StringBuilder ciag = new StringBuilder(wpisanaOsoba);
          ciag.setCharAt(0, '_');
          ciag.append("%");
          statement =
              "SELECT * from " + "Osoba_info" + " where imie_i_nazwisko like " + "'" + ciag
                  + "'";
        }

        // dodanie sortowania
        String kolumna;
        if (rbNazwisko.isSelected()) { //po nazwisku
          kolumna = "imie_i_nazwisko";
          statement += " order by " + kolumna;
        } else if (rbData.isSelected()) {   //po dacie narodzin
          kolumna = "data_narodzin";
          statement += " order by " + kolumna /*+ ", imie_i_nazwisko"*/;
        } else if (rbZawod.isSelected()) { //po zawodzie
          kolumna = "rola";
          statement += " order by " + kolumna /*+ ", imie_i_nazwisko"*/;
        }

        //System.out.println("Zapytanie 1 :\n" + statement + "\n");

        PreparedStatement pst = c
            .prepareStatement(statement,
                ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = pst.executeQuery();

        //liczba rekordow dla osob
        int counterOsoba = 0;

        if (!rs.next()) {
          //System.out.println("Wchodze do ifa bo nic nie znalazlem");
          Label error = new Label();
          error.setFont(Font.font("DejaVu Sans", FontWeight.BOLD, 12));
          error.setWrapText(true);
          error.setTextFill(Color.WHITE);
          error.setText("Nie znaleziono takiej osoby.");
          vboxOuterOsoba.getChildren().add(error);
          error = null;
        } else {

          //petla po wszystkich wynikach
          rs.previous();
          while (rs.next()) {

            //inkrementacja liczby rekordow dla obiektu Label
            //ustawienie max liczby rekordow
            if (counterOsoba == 5) {
              break;
            }
            counterOsoba++;

            ResultSetMetaData rsMetaData = rs.getMetaData();
            int liczbaKolumn = rsMetaData.getColumnCount();

            String[] osobaInfo = {"Data narodzin: ", "Data śmierci: ", "Biografia: ",
                "Zawód: ", "Produkcje, przy których pracował/a: "};

            //VBOX1 - vbox dla kazdego setu z elementami
            VBox vbox1 = new VBox();
            vbox1.setPrefWidth(800);
            vbox1.setPrefHeight(200);
            vbox1.setSpacing(5);
            vbox1.setPadding(new Insets(5, 5, 5, 5));
            //System.out.println("1. Stworzylem vboxa1");

            //HBOX na imie i nazwisko - dla kazdego vboxa
            HBox hboxNazwisko = new HBox();
            hboxNazwisko.setPrefWidth(200);
            hboxNazwisko.setPrefHeight(100);
            hboxNazwisko.setAlignment(Pos.CENTER_LEFT);
            //System.out.println("2. Stworzylem hboxa na tytul");

            //HBOX na zawartosc - plakat i info - dla kazdego vboxa
            HBox hboxZawartosc = new HBox();
            hboxZawartosc.setSpacing(5);
            hboxZawartosc.setPrefHeight(270);
            //System.out.println("3. Stworzylem hboxa na plakat i info.");

            //VBOX dla Hboxa tytul
            VBox vboxNazwisko = new VBox();
            vboxNazwisko.setMinWidth(200);
            //System.out.println("4. Stworzylem vboxa dla tytulu.");

            Label labelNazwisko = new Label();
            labelNazwisko.setTextFill(Color.WHITE);
            labelNazwisko.setFont(Font.font("DejaVu Sans", FontWeight.BOLD, 18));
            labelNazwisko.setWrapText(true);
            //System.out.println("5. Stworzylem label dla textflow.");

            //Gridpane
            GridPane gridPaneZawartosc = new GridPane();
            gridPaneZawartosc.setAlignment(Pos.CENTER_LEFT);
            gridPaneZawartosc.setPrefHeight(270);
            gridPaneZawartosc.setPrefWidth(800);
            gridPaneZawartosc.setMaxHeight(USE_COMPUTED_SIZE);
            gridPaneZawartosc.setMinHeight(USE_COMPUTED_SIZE);
            gridPaneZawartosc.setMaxWidth(USE_COMPUTED_SIZE);
            gridPaneZawartosc.setMinWidth(USE_COMPUTED_SIZE);

            RowConstraints thirdRow = new RowConstraints();
            thirdRow.setFillHeight(false);
            thirdRow.setMinHeight(25);
            thirdRow.setPercentHeight(-1);
            thirdRow.setValignment(VPos.TOP);
            thirdRow.setMaxHeight(USE_COMPUTED_SIZE);
            thirdRow.setPrefHeight(USE_COMPUTED_SIZE);
            thirdRow.setVgrow(Priority.SOMETIMES);

            RowConstraints row = new RowConstraints();
            row.setMinHeight(25);
            row.setPercentHeight(-1);
            row.setValignment(VPos.TOP);
            row.setMaxHeight(USE_COMPUTED_SIZE);
            row.setPrefHeight(USE_COMPUTED_SIZE);
            row.setVgrow(Priority.SOMETIMES);
            row.setFillHeight(false);

            ColumnConstraints col = new ColumnConstraints();
            col.setHgrow(Priority.SOMETIMES);
            col.setFillWidth(false);
            col.setPercentWidth(-1);
            col.setHalignment(HPos.LEFT);
            col.setMaxWidth(USE_COMPUTED_SIZE);
            col.setMinWidth(USE_COMPUTED_SIZE);
            col.setPrefWidth(USE_COMPUTED_SIZE);

            gridPaneZawartosc.getRowConstraints()
                .addAll(row, row, thirdRow, row, row);
            gridPaneZawartosc.getColumnConstraints().add(col);
            //System.out.println("7. Skonfigurowalem GridPane.");

            //zapisanie pierwszego wiersza danej ospby
            for (int i = 1; i <= liczbaKolumn - 3; i++) {
              String dane_kolumny = rs.getString(rsMetaData.getColumnName(i));
              if (dane_kolumny == null || dane_kolumny.equals("")) {
                dane_kolumny = "Brak";
              }
              if (i == 1) {

                //ImageView
                ImageView imageView = null;
                //System.out.println("ID osoby: " + dane_kolumny);
                try {
                  String przyklad =
                      "https://api.themoviedb.org/3/find/" + dane_kolumny + "?api_key=" + API_KEY
                          + "&external_source=imdb_id";
//                  String przyklad = "https://api.themoviedb.org/3/person/find/" + dane_kolumny + "/images?api_key=" + API_KEY + "&external_source=imdb_id" ;
                  URL url = new URL(przyklad);
                  HttpURLConnection con = (HttpURLConnection) url.openConnection();

                  StringBuffer response = null;
                  String USER_AGENT = "Mozilla/5.0";
                  con.setDoOutput(true);
                  con.setRequestMethod("GET");
                  con.setRequestProperty("User-Agent", USER_AGENT);
                  int responseCode = con.getResponseCode();
                  //System.out.println("GET Response Code :: " + responseCode);
                  if (responseCode == HttpURLConnection.HTTP_OK) { // success
                    BufferedReader in = new BufferedReader(new InputStreamReader(
                        con.getInputStream()));
                    String inputLine;
                    response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                      response.append(inputLine);
                    }
                    in.close();

                    // print result
                    //System.out.println(response.toString());
                  } else {
                    //System.out.println("GET request not worked");
                  }

                  assert response != null;
                  JsonReader rdr = Json.createReader(new StringReader(response.toString()));
                  //System.out.println("JsonReader: createReader");
                  JsonObject obj = rdr.readObject();
                  //System.out.println("readObject()");

                  JsonArray arr = obj
                      .getJsonArray("person_results"); //tablica z jednym slownikiem
                  JsonObject last = arr.getJsonObject(0);
                  String poster_path;
                  try {
                    poster_path = last.getString("profile_path");
                    //System.out.println("Sciezka do plakatu: " + poster_path);
                  } catch (ClassCastException e) {
                    //System.out.println("Nie ma obrazka osoby.");
                    throw new NullPointerException();
                  }

                  String imgLink = "http://image.tmdb.org/t/p/original" + poster_path;
                  //System.out.println("Link z obrazkiem: " + imgLink);

                  imageView = new ImageView(imgLink);

                  //System.out.println("Ustawilem ID danego vboxa na: " + dane_kolumny);
                } catch (NullPointerException | IndexOutOfBoundsException e) {
                  //System.out.println("Wchodze do bloku catch - nie znalazlem plakatu dla takiego filmu :C");
                  imageView = new ImageView(
                      getClass().getResource("resources/brakZdjecia.png").toExternalForm());
                } catch (IOException e) {
                  e.printStackTrace();
                }
                assert imageView != null;
                imageView.setFitHeight(270);
                imageView.setFitWidth(200);
                imageView.setPickOnBounds(true);
                hboxZawartosc.getChildren().add(imageView); //dodanie ImageView do hbox'a
                //System.out.println("8. Stworzylem i dodalem imageview z plakatem do hboxa z zawartoscia.");
              } else if (i == 2) {
                labelNazwisko.setText(dane_kolumny);
                TextFlow tfTytul = new TextFlow();
                tfTytul.setPrefHeight(200);
                tfTytul.setPrefWidth(200);
                tfTytul.getChildren().add(labelNazwisko);
                //System.out.println("9. Stworzylem textflow i dodalem label z tytulem.");
                vboxNazwisko.getChildren().add(tfTytul);
                //System.out.println("10. Dodalem textflow dla tytulu");

                hboxNazwisko.getChildren().add(vboxNazwisko);
                vbox1.getChildren().add(hboxNazwisko);
                //System.out.println("17. CALY hbox z tytulem i podtytulem dodany do vboxa");
              } else {
                TextFlow tfTemp;
                tfTemp = wypelnijGridPaneOsoba(gridPaneZawartosc,
                    osobaInfo[i - 3] + dane_kolumny);
                GridPane.setConstraints(tfTemp, 0, i - 3);
                GridPane.setValignment(tfTemp, VPos.CENTER);
                GridPane.setHalignment(tfTemp, HPos.LEFT);

                gridPaneZawartosc.getChildren().add(tfTemp);
                //System.out.println("Dodano " + (i - 2)+ ". element do gridpane i ustawiono V- i H- alignemnt.");
              }
            }
            //pobieramy info o osobie  z pierwszego zwrocenego wiersza
            String aktor = rs.getString(rsMetaData.getColumnName(2));

            //przejscie z powrotem na poczatek zwroconych danych
            rs.previous();

            //ta petla sluzy do przechodzeniu po filmach czyli po wierszach dla ktorych wartosc imie_i_nazwisko jest taka sama
            while (rs.next()) {
              //System.out.println("Wchodze do drugiego while - dla filmow");

              //ustawiamy pomocnicze zmienne
              String aktor2 = rs.getString(rsMetaData.getColumnName(2));
              String tytulFilmu = rs.getString(rsMetaData.getColumnName(7));
              String rok = rs.getString(rsMetaData.getColumnName(8));
              String postac = rs.getString(rsMetaData.getColumnName(9));

              //System.out.println("Licznik: " + counterOsoba + "Tytul filmu: " + tytulFilmu);

              //sprawdzamy czy nazwiska dla obu wierszy sa takie same
              if (aktor.equals(aktor2)) {
                if (postac != null && !postac.equals("")) {
                  osobaInfo[4] += tytulFilmu + "(" + rok + ")" + postac + "\t";
                } else {
                  osobaInfo[4] += tytulFilmu + "(" + rok + ")\t";
                }
              } else {
                rs.previous();
                break;
              }
            }
            //dodaj rezyserow
            TextFlow filmy = wypelnijGridPaneOsoba(gridPaneZawartosc, osobaInfo[4]);
            GridPane.setConstraints(filmy, 0, 4);
            GridPane.setValignment(filmy, VPos.CENTER);
            GridPane.setHalignment(filmy, HPos.LEFT);
            //System.out.println("Dodano 8. element do gridpane i ustawiono V- i H- alignemnt.");

            gridPaneZawartosc.getChildren().add(filmy);
            hboxZawartosc.getChildren().add(gridPaneZawartosc);
            //System.out.println("10. Dodalem gridpane do hboxa");

            vbox1.getChildren().add(hboxZawartosc);
//            hboxZawartosc = null;
            //System.out.println("17. Dodalem drugiego hboxa do vboxa.");

            vboxOuterOsoba.getChildren().add(vbox1);
            vboxOuterOsoba.setSpacing(20);
            //System.out.println("18. Dodalem do glownego vboxa cala zawartosc");
            //System.out.println("####### <KONIEC> ###### - Dodalem ostatni rekord.\n");


          }

          //po zewnetrznym while'u zapisujemy liczbe rekordow
          liczbaRekordowOsoba.setText("Liczba wyświetlonych rekordów: " + counterOsoba);
        }

        //zmiana przyciskow na aktywne
        if (user.getFlag()) {
          enableButtons();
        }

        rs.last();
        int nrOfRows = rs.getRow();
        liczbaZwroconychOsob.setText("Liczba zwróconych rekordów: " + String.valueOf(nrOfRows));

        rs.close();
        pst.close();

        //zamkniecie polaczenia z baza
        c.close();

      } catch (IllegalArgumentException e) {
        Label error = new Label();
        error.setFont(Font.font("DejaVu Sans", FontWeight.BOLD, 12));
        error.setWrapText(true);
        error.setTextFill(Color.WHITE);
        error.setText("Nie znaleziono takiej osoby.");
        vboxOuterOsoba.getChildren().add(error);
        error = null;
        //System.out.println("Nie znaleziono takiej osoby.");
      } catch (SQLException e) {
        //System.out.println("Blad podczas przetwarzania danych:" + e);
      }
    } else {
      //System.out.println("Brak polaczenia z baza, dalsza czesc aplikacji nie jest wykonywana.");
    }
  }

  /**
   * Handler obslugujacy wylogowanie uzytkownika
   */
  private static class wylogujHandler implements EventHandler<ActionEvent> {

    /**
     * Glowna metoda Handlera - ustawia flage uzytkownika na false, czyli niezalogowany
     * @param event klikniecie przycisku
     */
    @Override
    public void handle(ActionEvent event) {
      setUser(new User("", false));

      PrzejdzStart obj = new PrzejdzStart();
      try {
        obj.przejdzDoStartScreen(event);
      } catch (IOException e) {
        e.printStackTrace();
      }

      //alert na wylogowanie
      Alert alert = new Alert(AlertType.INFORMATION);
      alert.setTitle("Wylogowanie");
      Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
      stage.getIcons().add(
          new Image(this.getClass().getResource("resources/favicon.jpg").toString()));
      alert.setHeaderText(null);
      alert.setContentText("Wylogowano użytkownika.");
      alert.showAndWait();
    }
  }

  /**
   * Zmienia Scene na formularz z logowaniem controllera "Logowanie"
   * @param actionEvent klikniecie przycisku
   * @throws IOException wyjatek gdy funkcja load() z klasy FXMLLoader nie zadziala odpowiednio
   */
  public void przejdzDoLogowania(ActionEvent actionEvent) throws IOException {
    Parent regParent = FXMLLoader.load(getClass().getResource("Logowanie.fxml"));
    Scene regScene = new Scene(regParent);
    Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

    window.sizeToScene();
    window.setScene(regScene);
    window.show();
  }

  /**
   * Zmienia Scene na formularz z rejestracja controllera "Rejestracja"
   * @param actionEvent klikniecie przycisku
   * @throws IOException wyjatek gdy funkcja load() z klasy FXMLLoader nie zadziala odpowiednio
   */
  public void przejdzDoRejestracji(ActionEvent actionEvent) throws IOException {
    Parent regParent = FXMLLoader.load(getClass().getResource("Rejestracja.fxml"));
    Scene regScene = new Scene(regParent);
    Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();


    window.setScene(regScene);
    window.sizeToScene();
    window.show();
  }

  /**
   * Zmienia Scene na formularz z dodawaniem nowego filmu controllera "FormularzFilm"
   * @param actionEvent klikniecie przycisku
   * @throws IOException wyjatek gdy funkcja load() z klasy FXMLLoader nie zadziala odpowiednio
   */
  public void przejdzDoFormularzaFilm(ActionEvent actionEvent) throws IOException {
    Parent regParent = FXMLLoader.load(getClass().getResource("FormularzFilm.fxml"));
    Scene regScene = new Scene(regParent);
    Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

    window.sizeToScene();
    window.setScene(regScene);
    window.show();
  }

  /**
   * Zmienia Scene na formularz z dodawaniem nowej osoby controllera "FormularzOsoba"
   * @param actionEvent klikniecie przycisku
   * @throws IOException wyjatek gdy funkcja load() z klasy FXMLLoader nie zadziala odpowiednio
   */
  public void przejdzDoFormularzOsoba(ActionEvent actionEvent) throws IOException {
    Parent regParent = FXMLLoader.load(getClass().getResource("FormularzOsoba.fxml"));
    Scene regScene = new Scene(regParent);
    Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

    window.sizeToScene();
    window.setScene(regScene);
    window.show();
  }


}
