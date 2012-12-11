package com.morpion.server;

public class Game{

	private int id = 0;
	private int tour = 1;
	private int damier[] = new int[9];
	private String name="";
	private int nbJoueur=0;
	private int nbSpect=0;
	private int etat=0;  //0 en attente et 1 en cours
	
	public Game(){

	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getNbJoueur() {
		return nbJoueur;
	}
	public void setNbJoueur(int nbJoueur) {
		this.nbJoueur = nbJoueur;
	}
	public int getNbSpect() {
		return nbSpect;
	}
	public void setNbSpect(int nbSpect) {
		this.nbSpect = nbSpect;
	}
	public int getEtat() {
		return etat;
	}
	public void setEtat(int etat) {
		this.etat = etat;
	}

	public boolean jouerCoup(int joueur,int l,int c){
		return jouerCoup(joueur,l*3+c);
	}
	
	public boolean jouerCoup(int joueur,int c){
		if(tour==joueur && c<damier.length && damier[c]==0){
			damier[c]=joueur;
			return true;
		}
		return true;
	}

	public boolean gagne(int joueur){
		return (damier[0]==joueur && damier[1]==joueur && damier[2]==joueur) ||
			   (damier[3]==joueur && damier[4]==joueur && damier[5]==joueur) ||
			   (damier[6]==joueur && damier[7]==joueur && damier[8]==joueur) ||
			   (damier[0]==joueur && damier[3]==joueur && damier[6]==joueur) ||			   
			   (damier[1]==joueur && damier[4]==joueur && damier[7]==joueur) ||			   
			   (damier[2]==joueur && damier[5]==joueur && damier[8]==joueur) ||	
			   (damier[0]==joueur && damier[4]==joueur && damier[8]==joueur) ||
			   (damier[2]==joueur && damier[4]==joueur && damier[6]==joueur);		   
	}
}