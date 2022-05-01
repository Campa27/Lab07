package it.polito.tdp.poweroutages.model;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.poweroutages.DAO.PowerOutageDAO;

public class Model {
	
	PowerOutageDAO podao;
	List<Outage> result;
	List<Outage> start;
	int ore = 0;
	
	
	
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
		start = new ArrayList<Outage>(getOutagesByNerc(n));
		List<Outage> parziale = new ArrayList<Outage>();
		result = null;
		WorstCaseAnalysisRicorsiva(parziale, x, y);		//y è il livello, ossia il numero
		return result;											//totale di ore di disservizio
	}

	private void WorstCaseAnalysisRicorsiva(List<Outage> parziale, int x, int y) {
		if(ore == y) {
			int persone_coinvolte = personeCoinvolte(parziale);
			
			if(result == null || persone_coinvolte < personeCoinvolte(result)) {
				result = new ArrayList<Outage>(parziale);
			} 
		} else {
			for(Outage o : start) {
				if(valido(o, x, parziale)) {
					parziale.add(o);
					WorstCaseAnalysisRicorsiva(parziale, x, y-contaOre(parziale));
					parziale.remove(parziale.size()-1);
				}
			}
		}
	}
	
	private boolean valido(Outage o, int x, List<Outage> parziale) {
		LocalDate piuVecchio = null;
		LocalDate piuRecente = null;
		
		if(parziale.isEmpty()) {
			return true;
		} else {
			for(Outage outage : parziale) {
				if(piuVecchio == null || outage.getDate_event_began().isBefore(piuVecchio)) {
					piuVecchio = outage.getDate_event_began();
				}
			
				if(piuRecente == null || outage.getDate_event_began().isAfter(piuRecente)) {
					piuRecente = outage.getDate_event_began();
				}
			}
		
			if(Period.between(piuRecente, piuVecchio).getYears() > x) {
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
