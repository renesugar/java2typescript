package sample;

public class HelloWorld {

    public String sayHello() {
        return "HelloWorld";
    }

    public static void main(String[] args) {
        System.out.println(new HelloWorld().sayHello());
    }

}
