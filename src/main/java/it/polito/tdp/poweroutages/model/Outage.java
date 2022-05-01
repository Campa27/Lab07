package it.polito.tdp.poweroutages.model;

import java.time.*;
import java.util.Objects;

public class Outage {
	
	private int id;
	private int NercId;
	private int customers_affected;
	private LocalDate date_event_began;
	private LocalDate date_event_finished;
	private int diff;
	
	public Outage(int id, int nercId, int customers_affected, LocalDate date_event_began, LocalDate date_event_finished,
			int diff) {
		this.id = id;
		NercId = nercId;
		this.customers_affected = customers_affected;
		this.date_event_began = date_event_began;
		this.date_event_finished = date_event_finished;
		this.diff = diff;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNercId() {
		return NercId;
	}

	public void setNercId(int nercId) {
		NercId = nercId;
	}

	public int getCustomers_affected() {
		return customers_affected;
	}

	public void setCustomers_affected(int customers_affected) {
		this.customers_affected = customers_affected;
	}

	public LocalDate getDate_event_began() {
		return date_event_began;
	}

	public void setDate_event_began(LocalDate date_event_began) {
		this.date_event_began = date_event_began;
	}

	public LocalDate getDate_event_finished() {
		return date_event_finished;
	}

	public void setDate_event_finished(LocalDate date_event_finished) {
		this.date_event_finished = date_event_finished;
	}

	public int getDiff() {
		return diff;
	}

	public void setDiff(int diff) {
		this.diff = diff;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Outage other = (Outage) obj;
		return id == other.id;
	}

	@Override
	public String toString() {
		return "Outage [id=" + id + ", NercId=" + NercId + ", customers_affected=" + customers_affected
				+ ", date_event_began=" + date_event_began + ", date_event_finished=" + date_event_finished + ", diff="
				+ diff + "]";
	}
	
}
