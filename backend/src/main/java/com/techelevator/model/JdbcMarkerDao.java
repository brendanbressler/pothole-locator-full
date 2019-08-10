package com.techelevator.model;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class JdbcMarkerDao implements MarkerDao {
	
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	public JdbcMarkerDao(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public List<Marker> getAllMarkers() {
		List<Marker> markers = new ArrayList<>();
		
		String sql = "SELECT id, user_id, report_date, latitude, longitude, rating, comments "
				+ "FROM markers";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
		
		while(results.next()) {
			markers.add(mapRowToMarker(results));
		}
		
		return markers;
	}

	@Override
	public Marker getMarkerById(long id) {
		Marker m = null;
		
		String sql = "SELECT id, user_id, report_date, latitude, longitude, rating, comments "
				+ "FROM markers "
				+ "WHERE id = ?";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
		
		if(results.next()) {
			m = mapRowToMarker(results);
		}
		
		return m;
	}
	
	@Override
	public List<Marker> getMarkersByUserId(long userId) {
		List<Marker> markers = new ArrayList<>();
		
		String sql = "SELECT id, user_id, report_date, latitude, longitude, rating, comments "
				+ "FROM markers "
				+ "WHERE user_id = ?";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
		while(results.next()) {
			markers.add(mapRowToMarker(results));
		}
		
		return markers;
	}
	
	@Override
	public Marker create(long userId, double latitude, double longitude, String rating, String comments) {
		
		String sql = "INSERT INTO markers (user_id, latitude, longitude, rating, comments) "
				+ "VALUES(?, ?, ?, ?, ?) RETURNING id";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, latitude, longitude, rating, comments);
		Marker m = null;
		if(results.next()) {
			m = new Marker();
			m.setId(results.getLong("id"));
			m.setUserId(userId);
			m.setLatitude(latitude);
			m.setLongitude(longitude);
			m.setRating(rating);
			m.setComments(comments);
		}
		
		return m;
	}
	
	@Override
	public Marker update(long id, long userId, double latitude, double longitude, String rating, String comments) {
		String sql = "UPDATE markers "
				+ "SET user_id = ?, latitude = ?, longitude = ?, rating = ?, comments = ? "
				+ "WHERE id = ?";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, Double.toString(latitude), Double.toString(longitude), rating, comments, id);
		
		Marker m = null;
		if(results.next()) {
			m = mapRowToMarker(results);
		}
		
		return m;
	}
	
	
	private Marker mapRowToMarker(SqlRowSet row) {
		Marker m = new Marker();
		m.setId(row.getLong("id"));
		m.setUserId(row.getLong("user_id"));
		m.setReportDate(row.getDate("report_date").toLocalDate());
		m.setLatitude(Double.parseDouble(row.getString("latitude")));
		m.setLongitude(Double.parseDouble(row.getString("longitude")));
		m.setRating(row.getString("rating"));
		m.setComments(row.getString("comments"));
		
		return m;
	}
}
