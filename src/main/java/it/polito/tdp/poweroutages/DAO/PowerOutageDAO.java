package it.polito.tdp.poweroutages.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.poweroutages.model.Nerc;
import it.polito.tdp.poweroutages.model.Outage;

public class PowerOutageDAO {
	
	public List<Nerc> getNercList() {

		String sql = "SELECT id, value FROM nerc";
		List<Nerc> nercList = new ArrayList<>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				Nerc n = new Nerc(res.getInt("id"), res.getString("value"));
				nercList.add(n);
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return nercList;
	}
	
	public List<Outage> getOutagesByNerc(Nerc n){
		
		String sql ="select id, nerc_id, customers_affected, date_event_began, date_event_finished, datediff(date_event_finished, date_event_began) as 'diff' "
				+ "from PowerOutages "
				+ "where nerc_id = ? "
				+ "order by date_event_began";
		
		List<Outage>outagesList = new ArrayList<Outage>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, n.getId());
			ResultSet res = st.executeQuery();

			while (res.next()) {
				Outage o = new Outage(res.getInt("id"), res.getInt("nerc_id"), res.getInt("customers_affected"),  res.getDate("date_event_began").toLocalDate(), res.getDate("date_event_finished").toLocalDate(), res.getInt("diff"));
				outagesList.add(o);
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return outagesList;
		
	}
	

}
