package it.polito.tdp.extflightdelays.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;



public class Model {

	private List<Airport> listAereoPorti ;
	private Map<Integer, Airport> fermateIdMap ;
	
	// grafo non orientato, PESATO, semplice
	private SimpleWeightedGraph <Airport, DefaultWeightedEdge> grafo ; 
//  

	
	public void creaGrafo(int distanzaMinima) {
		
		// occhio che qui "grafo" è pesato
		this.grafo = new SimpleWeightedGraph<Airport, DefaultWeightedEdge>(DefaultWeightedEdge.class) ;
		/*   semplice, no orientato, pesato */ 
		
		/** next time mettiolo nel costruttore **/ 
		ExtFlightDelaysDAO dao= new ExtFlightDelaysDAO();
		listAereoPorti= dao.loadAllAirports();
		
		
		/** mappa con gli airports, id **/ 
		
		fermateIdMap = new HashMap<>(); 
		for(Airport aa: this.listAereoPorti)
			this.fermateIdMap.put(aa.getId(), aa) ;
		
		
		
		//aggiungo i vertici:
		Graphs.addAllVertices(this.grafo, this.listAereoPorti) ;
		

		// metodo 3, aggiunta ARCHI    
	//	trovaIdVoli
		List <Edge> listEdge = dao.trovaCoppieVoli(fermateIdMap,distanzaMinima);

		
		
		/** BY ME**/ 
		/*   
		  
		for(Edge cc: listEdge) {
			//DefaultWeightedEdge edg = grafo.getEdge(cc.getArrivo(), cc.getPartenza());
			Graphs.addEdge(grafo, cc.getArrivo(), cc.getPartenza(), cc.getDistanza());
			
			}
			
	*/ 
       /** BY SOLUTION, meglio **/ 
		for(Edge cc: listEdge) {
			//controllo se esiste già un arco
			//se esiste, aggiorno il peso
			DefaultWeightedEdge edge = grafo.getEdge(cc.getPartenza(), cc.getArrivo());
			
			if(edge == null) {
			/** aggiungo l'arco (se non c'era nulla)  **/ 
				Graphs.addEdge(grafo, cc.getPartenza(), cc.getArrivo(), cc.getDistanza());
			} else {
			/** aggiorno l'arco   -----> perchè chiede la media dei pesi */ 
				double peso = grafo.getEdgeWeight(edge);
				double newPeso = (peso + cc.getDistanza())/2;
				grafo.setEdgeWeight(edge, newPeso);
			}
		}
		
	
		}
		
		/*  NON E' NECESSARIO?    vedi solz 2023 
		  
		 
		 * for(Rotta rotta : dao.getRotte(idMap, distanzaMedia)) {
		 
	      "controllo se esiste già un arco, senno' lo creo"
	  
		DefaultWeightedEdge edge = grafo.getEdge(rotta.getA1(), rotta.getA2());
		if(edge == null) {
			Graphs.addEdge(grafo, rotta.getA1(), rotta.getA2(), rotta.getPeso());
		} 
		else {    "se esiste, aggiorno il peso"
			
			double peso = grafo.getEdgeWeight(edge);
			double newPeso = (peso + rotta.getPeso())/2;
			grafo.setEdgeWeight(edge, newPeso);
		}
	}
		*/ 
		
		
	public int getNumVertici () {
		return grafo.vertexSet().size();
	}
	
	public int getNumArchi () {
		return grafo.edgeSet().size();
	}
	
	
//  elenco di tutti gli archi con la relativa distanza   ---------------- IMPORTANTE --------------
    public List<Edge> getRotte(){
		
     //uso la classe Edge per salvare gli archi del grafo con il relativo peso
    		
	List<Edge> rotte = new ArrayList<Edge>();
	
	for(DefaultWeightedEdge e : this.grafo.edgeSet()) {  //occhio a questa classe "DefaultWeightedEdge"
		  Edge ee= new Edge(grafo.getEdgeSource(e), grafo.getEdgeTarget(e), grafo.getEdgeWeight(e));
		  rotte.add(ee);
		}
	
	/*     **** OPPURE: ****
	for(DefaultWeightedEdge e : this.grafo.edgeSet()) {  //occhio a questa classe "DefaultWeightedEdge"
	  rotte.add(new Edge(this.grafo.getEdgeSource(e), this.grafo.getEdgeTarget(e), this.grafo.getEdgeWeight(e)));
	}
	*/  

	return rotte;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
