module sources {
  export   module base {
    export class BaseElements {
      public printAll(a: number): void {
        for (let i: number = 0; i < 3; i++) {
          console.log("hey !");
        }
        for (let i: number = 0; i < 3; i++)
          console.log("hey !");

        let i: number = 0;
        while (i < 3) {
          console.log("hey !");
        }
        do {
          console.log("hey !");
        } while (i < 3);
        switch (i) {
          case 1:
            console.log("hey !");
          case 2: {
            console.log("hey !");
          }
          case 3:
          case 4: {
            console.log("hey !");
          }break;
        }
        if (i == 2) {
          console.log("hey !");
        } else if (i == 3) {
          console.log("hey !");
        } else {
          console.log("hey !");
        }

        if (i == 2)
          console.log("hey !");
        else
          console.log("hey !");

      }
    }
  }
}
