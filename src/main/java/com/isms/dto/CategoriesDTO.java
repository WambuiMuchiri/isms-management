package com.isms.dto;

public class CategoriesDTO {

	    private int id;
	    
	    private String initials;
	    
	    private String name;
   
		public CategoriesDTO() {
			
		}

		
		public CategoriesDTO(int id, String initials, String name) {
			super();
			this.id = id;
			this.initials = initials;
			this.name = name;
		}


		public CategoriesDTO(String initials, String name) {
			super();
			this.initials = initials;
			this.name = name;
		}


		public int getId() {
			return id;
		}


		public void setId(int id) {
			this.id = id;
		}


		public String getInitials() {
			return initials;
		}


		public void setInitials(String initials) {
			this.initials = initials;
		}


		public String getName() {
			return name;
		}


		public void setName(String name) {
			this.name = name;
		}


		@Override
		public String toString() {
			return "CategoriesDTO [name=" + name + "]";
		}
		
		
}
