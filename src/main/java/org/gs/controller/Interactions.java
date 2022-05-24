package org.gs.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.locks.LockSupport;
import java.awt.Desktop;

import org.gs.model.Prodotti;
import org.gs.model.Utenti;
import org.gs.model.Vendite;
import org.gs.service.Service;

public class Interactions {
    
    public Interactions(){}

    public static void comandMenu (ArrayList<Prodotti> listaProdotti, ArrayList<Utenti> listaUtenti, ArrayList<Vendite> listaVendite) {

        Service service = new Service();
        Scanner scanner = new Scanner(System.in);

        boolean loop = true;

        while(loop == true){

            System.out.println();
            System.out.println("Inserisci il numero del comando da eseguire:");
            System.out.println("1 Visualizzare tutti i prodotti all' interno del sistema");
            System.out.println("2 Comprare un prodotto esistente");
            System.out.println("3 Restituire un prodotto");
            System.out.println("4 Aggiungere un nuovo utente");
            System.out.println("5 Esportare un file con i prodotti disponibili");
            System.out.println("0 Uscire dal programma");

            String comando = scanner.nextLine();

            switch (comando) {
                case "1":
                service.getListaProdotti(listaProdotti);
                break;

                case "2":
                buyProduct(listaProdotti, listaUtenti , listaVendite);
                break;

                case "3":
                giveBackProduct(listaProdotti, listaVendite);
                break;

                case "4":
                addUser(listaUtenti);
                break;

                case "5":
                exportAvailableProducts(listaProdotti);
                break;

                case "0":
                System.out.println("Arrivederci!");
                loop = false;
                break;

                default:
                System.out.println("Devi selezionare uno dei comandi!");
            }

        };

        
    }

    //Comand 2 
    public static void buyProduct (ArrayList<Prodotti> listaProdotti, ArrayList<Utenti> listaUtenti, ArrayList<Vendite> listaVendite){

        Service service = new Service();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Inserisci il tuo id");
        int idUtente = scanner.nextInt();
        scanner.nextLine();
        if(idUtente < 0 || idUtente > listaUtenti.size()){ // check if the id entered is present in the data
            System.out.println("ID Utente non esistente!");
        }else{
            System.out.println("Inserisci l' id del prodotto che vuoi acquistare");
            int idProdotto = scanner.nextInt();
            scanner.nextLine();

            if(idProdotto < 0 || idProdotto > listaProdotti.size()){ // check if the product id entered is present in the data
                System.out.println("ID Prodotto non esistente!");
            }else{
                
                if (listaProdotti.get(idProdotto - 1).getDisponibile().equals("SI")){
                    listaProdotti.get(idProdotto - 1).setDisponibile("NO");

                    Vendite vendita = new Vendite(listaVendite.size() + 1, idProdotto, idUtente); 
                    listaVendite.add(vendita);
                    //System.out.println();
                    //service.getListaVendite(listaVendite);

                    System.out.println("Prodotto: " + listaProdotti.get(idProdotto - 1).getNome() + " acquistato corretamente.");

                }else{
                    System.out.println("Il prodotto non è disponibile");
                }
            }
        }
        

        

        
        
    }

    //Comand 3
    public static void giveBackProduct(ArrayList<Prodotti> listaProdotti, ArrayList<Vendite> listaVendite){

        Scanner scanner = new Scanner(System.in);
        Service service = new Service();

        System.out.println("Inserisci l' id del tuo acquisto"); 
        int idVendita = scanner.nextInt();
        scanner.nextLine();

        if(idVendita < 0 || idVendita > listaVendite.size()){ // check if the entered sales id is present in the data
            System.out.println("ID Acquisto non esistente!");
        }else{

            int idProdotto= listaVendite.get(idVendita-1).getIdProdotto();

            if (listaProdotti.get(idProdotto - 1).getDisponibile().equals("NO")){
                listaProdotti.get(idProdotto - 1).setDisponibile("SI");
            }
            
            listaVendite.remove(idVendita - 1); 

            System.out.println("Prodotto restituito correttamente.");

            //service.getListaProdotti(listaProdotti);
            //service.getListaVendite(listaVendite);

        }

        
    }

    //Comand 4
    public static void addUser(ArrayList<Utenti> listaUtenti){

        Scanner scanner = new Scanner(System.in);
        Service service = new Service();

        int id = listaUtenti.size() + 1;
        System.out.println("L' id di questo utente sarà: " + id);

        System.out.println("Inserisci il nome");
        String nome = scanner.nextLine();

        System.out.println("Inserisci il cognome");
        String cognome = scanner.nextLine();

        System.out.println("Inserisci la data di nascita");
        String dataDiNascita = scanner.nextLine();

        System.out.println("Inserisci l' indirizzo");
        String indirizzo = scanner.nextLine();

        System.out.println("Inserisci il documento ID");
        String documentoId = scanner.nextLine();

        Utenti utente = new Utenti(id, nome, cognome, dataDiNascita, indirizzo, documentoId);

        listaUtenti.add(utente);

        System.out.println("Utente inserito corretamente.");

        //service.getListaUtenti(listaUtenti); 

    }

    //Comand 5
    public static void exportAvailableProducts(ArrayList<Prodotti> listaProdotti){

        //creation of an array containing only the available products
        ArrayList<Prodotti> listaProdottiDisponibili = new ArrayList<>(); 

        for(int i = 0; i < listaProdotti.size(); i++ ){
            if (listaProdotti.get(i).getDisponibile().equals("SI")){
                listaProdottiDisponibili.add(listaProdotti.get(i));
            }
        }

        try {

            //Write in the file
            FileWriter writer = new FileWriter("prodottiDisponibili.csv");

            writer.write("Id;Nome;Data Inserimento;Prezzo;Marca;Disponibile\n");

            for(int i = 0; i < listaProdottiDisponibili.size(); i++ ){
                    writer.write(
                        listaProdottiDisponibili.get(i).getId() + ";" +
                        listaProdottiDisponibili.get(i).getNome() + ";" +
                        listaProdottiDisponibili.get(i).getDataInserimento() + ";" +
                        listaProdottiDisponibili.get(i).getPrezzo() + ";" +
                        listaProdottiDisponibili.get(i).getMarca() + ";" +
                        listaProdottiDisponibili.get(i).getDisponibile() + "\n" 
                    );
            }
            
            writer.close();

            //Desktop.getDesktop().open(new File("prodottiDisponibili.csv")); //simple mode

            //Open the file
            File file = new File("prodottiDisponibili.csv");
            //check if Desktop is supported by Platform or not
            if(!Desktop.isDesktopSupported()){
                System.out.println("Desktop is not supported");
            }
            
            Desktop desktop = Desktop.getDesktop();
            if(file.exists()) desktop.open(file);


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
}
