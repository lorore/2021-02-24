package it.polito.tdp.PremierLeague.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	private Graph<Player, DefaultWeightedEdge> graph;
	private PremierLeagueDAO dao;
	private Map<Integer, Player> idMap;
	
	
	public Model() {
		dao=new PremierLeagueDAO();
	}
	
	public String creaGrafo(int m) {
		idMap=new HashMap<>();
		graph=new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		dao.getVertici(idMap, m);
		Graphs.addAllVertices(graph, idMap.values());
		System.out.println(graph.vertexSet().size());
		/*
		dao.setEfficienza(idMap, m);
		for(Player p1: graph.vertexSet()) {
			for(Player p2: graph.vertexSet()) {
				if(!p1.getTeam().equals(p2.getTeam()) && !p1.equals(p2)) {
				Double e1=p1.getE();
				Double e2=p2.getE();
				if((e1-e2)>0) {
					
					
					Graphs.addEdge(graph, p1, p2, e1-e2);
				}
				}
			}
		}
		*/
		List<Adiacenza> archi=dao.getArchi(idMap, m);
		for(Adiacenza a: archi) {
			Player p1=a.getP1();
			Player p2=a.getP2();
			Double peso=a.getPeso();
			if(peso>=0) {
				if(graph.getEdge(p2, p1)==null)
				Graphs.addEdgeWithVertices(graph, p1, p2, a.getPeso());
			}
			
		}
		System.out.println(graph.edgeSet().size());
		String s="Num Vertici: "+graph.vertexSet().size()+" num archi: "+graph.edgeSet().size();
		return s;
	}
	
	public GiocatoreMigliore getGiocatoreMigliore() {
		double max=0;
		GiocatoreMigliore gm=null;
		for(Player p: idMap.values()) {
			List<Player> uscenti=Graphs.successorListOf(graph, p);
			List<Player> entranti=Graphs.predecessorListOf(graph, p);
			double totU=0.0;
			for(Player u: uscenti) {
				totU+=graph.getEdgeWeight(graph.getEdge(p, u));
			}
			double totE=0.0;
			for(Player e: entranti) {
				totE+=graph.getEdgeWeight(graph.getEdge(e, p));
			}
			if((totU-totE)>max) {
				max=totU-totE;
				gm=new GiocatoreMigliore(p, max);
			}
		}
		
		return gm;
	}
	
	public List<Match> getMatches(){
		return dao.listAllMatches();
	}
	
	
}
