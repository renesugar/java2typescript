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
package java2typescript.translators.expression;

import com.intellij.psi.*;
import java2typescript.context.TranslationContext;

/**
 * Created by duke on 11/6/14.
 */
public class ExpressionTranslator {

    public static void translate(PsiExpression expression, TranslationContext ctx) {
        if (expression instanceof PsiLiteralExpression) {
            LiteralTranslator.translate((PsiLiteralExpression) expression, ctx);
        } else if (expression instanceof PsiNewExpression) {
            NewExpressionTranslator.translate((PsiNewExpression) expression, ctx);
        } else if (expression instanceof PsiBinaryExpression) {
            BinaryExpressionTranslator.translate((PsiBinaryExpression) expression, ctx);
        } else if (expression instanceof PsiReferenceExpression) {
            ReferenceExpressionTranslator.translate((PsiReferenceExpression) expression, ctx);
        } else if (expression instanceof PsiPrefixExpression) {
            PrefixExpressionTranslator.translate((PsiPrefixExpression) expression, ctx);
        } else if (expression instanceof PsiTypeCastExpression) {
            TypeCastExpressionTranslator.translate((PsiTypeCastExpression) expression, ctx);
        } else if (expression instanceof PsiAssignmentExpression) {
            AssignmentExpressionTranslator.translate((PsiAssignmentExpression) expression, ctx);
        } else if (expression instanceof PsiMethodCallExpression) {
            MethodCallExpressionTranslator.translate((PsiMethodCallExpression) expression, ctx);
        } else if (expression instanceof PsiThisExpression) {
            ThisExpressionTranslator.translate((PsiThisExpression) expression, ctx);
        } else if (expression instanceof PsiPolyadicExpression) {
            PolyadicExpressionTranslator.translate((PsiPolyadicExpression) expression, ctx);
        } else if (expression instanceof PsiParenthesizedExpression) {
            ParenthesizedExpressionTranslator.translate((PsiParenthesizedExpression) expression, ctx);
        } else if (expression instanceof PsiPostfixExpression) {
            PostfixExpressionTranslator.translate((PsiPostfixExpression) expression, ctx);
        } else if (expression instanceof PsiArrayAccessExpression) {
            ArrayAccessExpressionTranslator.translate((PsiArrayAccessExpression) expression, ctx);
        } else if (expression instanceof PsiArrayInitializerExpression) {
            ArrayInitializerExpressionTranslator.translate((PsiArrayInitializerExpression) expression, ctx);
        } else if (expression instanceof PsiConditionalExpression) {
            ConditionalExpressionTranslator.translate((PsiConditionalExpression) expression, ctx);
        } else if (expression instanceof PsiInstanceOfExpression) {
            InstanceOfExpressionTranslator.translate((PsiInstanceOfExpression) expression, ctx);
        } else if (expression instanceof PsiSuperExpression) {
            SuperExpressionTranslator.translate((PsiSuperExpression) expression, ctx);
        } else if (expression instanceof PsiClassObjectAccessExpression) {
            ClassObjectAccessExpressionTranslator.translate((PsiClassObjectAccessExpression) expression, ctx);
        } else if (expression instanceof PsiLambdaExpression) {
            LambdaExpressionTranslator.translate((PsiLambdaExpression) expression, ctx);
        } else {
            System.err.println("EXPR " + expression);
        }
    }

}
