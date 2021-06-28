package it.polito.tdp.PremierLeague.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.PremierLeague.model.Action;
import it.polito.tdp.PremierLeague.model.Adiacenza;
import it.polito.tdp.PremierLeague.model.Match;
import it.polito.tdp.PremierLeague.model.Player;
import it.polito.tdp.PremierLeague.model.Team;

public class PremierLeagueDAO {
	
	public List<Player> listAllPlayers(){
		String sql = "SELECT * FROM Players";
		List<Player> result = new ArrayList<Player>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Player player = new Player(res.getInt("PlayerID"), res.getString("Name"));
				result.add(player);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Team> listAllTeams(){
		String sql = "SELECT * FROM Teams";
		List<Team> result = new ArrayList<Team>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Team team = new Team(res.getInt("TeamID"), res.getString("Name"));
				result.add(team);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Action> listAllActions(){
		String sql = "SELECT * FROM Actions";
		List<Action> result = new ArrayList<Action>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				
				Action action = new Action(res.getInt("PlayerID"),res.getInt("MatchID"),res.getInt("TeamID"),res.getInt("Starts"),res.getInt("Goals"),
						res.getInt("TimePlayed"),res.getInt("RedCards"),res.getInt("YellowCards"),res.getInt("TotalSuccessfulPassesAll"),res.getInt("totalUnsuccessfulPassesAll"),
						res.getInt("Assists"),res.getInt("TotalFoulsConceded"),res.getInt("Offsides"));
				
				result.add(action);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Match> listAllMatches(){
		String sql = "SELECT m.MatchID, m.TeamHomeID, m.TeamAwayID, m.teamHomeFormation, m.teamAwayFormation, m.resultOfTeamHome, m.date, t1.Name, t2.Name   "
				+ "FROM Matches m, Teams t1, Teams t2 "
				+ "WHERE m.TeamHomeID = t1.TeamID AND m.TeamAwayID = t2.TeamID";
		List<Match> result = new ArrayList<Match>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				
				Match match = new Match(res.getInt("m.MatchID"), res.getInt("m.TeamHomeID"), res.getInt("m.TeamAwayID"), res.getInt("m.teamHomeFormation"), 
							res.getInt("m.teamAwayFormation"),res.getInt("m.resultOfTeamHome"), res.getTimestamp("m.date").toLocalDateTime(), res.getString("t1.Name"),res.getString("t2.Name"));
				
				
				result.add(match);

			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public void getVertici(Map<Integer, Player> idMap, int m) {
		String sql="SELECT * "
				+ "FROM players p "
				+ "WHERE p.PlayerID IN ( "
				+ "SELECT a.PlayerID "
				+ "FROM actions a "
				+ "WHERE a.MatchID=?) ";
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, m);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				if(!idMap.containsKey(res.getInt("PlayerID"))) {
					Player p=new Player(res.getInt("PlayerID"), res.getString("Name"));
					idMap.put(p.getPlayerID(), p);
				}
			}
			conn.close();
			
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}
	
	public void setEfficienza(Map<Integer, Player> idMap, int m) {
		String sql="SELECT a.PlayerID, a.TeamID,(a.TotalSuccessfulPassesAll+a.Assists)/a.TimePlayed as e "
				+ "FROM actions a "
				+ "WHERE a.MatchID=?";
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, m);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				if(idMap.containsKey(res.getInt("PlayerID"))) {
					Player p=idMap.get(res.getInt("PlayerID"));
					p.setE(res.getDouble("e"));
					p.setTeam(res.getInt("TeamID"));
				}
			}
			conn.close();
			
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}
	
	public List<Adiacenza> getArchi(Map<Integer, Player> idMap, int m) {
		String sql="SELECT a.PlayerID as p1, a.TeamID, a1.playerID as p2, ((a.TotalSuccessfulPassesAll+a.Assists)/a.TimePlayed)- ((a1.TotalSuccessfulPassesAll+a1.Assists)/a1.TimePlayed) AS peso "
				+ "FROM actions a, actions a1 "
				+ "WHERE a.MatchID=? AND a.PlayerID<>a1.PlayerID AND a.MatchID=a1.MatchID AND a.TeamID<>a1.TeamId ";
		Connection conn = DBConnect.getConnection();
		List<Adiacenza> result=new ArrayList<>();
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, m);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				if(idMap.containsKey(res.getInt("p1")) && idMap.containsKey(res.getInt("p2"))) {
					Player p1=idMap.get(res.getInt("p1"));
					Player p2=idMap.get(res.getInt("p2"));
					Adiacenza a=new Adiacenza(p1, p2, res.getDouble("peso"));
					if(a.getPeso()>=0)
					result.add(a);
					
				}
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}
	
	public Integer getSquadraGMigliore(Player p, Match m) {
		String sql="SELECT a.TeamID "
				+ "FROM actions a "
				+ "WHERE a.MatchID=? AND a.PlayerID=?";
		
		Connection conn = DBConnect.getConnection();
		Integer result=null;
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, m.getMatchID());
			st.setInt(2, p.getPlayerID());
			ResultSet res = st.executeQuery();
			
			while (res.next()) {
				result=res.getInt("TeamID");
				
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			throw new RuntimeException();
		}
		
	}
	
}
