package com.morpion.server;

import java.net.*;
import java.io.*;

//** Classe associée à chaque client **
//** Il y aura autant d'instance de cette classe que de client connectés **
//implémentation de l'interface Runnable (une des 2 méthodes pour créer un thread)
class GameThread implements Runnable
{
  private Thread t; // contiendra le thread du client
  private Socket s; // recevra le socket liant au client
  private PrintWriter out; // pour gestion du flux de sortie
  private BufferedReader in; // pour gestion du flux d'entrée
  private MainServeur mainServeur; // pour utilisation des méthodes de la classe principale
  private int numClient=0; // contiendra le numéro de client géré par ce thread
  private Game game;
  private int etat=0; //o = initial; 2=en game; 3=en specat; 1=en attente 2eme joueur

  //** Constructeur : crée les éléments nécessaires au dialogue avec le client **
  public GameThread(Socket s, MainServeur mainServeur) // le param s est donnée dans BlablaServ par ss.accept()
  {
    this.mainServeur=mainServeur; // passage de local en global (pour gestion dans les autres méthodes)
    this.s=s; // passage de local en global
	
    try
    {
      // fabrication d'une variable permettant l'utilisation du flux de sortie avec des string
      out = new PrintWriter(s.getOutputStream());
      // fabrication d'une variable permettant l'utilisation du flux d'entrée avec des string
      in = new BufferedReader(new InputStreamReader(s.getInputStream()));
      // ajoute le flux de sortie dans la liste et récupération de son numero
      numClient = mainServeur.addClient(out);
    }
    catch (IOException e){ }

    t = new Thread(this); // instanciation du thread
    t.start(); // demarrage du thread, la fonction run() est ici lancée
  }

  //** Methode :  exécutée au lancement du thread par t.start() **
  //** Elle attend les messages en provenance du serveur et les redirige **
  // cette méthode doit obligatoirement être implémentée à cause de l'interface Runnable
  public void run()
  {
    String message = ""; // déclaration de la variable qui recevra les messages du client
    // on indique dans la console la connection d'un nouveau client
    System.out.println("Un nouveau client s'est connecte, no "+numClient);
    try
    {
      // la lecture des données entrantes se fait caractère par caractère ...
      // ... jusqu'à trouver un caractère de fin de chaine
      char charCur[] = new char[1]; // déclaration d'un tableau de char d'1 élement, _in.read() stockera y le char lu
      while(in.read(charCur, 0, 1)!=-1) // attente en boucle des messages provenant du client (bloquant sur _in.read())
      {
      	// on regarde si on arrive à la fin d'une chaine ...
        if (charCur[0] != '\u0000' && charCur[0] != '\n' && charCur[0] != '\r')
                message += charCur[0]; // ... si non, on concatène le caractère dans le message
        else if(!message.equalsIgnoreCase("")) // juste une vérification de principe
        {
          if(charCur[0]=='\u0000') // le dernier caractère était '\u0000' (char de terminaison nulle)
          	// on envoi le message en disant qu'il faudra concaténer '\u0000' lors de l'envoi au client
            mainServeur.sendAll(message,""+charCur[0]);
          else mainServeur.sendAll(message,""); // sinon on envoi le message à tous
          message = ""; // on vide la chaine de message pour qu'elle soit réutilisée
        }
      }
    }
    catch (Exception e){ }
    finally // finally se produira le plus souvent lors de la deconnexion du client
    {
      try
      {
      	// on indique à la console la deconnexion du client
        System.out.println("Le client no "+numClient+" s'est deconnecte");
        mainServeur.delClient(numClient); // on supprime le client de la liste
        s.close(); // fermeture du socket si il ne l'a pas déjà été (à cause de l'exception levée plus haut)
      }
      catch (IOException e){ }
    }
  }
}
