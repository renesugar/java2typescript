declare module 'junit' {
    export class Assert {
        static assertArrayEquals(p:any, p2:any):void;

        static assertNotNull(p:any):void;

        static assertNull(p:any):void;

        static assertEquals(p:any, p2:any):void;

        static assertNotEquals(p:any, p2:any):void;

        static assertTrue(b:boolean):void;

        static assertFalse(b:boolean):void;
    }
}
