import java.io.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

public class GUI extends JFrame implements ActionListener {

	private JButton saveButton1, modifyButton, modButton, endButton, deleteButton, addButton, endButton2, searchButton,
			startButton, delButton;
	private JPanel contentPane, mainPanel, infoPanel, searchPanel, infoPanel2, showPanel, modifyPanel, addPanel;
	private JTextField searchField, nameField, phonNumField, addressField, emailField, nameField2, phonNumField2,
			addressField2, emailField2;
	private JLabel addLabel, addnameLabel, addPhonNumLabel, addAddressLabel, addEmailLabel, viewname, viewNum,
			viewAddress, viewEmail, addNotice, searchLabel, modifyName, modifyNum, modifyAddress, modifyEmail, nameInfo,
			numInfo, addressInfo, emailInfo;
	private JTabbedPane menutabbedPane;
	private JTable table;
	private DefaultTableModel model;
	private JComboBox comboBox;
	private Object data[][] = {};
	String colNames[] = { "주소록 번호", "이름", "전화번호", "집주소", "이메일" };// 행 데이터
	Person p; // Person 객체를 받아 저장할 p
	AddressBook ad = null;
	private String name, phoneNum, address, email, search, field;
	private JLabel noticeInfo;
	int tableCount;
	String nameF, numF;
	ResultSet rs = null;

	/**
	 * Launch the application. main 함수
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI frame = new GUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame. 프레임 구성 / gui 구성
	 * 
	 * @throws Exception
	 */
	public GUI() throws Exception {

		Connection conn = null;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/addressbook", "root", "0527");
			ad = new AddressBook(conn);
		} catch (SQLException se) { // SQL 익셉션
			se.printStackTrace();
		} catch (Exception e4) {
			e4.printStackTrace();
		}
		// frame, contentPane 구성//
		JFrame frame = new JFrame("주소록"); // Frame 생성
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setBounds(100, 100, 700, 450);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		/* ========================== 메인화면 패널 ========================== */
		mainPanel = new JPanel();
		mainPanel.setBounds(0, 0, 694, 390);
		contentPane.add(mainPanel);
		mainPanel.setLayout(null);

		// 메뉴 시작 버튼
		startButton = new JButton("메뉴");
		startButton.setBounds(120, 323, 151, 47);
		mainPanel.add(startButton);
		startButton.addActionListener(this);

