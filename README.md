# University Management System

A Java console (and later GUI) application for managing students, professors,
staff, and courses at a university, built to demonstrate all four pillars of
Object-Oriented Programming.

## Status: Milestone 3 — GUI Complete ✅ (project feature-complete)

- [x] Model classes (`Person`, `Student`, `Professor`, `Staff`, `Course`)
- [x] Core interfaces (`Payable`, `Enrollable`)
- [x] `UniversityManager` (Singleton, in-memory store + lookups + payroll)
- [x] File-based persistence (`FileHandler` — save/load to `data/*.txt`)
- [x] Console menu (`Main.java` → `ConsoleUI`)
- [x] GUI (`GuiMain.java` → Swing, tabbed interface)

## How the OOP pillars show up here

**Abstraction**
`Person` is an `abstract class` — you can never create a bare `Person`,
only a `Student`, `Professor`, or `Staff`. `Payable` and `Enrollable` are
interfaces that describe *what* something can do (get paid, enroll in a
course) without saying *how*. `FileHandler` hides the on-disk text format
completely from the rest of the app.

**Encapsulation**
All fields in every class are `private`. Validation (e.g. GPA must be
0.0–4.0, email must contain `@`) lives inside the setters, not scattered
across the codebase. `UniversityManager`'s constructor is private
(Singleton) so the single instance can't be accidentally duplicated.

**Inheritance**
`Student`, `Professor`, and `Staff` all extend `Person` and inherit
`id`, `name`, `email`, `age`, and the shared `toString()` for free.

**Polymorphism**
- *Override*: `getRole()` and `displayInfo()` mean something different
  for each subclass, but `ConsoleUI` calls them through a single `Person`
  reference and the correct version always runs.
- *Interface polymorphism*: `Professor` and `Staff` both implement
  `Payable.calculateSalary()` with completely different formulas — the
  payroll report doesn't need to know which one it's looking at.
- *Overloading*: each model class has two constructors — one for brand
  new records, one ("loading constructor") that takes an explicit `id`
  for rebuilding objects from saved files.

**Design pattern bonus**
`UniversityManager` uses the **Singleton** pattern — exactly one instance
exists for the whole running app, reached via `getInstance()`.

## Project structure

```
src/university/
├── Main.java        console entry point
├── GuiMain.java     GUI entry point
├── core/            interfaces: Payable, Enrollable
├── model/           Person (abstract), Student, Professor, Staff, Course
├── service/         UniversityManager (Singleton), FileHandler (persistence)
├── ui/              ConsoleUI (text menu)
└── gui/             MainFrame + StudentPanel/ProfessorPanel/StaffPanel/CoursePanel/PayrollPanel (Swing)
```

## Running it

```bash
javac -d bin $(find src -name "*.java")

# console version
java -cp bin university.Main

# GUI version
java -cp bin university.GuiMain
```

Data is saved to `data/*.txt` on exit (either version) and loaded
automatically on startup — they share the same data files, so you can
switch between console and GUI freely.

## Author

Apurbo
