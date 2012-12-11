package com.morpion.server;

public class GameInfo {

	private int _id = 0;
	private String _name = "";
	private int _nbJoueur = 0;
	private int _nbSpectacteurs = 0;
	private int _status = 0;

	
	public GameInfo(int id, String name, int nbJoueur, int nbSpectacteurs, int status)
	{
		this._id = id; this._name = name; this._nbJoueur = nbJoueur; this._nbSpectacteurs = nbSpectacteurs; this._status = status;
	}
	
	public int getId() {
		return _id;
	}
	public void setId(int _id) {
		this._id = _id;
	}
	public String getName() {
		return _name;
	}
	public void setName(String _name) {
		this._name = _name;
	}
	public int getNbJoueur() {
		return _nbJoueur;
	}
	public void setNbJoueur(int _nbJoueur) {
		this._nbJoueur = _nbJoueur;
	}
	public int get_nbSpectacteurs() {
		return _nbSpectacteurs;
	}
	public void set_nbSpectacteurs(int _nbSpectacteurs) {
		this._nbSpectacteurs = _nbSpectacteurs;
	}
	public int getState() {
		return _status;
	}
	public void setState(int _status) {
		this._status = _status;
	}
}
