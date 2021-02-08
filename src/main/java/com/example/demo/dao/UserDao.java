package com.example.demo.dao;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class UserDao {
	
	@Override
	public String toString() {
		return "UserDao [email=" + email + ", password=" + password + ", roles=" + roles + ", active=" + active
				+ ", name=" + name + ", phone=" + phone + ", department=" + department + ", salary=" + salary + "]";
	}
	@Id
	private String email;
	private String password;
	private String roles;
	private boolean active;
	private String name;
	private String phone;
	private String department;
	private String salary;
	public String getName() {
		return name;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEmail() {
		return email;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getSalary() {
		return salary;
	}
	public void setSalary(String salary) {
		this.salary = salary;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRoles() {
		return roles;
	}
	public void setRoles(String roles) {
		this.roles = roles;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
}
