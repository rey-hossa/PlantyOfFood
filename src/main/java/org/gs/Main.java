package org.gs;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.gs.controller.Interactions;
import org.gs.model.Prodotti;
import org.gs.model.Utenti;
import org.gs.model.Vendite;
import org.gs.service.Service;


public class Main {

    public static void main(String[] args) {

        Service service = new Service();
        Interactions interactions = new Interactions();

        //transfer the data in the file to an arraylist of arraylist
        ArrayList<ArrayList<String>> prodotti = new ArrayList<>();
        ArrayList<ArrayList<String>> utenti = new ArrayList<>();
        ArrayList<ArrayList<String>> vendite = new ArrayList<>();

        service.FetchDataFromFile(prodotti, "src/main/java/org/gs/files/prodotti.csv");
        service.FetchDataFromFile(utenti, "src/main/java/org/gs/files/utenti.csv");
        service.FetchDataFromFile(vendite, "src/main/java/org/gs/files/vendite.csv");

        //transfer of data from the arraylist of arraylist to an arraylist of objects        
        ArrayList<Prodotti> listaProdotti = new ArrayList<>();
        ArrayList<Utenti> listaUtenti = new ArrayList<>();
        ArrayList<Vendite> listaVendite = new ArrayList<>();

        service.copyInListaProdotti(prodotti, listaProdotti);
        service.copyInListaUtenti(utenti, listaUtenti);
        service.copyInListaVendite(vendite, listaVendite);

        //Comand menu
        interactions.comandMenu(listaProdotti, listaUtenti, listaVendite);
        
    }
}