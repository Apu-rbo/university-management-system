package university.model;

/**
 * Abstract base class for every person in the university.
 *
 * ABSTRACTION: you can never instantiate a bare "Person" - it only
 *              exists as a Student, Professor, or Staff member.
 * ENCAPSULATION: fields are private; access/validation only through
 *              getters and setters.
 */
public abstract class Person {

    private static int idCounter = 1000; // shared across all people

    private final int id;
    private String name;
    private String email;
    private int age;

    public Person(String name, String email, int age) {
        this.id = idCounter++;
        this.name = name;
        setEmail(email);
        setAge(age);
    }

    /**
     * "Loading" constructor: used only when reconstructing a person from a
     * saved file, so the original ID is preserved instead of generating a
     * new one. Also nudges idCounter forward so future new people never
     * collide with an ID that was loaded from disk.
     */
    protected Person(int id, String name, String email, int age) {
        this.id = id;
        this.name = name;
        setEmail(email);
        setAge(age);
        if (id >= idCounter) {
            idCounter = id + 1;
        }
    }

    // ---- Encapsulation: validation lives inside the setter, never
    //      trusted to whoever is calling it from outside ----
    public void setEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Invalid email: " + email);
        }
        this.email = email;
    }

    public void setAge(int age) {
        if (age < 0 || age > 120) {
            throw new IllegalArgumentException("Invalid age: " + age);
        }
        this.age = age;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.name = name;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public int getAge() { return age; }

    // ---- Abstraction: every subclass MUST provide its own answer ----
    public abstract String getRole();
    public abstract String displayInfo();

    // ---- Polymorphism: each subclass inherits this, but getRole()
    //      inside it resolves to the SUBCLASS's version at runtime ----
    @Override
    public String toString() {
        return String.format("[%s] ID:%d | Name:%s | Email:%s | Age:%d",
                getRole(), id, name, email, age);
    }
}
