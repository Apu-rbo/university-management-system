package university.core;

/**
 * Abstraction: any class that implements this contract guarantees
 * it knows how to calculate its own salary, but each class is free
 * to calculate it completely differently (see Professor vs Staff).
 */
public interface Payable {
    double calculateSalary();
}
