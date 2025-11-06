import java.util.Scanner;
import java.io.*;

public class AppStock {
    static Scanner scanner = new Scanner(System.in);
    static int maxProduits = 10; 
    static int nbrProduits = 0;
    static int[] codesProduits = new int[maxProduits];
    static String[] nomsProduits = new String[maxProduits];
    static int[] quantitesProduits = new int[maxProduits];
    static double[] prixProduits = new double[maxProduits];

    // ------------------- Sauvegarde et chargement -------------------
    static void sauvegarderStock() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("stock.txt"))) {
            for (int i = 0; i < nbrProduits; i++) {
                writer.println(
                    "__________________" +  "\n" +
                    "-Code:" + " " + codesProduits[i]  + "\n" +
                    "-Nom:" + nomsProduits[i] + "\n" +
                    "-Quantite:" + quantitesProduits[i] + "\n" +
                    "-Prix:" +  " " + prixProduits[i]
                );
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la sauvegarde: " + e.getMessage());
        }
    }

    static void chargerStock() {
        try (BufferedReader reader = new BufferedReader(new FileReader("stock.txt"))) {
            String line;
            nbrProduits = 0;
            while ((line = reader.readLine()) != null && nbrProduits < maxProduits) {
                String[] parts = line.split(";");
                codesProduits[nbrProduits] = Integer.parseInt(parts[0]);
                nomsProduits[nbrProduits] = parts[1];
                quantitesProduits[nbrProduits] = Integer.parseInt(parts[2]);
                prixProduits[nbrProduits] = Double.parseDouble(parts[3]);
                nbrProduits++;
            }
        } catch (Exception e) {
            System.out.println("Aucun fichier trouvé ou erreur: " + e.getMessage());
        }
    }

    // ------------------- Gestion des produits -------------------
    static void ajouterProduit() {
        System.out.println("Ajouter un produit...");
        int code;
        do {
            System.out.println("Saisir le code:");
            while (!scanner.hasNextInt()) {
                System.out.println("Erreur : Vous devez saisir un nombre entier !");
                scanner.next();
            }
            code = scanner.nextInt();
            scanner.nextLine();
        } while (existeCode(code));

        System.out.println("Saisir le nom:");
        String nom = scanner.nextLine();

        System.out.println("Saisir la quantité:");
        while (!scanner.hasNextInt()) {
            System.out.println("Quantité invalide !");
            scanner.next();
        }
        int qte = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Saisir le prix:");
        while (!scanner.hasNextDouble()) {
            System.out.println("Prix invalide !");
            scanner.next();
        }
        double prix = scanner.nextDouble();
        scanner.nextLine();

        codesProduits[nbrProduits] = code;
        nomsProduits[nbrProduits] = nom;
        quantitesProduits[nbrProduits] = qte;
        prixProduits[nbrProduits] = prix;

        nbrProduits++;
        sauvegarderStock();
        System.out.println("Produit ajouté !");
    }

    static void modifierProduit() {
        System.out.println("Saisir le code du produit à modifier:");
        int code = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < nbrProduits; i++) {
            if (codesProduits[i] == code) {
                System.out.println("Saisir le nouveau nom:");
                nomsProduits[i] = scanner.nextLine();

                System.out.println("Saisir la nouvelle quantité:");
                while (!scanner.hasNextInt()) {
                    System.out.println("Quantité invalide !");
                    scanner.next();
                }
                quantitesProduits[i] = scanner.nextInt();
                scanner.nextLine();

                System.out.println("Saisir le nouveau prix:");
                while (!scanner.hasNextDouble()) {
                    System.out.println("Prix invalide !");
                    scanner.next();
                }
                prixProduits[i] = scanner.nextDouble();
                scanner.nextLine();

                sauvegarderStock();
                System.out.println("Produit modifié !");
                return;
            }
        }
        System.out.println("Produit non trouvé !");
    }

    static void deleteProduit() {
        System.out.println("Saisir le code à supprimer:");
        int code = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < nbrProduits; i++) {
            if (codesProduits[i] == code) {
                for (int j = i; j < nbrProduits - 1; j++) {
                    codesProduits[j] = codesProduits[j + 1];
                    nomsProduits[j] = nomsProduits[j + 1];
                    quantitesProduits[j] = quantitesProduits[j + 1];
                    prixProduits[j] = prixProduits[j + 1];
                }
                nbrProduits--;
                sauvegarderStock();
                System.out.println("Produit supprimé !");
                return;
            }
        }
        System.out.println("Produit non trouvé !");
    }

    static void printProduits() {
        if (nbrProduits == 0) {
            System.out.println("Aucun produit dans le stock.");
            return;
        }
        System.out.println("Liste des produits : (" + nbrProduits + ")");
        System.out.printf("%-10s%-20s %-10s %-10s%n", "Code", "Nom", "Quantité", "Prix");
        System.out.println("-----------------------------------------------");

        for (int i = 0; i < nbrProduits; i++) {
            System.out.printf("%-10d%-20s %-10d %-10.2f%n", codesProduits[i], nomsProduits[i],
                    quantitesProduits[i], prixProduits[i]);
        }
    }

    static void rechercherProduit() {
        System.out.println("Saisir le code à rechercher:");
        int code = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < nbrProduits; i++) {
            if (codesProduits[i] == code) {
                System.out.println("Produit trouvé : ");
                System.out.printf("Code: %d, Nom: %s, Quantité: %d, Prix: %.2f%n",
                        codesProduits[i], nomsProduits[i], quantitesProduits[i], prixProduits[i]);
                return;
            }
        }
        System.out.println("Produit non trouvé !");
    }

    static void valeurTotaleStock() {
        double total = 0;
        for (int i = 0; i < nbrProduits; i++) {
            total += quantitesProduits[i] * prixProduits[i];
        }
        System.out.printf("Valeur totale du stock: %.2f%n", total);
    }

    static boolean existeCode(int code) {
        for (int i = 0; i < nbrProduits; i++) {
            if (codesProduits[i] == code) {
                System.out.println("Produit existant!!!");
                return true;
            }
        }
        return false;
    }

    static void printMenu() {
        System.out.println("----- Gestion de Stock ----- ");
        System.out.println("1. Ajouter un produit");
        System.out.println("2. Modifier un produit");
        System.out.println("3. Supprimer un produit");
        System.out.println("4. Afficher la liste des produits");
        System.out.println("5. Rechercher un produit");
        System.out.println("6. Calculer la valeur totale du stock");
        System.out.println("0. Quitter");
        System.out.println("Choisissez une option :");
    }

    // ------------------- Main -------------------
    public static void main(String[] args) {
        chargerStock(); // charge les produits depuis stock.txt au démarrage
        int choix;
        do {
            printMenu();
            while (!scanner.hasNextInt()) {
                System.out.println("Veuillez saisir un nombre valide !");
                scanner.next();
            }
            choix = scanner.nextInt();
            scanner.nextLine();
            switch (choix) {
                case 1 -> ajouterProduit();
                case 2 -> modifierProduit();
                case 3 -> deleteProduit();
                case 4 -> printProduits();
                case 5 -> rechercherProduit();
                case 6 -> valeurTotaleStock();
                case 0 -> System.out.println("Au revoir !");
                default -> System.out.println("Option invalide !");
            }
        } while (choix != 0);
    }
}
