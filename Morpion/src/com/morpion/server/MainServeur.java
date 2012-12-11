package com.morpion.server;

import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MainServeur
{
  private ConcurrentHashMap<Integer,Object> _tabClients = new ConcurrentHashMap<Integer,Object>(); // contiendra tous les flux de sortie vers les clients
  private int _nbClients=0; // nombre total de clients connect�s
  private LinkedList<Game> _games = new LinkedList<Game>(); 
  
  public static void main(String args[])
  {
    MainServeur mainServeur = new MainServeur(); // instance de la classe principale
    try
    {
      Integer port;
      if(args.length<=0) port=new Integer("18000");
      else port = new Integer(args[0]);
	  
      ServerSocket ss = new ServerSocket(port.intValue()); 
      printWelcome(port);
      while (true) 
      {
        new GameThread(ss.accept(),mainServeur); 
      }
    }
    catch (Exception e) { }
  }
  
  //** Methode : envoie le message � tous les clients **
  public void sendAll(String message,String sLast)
  {
    PrintWriter out; // declaration d'une variable permettant l'envoie de texte vers le client
    for (int i = 0; i < _tabClients.size(); i++) // parcours de la table des connect�s
    {
      out = (PrintWriter) _tabClients.get(i); // extraction de l'�l�ment courant (type PrintWriter)
      if (out != null) // s�curit�, l'�l�ment ne doit pas �tre vide
      {
      	// ecriture du texte pass� en param�tre (et concat�nation d'ue string de fin de chaine si besoin)
        out.print(message+sLast);
        out.flush(); // envoi dans le flux de sortie
      }
    }
  }

  //** Methode : d�truit le client no i **
  public void delClient(int i)
  {
    _nbClients--; // un client en moins ! snif
    if (_tabClients.get(i) != null) // l'�l�ment existe ...
    {
      _tabClients.remove(i); // ... on le supprime
    }
  }

  //** Methode : ajoute un nouveau client dans la liste **
  public int addClient(PrintWriter out)
  {
    _nbClients++; // un client en plus ! ouaaaih
    _tabClients.put(_tabClients.size(), out); // on ajoute le nouveau flux de sortie au tableau
    return _tabClients.size()-1; // on retourne le num�ro du client ajout� (size-1)
  }
  
  //** Methode : retourne une liste d'information sur les games en cours **
  public List<GameInfo> getGames()
  {
	  List<GameInfo> gamesInfo = new LinkedList<GameInfo>();
	  
	  for(Game g : _games)
	  {
		  GameInfo gi = new GameInfo(g.getId(),g.getName(),g.getNbJoueur(),g.getNbSpect(),g.getEtat());
		  gamesInfo.add(gi);
	  }
	  
	  return gamesInfo;
  }

  //** Methode : retourne le nombre de clients connect�s **
  public int getNbClients()
  {
    return _nbClients; // retourne le nombre de clients connect�s
  }

  static private void printWelcome(Integer port)
  {
   
    System.out.println("--------");
    System.out.println("Demarre sur le port : "+port.toString());
    System.out.println("--------");
    
  }
  
}
