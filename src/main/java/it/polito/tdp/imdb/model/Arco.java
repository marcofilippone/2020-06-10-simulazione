package it.polito.tdp.imdb.model;

public class Arco {
	private Actor a;
	private Actor b;
	private Integer peso;
	public Arco(Actor a, Actor b, Integer peso) {
		super();
		this.a = a;
		this.b = b;
		this.peso = peso;
	}
	public Actor getA() {
		return a;
	}
	public void setA(Actor a) {
		this.a = a;
	}
	public Actor getB() {
		return b;
	}
	public void setB(Actor b) {
		this.b = b;
	}
	public Integer getPeso() {
		return peso;
	}
	public void setPeso(Integer peso) {
		this.peso = peso;
	}
	

}
