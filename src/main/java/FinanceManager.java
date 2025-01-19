package org.example;

import java.io.*;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

public class FinanceManager {
    private Map<String, User> users;
    private Scanner scanner;

    public FinanceManager() {
        this.users = new HashMap<>();
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        loadData();
        while (true) {
            System.out.println("1. Регистрация");
            System.out.println("2. Авторизация");
            System.out.println("3. Выход");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    registerUser();
                    break;
                case 2:
                    authorizeUser();
                    break;
                case 3:
                    saveData();
                    return;
                default:
                    System.out.println("Некорректный выбор");
            }
        }
    }


    private void registerUser() {
        System.out.print("Введите логин: ");
        String login = scanner.next();
        if (login.isEmpty()) {
            System.out.println("Логин не может быть пустым");
            return;
        }
        if (users.containsKey(login)) {
            System.out.println("Пользователь с таким логином уже существует");
            return;
        }
        System.out.print("Введите пароль: ");
        String password = scanner.next();
        if (password.isEmpty()) {
            System.out.println("Пароль не может быть пустым");
            return;
        }
        if (password.length() < 8) {
            System.out.println("Пароль должен быть не менее 8 символов");
            return;
        }
        User user = new User(login, password);
        users.put(login, user);
        System.out.println("Пользователь зарегистрирован");
    }

    private void authorizeUser() {
        System.out.print("Введите логин: ");
        String login = scanner.next();
        if (!users.containsKey(login)) {
            System.out.println("Пользователь с таким логином не найден");
            return;
        }
        System.out.print("Введите пароль: ");
        String password = scanner.next();
        if (!users.get(login).getPassword().equals(password)) {
            System.out.println("Некорректный пароль");
            return;
        }
        System.out.println("Авторизация успешна");
        manageFinances(users.get(login));
    }


    private void manageFinances(User user) {
        while (true) {
            System.out.println("1. Добавить доход");
            System.out.println("2. Добавить расход");
            System.out.println("3. Установить бюджет");
            System.out.println("4. Вывести информацию");
            System.out.println("5. Управление категориями");
            System.out.println("6. Назад");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    addIncome(user);
                    break;
                case 2:
                    addExpense(user);
                    break;
                case 3:
                    setBudget(user);
                    break;
                case 4:
                    printInfo(user);
                    break;
                case 5:
                    manageCategories(user);
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Некорректный выбор");
            }
        }
    }


    private void addIncome(User user) {
        System.out.print("Введите сумму дохода: ");
        double amount;
        while (true) {
            try {
                amount = scanner.nextDouble();
                if (amount <= 0) {
                    System.out.println("Сумма должна быть положительной");
                } else {
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Некорректный формат суммы");
                scanner.next(); // Очистка буфера
            }
        }
        System.out.print("Введите категорию: ");
        String category = scanner.next();
        user.getWallet().addTransaction(new Transaction(amount, category, true));
        System.out.println("Доход добавлен");
    }

    private void addExpense(User user) {
        System.out.print("Введите сумму расхода: ");
        double amount;
        while (true) {
            try {
                amount = scanner.nextDouble();
                if (amount <= 0) {
                    System.out.println("Сумма должна быть положительной");
                } else {
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Некорректный формат суммы");
                scanner.next(); // Очистка буфера
            }
        }
        System.out.print("Введите категорию: ");
        String category = scanner.next();
        user.getWallet().addTransaction(new Transaction(amount, category, false));
        if (user.getWallet().getBudgets().containsKey(category) && getExpensesByCategory(user, category) > user.getWallet().getBudgets().get(category).getLimit()) {
            System.out.println("Превышен лимит бюджета по категории " + category);
        }
        if (getTotalExpenses(user) > getTotalIncome(user)) {
            System.out.println("Расходы превысили доходы");
        }
        System.out.println("Расход добавлен");
    }




    private void setBudget(User user) {
        System.out.print("Введите категорию: ");
        String category = scanner.next();
        System.out.print("Введите бюджет: ");
        double budget = scanner.nextDouble();

        user.getWallet().setBudget(category, new Budget(budget));
    }

    private void printInfo(User user) {
        System.out.println("1. Вывести информацию в терминал");
        System.out.println("2. Вывести информацию в файл");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                printInfoToConsole(user);
                break;
            case 2:
                printInfoToFile(user);
                System.out.println("Информация сохранена в файл finance_info.txt");
                break;
            default:
                System.out.println("Некорректный выбор");
        }
    }

    private void printInfoToConsole(User user) {
        System.out.println("Общий доход: " + getTotalIncome(user));
        System.out.println("Общие расходы: " + getTotalExpenses(user));
        System.out.println("Бюджет по категориям:");
        for (Map.Entry<String, Budget> entry : user.getWallet().getBudgets().entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue().getLimit() + ", Оставшийся бюджет: " + (entry.getValue().getLimit() - getExpensesByCategory(user, entry.getKey())));
        }
        System.out.println("Доходы по категориям:");
        for (String category : user.getWallet().getCategories()) {
            System.out.println(category + ": " + getIncomeByCategory(user, category));
        }
        System.out.println("Расходы по категориям:");
        for (String category : user.getWallet().getCategories()) {
            System.out.println(category + ": " + getExpensesByCategory(user, category));
        }
    }

    private double getIncomeByCategory(User user, String category) {
        double total = 0;
        for (Transaction transaction : user.getWallet().getTransactions()) {
            if (transaction.isIncome() && transaction.getCategory().equals(category)) {
                total += transaction.getAmount();
            }
        }
        return total;
    }

    private double getTotalIncome(User user) {
        double total = 0;
        for (Transaction transaction : user.getWallet().getTransactions()) {
            if (transaction.isIncome()) {
                total += transaction.getAmount();
            }
        }
        return total;
    }

    private double getTotalExpenses(User user) {
        double total = 0;
        for (Transaction transaction : user.getWallet().getTransactions()) {
            if (!transaction.isIncome()) {
                total += transaction.getAmount();
            }
        }
        return total;
    }

    private void manageCategories(User user) {
        while (true) {
            System.out.println("1. Добавить категорию");
            System.out.println("2. Удалить категорию");
            System.out.println("3. Просмотреть категории");
            System.out.println("4. Назад");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    addCategory(user);
                    break;
                case 2:
                    removeCategory(user);
                    break;
                case 3:
                    printCategories(user);
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Некорректный выбор");
            }
        }
    }

    private void addCategory(User user) {
        System.out.print("Введите название категории: ");
        String category = scanner.next();
        if (category.isEmpty()) {
            System.out.println("Название категории не может быть пустым");
            return;
        }
        if (user.getWallet().getCategories().contains(category)) {
            System.out.println("Категория с таким названием уже существует");
            return;
        }
        user.getWallet().addCategory(category);
        System.out.println("Категория добавлена");
    }

    private void removeCategory(User user) {
        System.out.print("Введите название категории для удаления: ");
        String category = scanner.next();
        if (!user.getWallet().getCategories().contains(category)) {
            System.out.println("Категория не найдена");
            return;
        }
        user.getWallet().removeCategory(category);
        System.out.println("Категория удалена");
    }


    private void printCategories(User user) {
        System.out.println("Список категорий:");
        for (String category : user.getWallet().getCategories()) {
            System.out.println(category);
        }
    }

    private double getExpensesByCategory(User user, String category) {
        double total = 0;
        for (Transaction transaction : user.getWallet().getTransactions()) {
            if (!transaction.isIncome() && transaction.getCategory().equals(category)) {
                total += transaction.getAmount();
            }
        }
        return total;
    }

    private void printInfoToFile(User user) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("finance_info.txt"))) {
            writer.write("Общий доход: " + getTotalIncome(user) + "\n");
            writer.write("Общие расходы: " + getTotalExpenses(user) + "\n");
            writer.write("Бюджет по категориям:\n");
            for (Map.Entry<String, Budget> entry : user.getWallet().getBudgets().entrySet()) {
                writer.write(entry.getKey() + ": " + entry.getValue().getLimit() + ", Оставшийся бюджет: " + (entry.getValue().getLimit() - getExpensesByCategory(user, entry.getKey())) + "\n");
            }
            writer.write("Доходы по категориям:\n");
            for (String category : user.getWallet().getCategories()) {
                writer.write(category + ": " + getIncomeByCategory(user, category) + "\n");
            }
            writer.write("Расходы по категориям:\n");
            for (String category : user.getWallet().getCategories()) {
                writer.write(category + ": " + getExpensesByCategory(user, category) + "\n");
            }
        } catch (IOException e) {
            System.out.println("Ошибка при записи в файл: " + e.getMessage());
        }
    }

    private void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("users.dat"))) {
            oos.writeObject(users);
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении данных: " + e.getMessage());
        }
    }

    private void loadData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("users.dat"))) {
            users = (Map<String, User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Ошибка при загрузке данных: " + e.getMessage());
            users = new HashMap<>(); // Инициализация пустой карты, если файл не найден
        }
    }



}
