package Filmoholik;

import static Filmoholik.FormularzFilm.checkDate;

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
import java.util.Date;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Klasa reprezentujaca Scene z formularzem do wprowadzania informacji o nowej osobie zw. z filmami, bedaca controllerem dla pliku FormularzOsoba.fxml
 */
public class FormularzOsoba {

  /**
   * Pola tekstowe do wprowadzania informacji
   */
  @FXML
  private TextField TFNazwisko, TFUrodziny, TFSmierc, TFBio, TFFilmy;

  /**
   * Labele wyswietlane na niepoprawnosc ktorejs z wprowadzonych danych
   */
  @FXML
  private Label mandat1, mandat2, mandat3, mandat5, mandat6;

  /**
   * Checkboxy z konkretnymi zawodami do wyboru: aktor, scenarzysta, archiwista itp.
   */
  @FXML
  private CheckBox cbAktor, cbAktorka, cbRezyser, cbScenarzysta, cbDzwiekowiec, cbKamerzysta, cbScenograf, cbArchiwista, cbOnSam, cbProducent, cbKompozytor, cbEdytor;

  /**
   * Odpowiada za przejscie do glownego ekranu aplikacji, wykorzystuje metode z klasy PrzejdzStart
   * @param event event - w tym przypadku klikniecie przycisku
   * @throws IOException wyjatek w przypadku bledu
   */
  public void przejdzDoStartScreen(ActionEvent event) throws IOException {
    PrzejdzStart obj = new PrzejdzStart();
    obj.przejdzDoStartScreen(event);
  }

  /**
   * Odznaczenie checkboxa aktora, uzywane przy zaznaczaniu checkboxa aktorki
   */
  public void uncheckAktor() {
    cbAktor.setSelected(false);
  }

  /**
   * Odznaczenie checkboxa aktorki, uzywane przy zaznaczaniu checkboxa aktora
   */
  public void uncheckAktorka() {
    cbAktorka.setSelected(false);
  }

  /**
   * Metoda dodajaca Osobe zwiazana z filmem do tabeli Osoba i tabeli Rola_osoby
   * @param event event - na klikniecie przycisku
   * @throws IOException wyjatek w przypadku bledu
   */
  public void dodajOsobe(ActionEvent event) throws IOException {

    TextField[] osobaForm = {TFNazwisko, TFUrodziny, TFSmierc, TFBio, TFFilmy};
    Label[] osobaFormLabel = {mandat1, mandat2, mandat3, mandat5, mandat6};
    CheckBox[] osobaFormCheckBox = {cbAktorka, cbAktor, cbRezyser, cbProducent, cbKompozytor,
        cbKamerzysta, cbScenarzysta, cbScenograf, cbEdytor, cbArchiwista, cbOnSam, cbDzwiekowiec};
    int[] rola = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};

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
        boolean flag = true;
            if (osobaForm[0].getText().equals("")) {
              //System.out.println("Puste miejsce w ktoryms polu");
              osobaFormLabel[0].setVisible(true);
              c.close();
            } else {
              osobaFormLabel[0].setVisible(false);
            }
            if (osobaForm[4].getText().equals("")) {
              //System.out.println("Puste miejsce w ktoryms polu");
              osobaFormLabel[3].setVisible(true);
              c.close();
            } else {
              osobaFormLabel[3].setVisible(false);
            }
            if (!osobaForm[1].getText().equals("")) {
              if (!checkDate(osobaForm[1].getText())) {
                //System.out.println("W zakladce Data Urodzin - błędny format!");
                osobaFormLabel[1].setText("Błędny format.");
                osobaFormLabel[1].setVisible(true);
                c.close();
              }
            }
            else {
              osobaFormLabel[1].setVisible(false);
            }
//          }
          if (!osobaForm[2].getText().equals("")) {
            if (!checkDate(osobaForm[2].getText())) {
              //System.out.println("W zakladce Data Smierci - błędny format!");
              osobaFormLabel[2].setText("Błędny format.");
              osobaFormLabel[2].setVisible(true);
              c.close();
            }
          } else {
            osobaFormLabel[2].setVisible(false);
          }
//        }

        //sprawdzenie czy checkboxy zaznaczone
        for (CheckBox x : osobaFormCheckBox) {
          if (x.isSelected()) {
            //System.out.println("Ktorys checkbox zaznaczony.");
            flag = false; //nie zamykaj polaczenia
          }
        }

