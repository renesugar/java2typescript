package sources.base;

/**
 * Created by gnain on 13/09/16.
 */
public class BaseElements {

    public void printAll(int a) {

        for (int i = 0; i < 3; i++) {
            System.out.println("hey !");
        }

        for (int i = 0; i < 3; i++)
            System.out.println("hey !");

        int i = 0;
        while (i < 3) {
            System.out.println("hey !");
        }

        do {
            System.out.println("hey !");
        } while (i < 3);

        switch (i) {
            case 1:
                System.out.println("hey !");
            case 2: {
                System.out.println("hey !");
            }
            case 3:
            case 4: {
                System.out.println("hey !");
            }
            break;
        }

        if (i == 2) {
            System.out.println("hey !");
        } else if (i == 3) {
            System.out.println("hey !");
        } else {
            System.out.println("hey !");
        }

        if (i == 2)
            System.out.println("hey !");
        else
            System.out.println("hey !");

    }


}