		// 주소록 화면 라벨
		JLabel lblNewLabel = new JLabel("주소록");
		lblNewLabel.setBounds(260, 87, 211, 183);
		mainPanel.add(lblNewLabel);
		lblNewLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 70));

		// 종료 버튼
		endButton = new JButton("종료");
		endButton.setBounds(432, 323, 151, 47);
		mainPanel.add(endButton);
		endButton.addActionListener(this);

		// TabbedPane 구성
		menutabbedPane = new JTabbedPane(JTabbedPane.TOP);
		menutabbedPane.setBounds(0, 0, 700, 365);
		contentPane.add(menutabbedPane);

		// menutabbedPane은 메뉴 버튼을 누르기 전까지 보이지 않는다.
		menutabbedPane.setVisible(false);

		// 종료버튼2
		endButton2 = new JButton("프로그램 종료");
		endButton2.setBounds(526, 360, 117, 29);
		contentPane.add(endButton2);
		endButton2.addActionListener(this);

		/* ======================== 주소록 등록 패널======================== */
		addPanel = new JPanel();
		menutabbedPane.addTab("주소록 추가", null, addPanel, null);
		addPanel.setLayout(null);

		// ----입력 받을 필드들----
		nameField = new JTextField();
		nameField.setBounds(101, 87, 130, 26);
		addPanel.add(nameField);
		nameField.setColumns(10);

		phonNumField = new JTextField();
		phonNumField.setBounds(101, 125, 130, 26);
		addPanel.add(phonNumField);
		phonNumField.setColumns(10);

		addressField = new JTextField();
		addressField.setBounds(101, 163, 130, 26);
		addPanel.add(addressField);
		addressField.setColumns(10);

		emailField = new JTextField();
		emailField.setBounds(101, 201, 130, 26);
		addPanel.add(emailField);
		emailField.setColumns(10);

		// ----정보 라벨들----
		addLabel = new JLabel("등록할 사람의 정보를 입력해주세요.");
		addLabel.setBounds(28, 35, 239, 26);
		addPanel.add(addLabel);

		addnameLabel = new JLabel("이름");
		addnameLabel.setBounds(28, 92, 61, 16);
		addPanel.add(addnameLabel);

		addPhonNumLabel = new JLabel("번호");
		addPhonNumLabel.setBounds(28, 130, 61, 16);
		addPanel.add(addPhonNumLabel);

		addAddressLabel = new JLabel("주소");
		addAddressLabel.setBounds(28, 168, 61, 16);
		addPanel.add(addAddressLabel);

		addEmailLabel = new JLabel("이메일");
		addEmailLabel.setBounds(28, 206, 61, 16);
		addPanel.add(addEmailLabel);

		// 입력 버튼
		addButton = new JButton("입력하기");
		addButton.setBounds(114, 239, 117, 29);
		addPanel.add(addButton);
		addButton.addActionListener(this); // 이벤트 추가

		// ----입력 정보 보여줄 패널, 패널 위 라벨들----
		infoPanel = new JPanel();
		infoPanel.setBounds(374, 83, 239, 164);
		infoPanel.setBackground(Color.WHITE);
		addPanel.add(infoPanel);
		infoPanel.setLayout(null);

		viewname = new JLabel("이름:");
		viewname.setBounds(6, 0, 233, 30);
		infoPanel.add(viewname);

		viewNum = new JLabel("전화번호: ");
		viewNum.setBounds(6, 28, 233, 30);
		infoPanel.add(viewNum);

		viewAddress = new JLabel("주소:");
		viewAddress.setBounds(6, 55, 233, 30);
		infoPanel.add(viewAddress);

		viewEmail = new JLabel("이메일:");
		viewEmail.setBounds(6, 82, 233, 35);
		infoPanel.add(viewEmail);

		addNotice = new JLabel("입력한 정보를 보여줍니다.");
		addNotice.setBounds(6, 142, 204, 16);
		infoPanel.add(addNotice);

		/*------------------- 주소록 검색 탭 패널 -------------------*/
		searchPanel = new JPanel();
		menutabbedPane.addTab("주소록 검색/수정/삭제", null, searchPanel, null);
		searchPanel.setLayout(null);

		searchLabel = new JLabel("검색/수정/삭제할 사람의 정보를 입력해주세요.");
		searchLabel.setBounds(20, 20, 239, 26);
		searchPanel.add(searchLabel);

		// 검색어 입력받을 필드
		searchField = new JTextField();
		searchField.setBounds(100, 52, 131, 26);
		searchPanel.add(searchField);
		searchField.setColumns(10);

		// 콤보박스
		comboBox = new JComboBox();
		comboBox.setBounds(5, 52, 95, 27);
		comboBox.addItem("이름");
		comboBox.addItem("전화번호");
		searchPanel.add(comboBox);

		// ---- 검색 화면 패널, 패널에 들어갈 라벨들 ----
		infoPanel2 = new JPanel();
		infoPanel2.setBounds(375, 83, 239, 164);
		infoPanel2.setBackground(Color.WHITE);
		searchPanel.add(infoPanel2);
		infoPanel2.setLayout(null);

		nameInfo = new JLabel("이름:");
		nameInfo.setBounds(6, 0, 233, 30);
		infoPanel2.add(nameInfo);

		numInfo = new JLabel("전화번호: ");
		numInfo.setBounds(6, 28, 233, 30);
		infoPanel2.add(numInfo);

		addressInfo = new JLabel("주소:");
		addressInfo.setBounds(6, 55, 233, 30);
		infoPanel2.add(addressInfo);

		emailInfo = new JLabel("이메일:");
		emailInfo.setBounds(6, 82, 233, 35);
		infoPanel2.add(emailInfo);

		noticeInfo = new JLabel("검색 정보를 보여주는 창입니다.");
		noticeInfo.setBounds(6, 132, 233, 35);
		infoPanel2.add(noticeInfo);

		/*----------수정 패널-----------*/ // 검색 패널 위에 들어간다
		modifyPanel = new JPanel();
		modifyPanel.setBounds(0, 80, 264, 300);
		searchPanel.add(modifyPanel);
		modifyPanel.setLayout(null);
		modifyPanel.setVisible(false); // 수정 버튼을 누르기 전까지 보이지 않게 함

		// ---- 수정 라벨 ----
		modifyName = new JLabel("이름");
		modifyName.setBounds(28, 20, 61, 16);
		modifyPanel.add(modifyName);

		modifyNum = new JLabel("번호");
		modifyNum.setBounds(28, 55, 61, 16);
		modifyPanel.add(modifyNum);

		modifyAddress = new JLabel("주소");
		modifyAddress.setBounds(28, 90, 61, 16);
		modifyPanel.add(modifyAddress);

		modifyEmail = new JLabel("이메일");
		modifyEmail.setBounds(28, 125, 61, 16);
		modifyPanel.add(modifyEmail);

		// ---- 수정 텍스트 필드 -----
		nameField2 = new JTextField();
		nameField2.setBounds(101, 20, 130, 26);
		modifyPanel.add(nameField2);
		nameField2.setColumns(10);

		phonNumField2 = new JTextField();
		phonNumField2.setBounds(101, 55, 130, 26);
		modifyPanel.add(phonNumField2);
		phonNumField2.setColumns(10);

		addressField2 = new JTextField();
		addressField2.setBounds(101, 90, 130, 26);
		modifyPanel.add(addressField2);
		addressField2.setColumns(10);

		emailField2 = new JTextField();
		emailField2.setBounds(101, 125, 130, 26);
		modifyPanel.add(emailField2);
		emailField2.setColumns(10);

		// 버튼

		searchButton = new JButton("검색");
		searchButton.setBounds(235, 52, 65, 29);
		searchPanel.add(searchButton);
		searchButton.addActionListener(this); // 이벤트 추가

		modifyButton = new JButton("수정");
		modifyButton.setBounds(393, 251, 65, 29);
		searchPanel.add(modifyButton);
		modifyButton.setVisible(false);
		modifyButton.addActionListener(this); // 이벤트 추가

		modButton = new JButton("수정하기");
		modButton.setBounds(105, 160, 117, 29);
		modifyPanel.add(modButton);
		modButton.addActionListener(this); // 이벤트 추가

		deleteButton = new JButton("삭제");
		deleteButton.setBounds(497, 251, 65, 29);
		searchPanel.add(deleteButton);
		deleteButton.setVisible(false);
		deleteButton.addActionListener(this); // 이벤트 추가

		delButton = new JButton("삭제하기");
		delButton.setBounds(105, 160, 117, 29);
		delButton.setVisible(false);
		searchPanel.add(delButton);
		delButton.addActionListener(this); // 이벤트 추가

		/*------------------- 전체 주소록 조회 탭 패널 -------------------*/
		showPanel = new JPanel();
		menutabbedPane.addTab("전체 주소록 조회", null, showPanel, null);
		showPanel.setLayout(null);

