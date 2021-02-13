package Filmoholik;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Klasa reprezentujaca formularz z rejestracja nowego uzytkownika, jest controllerem dla pliku Rejestracja.fxml
 */
public class Rejestracja {

  /**
   * Pole tekstowe na nazwe uzytkownika
   */
  @FXML
  private TextField rej_nick;

  /**
   * Pole tekstowe na haslo uzytkownika
   */
  @FXML
  private TextField rej_haslo;

  /**
   * Etykieta wyswietlana gdy nazwa uzytkownika jest bledna
   */
  @FXML
  private Label rej_error;

  /**
   * Etykieta wyswietlana gdy haslo jest bledne
   */
  @FXML
  private Label rej_error_pass;

  /**
   * Pomocnicza metoda odpowiadajaca za przejscie do ekranu glownego
   * @param event klikniecie przycisku
   * @throws IOException wyjatek wyrzucany gdy funkcja load() z klasy FXMLLoader nie zadziala poprawnie
   */
  public void przejdzDoStartScreen(ActionEvent event) throws IOException {
    PrzejdzStart obj = new PrzejdzStart();
    obj.przejdzDoStartScreen(event);
  }

  /**
   * Funkcja dodajaca nowego uzytkownika
   * @param event klikniecie przycisku
   * @throws IOException wyjatek wyrzucany gdy funkcja load() z klasy FXMLLoader nie zadziala poprawnie
   */
  public void zarejestruj(ActionEvent event) throws IOException {
    String nick = rej_nick.getText();
    String pass = rej_haslo.getText();

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

        //flaga do sprawdzenia czy pola zostaly dobrze wypelnione
        boolean flag = false;

        //sprawdzenie czy nie ma juz takiej nazwy uzytkownika
        PreparedStatement lookFor = c.prepareStatement(
            "SELECT nazwa_uzytkownika FROM \"Uzytkownik\" where nazwa_uzytkownika=?");
        lookFor.setString(1, nick);
        ResultSet returnedNick = lookFor.executeQuery();
        while (returnedNick.next()) {
          if (returnedNick.getString("nazwa_uzytkownika").equals(nick)) {
            rej_error.setText("Użytkownik o takiej nazwie już istnieje - użyj innej nazwy.");
            rej_error.setVisible(true);
            flag = true; // taki uzytkownik juz istnieje
          }
        }
        returnedNick.close();
        lookFor.close();

        //jezeli takiego uzytkownika nie ma - sprawdz pozostale przypadki
        //jezeli jest - przerwij polaczenie
        if (flag) {
          c.close();
        } else {
          //sprawdzenie czy nazwa uzytkownika lub haslo sa prawidlowe
          if (nick.equals("") && pass.equals("")) {
            rej_error.setText("Wpisz nazwę użytkownika!");
            rej_error_pass.setText("Wpisz hasło!");
            rej_error.setVisible(true);
            rej_error_pass.setVisible(true);
            c.close();
          } else if (nick.equals("")) {
            rej_error.setText("Wpisz nazwę użytkownika!");
            rej_error.setVisible(true);
            rej_error_pass.setVisible(false);
            c.close();
          } else if (pass.equals("")) {
            rej_error_pass.setText("Wpisz hasło!");
            rej_error_pass.setVisible(true);
            rej_error.setVisible(false);
            c.close();
          } else if ((!nick.matches("^([A-Za-z]|[0-9])+$")) && (!pass
              .matches("^([A-Za-z]|[0-9])+$"))) {
            rej_error.setText("Użyj jedynie znaków alfanumerycznych.");
            rej_error_pass.setText("Użyj jedynie znaków alfanumerycznych.");
            rej_error.setVisible(true);
            rej_error_pass.setVisible(true);
            c.close();
          } else if ((!nick.matches("^([A-Za-z]|[0-9])+$"))) {
            rej_error.setText("Użyj jedynie znaków alfanumerycznych.");
            rej_error.setVisible(true);
            rej_error_pass.setVisible(false);
            c.close();
          } else if ((!pass.matches("^([A-Za-z]|[0-9])+$"))) {
            rej_error_pass.setText("Użyj jedynie znaków alfanumerycznych.");
            rej_error_pass.setVisible(true);
            rej_error.setVisible(false);
            c.close();
          } else {

            rej_error.setVisible(false);
            rej_error_pass.setVisible(false);

            Statement st = c.createStatement();
            ResultSet rs = st
                .executeQuery("SELECT max(id_uzytkownik) as \"id\" FROM \"Uzytkownik\"");
            int lastID = 0;
            while (rs.next()) {
              lastID = rs.getInt("id");
              //System.out.println("Ostatnie ID: " + lastID);
            }

            //MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            //haslo z pola textfield przekazane jako bajty
            md.update(pass.getBytes());
            byte[] msgDigest = md.digest();

            //konwertowanie na reprezentacje signum
            BigInteger no = new BigInteger(1, msgDigest);

            //konwertowanie na wartosc hex
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
              hashtext = "0" + hashtext;
            }

            //nadpisanie zmiennej pass
            pass = hashtext;
            //System.out.println("<Rejestracja> - szyfrowane haslo: " + pass);

            PreparedStatement pst = c.prepareStatement(
                "INSERT INTO \"Uzytkownik\" VALUES (?,?,?)");
            pst.setInt(1, lastID + 1);
            pst.setString(2, nick);
            pst.setString(3, pass);

            pst.executeUpdate();
            //System.out.println("Powinien wstawic: " + (lastID+1) + ", " + nick + ", " + pass);

            pst.close();
            rs.close();
            st.close();
            c.close();

            przejdzDoStartScreen(event);
            //alert na zarejestrowanie
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Rejestracja");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(
                new Image(this.getClass().getResource("resources/favicon.jpg").toString()));
            alert.setHeaderText(null);
            alert.setContentText("Użytkownik zarejestrowany.");
            alert.showAndWait();
          }

        }
      } catch (SQLException | NoSuchAlgorithmException e) {
        //System.out.println("Blad podczas przetwarzania danych:" + e);
      }
    } else {
      //System.out.println("Brak polaczenia z baza, dalsza czesc aplikacji nie jest wykonywana.");
    }
  }
}
