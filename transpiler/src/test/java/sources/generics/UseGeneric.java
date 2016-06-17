package sources.generics;

public class UseGeneric {

    MyList<String> strList;
    MyMap<String, Number> map;

    public UseGeneric() {
        this.strList = new MyArrayList<>();
        this.map = new MyHashMap<>();
    }

    public void arrayAsGenType(MyList<MyList[]> list) {
    }

    public static int LEVELA = 0;
    public static int LEVELB = LEVELA;
    public static String LEVEL = new String("" + LEVELA);
}