//		 ---- 표 생성 ----
		model = new DefaultTableModel(data, colNames) {
			public boolean isCellEditable(int i, int c) {// table 더블 클릭의 수정을 막음
				return false;
			}
		};
		table = new JTable(model);

		JScrollPane scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);// 스크롤을 붙여주기 위한 객체 scrollPane
		scrollPane.setBounds(20, 26, 630, 257);// 좌표 및 크기
		showPanel.add(scrollPane);// tablePanel에 scrollPane(표) 붙여넣기

		rs = ad.view();
		while (rs.next()) {
			String name = rs.getString("name");
			String phoneNum = rs.getString("phoneNum");
			String address = rs.getString("address");
			String email = rs.getString("email");
			tableCount = table.getRowCount();
			Object[] row = new Object[] { (tableCount + 1) + "번", name, phoneNum, address, email };
			model.addRow(row); // 추가
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		try {
			// 메뉴버튼 (startButton) 누르면 메뉴로 이동
			if (e.getSource() == startButton) {
				mainPanel.setVisible(false);
				menutabbedPane.setVisible(true);
			}
			// 메인 화면의 종료 버튼(endButton) 누르면 종료
			else if (e.getSource() == endButton) {
				System.exit(0);
			}

			/* =================입력 버튼 (addButton) 이벤트================= */
			if (e.getSource() == addButton) {

				name = nameField.getText();
				phoneNum = phonNumField.getText();
				address = addressField.getText();
				email = emailField.getText();
				try {
					// 이름과 번호가 빈 필드일 시
					if (name.equals("") || phoneNum.equals("")) {
						JOptionPane.showMessageDialog(null, "비어있는 정보를 입력해주세요.", "message", JOptionPane.PLAIN_MESSAGE);
					}
					// 동명이인 중복 번호 체크
					else if (ad.checkName(nameField.getText()) == true
							|| ad.checkPhoneNum(phonNumField.getText()) == true) {
						JOptionPane.showMessageDialog(null, "동명이인이 존재합니다. 뒤에 숫자를 붙여 구분해주세요.", "message",
								JOptionPane.PLAIN_MESSAGE);
						// 필드 비워주기
						nameField.setText("");
						phonNumField.setText("");
						addressField.setText("");
						emailField.setText("");
					}

					else {
						ad.add(new Person(name, phoneNum, address, email)); // add 해준다.
						String arr[] = new String[6];
						arr[0] = table.getRowCount() + "번";// (열에 저장된 수 = 표에 저장된 사람 수)
						arr[1] = name;
						arr[2] = phoneNum;
						arr[3] = address;
						arr[4] = email;
						model.addRow(arr);

						// 입력된 정보를 보여준다
						viewname.setText("이름: " + nameField.getText());
						viewNum.setText("전화번호: " + phonNumField.getText());
						viewAddress.setText("주소: " + addressField.getText());
						viewEmail.setText("이메일: " + emailField.getText());
						addNotice.setText("주소록이 입력되었습니다.");

						// field를 빈칸으로 만들어 준다.
						nameField.setText("");
						phonNumField.setText("");
						addressField.setText("");
						emailField.setText("");
					}
				} catch (SQLException se) { // SQL 익셉션
					se.printStackTrace();
				} catch (Exception e4) {
					e4.printStackTrace();
				}
			}

			/* =================검색 버튼(searchButton) 이벤트================= */
			else if (e.getSource() == searchButton) {
				try {
					if (searchField.getText().equals("")) {
						JOptionPane.showMessageDialog(null, "비어있는 정보를 입력해주세요.", "message", JOptionPane.PLAIN_MESSAGE);
					} else if (comboBox.getSelectedItem().toString() == "이름") {
						rs = ad.searchName(searchField.getText());
					} else if (comboBox.getSelectedItem().toString() == "전화번호") {
						rs = ad.searchPhoneNum(searchField.getText());
					}

				} catch (SQLException se) { // SQL 익셉션
					JOptionPane.showMessageDialog(null, "일치하는 주소록이 없습니다.", "message", JOptionPane.PLAIN_MESSAGE);
					se.printStackTrace();
				} catch (Exception e6) {
					// 일치하는 이름이 없으면 수정, 삭제 버튼 안보여줌
					// searchField 비우기
					JOptionPane.showMessageDialog(null, "일치하는 주소록이 없습니다.", "message", JOptionPane.PLAIN_MESSAGE);
					searchField.setText("");
					deleteButton.setVisible(false);
					modifyButton.setVisible(false);
					// 메시지 창
				}

				while (rs.next()) { // rs 읽어오기
					name = rs.getString("name");
					phoneNum = rs.getString("phoneNum");
					address = rs.getString("address");
					email = rs.getString("email");
				}

				if (name == null) {
					JOptionPane.showMessageDialog(null, "일치하는 주소록이 없습니다.", "message", JOptionPane.PLAIN_MESSAGE);
				} else {
					// 정보를 보여준다
					nameInfo.setText("이름: " + name);
					numInfo.setText("전화번호: " + phoneNum);
					addressInfo.setText("주소: " + address);
					emailInfo.setText("이메일: " + email);
					noticeInfo.setText("검색한 정보입니다");
					deleteButton.setVisible(true);
					modifyButton.setVisible(true);
					 nameF = name;
				}

				// 수정필드에 값을 넣어준다.
				nameField2.setText(name);
				phonNumField2.setText(phoneNum);
				addressField2.setText(address);
				emailField2.setText(email);

			}

			/* =================수정 버튼(modifybutton) 이벤트================= */
			else if (e.getSource() == modifyButton) {// 수정 버튼 누르면 수정 창 띄움
				modifyPanel.setVisible(true);
				delButton.setVisible(false);
			}
			// 수정하기 버튼 => 정보 수정 (modButton) 이벤트
			else if (e.getSource() == modButton) { // 수정하기 이벤트
				name = nameField2.getText();
				phoneNum = phonNumField2.getText();
				address = addressField2.getText();
				email = emailField2.getText();


				// 이름과 번호가 빈 필드일 시
				if (name.equals("") || phoneNum.equals("")) {
					JOptionPane.showMessageDialog(null, "비어있는 정보를 입력해주세요.", "message", JOptionPane.PLAIN_MESSAGE);
				}
				// 중복 체크 
				else if (ad.checkName(name) && !name.equals(nameF)) {
					JOptionPane.showMessageDialog(null, "동명이인이 존재합니다. 뒤에 숫자를 붙여 구분해주세요.", "message",
							JOptionPane.PLAIN_MESSAGE);
				}
				else if (ad.checkName(phoneNum) && !phoneNum.equals(numF)) {
					JOptionPane.showMessageDialog(null, "중복된 번호가 존재합니다. 뒤에 숫자를 붙여 구분해주세요.", "message",
							JOptionPane.PLAIN_MESSAGE);
				}
				else {
					try {
						p = new Person(name, phoneNum, address, email);
						field = searchField.getText();
						ad.modify(field, p); // 주소록 수정
						// 테이블 수정
						for (int i = 0; i < model.getRowCount(); i++) {
							if (field.equals(table.getValueAt(i, 1))) {
								model.setValueAt(name, i, 1);
								model.setValueAt(phoneNum, i, 2);
								model.setValueAt(address, i, 3);
								model.setValueAt(email, i, 4);
							}
						}
					} catch (SQLException se) { // SQL 익셉션
						se.printStackTrace();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					// infoPanel 에 보여준다
					nameInfo.setText("이름: " + name);
					numInfo.setText("전화번호: " + phoneNum);
					addressInfo.setText("주소: " + address);
					emailInfo.setText("이메일: " + email);
					noticeInfo.setText("연락처가 위와 같이 수정되었습니다. ");
					searchField.setText("");
					modifyPanel.setVisible(false);
					modifyButton.setVisible(false);
					deleteButton.setVisible(false);
				}
			}

			/* =================삭제 버튼(deleteButton) 이벤트================= */
			else if (e.getSource() == deleteButton) { // 삭제 이벤트
				modifyPanel.setVisible(false);
				int exitOption = JOptionPane.showConfirmDialog(null, "삭제하시겠습니까?", "삭제완료", JOptionPane.YES_NO_OPTION);
				// YES_OPTION은 0, NO_OPTION은 1, CLOSED_OPTION은 -1을 반환한다
				if (exitOption == JOptionPane.YES_OPTION) {
					try {
						String field = searchField.getText();
						ad.delete(field); // 삭제
						// 테이블에서 삭제
						for (int i = 0; i < model.getRowCount(); i++) {
							if (field.equals(table.getValueAt(i, 1)))
								model.removeRow(i);
						}
					} catch (SQLException e1) {
						e1.printStackTrace();
					} catch (Exception e2) {
						e2.printStackTrace();
					}

					nameInfo.setText("");
					numInfo.setText("");
					addressInfo.setText("");
					emailInfo.setText("");
					searchField.setText("");
					noticeInfo.setText("검색 정보를 보여주는 창입니다.");
					modifyButton.setVisible(false);
					deleteButton.setVisible(false);
				} else if ((exitOption == JOptionPane.NO_OPTION) || (exitOption == JOptionPane.CLOSED_OPTION)) {
					return; // 아무 작업도 하지 않고 다이얼로그 상자를 닫는다
				}
			}

			// contentPane의 종료 버튼(endButton2) 이벤트
			else if (e.getSource() == endButton2) {
				// JOptionPane의 ConfirmDialog는 결과에 따라 정수값을 반환한다.
				int exitOption = JOptionPane.showConfirmDialog(null, "종료하시겠습니까?", "프로그램 종료", JOptionPane.YES_NO_OPTION);
				// YES_OPTION은 0, NO_OPTION은 1, CLOSED_OPTION은 -1을 반환한다
				if (exitOption == JOptionPane.YES_OPTION) {
					ad.execClose();

					System.exit(JFrame.EXIT_ON_CLOSE); // 프레임을 종료한다
				} else if ((exitOption == JOptionPane.NO_OPTION) || (exitOption == JOptionPane.CLOSED_OPTION)) {
					return; // 아무 작업도 하지 않고 다이얼로그 상자를 닫는다
				}

			}

		} catch (Exception e1) {
			System.out.println("오류가 발생했습니다. ");
		}

	}// actionPerformed()

}// GUI()
