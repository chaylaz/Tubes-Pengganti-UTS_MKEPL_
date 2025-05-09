import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class Employee {

	private String employeeId;
	private String firstName;
	private String lastName;
	private String idNumber;
	private String address;
	
	private int yearJoined;
	private int monthJoined;
	private int dayJoined;
	private int monthWorkingInYear;
	
	private boolean isForeigner;
	private boolean gender; //true = Laki-laki, false = Perempuan
	
	private int monthlySalary;
	private int otherMonthlyIncome;
	private int annualDeductible;
	
	private String spouseName;
	private String spouseIdNumber;

	private List<String> childNames;
	private List<String> childIdNumbers;
	
	public Employee(String employeeId, String firstName, String lastName, String idNumber, String address, int yearJoined, int monthJoined, int dayJoined, boolean isForeigner, boolean gender) {
		this.employeeId = employeeId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.idNumber = idNumber;
		this.address = address;
		this.yearJoined = yearJoined;
		this.monthJoined = monthJoined;
		this.dayJoined = dayJoined;
		this.isForeigner = isForeigner;
		this.gender = gender;
		
		childNames = new LinkedList<String>();
		childIdNumbers = new LinkedList<String>();
	}
	
	/**
	 * Fungsi untuk menentukan gaji bulanan pegawai berdasarkan grade kepegawaiannya (grade 1: 3.000.000 per bulan, grade 2: 5.000.000 per bulan, grade 3: 7.000.000 per bulan)
	 * Jika pegawai adalah warga negara asing gaji bulanan diperbesar sebanyak 50%
	 */
	
	// Refactored to improve readability and fix logic bug
	public void setMonthlySalary(int grade) {
		monthlySalary = calculateBaseSalary(grade);
		if (isForeigner) {
			monthlySalary *= 1.5;
		}
	}

	// New method extracted from setMonthlySalary to reduce duplication
	private int calculateBaseSalary(int grade) {
		switch (grade) {
			case 1:
				return 3000000;
			case 2:
				return 5000000;
			case 3:
				return 7000000;
			default:
				throw new IllegalArgumentException("Invalid grade: " + grade);
		}
	}
	
	public void setAnnualDeductible(int deductible) {	
		this.annualDeductible = deductible;
	}
	
	public void setAdditionalIncome(int income) {	
		this.otherMonthlyIncome = income;
	}
	
	public void setSpouse(String spouseName, String spouseIdNumber) {
		this.spouseName = spouseName;
		this.spouseIdNumber = idNumber;
	}
	
	public void addChild(String childName, String childIdNumber) {
		childNames.add(childName);
		childIdNumbers.add(childIdNumber);
	}
	
	public int getAnnualIncomeTax() {
		
		//Menghitung berapa lama pegawai bekerja dalam setahun ini, jika pegawai sudah bekerja dari tahun sebelumnya maka otomatis dianggap 12 bulan.
		LocalDate date = LocalDate.now();
		
		if (date.getYear() == yearJoined) {
			monthWorkingInYear = date.getMonthValue() - monthJoined;
		} else {
			monthWorkingInYear = 12;
		}
		
	// Refactor kecil : pengecekan spouse lebih aman (null-safe)
		boolean isSingle = spouseIdNumber == null || spouseIdNumber.isEmpty();
		int numberOfChildren = childIdNumbers.size();

		return TaxFunction.calculateTax(
			monthlySalary,
			otherMonthlyIncome,
			monthWorkingInYear,
			annualDeductible,
			!isSingle,
			numberOfChildren
		);
	}
		
	// Getter tambahan agar field tidak dianggap unused dan bisa diakses jika perlu

	public String getEmployeeId() {
		return employeeId;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public String getAddress() {
		return address;
	}

	public int getDayJoined() {
		return dayJoined;
	}

	public boolean isMale() {
		return gender;
	}

	public String getSpouseName() {
		return spouseName;
	}

	// Untuk keperluan demo/test
	public static void main(String[] args) {
		Employee emp = new Employee("001", "Budi", "Santoso", "123456", "Jl. Melati", 2022, 4, 10, false, true);
		emp.setMonthlySalary(2); // Grade 2
		emp.setAnnualDeductible(2000000);
		emp.setAdditionalIncome(1000000);
		emp.setSpouse("Siti", "654321");
		emp.addChild("Anak1", "999001");
		emp.addChild("Anak2", "999002");

		System.out.println("Nama Pegawai: " + emp.getFirstName() + " " + emp.getLastName());
		System.out.println("Pajak Tahunan: Rp " + emp.getAnnualIncomeTax());
	}
}
