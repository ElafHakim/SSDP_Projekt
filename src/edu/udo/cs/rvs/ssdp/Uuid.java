package edu.udo.cs.rvs.ssdp;

import java.util.*;

public class Uuid {

	private String s;
	private List<String> service = new ArrayList<String>();

	// Konstruktor
	public Uuid(String x, List<String> service) {
		this.s = x;
		this.service = service;
	}

	// Konstruktor ohne Argumente
	public Uuid() {
		s = null;
		service = null;
	}

	public String getUuid() {
		return s;
	}

	public List<String> getService() {
		return service;
	}

	public void addService(String v) {
		if (v.contains(":")) {
			v = v.trim().split(":", 2)[1];

		}
		this.service.add(v);
	}

	public void setUuid(String u) {
		u = u.trim().split(":", 2)[2];
		this.s = u;
	}
}
