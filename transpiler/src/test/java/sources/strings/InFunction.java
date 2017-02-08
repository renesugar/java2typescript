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
package sources.strings;

public class InFunction {

    private String str;
    private String str2;

    public void foo() {
        String str = new String("foo");
        String str2 = String.join(", ", new String[] { "a", "b", "c" });
        String str3 = str2.trim();
    }

    public void bar() {
        this.str = this.str2.concat(this.str);
        String s = getString();
        String s2 = getString().replace('o', 'a');
        String s3 = this.getF().getF().getString();

        s3.startsWith("a");

        String[] tt = new String[]{"f1", "f2"};
        essay(new String[]{"f1", "f2"});

    }

    public String getString() {
        return "potato";
    }

    public InFunction getF() {
        return this;
    }


    public void essay(String[] res) {

    }

}