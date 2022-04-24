public class Employee {
    public long id;
    public String firstName;
    public String lastName;
    public String country;
    public int age;

    public Employee() {
        // Пустой конструктор
    }

    public Employee(long id, String firstName, String lastName, String country, int age) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.country = country;
        this.age = age;
    }

    @Override
    public String toString() {
        return "id : " + id + NL() +
                "firstName : " + firstName + NL() +
                "lastName : " + lastName + NL() +
                "country : " + country + NL() +
                "age : " + age + NL();
    }

    public String NL(){
        return "\n";
    }
}