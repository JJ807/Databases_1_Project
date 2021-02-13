package Filmoholik;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;


/**
 * Klasa reprezentuajca formularz logowania, bedaca controllerem dla pliku Logowanie.fxml
 */
public class Logowanie {

  /**
   * Pole tekstowe na nazwe uzytkownika
   */
  @FXML
  private TextField log_nick;

  /**
   * Pole tekstowe na haslo uzytkownika
   */
  @FXML
  private TextField log_haslo;

  /**
   * Etykieta wyswietlana gdy nazwa uzytkownika jest bledna
   */
  @FXML
  private Label log_error;

  /**
   * Etykieta wyswietlana gdy haslo jest bledne
   */
  @FXML
  private Label log_error_pass;

  /**
   * Pomocnicza metoda odpowiadajaca za przejscie do glownej sceny programu, wykorzystuje do tego funkcje z klasy PrzejdzStart
   * @param event klikniecie przycisku
   * @throws IOException wyjatek wyrzucany gdy funkcja load() z klasy FXMLLoader nie zadziala poprawnie
   */
  public void przejdzDoStartScreen(ActionEvent event) throws IOException {
    PrzejdzStart obj = new PrzejdzStart();
    obj.przejdzDoStartScreen(event);
  }

  /**
   * Loguje uzytkownika, udostepniajac mu tym samym przyciski z mozliwoscia dodawania rekordow do bazy
   * @param event klikniecie przycisku Zaloguj
   */
  public void zaloguj(ActionEvent event) {
    Connection c = null;

    String nick = log_nick.getText();
    String pass = log_haslo.getText();

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

        String idUzytkownika=null;

        String statement = "SELECT id_uzytkownik, nazwa_uzytkownika, haslo FROM \"Uzytkownik\" where nazwa_uzytkownika=? and haslo=?";

            PreparedStatement lookFor = c
            .prepareStatement(statement,
                ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE);

            //pseudo deszyfrowanie MD5
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
        //System.out.println("<Logowanie> - szyfrowane haslo: " + pass);

        lookFor.setString(1, nick);
        lookFor.setString(2, pass);
        ResultSet returnedData = lookFor.executeQuery();
        if (!returnedData.next()) {
          log_error_pass.setText("Błędna nazwa użytkownika lub hasło. Spróbuj ponownie.");
          log_error_pass.setVisible(true);
          flag = true;
        }else {
          returnedData.previous();
          returnedData.next();
          ResultSetMetaData rsmeta = returnedData.getMetaData();
          idUzytkownika = returnedData.getString(rsmeta.getColumnName(1));
          //System.out.println("Zapytanie zwrocilo idUzytkownika: " + idUzytkownika);
        }
        returnedData.close();
        lookFor.close();

        //jezeli takiego uzytkownika nie ma - zamknij polaczenie
        //jezeli jest - wykonaj pozostale instrukcje
        if (flag) {
          c.close();
        } else {

          log_error.setVisible(false);
          log_error_pass.setVisible(false);

          // 1. zapisz info w klasie uzytkownika
          Controller.user = new User(idUzytkownika, true);

          //zakmniecie polaczenia
          c.close();

          przejdzDoStartScreen(event);
          //alert na zalogowanie
          Alert alert = new Alert(AlertType.INFORMATION);
          alert.setTitle("Logowanie");
          Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
          stage.getIcons().add(
              new Image(this.getClass().getResource("resources/favicon.jpg").toString()));
          alert.setHeaderText(null);
          alert.setContentText("Użytkownik zalogowany.\nWłączona możliwość dodawania rekordów.");
          alert.showAndWait();
          //System.out.println("Przeslalem dane z logowania.");

        }
      } catch (SQLException e) {
        //System.out.println("Blad podczas przetwarzania danych:" + e);
      }
      catch (IOException e) {
        //System.err.printf("Error: %s%n", e.getMessage());
      } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
      }
    } else {
      //System.out.println("Brak polaczenia z baza, dalsza czesc aplikacji nie jest wykonywana.");
    }
  }

}
