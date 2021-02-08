package com.example.demo.model;

public class SaveUser {

	private String name;
	private String password;
	@Override
	public String toString() {
		return "SaveUser [name=" + name + ", password=" + password + ", confirmpassword=" + confirmpassword
				+ ", department=" + department + ", salary=" + salary + ", phone=" + phone + "]";
	}
	private String confirmpassword;
	private String department;
	private String salary;
	private String phone;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getConfirmpassword() {
		return confirmpassword;
	}
	public void setConfirmpassword(String confirmpassword) {
		this.confirmpassword = confirmpassword;
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
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
}
