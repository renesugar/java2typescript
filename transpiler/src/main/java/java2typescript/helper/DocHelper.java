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
package java2typescript.helper;

import com.intellij.psi.PsiElement;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.javadoc.PsiDocTag;
import com.intellij.psi.javadoc.PsiInlineDocTag;
import java2typescript.metas.DocMeta;
import java2typescript.translators.DocTagTranslator;

import java.util.ArrayList;

public class DocHelper {

    public static DocMeta process(PsiDocComment comment) {
        DocMeta metas = new DocMeta();
        if (comment != null) {
            ArrayList<PsiDocTag> tags = new ArrayList<>();
            //Collects usual tags
            PsiDocTag[] usualTags = comment.getTags();
            for (int i = 0; i < usualTags.length; i++) {
                tags.add(usualTags[i]);
            }
            //Collects inline tags
            for (PsiElement child : comment.getChildren()) {
                if (child instanceof PsiInlineDocTag) {
                    tags.add((PsiInlineDocTag) child);
                }
            }
            for (PsiDocTag tag : tags) {
                if (tag.getName().equals(DocTagTranslator.NATIVE) && tag.getValueElement() != null && tag.getValueElement().getText().equals(DocTagTranslator.TS)) {
                    metas.nativeActivated = true;
                    String value = tag.getText();
                    String[] lines = value.split("\n");
                    int curlyBraceCounter = 0;
                    for (String line : lines) {
                        String trimmedLine = line.trim();
                        if (trimmedLine.length() > 0 && !trimmedLine.contains("@" + DocTagTranslator.NATIVE) && !trimmedLine.contains("@" + DocTagTranslator.IGNORE) && !trimmedLine.contains("@" + DocTagTranslator.EXTEND)) {
                            if (trimmedLine.charAt(0) == '*') {
                                trimmedLine = trimmedLine.substring(1).trim();
                            }
                            int idx = 0;
                            int nextIdx;
                            while ((nextIdx = trimmedLine.indexOf('{', idx)) != -1) {
                                curlyBraceCounter++;
                                idx = nextIdx + 1;
                            }
                            idx = 0;
                            while ((nextIdx = trimmedLine.indexOf('}', idx)) != -1) {
                                curlyBraceCounter--;
                                idx = nextIdx + 1;
                            }
                            if (trimmedLine.trim().equals("}") && curlyBraceCounter == -1) {
                                trimmedLine = "";
                            }
                            if (!trimmedLine.isEmpty()) {
                                if (metas.nativeBodyLines == null) {
                                    metas.nativeBodyLines = new ArrayList<>();
                                }
                                metas.nativeBodyLines.add(trimmedLine + "\n");
                            }
                        }
                    }
                }
                if (tag.getName().equals(DocTagTranslator.EXTEND) && tag.getValueElement() != null && tag.getValueElement().getText().equals(DocTagTranslator.TS)) {
                    metas.extend = true;
                    String value = tag.getText();
                    String[] lines = value.split("\n");
                    int curlyBraceCounter = 0;
                    for (String line : lines) {
                        String trimmedLine = line.trim();
                        if (trimmedLine.length() > 0 && !trimmedLine.contains("@" + DocTagTranslator.NATIVE) && !trimmedLine.contains("@" + DocTagTranslator.IGNORE) && !trimmedLine.contains("@" + DocTagTranslator.EXTEND)) {
                            if (trimmedLine.charAt(0) == '*') {
                                trimmedLine = trimmedLine.substring(1).trim();
                            }
                            int idx = 0;
                            int nextIdx;
                            while ((nextIdx = trimmedLine.indexOf('{', idx)) != -1) {
                                curlyBraceCounter++;
                                idx = nextIdx + 1;
                            }
                            idx = 0;
                            while ((nextIdx = trimmedLine.indexOf('}', idx)) != -1) {
                                curlyBraceCounter--;
                                idx = nextIdx + 1;
                            }
                            if (trimmedLine.trim().equals("}") && curlyBraceCounter == -1) {
                                trimmedLine = "";
                            }
                            if (!trimmedLine.isEmpty()) {
                                if (metas.nativeBodyLines == null) {
                                    metas.nativeBodyLines = new ArrayList<>();
                                }
                                metas.nativeBodyLines.add(trimmedLine + "\n");
                            }
                        }
                    }
                }
                if (tag.getName().equals(DocTagTranslator.IGNORE) && tag.getValueElement() != null && tag.getValueElement().getText().equals(DocTagTranslator.TS)) {
                    metas.ignored = true;
                }
                if (tag.getName().equals(DocTagTranslator.OPTIONAL)) {
                    for (PsiElement elem : tag.getDataElements()) {
                        if (!elem.getText().contains(" ")) {
                            metas.optional.add(elem.getText());
                        }
                    }
                }
                if (tag.getName().equals(DocTagTranslator.TS_CALLBACK)) {
                    metas.functionType = true;
                }
            }
        }
        return metas;
    }
}
