/**
 * Copyright 2017 The Java2TypeScript Authors.  All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
