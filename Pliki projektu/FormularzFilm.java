package Filmoholik;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Klasa reprezentujaca Scene z formularzem do wprowadzania danych o filmie, bedaca controllerem dla pliku FormularzFilm.fxml
 */
public class FormularzFilm {

  /**
   * Pola tekstowe na odpowiednie informacje o filmie: tytul, czas trwania, itp.
   */
  @FXML
  private TextField TFTytul, TFCzas, TFData, TFRok, TFOpis, TFGatunek, TFKraj, TFWytwornia, TFJezyk;

  /**
   * Etykiety (labele) reprezentuajce komunikaty o niepoprawnosci wprowadzonych danych
   */
  @FXML
  private Label obowiazkowe1, obowiazkowe3, obowiazkowe4, obowiazkowe5, obowiazkowe6, obowiazkowe7, obowiazkowe8, obowiazkowe9, obowiazkowe10;

  /**
   * Sprawdza czy wpisany tekst jest liczba
   * @param str tekst z pola tekstowego z czasem trwania filmu
   * @return prawda lub falsz
   */
  public static boolean isNumeric(String str) {
    try {
      Integer.parseInt(str);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  /**
   * Sprawdza czy wpisany tekst jest w formacie odpowiednim dla Daty, czyli tutaj YYYY-MM-dd
   * @param str tekst z pola tekstowego z data
   * @return prawda lub falsz
   */
  public static boolean checkDate(String str) {
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    dateFormat.setLenient(false);
    try {
      dateFormat.parse(str);

    } catch (ParseException ex) {
      return false;
    }
    return true;
  }

  /**
   * Pomocnicza metoda pozwalajaca na przejscie do ekranu glownego, wykorzystuje metode z klasy PrzejdzStart
   * @param event klikniecie przycisku
   * @throws IOException wyjatek w przypadku bledu
   */
  public void przejdzDoStartScreen(ActionEvent event) throws IOException {
    PrzejdzStart obj = new PrzejdzStart();
    obj.przejdzDoStartScreen(event);
  }

  /**
   * Dodaje produkcje filmowa z informacjami o filmie do odpowiednich tabel - Film, Film_gatunek, itp.
   * @param event klikniecie przycisku
   * @throws IOException wyjatek w przypadku bledu
   */
  public void dodajFilm(ActionEvent event) throws IOException {

    TextField[] filmForm = {TFTytul, TFRok, TFData, TFCzas, TFOpis, TFGatunek, TFKraj,
        TFWytwornia, TFJezyk};
    Label[] filmFormLabel = {obowiazkowe1, obowiazkowe3, obowiazkowe4, obowiazkowe5,
        obowiazkowe6, obowiazkowe7, obowiazkowe8, obowiazkowe9, obowiazkowe10};

    Connection c = null;

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
        boolean flag = false;

        for (int i = 0; i < filmForm.length; ++i) {
          if (filmForm[i].getText().equals("")) {
            //System.out.println("Puste miejsce w ktoryms polu");
            filmFormLabel[i].setVisible(true);
            flag = true;
          } else {
            filmFormLabel[i].setVisible(false);
          }
          if (!isNumeric(filmForm[1].getText()) || (filmForm[1].getText().length() != 4)) {
            //System.out.println("W zakladce Rok - błędny format!");
            filmFormLabel[1].setText("Błędny format");
            filmFormLabel[1].setVisible(true);
            flag = true;
          }
          if (!checkDate(filmForm[2].getText())) {
            //System.out.println("W zakladce Data wydania - błędny format!");
            filmFormLabel[2].setText("Błędny format");
            filmFormLabel[2].setVisible(true);
            flag = true;
          }
          if (!isNumeric(filmForm[3].getText())) {
            //System.out.println("W zakladce Czas - bledny format!");
            filmFormLabel[3].setText("Błędny format");
            filmFormLabel[3].setVisible(true);
            flag = true;
          }
        }

        if (flag) {
          c.close();
        } else {

          for (Label label : filmFormLabel) {
            label.setVisible(false);
          }

          //Film
          Statement st = c.createStatement();
          ResultSet rs = st
              .executeQuery("SELECT max(id_film) as \"id\" FROM \"Film\"");
          String lastIdFilm = null;
          while (rs.next()) {
            lastIdFilm = rs.getString("id");
            //System.out.println("Ostatnie max ID filmu: " + lastIdFilm);
          }
          assert lastIdFilm != null;
          String[] parts = lastIdFilm.split("(?<=\\D)(?=\\d)");

          //update indeksu filmu:
          lastIdFilm = parts[0] + (Integer.parseInt(parts[1]) + 1);
          //System.out.println("Nowe ID filmu: " + lastIdFilm);

          PreparedStatement pst = c.prepareStatement(
              "INSERT INTO \"Film\" VALUES (?,?,?,?,?,?)");
          pst.setString(1, lastIdFilm);
          pst.setString(2, filmForm[0].getText());
          pst.setInt(3, Integer.parseInt(filmForm[1].getText())); //rok
          pst.setString(4, filmForm[2].getText());
          pst.setInt(5, Integer.parseInt(filmForm[3].getText())); //czas trwania
          pst.setString(6, filmForm[4].getText());
          pst.executeUpdate();
          //System.out.println("Wstawilem do FIlm.");

          //Film_gatunek i Gatunek
          Statement st2 = c.createStatement();
          ResultSet rs2 = st2
              .executeQuery("SELECT max(id_gatunku) as \"id\" FROM \"Gatunek\"");
          int lastIdGatunek = 0;
          while (rs2.next()) {
            lastIdGatunek = rs2.getInt("id");
            //System.out.println("Ostatnie ID gatunku: " + lastIdGatunek);
          }
          PreparedStatement pst2 = c.prepareStatement(
              "INSERT INTO \"Gatunek\" VALUES (?,?);");
          PreparedStatement pst3 = c.prepareStatement(
              " INSERT INTO \"Film_gatunek\" VALUES (?,?)");
          pst2.setString(1, TFGatunek.getText());
          pst2.setInt(2, lastIdGatunek + 1);
          pst3.setInt(1, lastIdGatunek + 1);
          pst3.setString(2, lastIdFilm);
          pst2.executeUpdate();
          //System.out.println("Wstawilem do Gatunek.");
          pst3.executeUpdate();
          //System.out.println("Wstawilem do Film_gatunek.");

          //Film_kraj i Kraj
          Statement st3 = c.createStatement();
          ResultSet rs3 = st3
              .executeQuery("SELECT max(id_kraj) as \"id\" FROM \"Kraj\"");
          int lastIdKraj = 0;
          while (rs3.next()) {
            lastIdKraj = rs3.getInt("id");
            //System.out.println("Ostatnie ID kraju: " + lastIdKraj);
          }
          PreparedStatement pst4 = c.prepareStatement(
              "INSERT INTO \"Kraj\" VALUES (?,?)");
          PreparedStatement pst5 = c.prepareStatement(" INSERT INTO \"Film_kraj\" VALUES (?,?)");
          pst4.setInt(1, lastIdKraj + 1); //id_kraj
          pst4.setString(2, TFKraj.getText()); //nazwa
          pst5.setString(1, lastIdFilm); //id_film
          pst5.setInt(2, lastIdKraj + 1); //id_kraj
          pst4.executeUpdate();
          //System.out.println("Wstawilem do Kraj.");
          pst5.executeUpdate();
          //System.out.println("Wstawilem do Film_kraj.");

          //Film_jezyk i Jezyk
          Statement st4 = c.createStatement();
          ResultSet rs4 = st4
              .executeQuery("SELECT max(id_jezyk) as \"id\" FROM \"Jezyk\"");
          int lastIdJezyk = 0;
          while (rs4.next()) {
            lastIdJezyk = rs4.getInt("id");
            //System.out.println("Ostatnie ID jezyka: " + lastIdJezyk);
          }
          PreparedStatement pst6 = c.prepareStatement(
              "INSERT INTO \"Jezyk\" VALUES (?,?)");
          PreparedStatement pst7 = c.prepareStatement(" INSERT INTO \"Film_jezyk\" VALUES (?,?)");
          pst6.setString(1, TFJezyk.getText()); //jezyk mowiony
          pst6.setInt(2, lastIdJezyk + 1); //id_jezyk
          pst7.setString(1, lastIdFilm); //id_film
          pst7.setInt(2, lastIdJezyk + 1); //id_jezyk
          pst6.executeUpdate();
          //System.out.println("Wstawilem do Jezyk.");
          pst7.executeUpdate();
          //System.out.println("Wstawilem do Film_jezyk.");

          //Film_wytwornia i Wytwornia
          Statement st5 = c.createStatement();
          ResultSet rs5 = st5
              .executeQuery("SELECT max(id_wytwornia) as \"id\" FROM \"Wytwornia\"");
          int lastIdWytwornia = 0;
          while (rs5.next()) {
            lastIdWytwornia = rs5.getInt("id");
            //System.out.println("Ostatnie ID wytwornia: " + lastIdWytwornia);
          }
          PreparedStatement pst8 = c.prepareStatement(
              "INSERT INTO \"Wytwornia\" VALUES (?,?)");
          PreparedStatement pst9 = c
              .prepareStatement(" INSERT INTO \"Filmy_wytworni\" VALUES (?,?)");
          pst8.setString(1, TFWytwornia.getText()); //nazwa
          pst8.setInt(2, lastIdWytwornia + 1); //id_wytwornia
          pst9.setString(1, lastIdFilm); //id_film
          pst9.setInt(2, lastIdWytwornia + 1); //id_wytwornia
          pst8.executeUpdate();
          //System.out.println("Wstawilem do Wytwornia.");
          pst9.executeUpdate();
          //System.out.println("Wstawilem do Film_wytwornia.");

          //INSERT do tabeli Oceny_filmow nowej produkcji
          PreparedStatement pst10 = c.prepareStatement(
              "INSERT INTO \"Oceny_filmow\" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)");
          pst10.setString(1, lastIdFilm);
          //wstawienie poczatkowo samych zer
          for (int i = 2; i < 14; ++i) {
            pst10.setInt(i, 0);
          }
          pst10.executeUpdate();
          pst10.close();


          //wypisanie prawdopodobnego wyniku
          String temp = "";
          //System.out.println("Powinien wstawic: " + filmForm[0].getText());
          for (int i = 2; i < filmForm.length; ++i) {
            temp += " " + filmForm[i].getText();
          }
          temp += "\n";
          //System.out.println(temp);
          //////////////////////////////////

          pst9.close();
          pst8.close();
          rs5.close();
          st5.close();

          pst7.close();
          pst6.close();
          rs4.close();
          st4.close();

          pst5.close();
          pst4.close();
          rs3.close();
          st3.close();

          pst3.close();
          pst2.close();
          rs2.close();
          st2.close();

          pst.close();
          rs.close();
          st.close();
          c.close();

          przejdzDoStartScreen(event);
          //alert na dodanie
          Alert alert = new Alert(AlertType.INFORMATION);
          alert.setTitle("Formularz nowej produkcji");
          Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
          stage.getIcons().add(
              new Image(this.getClass().getResource("resources/favicon.jpg").toString()));
          alert.setHeaderText(null);
          alert.setContentText("Produkcja została dodana.");
          alert.showAndWait();
        }

      } catch (SQLException e) {
        //System.out.println("Blad podczas przetwarzania danych:" + e);
      }
    } else {
      //System.out.println("Brak polaczenia z baza, dalsza czesc aplikacji nie jest wykonywana.");
    }
  }
}
