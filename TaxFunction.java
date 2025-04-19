public class TaxFunction {

	private static final int BASIC_NON_TAXABLE_INCOME = 54000000;
	private static final int MARRIED_ADDITION = 4500000;
	private static final int CHILD_ADDITION = 1500000;
	private static final int MAX_CHILDREN_COUNT = 3;
	private static final double TAX_RATE = 0.05;

	/**
	 * Refactored:
	 * - Ekstrak angka-angka ke constant
	 * - Pisahkan logika panjang ke method tambahan untuk keterbacaan
	 * - Hindari duplikasi
	 */
	public static int calculateTax(int monthlySalary, int otherMonthlyIncome, int numberOfMonthWorking,
			int deductible, boolean isMarried, int numberOfChildren) {

		if (numberOfMonthWorking > 12) {
			System.err.println("More than 12 month working per year");
		}

		int netIncome = calculateNetIncome(monthlySalary, otherMonthlyIncome, numberOfMonthWorking, deductible);
		int nonTaxableIncome = calculateNonTaxableIncome(isMarried, numberOfChildren);

		int taxableIncome = netIncome - nonTaxableIncome;
		int tax = (int) Math.round(TAX_RATE * taxableIncome);

		return Math.max(tax, 0);
	}

	// New method untuk menghitung penghasilan bersih
	private static int calculateNetIncome(int salary, int otherIncome, int monthsWorked, int deductible) {
		return (salary + otherIncome) * monthsWorked - deductible;
	}

	// New method untuk menghitung PTKP (penghasilan tidak kena pajak)
	private static int calculateNonTaxableIncome(boolean isMarried, int childrenCount) {
		int nonTaxable = BASIC_NON_TAXABLE_INCOME;

		if (isMarried) {
			nonTaxable += MARRIED_ADDITION;
		}

		nonTaxable += Math.min(childrenCount, MAX_CHILDREN_COUNT) * CHILD_ADDITION;
		return nonTaxable;
	}
}
