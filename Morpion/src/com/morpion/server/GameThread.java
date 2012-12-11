package com.morpion.server;

import java.net.*;
import java.io.*;

//** Classe associ�e � chaque client **
//** Il y aura autant d'instance de cette classe que de client connect�s **
//impl�mentation de l'interface Runnable (une des 2 m�thodes pour cr�er un thread)
class GameThread implements Runnable
{
  private Thread t; // contiendra le thread du client
  private Socket s; // recevra le socket liant au client
  private PrintWriter out; // pour gestion du flux de sortie
  private BufferedReader in; // pour gestion du flux d'entr�e
  private MainServeur mainServeur; // pour utilisation des m�thodes de la classe principale
  private int numClient=0; // contiendra le num�ro de client g�r� par ce thread
  private Game game;
  private int etat=0; //o = initial; 2=en game; 3=en specat; 1=en attente 2eme joueur

  //** Constructeur : cr�e les �l�ments n�cessaires au dialogue avec le client **
  public GameThread(Socket s, MainServeur mainServeur) // le param s est donn�e dans BlablaServ par ss.accept()
  {
    this.mainServeur=mainServeur; // passage de local en global (pour gestion dans les autres m�thodes)
    this.s=s; // passage de local en global
	
    try
    {
      // fabrication d'une variable permettant l'utilisation du flux de sortie avec des string
      out = new PrintWriter(s.getOutputStream());
      // fabrication d'une variable permettant l'utilisation du flux d'entr�e avec des string
      in = new BufferedReader(new InputStreamReader(s.getInputStream()));
      // ajoute le flux de sortie dans la liste et r�cup�ration de son numero
      numClient = mainServeur.addClient(out);
    }
    catch (IOException e){ }

    t = new Thread(this); // instanciation du thread
    t.start(); // demarrage du thread, la fonction run() est ici lanc�e
  }

  //** Methode :  ex�cut�e au lancement du thread par t.start() **
  //** Elle attend les messages en provenance du serveur et les redirige **
  // cette m�thode doit obligatoirement �tre impl�ment�e � cause de l'interface Runnable
  public void run()
  {
    String message = ""; // d�claration de la variable qui recevra les messages du client
    // on indique dans la console la connection d'un nouveau client
    System.out.println("Un nouveau client s'est connecte, no "+numClient);
    try
    {
      // la lecture des donn�es entrantes se fait caract�re par caract�re ...
      // ... jusqu'� trouver un caract�re de fin de chaine
      char charCur[] = new char[1]; // d�claration d'un tableau de char d'1 �lement, _in.read() stockera y le char lu
      while(in.read(charCur, 0, 1)!=-1) // attente en boucle des messages provenant du client (bloquant sur _in.read())
      {
      	// on regarde si on arrive � la fin d'une chaine ...
        if (charCur[0] != '\u0000' && charCur[0] != '\n' && charCur[0] != '\r')
                message += charCur[0]; // ... si non, on concat�ne le caract�re dans le message
        else if(!message.equalsIgnoreCase("")) // juste une v�rification de principe
        {
          if(charCur[0]=='\u0000') // le dernier caract�re �tait '\u0000' (char de terminaison nulle)
          	// on envoi le message en disant qu'il faudra concat�ner '\u0000' lors de l'envoi au client
            mainServeur.sendAll(message,""+charCur[0]);
          else mainServeur.sendAll(message,""); // sinon on envoi le message � tous
          message = ""; // on vide la chaine de message pour qu'elle soit r�utilis�e
        }
      }
    }
    catch (Exception e){ }
    finally // finally se produira le plus souvent lors de la deconnexion du client
    {
      try
      {
      	// on indique � la console la deconnexion du client
        System.out.println("Le client no "+numClient+" s'est deconnecte");
        mainServeur.delClient(numClient); // on supprime le client de la liste
        s.close(); // fermeture du socket si il ne l'a pas d�j� �t� (� cause de l'exception lev�e plus haut)
      }
      catch (IOException e){ }
    }
  }
}
