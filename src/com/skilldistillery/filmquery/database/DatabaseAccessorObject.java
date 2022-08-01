package com.skilldistillery.filmquery.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;

public class DatabaseAccessorObject implements DatabaseAccessor {
	private static final String URL = "jdbc:mysql://localhost:3306/sdvid?useSSL=false&useLegacyDatetimeCode=false&serverTimezone=US/Mountain";

	private final String user = "student";
	private final String pass = "student";

	static {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Film findFilmById(int filmId) {
		Film film = null;

		try {
			Connection conn = DriverManager.getConnection(URL, user, pass);
			String sql = "SELECT * FROM Film WHERE id= ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, filmId);

			ResultSet filmResult = stmt.executeQuery();

			if (filmResult.next()) {
				film = new Film();
				film.setId(filmResult.getInt("id"));
				film.setTitle(filmResult.getString("title"));
				film.setDescription(filmResult.getString("description"));
				film.setLanguageId(filmResult.getInt("language_id"));
				film.setReleaseYear(filmResult.getInt("release_year"));
				film.setRentalDuration(filmResult.getInt("rental_duration"));
				film.setRentalRate(filmResult.getDouble("rental_rate"));
				film.setLength(filmResult.getInt("length"));
				film.setReplacementCost(filmResult.getDouble("replacement_cost"));
				film.setRating(filmResult.getString("rating"));
				film.setSpecialFeatures(filmResult.getString("special_features"));

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return film;
	}

	@Override
	public Actor findActorById(int actorId) {
		Actor actor = null;

		try {
			Connection conn = DriverManager.getConnection(URL, user, pass);
			String sql = "SELECT * FROM Actor WHERE id= ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, actorId);

			ResultSet actorResult = stmt.executeQuery();

			if (actorResult.next()) {
				actor = new Actor();
				actor.setId(actorResult.getInt("id"));
				actor.setLastName(actorResult.getString("last_name"));
				actor.setFirstName(actorResult.getString("first_name"));
			} else {
				System.out.println("Actor not found with that ID number.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return actor;
	}

	@Override
	public List<Actor> findActorsByFilmId(int filmId) {
		List<Actor> actors = new ArrayList<>();

		try {
			Connection conn = DriverManager.getConnection(URL, user, pass);
			String sql = "SELECT atr.first_name, atr.last_name, atr.id, fa.film_id "
					+ "FROM actor atr JOIN film_actor fa ON atr.id = fa.actor_id JOIN film f ON fa.film_id = f.id "
					+ "WHERE f.id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, filmId);

			ResultSet actorResult = stmt.executeQuery();

			while (actorResult.next()) {
				int id = actorResult.getInt("id");
				String fName = actorResult.getString("first_name");
				String lName = actorResult.getString("last_name");

				Actor actor = new Actor(id, fName, lName);
				actors.add(actor);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return actors;
	}

	public List<Film> findFilmByKeyWord(String keyword) {
		List<Film> films = new ArrayList<>();
		Film film = null;

		try {
			Connection conn = DriverManager.getConnection(URL, user, pass);
			String sql = "SELECT * FROM Film WHERE title LIKE ? OR description LIKE ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, "%" + keyword + "%");
			stmt.setString(2, "%" + keyword + "%");

			ResultSet keywordResult = stmt.executeQuery();

			while (keywordResult.next()) {

				film = new Film();
				film.setId(keywordResult.getInt("id"));
				film.setTitle(keywordResult.getString("title"));
				film.setDescription(keywordResult.getString("description"));
				film.setReleaseYear(keywordResult.getInt("release_year"));
				film.setRentalDuration(keywordResult.getInt("rental_duration"));
				film.setRentalRate(keywordResult.getDouble("rental_rate"));
				film.setLength(keywordResult.getInt("length"));
				film.setReplacementCost(keywordResult.getDouble("replacement_cost"));
				film.setRating(keywordResult.getString("rating"));
				film.setSpecialFeatures(keywordResult.getString("special_features"));
				film.setLanguageId(keywordResult.getInt("language_id"));

				films.add(film);

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (film == null) {
			System.out.println("No results found");
		}
		return films;
	}

	public String findLanguageByFilm(int langId) {
		String lang = null;

		try {
			Connection conn = DriverManager.getConnection(URL, user, pass);
			String sql = "SELECT name FROM language WHERE language.id = ? ";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, langId);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				lang = rs.getString("name");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return lang;
	}
}
