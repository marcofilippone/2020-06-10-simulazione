package it.polito.tdp.imdb.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Arco;
import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Movie;

public class ImdbDAO {
	
	public List<Actor> listAllActors(){
		String sql = "SELECT * FROM actors";
		List<Actor> result = new ArrayList<Actor>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Actor actor = new Actor(res.getInt("id"), res.getString("first_name"), res.getString("last_name"),
						res.getString("gender"));
				
				result.add(actor);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Movie> listAllMovies(){
		String sql = "SELECT * FROM movies";
		List<Movie> result = new ArrayList<Movie>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Movie movie = new Movie(res.getInt("id"), res.getString("name"), 
						res.getInt("year"), res.getDouble("rank"));
				
				result.add(movie);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public List<Director> listAllDirectors(){
		String sql = "SELECT * FROM directors";
		List<Director> result = new ArrayList<Director>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Director director = new Director(res.getInt("id"), res.getString("first_name"), res.getString("last_name"));
				
				result.add(director);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public List<String> generi(){
		String sql = "SELECT DISTINCT genre "
				+ "FROM movies_genres";
		List<String> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				
				result.add(res.getString("genre"));
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void vertici(String genere, Map<Integer, Actor> idMap){
		String sql = "SELECT DISTINCT a.id, a.first_name, a.last_name, a.gender "
				+ "FROM movies_genres g, actors a, roles r "
				+ "WHERE g.movie_id = r.movie_id AND r.actor_id = a.id AND g.genre = ?";
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, genere);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				if(!idMap.containsKey(res.getInt("a.id"))) {
					Actor actor = new Actor(res.getInt("a.id"), res.getString("a.first_name"), res.getString("a.last_name"),
							res.getString("a.gender"));
					idMap.put(res.getInt("a.id"), actor);
				}
			}
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<Arco> edges(String genere, Map<Integer, Actor> idMap){
		String sql = "SELECT DISTINCT r1.actor_id, r2.actor_id, COUNT(DISTINCT g1.movie_id) AS peso "
				+ "FROM movies_genres g1, roles r1, movies_genres g2, roles r2 "
				+ "WHERE g1.movie_id = r1.movie_id AND g2.movie_id = r2.movie_id AND g1.movie_id = g2.movie_id "
				+ "AND g1.genre = ? AND g2.genre = g1.genre AND r1.actor_id > r2.actor_id "
				+ "GROUP BY r1.actor_id, r2.actor_id";
		List<Arco> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, genere);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				if(idMap.containsKey(res.getInt("r1.actor_id")) && idMap.containsKey(res.getInt("r2.actor_id"))) {
					Arco a = new Arco(idMap.get(res.getInt("r1.actor_id")), idMap.get(res.getInt("r2.actor_id")), res.getInt("peso"));
					result.add(a);
				}
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
}
