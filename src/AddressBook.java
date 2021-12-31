import java.io.*;
import java.sql.*;
import java.util.ArrayList;

@SuppressWarnings("unused")

/*
 * 프로그램 이름 : AddressBook.java 프로그램 설명 : 클래스 AddressBook 정의 프로그램 작성일 : 2020-09-08
 * 작성자 : Yujung Lee
 */

public class AddressBook {
	Connection conn = null;
	Statement stmt = null;
	ResultSet rs = null;

	public AddressBook(Connection conn) throws Exception {// UI에서 생성할 사람의 수 가져와서 객체 생성
		Class.forName("org.mariadb.jdbc.Driver"); // 드라이버 연걸
		stmt = conn.createStatement();
	}

	// 동명이인 확인 메소드
	public boolean checkName(String name) throws Exception {

		// 파라미터로 받아온 name을 검색한다.
		rs = stmt.executeQuery("select * from Person where name='" + name + "'");

		if (rs.next()) {
			return true; // true 반환
		} else
			return false;
	}

	// 등록된 전화번호가 있는지 확인 메소드
	public boolean checkPhoneNum(String phoneNum) throws Exception {
		try {
			// 파라미터로 받아온 phoneNum을 검색한다.
			rs = stmt.executeQuery("select * from Person where phoneNum='" + phoneNum + "'");

			if (rs.next()) {
				return true;// true 반환
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false; // 없으면 false
	}

	// 주소록 등록 메소드
	public void add(Person p) throws Exception {
		// 받아온 Person 객체 P를 db에 추가해 준다.
		rs = stmt.executeQuery("insert into Person (name, phoneNum, address, email ) values('" + p.getName() + "', '"
				+ p.getPhoneNum() + "', '" + p.getAddress() + "', '" + p.getEmail() + "');");

	}

	// 주소록 이름 검색 메소드
	public ResultSet searchName(String name) throws SQLException // 이름을 이용한 검색
	{
		rs = stmt.executeQuery("select * from Person where name like '%" + name + "%';");
		return rs;
	}

	// 주소록 번호 검색 메소드
	public ResultSet searchPhoneNum(String phoneNum) throws SQLException // 전화번호를 이용한 검색
	{
		return stmt.executeQuery("select * from Person where phoneNum like '%" + phoneNum + "%';");
	}

	// 주소록 수정 메소드
	public void modify(String name, Person p) throws Exception {
		stmt.executeUpdate("update Person set name='" + p.getName() + "', phoneNum='" + p.getPhoneNum() + "', address='"
				+ p.getAddress() + "', email='" + p.getEmail() + "' where name like '%" + name + "%';");
	}

	// 주소록 삭제 메소드
	public void delete(String name) throws Exception {
		stmt.executeUpdate("delete from Person where name like '%" + name + "%';");
	}

	public ResultSet view() throws Exception {
		rs = stmt.executeQuery("select name, phoneNum, address, email from Person;");
		return rs;
	}

	// 자원정리 메소드
	public void execClose() throws Exception {
		// 자원정리
		if (rs != null)
			try {
				rs.close();
			} catch (SQLException sqle) {
			}
		if (stmt != null)
			try {
				stmt.close();
			} catch (SQLException sqle) {
			}
//		 커넥션 풀로 반납
		if (conn != null)
			try {
				conn.close();
			} catch (SQLException sqle) {
			}
	}

}
