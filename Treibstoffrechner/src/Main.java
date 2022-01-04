import java.sql.*;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {

		try {

			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();

			Connection con = DriverManager.getConnection(
					"jdbc:mysql://localhost/flugzeugtypen?useTimezone=true&serverTimezone=UTC", "root", "root");

			con.setReadOnly(true);

			Statement stmt = con.createStatement();

			ResultSet rs;

			

			// Datengewinnung
			Scanner scan = new Scanner(System.in);
			ResultSet rs2 = stmt.executeQuery("Select * from flugzeugtypendaten");

			int count = 0;

			while (rs2.next()) {
				System.out.println("ID: " + rs2.getInt("ID") + " Typ: " + rs2.getString("Flugzeugtyp"));
				count = rs2.getInt("ID");
			}

			rs2.close();
			int id = 0;

			while (id < 1 || id > count) {
				System.out.print("Geben Sie die gewünschte ID des Flugzeuges an 1-" + count + ": ");
				id = scan.nextInt();
			}

			String wind = "";
			int windrichtung = -1;
			int windstaerke = -1;
			while (wind.split("/").length != 2 || windrichtung < 0 || windrichtung > 360 || windstaerke < 0) {
				System.out.print("Geben Sie die Windstärke an (Grad/Knoten): ");
				wind = scan.next();
				if (wind.split("/").length == 2) {
					windrichtung = Integer.valueOf(wind.split("/")[0]);
					windstaerke = Integer.valueOf(wind.split("/")[1]);
				}
			}

			int steuerkurs = -1;

			while (steuerkurs < 0 || steuerkurs > 360) {
				System.out.print("Bitte geben sie ihren Steuerkurs zum Ziel Flugplatz ein: !");
				steuerkurs = scan.nextInt();
			}

			int distanz = -1;

			while (distanz < 0) {
				System.out.print("Geben Sie die Flugdistanz ein !: ");
				distanz = scan.nextInt();
			}

			int etxmenge = -1;

			while (etxmenge < 0) {
				System.out.print("Bitte geben sie wenn gewünscht eine extra Menge an Treibstoff ein !: ");
				etxmenge = scan.nextInt();
			}

			rs = stmt.executeQuery("Select * from flugzeugtypendaten where ID = " + id);
			rs.next();

			int verbrauch = rs.getInt("Verbrauch");
			int geschwindigkeit = rs.getInt("Geschwindigkeit");

			// Berechnungen

			int taxifuel = 5;

			double windkomponente = windstaerke * Math.cos((windrichtung - steuerkurs)  *(Math.PI/180));

			double endgeschwindigkeit = windkomponente + geschwindigkeit;

			double result = (double) distanz / (1.852d * endgeschwindigkeit) * verbrauch + etxmenge + taxifuel;
			result = ((double) (int) (result * 1000)) / 1000;
			System.out.println("Der Verbrauch beträgt: " + result + "Liter");

			rs.close();
			stmt.close();
			con.close();
			scan.close();

		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
