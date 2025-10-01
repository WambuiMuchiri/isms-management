package com.isms.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "visitors")
@Table(name = "visitors", catalog = "isms_db", schema = "")
public class Visitors {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "user_name", nullable = true)
	private String user;

	@Column(name = "ip", nullable = true)
	private String ip;

	@Column(name = "method", nullable = true)
	private String method;

	@Column(name = "url", nullable = true)
	private String url;

	@Column(name = "page", nullable = true)
	private String page;

	@Column(name = "query_string", nullable = true, length = 400)
	private String queryString;

	@Column(name = "referer_page", nullable = true)
	private String refererPage;

	@Column(name = "user_agent", nullable = true)
	private String userAgent;

	@Column(name = "logged_time", nullable = true)
	private LocalDateTime loggedTime;

	@Column(name = "unique_visitor", nullable = true)
	private boolean uniqueVisitor;
}
