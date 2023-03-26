package UserAuth;

import java.sql.*;
import java.util.Scanner;
import org.mindrot.jbcrypt.BCrypt;

public class UserAuth {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/";
    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "Jed3924KK";

    public static void start() {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            Scanner scanner = new Scanner(System.in);
            boolean running = true;

            while (running) {
                System.out.print("Введите 'Вход', 'Регистрация' или 'Выход': ");
                String command = scanner.nextLine().trim().toLowerCase();

                switch (command) {
                    case "вход":
                        login(connection, scanner);
                        break;
                    case "регистрация":
                        register(connection, scanner);
                        break;
                    case "выход":
                        running = false;
                        break;
                    default:
                        System.out.print("Неверная команда. Повторите ввод.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void login(Connection connection, Scanner scanner) {
        System.out.println("Введите имя пользователя:");
        String username = scanner.nextLine();
        System.out.println("Введите пароль:");
        String password = scanner.nextLine();

        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT password_hash FROM users WHERE name = ?");
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String hashedPassword = rs.getString("password_hash");

                if (BCrypt.checkpw(password, hashedPassword)) {
                    System.out.println("Успешный вход!");
                } else {
                    System.out.println("Неверный пароль.");
                }
            } else {
                System.out.println("Пользователь с таким именем не найден.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void register(Connection connection, Scanner scanner) {
        System.out.println("Введите имя пользователя:");
        String username = scanner.nextLine();
        System.out.println("Введите пароль:");
        String password = scanner.nextLine();
        System.out.println("Подтвердите пароль:");
        String confirmPassword = scanner.nextLine();

        if (!password.equals(confirmPassword)) {
            System.out.println("Пароли не совпадают.");
            return;
        }

        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM users WHERE name = ?");
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("Пользователь с таким именем уже существует.");
            } else {
                String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
                stmt = connection.prepareStatement("INSERT INTO users (name, password_hash) VALUES (?, ?)");
                stmt.setString(1, username);
                stmt.setString(2, hashedPassword);
                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Регистрация успешна!");
                } else {
                    System.out.println("Ошибка регистрации. Повторите попытку.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