        if (flag) {
          osobaFormLabel[4].setVisible(true);
          c.close();
        } else {

          //jezeli formularz dobrze wypelniony odznacz "warningi"
          for (Label label : osobaFormLabel) {
            label.setVisible(false);
          }

          //Osoba
          Statement st = c.createStatement();
          ResultSet rs = st
              .executeQuery("SELECT max(id_osoba) as \"id\" FROM \"Osoba\"");
          String lastIdOsoba = null;
          while (rs.next()) {
            lastIdOsoba = rs.getString("id");
            //System.out.println("Ostatnie max ID osoby: " + lastIdOsoba);
          }
          assert lastIdOsoba != null;
          String[] parts = lastIdOsoba.split("(?<=\\D)(?=\\d)");

          //update indeksu osoby:
          lastIdOsoba = parts[0] + (Integer.parseInt(parts[1]) + 1);
          //System.out.println("Nowe ID osoby: " + lastIdOsoba);

          PreparedStatement pst = c.prepareStatement(
              "INSERT INTO \"Osoba\" VALUES (?,?,?,?,?)");
          pst.setString(1, lastIdOsoba); //id_osoba
          pst.setString(2, osobaForm[0].getText());//imie_i_nazwisko
          if (osobaForm[1].getText().equals("")) {
            pst.setDate(3, null);
          } else {
//            pst.setDate(3, (java.sql.Date) stringToDate(osobaForm[1].getText())); //data_narodzin
            pst.setDate(3, java.sql.Date.valueOf(osobaForm[1].getText())); //data_narodzin
          }
          if (osobaForm[2].getText().equals("")) {
            pst.setDate(4, null);
          } else {
            pst.setDate(4, java.sql.Date.valueOf(osobaForm[2].getText())); //data_smierci
          }
          pst.setString(5, osobaForm[3].getText()); //bio
          pst.executeUpdate();
          //System.out.println("Wstawilem do Osoba.");

          String[] tytulyFilmow = new String[1];
          if (osobaForm[4].getText().contains(",")) {
            tytulyFilmow = osobaForm[4].getText().split(",");
          } else {
            tytulyFilmow[0] = osobaForm[4].getText();
          }

          //flaga do sprawdzenia czy filmy znalezione
          boolean filmFound = true;

          String statement = " SELECT id_film FROM \"Film\" where ";

          //najpierw dodaj "tytul=?" dla kazdego filmu, potem umiesc w PreparedStatement
          for (int i = 0; i < tytulyFilmow.length; ++i) {
            if (i != tytulyFilmow.length - 1) {
              statement += " tytul = ? or ";
            } else {
              statement += " tytul=? ";
            }
          }
          //System.out.println("Zapytanie przed wstawieniem w pytajniki wartosci: " + statement);

          //sprawdzenie czy film istnieje
          PreparedStatement lookFor = c.prepareStatement(statement,
              ResultSet.TYPE_SCROLL_SENSITIVE,
              ResultSet.CONCUR_UPDATABLE);

          //wstawienie do PreparedStatement
          for (int i = 1; i <= tytulyFilmow.length; ++i) {
            lookFor.setString(i, tytulyFilmow[i - 1]);
          }

          //System.out.println("Zapytanie po wstawieniu: " + lookFor.toString());

          //wywolanie
          ResultSet returnedData = lookFor.executeQuery();

          //sprawdzenie czy cokolwiek znalazl
          if (!returnedData.next()) {
            //System.out.println("Nie znalazlem takiego filmu w bazie.");
            mandat5.setText("Nie ma takiego filmu.");
            mandat5.setVisible(true);
            filmFound = false;
          }
          if (!filmFound) {
            //System.out.println("Skoro nie znalazlem wchodze do ifa.");
            returnedData.close();
            lookFor.close();
            pst.close();
            rs.close();
            st.close();
            c.close();
          } else {
            //System.out.println("Cos znalazlem w bazie skoro tu jestem.");

            //mozliwe ze trzeba bd to dac do petli
            PreparedStatement pst2 = c.prepareStatement(
                "INSERT INTO \"Rola_osoby\" VALUES (?,?,?)");

            //ustawiamy kursor na poprzednia pozycje przez ifa u gory
            returnedData.previous();

            while (returnedData.next()) {
              //System.out.println("Wchodze do while'a.");
              //System.out.println("Dlugosc tablicy: " + osobaFormCheckBox.length);
              for (int i = 0; i < osobaFormCheckBox.length; ++i) {
                if (osobaFormCheckBox[i].isSelected()) {
                  //System.out.println("Zaznaczony checbox o indeksie: " + i);
                  pst2.setString(1, returnedData.getString("id_film"));
                  pst2.setString(2, lastIdOsoba);
                  pst2.setInt(3, rola[i]);
                  pst2.executeUpdate();
                  //System.out.println("Wstawilem do Rola_osoby.");
                }
              }
            }

            //wypisanie prawdopodobnego wyniku
            String temp = "";
            //System.out.println("Powinien wstawic: " + osobaForm[0].getText());
            for (TextField textField : osobaForm) {
              temp += textField.getText();
            }
            temp += "\n";
            //System.out.println(temp);
            //////////////////////////////////

            pst2.close();
            returnedData.close();
            lookFor.close();
            pst.close();
            rs.close();
            st.close();
            c.close();

            //wroc do ekranu glownego
            przejdzDoStartScreen(event);

            //alert na dodanie
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Formularz nowej osoby");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(
                new Image(this.getClass().getResource("resources/favicon.jpg").toString()));
            alert.setHeaderText(null);
            alert.setContentText("Osoba została dodana.");
            alert.showAndWait();
          }
        }
      } catch (SQLException e) {
        //System.out.println("Blad podczas przetwarzania danych:" + e);
      }
    } else {
      //System.out.println("Brak polaczenia z baza, dalsza czesc aplikacji nie jest wykonywana.");
    }
  }
}
