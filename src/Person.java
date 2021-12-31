import java.io.*;
import java.util.*;

@SuppressWarnings("unused")

/*
 * 프로그램 이름 : Person.java 프로그램 설명 : 클래스 Person 정의 프로그램 작성일 : 2020-09-08 작성자 : Yujung Lee
 */

public class Person implements java.io.Serializable{

	private String name; // 이름 필드
	private String phoneNum; // 전화번호 필드
	private String address; // 집주소 필드
	private String email; // 이메일 필드

	public Person(String name, String phoneNum, String address, String email) {
		this.name = name;
		this.phoneNum = phoneNum;
		this.address = address;
		this.email = email;
	}

	public Person() {
		this.name = null;
		this.phoneNum = null;
		this.address = null;
		this.email = null;
	}

	public void setName(String name) { // 이름 설정자
		this.name = name;
	}

	public void setPhoneNum(String phoneNum) { // 전화번호 설정자
		this.phoneNum = phoneNum;
	}

	public void setAddress(String address) { // 집주소 설정자
		this.address = address;
	}

	public void setEmail(String email) { // 이메일 설정자
		this.email = email;
	}

	public String getEmail() { // 이메일 접근자
		return email;
	}

	public String getName() { // 이름 접근자
		return name;
	}

	public String getPhoneNum() { // 전화번호 접근자
		return phoneNum;
	}

	public String getAddress() { // 집주소 접근자
		return address;
	}


}
