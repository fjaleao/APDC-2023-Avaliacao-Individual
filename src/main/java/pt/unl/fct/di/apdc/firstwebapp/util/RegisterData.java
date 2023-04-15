package pt.unl.fct.di.apdc.firstwebapp.util;

public class RegisterData {

	public static final String USERNAME = "user_id";
	public static final String NAME = "user_name";
	public static final String PASSWORD = "user_pwd";
	public static final String EMAIL = "user_email";
	public static final String CREATION_TIME = "user_creation_time";
	public static final String TYPE = "user_type";
	public static final String STATE = "user_state_activated";

	public static final String VISIBILITY = "user_visibility";
	public static final String MOBILE = "user_mobile_phone_number";
	public static final String PHONE = "user_phone_number";
	public static final String OCCUPATION = "user_occupation";
	public static final String WORK_ADDRESS = "user_work_address";
	public static final String ADDRESS = "user_address";
	public static final String SECOND_ADDRESS = "user_second_address";
	public static final String POST_CODE = "user_post_code";
	public static final String NIF = "user_nif";

	
	private String username;
	private String name;
	private String email;
	private String password;

	private boolean visible;
	private String mobilePhoneNumber;
	private String phoneNumber;
	private String occupation;
	private String workAddress;
	private String address;
	private String secondAddress;
	private String postCode;
	private String nif;

	
	public RegisterData() {
		
	}

	public RegisterData(String username, String name, String email, String password, boolean visible, String mobilePhoneNumber,
			String phoneNumber, String occupation, String workAddress, String address, String secondAddress,
			String postCode, String nif) {
		this.username = username;
		this.name = name;
		this.email = email;
		this.password = password;
		this.visible = visible;
		this.mobilePhoneNumber = mobilePhoneNumber;
		this.phoneNumber = phoneNumber;
		this.occupation = occupation;
		this.workAddress = workAddress;
		this.address = address;
		this.secondAddress = secondAddress;
		this.postCode = postCode;
		this.nif = nif;
	}

	public boolean isValid() {
		return !(this.username == null || this.password == null || this.email == null);
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @return the visible
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * @return the mobilePhoneNumber
	 */
	public String getMobilePhoneNumber() {
		return mobilePhoneNumber;
	}

	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * @return the occupation
	 */
	public String getOccupation() {
		return occupation;
	}

	/**
	 * @return the workAddress
	 */
	public String getWorkAddress() {
		return workAddress;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @return the secondAddress
	 */
	public String getSecondAddress() {
		return secondAddress;
	}

	/**
	 * @return the postCode
	 */
	public String getPostCode() {
		return postCode;
	}

	/**
	 * @return the nif
	 */
	public String getNif() {
		return nif;
	}
	
	

}
