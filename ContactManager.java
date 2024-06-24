import java.io.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ContactManager {
    private static final String FILE_NAME = "contacts.ser";
    private ArrayList<Contact> contacts;
    private Scanner scanner;

    public ContactManager() {
        contacts = loadContacts();
        scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        ContactManager manager = new ContactManager();
        manager.run();
    }

    private void run() {
        boolean running = true;
        while (running) {
            System.out.println("\nContact Manager");
            System.out.println("1. Add a new contact");
            System.out.println("2. View contacts");
            System.out.println("3. Edit a contact");
            System.out.println("4. Delete a contact");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int choice = getIntInput();
            scanner.nextLine(); // Consume leftover newline

            switch (choice) {
                case 1:
                    addContact();
                    break;
                case 2:
                    viewContacts();
                    break;
                case 3:
                    editContact();
                    break;
                case 4:
                    deleteContact();
                    break;
                case 5:
                    running = false;
                    saveContacts();
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void addContact() {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter phone number: ");
        String phoneNumber = scanner.nextLine();
        System.out.print("Enter email address: ");
        String emailAddress = scanner.nextLine();

        contacts.add(new Contact(name, phoneNumber, emailAddress));
        System.out.println("Contact added.");
    }

    private void viewContacts() {
        if (contacts.isEmpty()) {
            System.out.println("No contacts available.");
        } else {
            for (int i = 0; i < contacts.size(); i++) {
                System.out.println((i + 1) + ". " + contacts.get(i));
            }
        }
    }

    private void editContact() {
        System.out.print("Enter the contact number to edit: ");
        int contactNumber = getIntInput();
        scanner.nextLine(); // Consume leftover newline

        if (contactNumber > 0 && contactNumber <= contacts.size()) {
            Contact contact = contacts.get(contactNumber - 1);
            System.out.print("Enter new name (leave blank to keep current): ");
            String name = scanner.nextLine();
            System.out.print("Enter new phone number (leave blank to keep current): ");
            String phoneNumber = scanner.nextLine();
            System.out.print("Enter new email address (leave blank to keep current): ");
            String emailAddress = scanner.nextLine();

            if (!name.isBlank()) {
                contact.setName(name);
            }
            if (!phoneNumber.isBlank()) {
                contact.setPhoneNumber(phoneNumber);
            }
            if (!emailAddress.isBlank()) {
                contact.setEmailAddress(emailAddress);
            }

            System.out.println("Contact updated.");
        } else {
            System.out.println("Invalid contact number.");
        }
    }

    private void deleteContact() {
        System.out.print("Enter the contact number to delete: ");
        int contactNumber = getIntInput();
        scanner.nextLine(); // Consume leftover newline

        if (contactNumber > 0 && contactNumber <= contacts.size()) {
            contacts.remove(contactNumber - 1);
            System.out.println("Contact deleted.");
        } else {
            System.out.println("Invalid contact number.");
        }
    }

    @SuppressWarnings("unchecked")
    private ArrayList<Contact> loadContacts() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return (ArrayList<Contact>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    private void saveContacts() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(contacts);
            System.out.println("Contacts saved.");
        } catch (IOException e) {
            System.out.println("Error saving contacts: " + e.getMessage());
        }
    }

    private int getIntInput() {
        while (true) {
            try {
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.print("Invalid input. Please enter a number: ");
                scanner.next(); // Consume the invalid input
            }
        }
    }
}