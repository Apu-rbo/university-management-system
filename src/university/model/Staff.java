package university.model;

import university.core.Payable;

public class Staff extends Person implements Payable {

    private String position;
    private double monthlySalary;

    public Staff(String name, String email, int age, String position, double monthlySalary) {
        super(name, email, age);
        this.position = position;
        this.monthlySalary = monthlySalary;
    }

    /** Loading constructor - used by FileHandler to restore a saved Staff member. */
    public Staff(int id, String name, String email, int age, String position, double monthlySalary) {
        super(id, name, email, age);
        this.position = position;
        this.monthlySalary = monthlySalary;
    }

    public double getMonthlySalary() { return monthlySalary; }

    public String getPosition() { return position; }

    @Override
    public double calculateSalary() {
        return monthlySalary; // flat rate - no bonus logic like Professor
    }

    @Override
    public String getRole() {
        return "Staff";
    }

    @Override
    public String displayInfo() {
        return toString() + String.format(" | Position:%s | Salary:$%.2f",
                position, monthlySalary);
    }
}
