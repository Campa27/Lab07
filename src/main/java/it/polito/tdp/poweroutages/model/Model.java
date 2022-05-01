package it.polito.tdp.poweroutages.model;

import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.poweroutages.DAO.PowerOutageDAO;

public class Model {
	
	PowerOutageDAO podao;
	List<Outage> result;
	List<Outage> start;
	int oreMax = 0;
	int anniMax = 0;
	
	public Model() {
		podao = new PowerOutageDAO();
	}
	
	public List<Nerc> getNercList() {
		return podao.getNercList();
	}
	
	public List<Outage> getOutagesByNerc(Nerc n){
		return podao.getOutagesByNerc(n);
	}
	
	public List<Outage> WorstCaseAnalysis(Nerc n, int x, int y) {														
		anniMax = x;													
		oreMax = y;
		start = new ArrayList<Outage>(getOutagesByNerc(n));				//creo la lista di Outages relativi al Nerc richiesto
		List<Outage> parziale = new ArrayList<Outage>();				//creo parziale, per ora vuota
		result = null;													//inizializzo a vuota la lista dei risultati
		WorstCaseAnalysisRicorsiva(parziale, 0);						//il livello é: il numero totale di ore di disservizio
		return result;													
	}

	private void WorstCaseAnalysisRicorsiva(List<Outage> parziale, int Livello) {
		if(Livello > oreMax) {
			return;
		}
		if(Livello == oreMax || parziale.size() == start.size()) {							//caso terminale
			int persone_coinvolte = personeCoinvolte(parziale);
			
			if(result == null || persone_coinvolte > personeCoinvolte(result)) {
				result = new ArrayList<Outage>(parziale);
			} 
		} else {																			//caso intermedio
			for(Outage o : start) {
				if(parziale.isEmpty() || parziale.contains(o) == false) {
					if(possoAggiungere(o, parziale) == true) {
						parziale.add(o);
						WorstCaseAnalysisRicorsiva(parziale, contaOre(parziale));
						parziale.remove(parziale.size()-1);
					}
				}
			}
		}
	}
	
	private boolean possoAggiungere(Outage o, List<Outage> parziale) {
		int piuVecchio = 0;
		int piuRecente = 0;
		
		List<Outage> prova = new ArrayList<Outage>(parziale);
		
		if(prova.isEmpty()) {
			return true;
		} else {
			prova.add(o);
			
			for(Outage outage : prova) {
				if(piuVecchio == 0 || outage.getDate_event_began().getYear() < piuVecchio) {
					piuVecchio = outage.getDate_event_began().getYear();
				}
			
				if(piuRecente == 0 || outage.getDate_event_began().getYear() > piuRecente) {
					piuRecente = outage.getDate_event_began().getYear();
				}
			}
		
			if((piuRecente - piuVecchio) > anniMax) {
				return false;
			} else {
				return true;
			}
		}
	}

	private int personeCoinvolte(List<Outage> parziale) {
		int persone = 0;
		
		for(Outage o : parziale) {
			persone += o.getCustomers_affected();
		}
		
		return persone;
	}
	
	private int contaOre(List<Outage> parziale) {
		int conta = 0;
		
		for(Outage o : parziale) {
			conta += o.getDiff();
		}
		
		return conta;
	}

}
