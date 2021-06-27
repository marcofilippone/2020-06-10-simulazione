package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

}
