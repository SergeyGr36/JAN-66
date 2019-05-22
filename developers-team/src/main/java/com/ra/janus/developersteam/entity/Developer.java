package com.ra.janus.developersteam.entity;

public class Developer {

   long id;
   String developerName;
   
   public Developer(final long id, final String developerName) {
	   
	   this.id = id;
	   this.developerName = developerName;
   }

public long getId() {
	return id;
}

public void setId(long id) {
	this.id = id;
}

public String getDeveloperName() {
	return developerName;
}

public void setDeveloperName(String developerName) {
	this.developerName = developerName;
}
}
