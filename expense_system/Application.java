///////////////////////////////////////////////////////////////////////////////
//                                                                           //
//      MOD003484 - Assignment 011 (element design and implementation)       //
//                                                                           //
//      SID:                    2011781                                      //
//      Team for element 10:    The Holla Scholars                           //
//                                                                           //
///////////////////////////////////////////////////////////////////////////////

package expense_system;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * The Application class provides an interface for users to interact with the expense system.
 */
class Application {

    // FIELDS
    private List<Team> teams;
    private List<User> users;
    private List<Expense> expenses;
    private User currentUser;
    private boolean userValidated;
    private Csv teamsCsv;
    private Csv usersCsv;
    private Csv expensesCsv;

    // CONSTRUCTOR
    public Application() {
        this.teams = new ArrayList<>();
        this.users = new ArrayList<>();
        this.expenses = new ArrayList<>();
        this.currentUser = null;
        this.userValidated = false;
        this.teamsCsv = null;
        this.usersCsv = null;
        this.expensesCsv = null;
    }

    // METHODS

    // getters
    public User getCurrentUser() {
        return this.currentUser;
    }
    public List<Team> getTeams() {
        return this.teams;
    }
    public List<User> getUsers() {
        return this.users;
    }
    public List<Expense> getExpenses() {
        return this.expenses;
    }

    // Other methods

    // Get Team by name
    public Team getTeamByName(String teamName) {
        for (Team team : this.teams) {
            if (team.getTeamName().equals(teamName)) {
                return team;
            }
        }
        return null;
    }

    // Get User by name
    public User getUserByName(String username) {
        for (User user : this.users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    // Add a team record
    public void addTeam(Team team) {
        this.teams.add(team);
    }
    
    // Add a user record
    public void addUser(User user) {
        this.users.add(user);
    }

    // Add an expense record
    public void addExpense(Expense expense) {
        this.expenses.add(expense);
    }

    // METHODS READING FROM (AND WRITING TO) CSV FILES

    // Populates the `teams` attribute of the Application
    private void addTeamsFromCsv(String filepath) throws IOException {
        this.teamsCsv = new Csv(filepath);

        for (String[] row : this.teamsCsv.getData()) {
            String teamName = row[0];
            // Convert the budget text to a Float
            Float teamBudget = Float.parseFloat(row[1]);
            // Make a new Team instance
            Team team = new Team(teamName, teamBudget);
            // Add it to the Application's list of teams
            this.teams.add(team);
        }
    }

    // Populates the `users` attribute of the Application
    private void addUsersFromCsv(String filepath) throws IOException {
        this.usersCsv = new Csv(filepath);

        for (String[] row : this.usersCsv.getData()) {
            String username = row[0];
            String password = row[1];
            String teamName = row[2];

            // Find the user's team by name
            Team team = this.getTeamByName(teamName);
            // Should raise an error here if the team isn't found(?)
            // Create the new User instance
            User user = new User(username, password, team);
            // Add it to the Application's list of users
            this.users.add(user);
        }
    }

    // Populates the `expenses` attribute of the Application
    private void addExpensesFromCsv(String filepath) throws IOException, ParseException {
        this.expensesCsv = new Csv(filepath);

        for (String[] row : this.expensesCsv.getData()) {
            Double amount = Double.parseDouble(row[0]);
            String description = row[1];
            String stringDate = row[2];
            String username = row[3];

            // Find the user by name
            // Should raise an error here if the team isn't found(?)
            User user = this.getUserByName(username);
            // Create an Expense instance
            Expense expense = new Expense(amount, description, stringDate, user);
            // Add it to the Application's list of expenses
            this.expenses.add(expense);
            // Record expense against team budget
            user.getTeam().getBudget().recordExpense(expense);
        }
    }

    // METHODS RELATING TO USER INTERACTIONS WITH THE APPLICATION

    // Returns the user's input
    public String takeUserInput(String message) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(message);
        return scanner.nextLine();
    }

    // Login method
    public void takeUserCredentials(){
        do {
            String username = takeUserInput("Please enter your username: ");
            String password = takeUserInput("Please enter your password: ");

            this.validateUser(username, password);
        }
        // Keep asking until we are validated
        while (!this.userValidated);
    }

    // Check login credentials
    private void validateUser(String username, String password) {

        // See if the Application knows a user of that name
        User user = this.getUserByName(username);
        String failMessage = "Username and/or password are incorrect, please try again.";
        if (user != null) {
            if (user.getPassword().equals(password)) {
                System.out.println("Username and password match");
                System.out.print("Login successful\n\n");
                // If successful, set the current user
                this.currentUser = user;
                // And set the userValidated boolean
                this.userValidated = true;
            } else {
                System.out.println(failMessage);
            }
        } else {
            // Don't give too much away - ie don't confirm user exists, but password is wrong
            System.out.println(failMessage);
        }
    } 

    // Get menu choice
    public int takeUserChoice() {
        Scanner scanner = new Scanner(System.in);
        int userChoice = 0;
        do {
            try {
                System.out.println("Please enter your choice: ");
                userChoice = scanner.nextInt();  
            }
            catch (InputMismatchException e) {
                System.out.println("Invalid type choice. ");
            }
            scanner.nextLine();
        } while (userChoice <= 0);
        return userChoice;
    }

    // Reset user info
    public void resetCurrentUser() {
        this.currentUser = null;
        this.userValidated = false;
    }

    // Update Users CSV data
    public void updateUsersCsvData() {
        List<String[]> updatedData = new ArrayList<>();

        for (User user : this.users) {
            String username = user.getUsername();
            String password = user.getPassword();
            String teamName = user.getTeam().getTeamName();

            String[] dataRow = {username, password, teamName};
            updatedData.add(dataRow);
        }
        this.usersCsv.setData(updatedData);
    }

    // Update Expenses CSV data
    public void updateExpensesCsvData() {
        List<String[]> updatedData = new ArrayList<>();

        for (Expense expense : this.expenses) {
            String stringAmount = Double.toString(expense.getAmount());
            String description = expense.getDescription();
            String stringDate = expense.getDateAsString();
            String username = expense.getUser().getUsername();

            String[] dataRow = {stringAmount, description, stringDate, username};
            updatedData.add(dataRow);
        }
        this.expensesCsv.setData(updatedData);
    }
    
    // Synchronise application Csv objects with the filesystem
    public void writeBackToFiles() throws IOException {
        this.updateUsersCsvData();
        this.usersCsv.writeCsv(this.usersCsv.getFilepath());
        this.updateExpensesCsvData();
        this.expensesCsv.writeCsv(this.expensesCsv.getFilepath());
    }

    public static void main(String[] args) throws ParseException, IOException {

        // Create an Application instance
        Application app = new Application();
        // Add teams and users from CSV files (ie the 'database')
        app.addTeamsFromCsv("Teams.csv");
        app.addUsersFromCsv("Users.csv");
        // Record expenses from CSV file
        app.addExpensesFromCsv("Expenses.csv");
        
        // Create the Application menu
        new Menu(app);
    }
}