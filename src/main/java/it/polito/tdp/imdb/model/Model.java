package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	private SimpleWeightedGraph<Actor, DefaultWeightedEdge> grafo;
	private ImdbDAO dao;
	private Map<Integer, Actor> idMap;
	
	public Model() {
		dao = new ImdbDAO();
	}
	
	public List<String> generi(){
		return dao.generi();
	}
	
	public void creaGrafo(String genere) {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		idMap = new HashMap<>();
		dao.vertici(genere, idMap);
		Graphs.addAllVertices(this.grafo, idMap.values());
		for(Arco arco : dao.edges(genere, idMap)) {
			Graphs.addEdgeWithVertices(grafo, arco.getA(), arco.getB(), arco.getPeso());
		}
	}
	
	public Set<Actor> getVertici(){
		return grafo.vertexSet();
	}
	
	public Set<DefaultWeightedEdge> getArchi(){
		return grafo.edgeSet();
	}
	
	public List<Actor> trovaCollegati(Actor partenza){
		BreadthFirstIterator<Actor, DefaultWeightedEdge> bfv = new BreadthFirstIterator<>(this.grafo, partenza);
		List<Actor> raggiungibili = new ArrayList<>();
		
		while(bfv.hasNext()) {
			Actor a = bfv.next();
			raggiungibili.add(a);
		}
		Collections.sort(raggiungibili);
		return raggiungibili;
	}
	
	
	//Simulazione
	//Parametri input
	int n;
	List<Actor> attori;
	//Stato del sistema
	int giorno;
	Actor ultimo;
	Actor attuale;
	int caso;
	boolean salto = false;
	//Output
	List<Actor> intervistati;
	int pause;
	
	public void simula(int n) {
		this.n=n;
		attori = new ArrayList<>(this.grafo.vertexSet());
		this.giorno = 1;
		intervistati = new ArrayList<>();
		pause = 0;
		ultimo = null;
		
		caso = this.getRandomNumber();
		attuale = attori.get(caso);
		intervistati.add(attuale);
		attori.remove(caso);
		
		while(this.giorno < n) {
			this.giorno++;
			ultimo = attuale;
			int numero = (int) Math.random() * 100;
			
			if(numero < 60 || salto==true) {
				caso = this.getRandomNumber();
				attuale = attori.get(caso);
				if(salto)
					salto = false;
			}
			else {
				attuale = this.getVicinoMigliore(ultimo);
				if(attuale==null) {
					caso = this.getRandomNumber();
					attuale = attori.get(caso);
				}
			}
			intervistati.add(attuale);
			attori.remove(attuale);
			
			if(attuale.getGender().equals(ultimo.getGender())) {
				if(Math.random() < 0.90 && this.giorno!=n) {
					this.giorno++; //salto un giorno se non è gia l'ultimo
					this.pause++;
					salto = true;
				}
			}
		}
	}
	
	public int getRandomNumber() {
		Random random = new Random();
		int randomInteger = random.nextInt(this.attori.size());
		return randomInteger;
    }
	
	public Actor getVicinoMigliore(Actor a) {
		int pesoBest = 0;
		int peso = 0;
		Actor migliore = null;
		if(Graphs.neighborListOf(this.grafo, a).size() > 0) {
			for(Actor vicino : Graphs.neighborListOf(this.grafo, a)) {
				peso = (int) grafo.getEdgeWeight(grafo.getEdge(a, vicino));
				if(this.attori.contains(vicino) && (pesoBest == 0 || peso > pesoBest)) {
					migliore = vicino;
					pesoBest = peso;
				}
			}
		}
		return migliore;
	}

	public List<Actor> getIntervistati() {
		return intervistati;
	}

	public int getPause() {
		return pause;
	}
	
	
}
