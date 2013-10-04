/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.tools.metrics.service;

import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.core.factory.MetricManagerFactory;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.Metric;
import br.uff.ic.oceano.core.tools.metrics.DerivedMetricManager;
import br.uff.ic.oceano.core.tools.metrics.MetricManager;
import br.uff.ic.oceano.core.tools.metrics.expression.Add;
import br.uff.ic.oceano.core.tools.metrics.expression.Div;
import br.uff.ic.oceano.core.tools.metrics.expression.DoubleValue;
import br.uff.ic.oceano.core.tools.metrics.expression.ExpressionPackage;
import br.uff.ic.oceano.core.tools.metrics.expression.MetricExpression;
import br.uff.ic.oceano.core.tools.metrics.expression.MetricManagerExpression;
import br.uff.ic.oceano.core.tools.metrics.expression.Mult;
import br.uff.ic.oceano.core.tools.metrics.expression.Pow;
import br.uff.ic.oceano.core.tools.metrics.expression.PushdownAutomaton;
import br.uff.ic.oceano.core.tools.metrics.expression.Sqrt;
import br.uff.ic.oceano.core.tools.metrics.expression.Sub;
import br.uff.ic.oceano.core.tools.metrics.expression.Token;
import br.uff.ic.oceano.core.tools.metrics.expression.UnarySub;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author wallace
 */
public class DerivedMetricService {

    public final static int METRIC_EXPRESSION = -5;
    public final static int DOUBLE_VALUE = 0;
    public final static int OPEN_BRACKETS = 1;
    public final static int CLOSE_BRACKETS = 2;
    public final static int ADD = 3;
    public final static int SUB = 4;
    public final static int MULT = 5;
    public final static int DIV = 6;
    public final static int POW = 7;
    public final static int SQRT = 8;
    public final static int METRIC_MANAGER = 9;
    public final static int UNARY_SUB = 44;
    private MetricService metricService = ObjectFactory.getObjectWithDataBaseDependencies(MetricService.class);

    public DerivedMetricManager createDerivedMetric(String metricName, String expression) throws ServiceException {
        LinkedList<ExpressionPackage> list = null;
        list = createExpressionPackages(expression);
        int result = 0;
        if (result == 0) {
            MetricExpression metricExpression = createMetricExpression(list);
            if (metricExpression == null) {
                //System.out.println("Nulo");
            } else {
//                return new DerivedMetricManager(metricName, expression, metricExpression);
            }
        }
        throw new ServiceException("Malformed derived metric expression.");
    }

    public MetricExpression buildExpression(String expression) throws ServiceException {
//        LinkedList<ExpressionPackage> list = null;
//        list = createExpressionPackages(expression);
//        if (list.size() <= 0) {
//            throw new ServiceException("Null Expression.");
//        }
//        if (bracketsAreMalformed(list)) {
//            throw new ServiceException("Malformed brackets.");
//        }
//        if (sqrtIsMalformed(list)) {
//            throw new ServiceException("Malformed sqrt. Please use brackets with srqt.");
//        }
//
//        return createMetricExpression(list);
        MetricExpression metricExpression = null;
        //System.out.println("Passou");
        expression = eliminateSpaces(expression);
        //System.out.println("Passou eliminar");
        Token head = buildTokens(expression);
        //System.out.println("Passou construir");

        PushdownAutomaton automaton = new PushdownAutomaton();
        try {
            head = automaton.expressionReckonigze(head);
            //System.out.println("Passou reconheceu");
            Token aux = head;
            while (aux != null) {
                //System.out.println("simbolo: "+aux.getType());
                aux = aux.getProx();
            }
            head = createTreeExpression(head);
            //System.out.println("criou arvore");
            //System.out.print("Arvore");
            printarvore(head);
            metricExpression = createExpressionFromTree(head);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        return metricExpression;
    }

    private void printarvore(Token token) {

        if (token != null) {

            if (token.getType() == 0) {
                //System.out.println(token.getType()+" : "+token.getDoubleValue());
            } else {
                //System.out.println(token.getType());
            }
            printarvore(token.getLeft());
            printarvore(token.getRight());
        }

    }

    private MetricExpression createExpressionFromTree(Token head) throws Exception {
        MetricExpression metricExpression = null;
        if (head.getType() == DerivedMetricService.SQRT) {
            metricExpression = new Sqrt(createExpressionFromTree(head.getRight()));
        } else {
            if (head.getType() == DerivedMetricService.UNARY_SUB) {
                metricExpression = new UnarySub(createExpressionFromTree(head.getRight()));
            } else {
                if (head.getType() == DerivedMetricService.ADD) {
                    metricExpression = new Add(createExpressionFromTree(head.getLeft()), createExpressionFromTree(head.getRight()));
                } else {
                    if (head.getType() == DerivedMetricService.SUB) {
                        metricExpression = new Sub(createExpressionFromTree(head.getLeft()), createExpressionFromTree(head.getRight()));
                    } else {
                        if (head.getType() == DerivedMetricService.MULT) {
                            metricExpression = new Mult(createExpressionFromTree(head.getLeft()), createExpressionFromTree(head.getRight()));
                        } else {
                            if (head.getType() == DerivedMetricService.DIV) {
                                metricExpression = new Div(createExpressionFromTree(head.getLeft()), createExpressionFromTree(head.getRight()));
                            } else {
                                if (head.getType() == DerivedMetricService.POW) {
                                    metricExpression = new Pow(createExpressionFromTree(head.getLeft()), createExpressionFromTree(head.getRight()));
                                } else {
                                    if (head.getType() == DerivedMetricService.DOUBLE_VALUE) {
                                        metricExpression = new DoubleValue(head.getDoubleValue());
                                    } else {
                                        if (head.getType() == DerivedMetricService.METRIC_MANAGER) {
                                            try {
                                                Metric metric = metricService.getByAcronym(head.getMetricAcronym());
                                                MetricManager metricManager = (MetricManager) MetricManagerFactory.getInstance().getMetricManager(metric);
                                                metricExpression = new MetricManagerExpression(metricManager);
                                            } catch (Exception e) {
                                                throw e;
                                            }
                                        }
                                    }
                                }
                            }

                        }

                    }
                }
            }
        }
        return metricExpression;

    }

    private Token buildTokens(String expression) throws ServiceException {
        char expressionArray[] = expression.toCharArray();
        Token head = null;
        Token aux;
        String subexpression = null;
        int i = 0;
//        System.out.print("expression: " + expression);
        while (i < expressionArray.length) {
            int numberSymbol = getNumberSymbol(expressionArray[i]);
            if (numberSymbol != -1) {
                aux = new Token(numberSymbol);
                i++;
            } else {
                int j = i + 1;
                boolean flag = true;
                while (j < expressionArray.length && flag) {
                    if (getNumberSymbol(expressionArray[j]) != -1) {
                        flag = false;
                    } else {
                        j++;
                    }
                }
                subexpression = expression.substring(i, j);
                aux = getTokenFromSubExpression(subexpression);
                i = j;
                //System.out.println("Expression: "+subexpression);
            }
            if (aux == null) {
                //System.out.println("Expression: "+subexpression);
                throw new ServiceException("Invalid " + subexpression + " expression");
            } else {
                head = insertInTokenList(head, aux);
            }

        }
        return head;
    }

    private Token insertInTokenList(Token head, Token token) {
        Token aux = head;
        if (head == null) {
            head = token;
            head.setProx(null);
        } else {
            while (aux.getProx() != null) {
                aux = aux.getProx();
            }
            token.setProx(null);
            aux.setProx(token);

        }
        return head;
    }

    private Token createTreeExpression(Token head) {
        Token aux = head;
        Token headaux = null;
        Token ant, prox, aux2 = null;
        ant = null;
        prox = null;

        aux2 = head;
        //System.out.println(" sequencia");
        while (aux2 != null) {
            //System.out.print(aux2.getType() + " ");
            aux2 = aux2.getProx();
        }
        //System.out.println();

        while (aux != null) {
            if (aux.getType() == DerivedMetricService.OPEN_BRACKETS) {
                headaux = null;
                headaux = aux.getProx();
                aux2 = headaux;
                aux = aux.getProx();
                int brackets = 1;
                if (aux.getType() == DerivedMetricService.OPEN_BRACKETS) {
                    brackets++;
                }
                while (brackets > 0) {
                    aux = aux.getProx();
                    if (aux.getType() == DerivedMetricService.OPEN_BRACKETS) {
                        brackets++;
                    } else {
                        if (aux.getType() == DerivedMetricService.CLOSE_BRACKETS) {
                            brackets--;
                        }
                    }
                    if (brackets != 0) {
                        aux2 = aux;
                    }
//                    if (brackets != 0) {
//                        //System.out.println("inseriu: "+aux.getType());
//                        headaux=this.insertInTokenList(headaux, aux);
//                    }
                }
//                //System.out.println("Cabeca: "+head.getType());

                aux2.setProx(null);
                Token token = createTreeExpression(headaux);
                aux = aux.getProx();
                if (ant != null) {
                    //System.out.println("O ant e: "+ant.getType());
                } else {
                    //System.out.println("Ant nulo");
                }

                if (aux != null) {
                    //System.out.println("O aux e: "+aux.getType());
                } else {
                    //System.out.println("Aux nulo");
                }


                token.setProx(aux);
                if (ant == null) {
                    head = token;
                } else {
                    ant.setProx(token);
                }
                ant = token;

            } else {
                ant = aux;
                aux = aux.getProx();
            }


        }

        aux = head;
        ant = null;
        while (aux != null) {
            if (aux.getType() == DerivedMetricService.SQRT && (aux.getRight() == null)) {
                prox = aux.getProx();
                aux.setRight(prox);
                //System.out.println("Sqrt1 prox: "+prox.getType());
                aux.setProx(prox.getProx());
                if (ant == null) {
                    head = aux;
                } else {
                    ant.setProx(aux);
                }

                aux = aux.getProx();
            } else {
                ant = aux;
                aux = aux.getProx();
            }
        }

        aux = head;
        ant = null;
        while (aux != null) {
            if (aux.getType() == DerivedMetricService.UNARY_SUB && (aux.getRight() == null)) {
                prox = aux.getProx();
                aux.setRight(prox);
                aux.setProx(prox.getProx());
                if (ant == null) {
                    head = aux;
                } else {
                    ant.setProx(aux);
                }
                aux = aux.getProx();
            } else {
                ant = aux;
                aux = aux.getProx();
            }
        }



        Token ant2 = null;
        ant = null;
        aux = head;
        headaux = null;
        while (aux != null) {
            if (aux.getType() == DerivedMetricService.POW && ((aux.getRight() == null))) {
                prox = aux.getProx();
                aux.setProx(prox.getProx());
                aux.setLeft(ant);
                aux.setRight(prox);
                if (ant2 == null) {
                    head = aux;
                } else {
                    ant2.setProx(aux);

                }
                ant = aux;
                aux = aux.getProx();
            } else {
                ant2 = ant;
                ant = aux;
                aux = aux.getProx();
            }
        }



        ant = null;
        headaux = null;
        ant2 = null;
        aux = head;
        while (aux != null) {
            if ((aux.getType() == DerivedMetricService.MULT || aux.getType() == DerivedMetricService.DIV) && ((aux.getRight() == null))) {
                prox = aux.getProx();
                aux.setProx(prox.getProx());
                aux.setLeft(ant);
                aux.setRight(prox);
                if (ant2 == null) {
                    head = aux;
                } else {
                    ant2.setProx(aux);

                }
                ant = aux;
                aux = aux.getProx();
            } else {
                ant2 = ant;
                ant = aux;
                aux = aux.getProx();
            }
        }

        boolean flag = false;
        ant = null;
        aux = head;
        headaux = null;
        ant2 = null;
        while (aux != null && !flag) {
            if ((aux.getType() == DerivedMetricService.ADD || aux.getType() == DerivedMetricService.SUB) && ((aux.getRight() == null))) {

                prox = aux.getProx();
                aux.setProx(prox.getProx());
                aux.setLeft(ant);
                aux.setRight(prox);
                if (ant2 == null) {
                    head = aux;
                } else {
                    ant2.setProx(aux);

                }
                ant = aux;
                aux = aux.getProx();

//                ant=createTreeExpression(headaux);
//                headaux=null;
//                prox=aux;
//                while(prox!=null){
//                    headaux=this.insertInTokenList(headaux, prox);
//                    prox=prox.getProx();
//                }
//                prox=createTreeExpression(headaux);
//                aux.setLeft(ant);
//                aux.setRight(prox);
//                aux.setProx(null);
//                head=aux;
            } else {
                ant2 = ant;
                ant = aux;
                aux = aux.getProx();
//                head=this.insertInTokenList(headaux, aux);
            }
        }


        //System.out.println("final: "+head.getType());
        return head;
    }

    private MetricExpression createMetricExpression(LinkedList<ExpressionPackage> list) throws ServiceException {
        list = eliminateBrackets(list);
        list = eliminateSqrt(list);
        list = eliminateDoubleValue(list);
        list = eliminateMinus(list);
        list = eliminateMult(list);
        list = eliminateDiv(list);
        list = eliminatePow(list);
        list = eliminateAddandSub(list);
        if (list.size() <= 0) {
            return null;
        }
        return ((ExpressionPackage) list.get(0)).getMetricExpression();
    }

    LinkedList<ExpressionPackage> eliminateBrackets(LinkedList<ExpressionPackage> list) throws ServiceException {

        LinkedList<ExpressionPackage> newList = new LinkedList<ExpressionPackage>();
        Iterator i = list.iterator();
        //eliminate the brackets
        while (i.hasNext()) {
            ExpressionPackage expressionPackage = (ExpressionPackage) i.next();
            if (expressionPackage.getType() == DerivedMetricService.OPEN_BRACKETS) {
                int brackets = 1;
                LinkedList<ExpressionPackage> auxList = new LinkedList<ExpressionPackage>();
                while (brackets > 0) {
                    expressionPackage = (ExpressionPackage) i.next();
                    if (expressionPackage.getType() == DerivedMetricService.OPEN_BRACKETS) {
                        brackets++;
                    } else {
                        if (expressionPackage.getType() == DerivedMetricService.CLOSE_BRACKETS) {
                            brackets--;
                        }
                    }
                    if (brackets != 0) {
                        auxList.add(expressionPackage);
                    }
                }
                MetricExpression metricExpression = createMetricExpression(auxList);
                if (metricExpression == null) {
                    throw new ServiceException("Expression within the brackets has nothing ");
                }
                ExpressionPackage auxExpressionPackage = new ExpressionPackage(metricExpression);
                newList.add(auxExpressionPackage);
            } else {
                newList.add(expressionPackage);
            }
        }
        return newList;
    }

    LinkedList<ExpressionPackage> eliminateSqrt(LinkedList<ExpressionPackage> list) throws ServiceException {
        LinkedList<ExpressionPackage> newList = new LinkedList<ExpressionPackage>();
        Iterator i = list.iterator();
        //eliminate the sqrt
        while (i.hasNext()) {
            ExpressionPackage expressionPackage = (ExpressionPackage) i.next();
            if (expressionPackage.getType() == DerivedMetricService.SQRT) {
                if (!i.hasNext()) {
                    throw new ServiceException("Expression SQRT has not a SubExpression ");
                }
                ExpressionPackage auxExpressionPackage = (ExpressionPackage) i.next();
                if (auxExpressionPackage.getType() != DerivedMetricService.METRIC_EXPRESSION) {
                    throw new ServiceException("Expression SQRT has not a valid SubExpression ");
                }
                MetricExpression metricExpression = new Sqrt(auxExpressionPackage.getMetricExpression());
                expressionPackage = new ExpressionPackage(metricExpression);
                newList.add(expressionPackage);
            } else {
                newList.add(expressionPackage);
            }
        }
        return newList;
    }

    LinkedList<ExpressionPackage> eliminateDoubleValue(LinkedList<ExpressionPackage> list) {
        //set the doubleValue to a MetricExpression
        for (ExpressionPackage expressionPackage : list) {
            if (expressionPackage.getType() == DerivedMetricService.DOUBLE_VALUE) {
                MetricExpression metricExpression = new DoubleValue(expressionPackage.getDoubleValue());
                expressionPackage.setMetricExpression(metricExpression);
                expressionPackage.setType(DerivedMetricService.METRIC_EXPRESSION);
            }
        }
        return list;
    }

    LinkedList<ExpressionPackage> eliminateMinus(LinkedList<ExpressionPackage> list) throws ServiceException {
        LinkedList<ExpressionPackage> newList = new LinkedList<ExpressionPackage>();
        Iterator i = list.iterator();
        //eliminate the minus after *+/ operator
        while (i.hasNext()) {
            ExpressionPackage expressionPackage = (ExpressionPackage) i.next();
            if (expressionPackage.getType() == DerivedMetricService.SUB) {
                ExpressionPackage leftExpressionPackage = null;
                if (newList.size() > 0) {
                    leftExpressionPackage = (ExpressionPackage) newList.removeLast();
                }
                if (!i.hasNext()) {
                    throw new ServiceException("The operator \"-\" has not a subexpression after ");
                }
                ExpressionPackage rightExpressionPackage = (ExpressionPackage) i.next();

                if ((rightExpressionPackage.getType() != DerivedMetricService.METRIC_EXPRESSION)) {
                    throw new ServiceException("The operator \"-\" has a invalid expression after ");
                } else {
                    if (leftExpressionPackage == null) {
                        MetricExpression auxmetricExpression = new DoubleValue(0);
                        MetricExpression metricExpression = new Sub(auxmetricExpression, rightExpressionPackage.getMetricExpression());
                        expressionPackage = new ExpressionPackage(metricExpression);
                        newList.add(expressionPackage);
                    } else {
                        if ((leftExpressionPackage.getType() != DerivedMetricService.METRIC_EXPRESSION)) {
                            if (leftExpressionPackage.getType() == DerivedMetricService.SUB) {
                                throw new ServiceException("Invalid two consecutives operators \"-\" ");
                            }
                            MetricExpression auxmetricExpression = new DoubleValue(0);
                            MetricExpression metricExpression = new Sub(auxmetricExpression, rightExpressionPackage.getMetricExpression());
                            expressionPackage = new ExpressionPackage(metricExpression);
                            newList.add(leftExpressionPackage);
                            newList.add(expressionPackage);
                        } else {
                            newList.add(leftExpressionPackage);
                            newList.add(expressionPackage);
                            newList.add(rightExpressionPackage);
                        }
                    }
                }

            } else {
                newList.add(expressionPackage);
            }
        }
        return newList;
    }

    LinkedList<ExpressionPackage> eliminateMult(LinkedList<ExpressionPackage> list) throws ServiceException {
        LinkedList<ExpressionPackage> newList = new LinkedList<ExpressionPackage>();
        Iterator i = list.iterator();
        //eliminate the * terminal
        while (i.hasNext()) {
            ExpressionPackage expressionPackage = (ExpressionPackage) i.next();
            if (expressionPackage.getType() == DerivedMetricService.MULT) {
                ExpressionPackage leftExpressionPackage = null;
                if (newList.size() <= 0) {
                    throw new ServiceException("The operator \"*\" has not a expression before");
                } else {
                    leftExpressionPackage = (ExpressionPackage) newList.removeLast();
                    if (leftExpressionPackage.getType() != DerivedMetricService.METRIC_EXPRESSION) {
                        throw new ServiceException("The operator \"*\" has not a valid expression before");
                    }
                }
                if (!i.hasNext()) {
                    throw new ServiceException("The operator \"*\" has not a expression after");
                }
                ExpressionPackage rightExpressionPackage = (ExpressionPackage) i.next();
                if (rightExpressionPackage.getType() != DerivedMetricService.METRIC_EXPRESSION) {
                    throw new ServiceException("The operator \"*\" has not a valid expression after");
                }
                MetricExpression metricExpression = new Mult(leftExpressionPackage.getMetricExpression(), rightExpressionPackage.getMetricExpression());
                expressionPackage = new ExpressionPackage(metricExpression);
                newList.add(expressionPackage);
            } else {
                newList.add(expressionPackage);
            }
        }
        return newList;
    }

    LinkedList<ExpressionPackage> eliminateDiv(LinkedList<ExpressionPackage> list) throws ServiceException {
        LinkedList<ExpressionPackage> newList = new LinkedList<ExpressionPackage>();
        Iterator i = list.iterator();
        //eliminate the / terminal
        while (i.hasNext()) {
            ExpressionPackage expressionPackage = (ExpressionPackage) i.next();
            if (expressionPackage.getType() == DerivedMetricService.DIV) {
                ExpressionPackage leftExpressionPackage = null;
                if (newList.size() <= 0) {
                    throw new ServiceException("The operator \"/\" has not a expression before");
                } else {
                    leftExpressionPackage = (ExpressionPackage) newList.removeLast();
                    if (leftExpressionPackage.getType() != DerivedMetricService.METRIC_EXPRESSION) {
                        throw new ServiceException("The operator \"/\" has not a valid expression before");
                    }
                }
                if (!i.hasNext()) {
                    throw new ServiceException("The operator \"/\" has not a expression after");
                }
                ExpressionPackage rightExpressionPackage = (ExpressionPackage) i.next();
                if (rightExpressionPackage.getType() != DerivedMetricService.METRIC_EXPRESSION) {
                    throw new ServiceException("The operator \"/\" has not a valid expression after");
                }
                MetricExpression metricExpression = new Div(leftExpressionPackage.getMetricExpression(), rightExpressionPackage.getMetricExpression());
                expressionPackage = new ExpressionPackage(metricExpression);
                newList.add(expressionPackage);
            } else {
                newList.add(expressionPackage);
            }
        }
        return newList;
    }

    LinkedList<ExpressionPackage> eliminatePow(LinkedList<ExpressionPackage> list) throws ServiceException {
        LinkedList<ExpressionPackage> newList = new LinkedList<ExpressionPackage>();
        Iterator i = list.iterator();
        //eliminate the ^ terminal
        while (i.hasNext()) {
            ExpressionPackage expressionPackage = (ExpressionPackage) i.next();
            if (expressionPackage.getType() == DerivedMetricService.POW) {
                ExpressionPackage leftExpressionPackage = null;
                if (newList.size() <= 0) {
                    throw new ServiceException("The operator \"^\" has not a expression before");
                } else {
                    leftExpressionPackage = (ExpressionPackage) newList.removeLast();
                    if (leftExpressionPackage.getType() != DerivedMetricService.METRIC_EXPRESSION) {
                        throw new ServiceException("The operator \"^\" has not a valid expression before");
                    }
                }
                if (!i.hasNext()) {
                    throw new ServiceException("The operator \"^\" has not a expression after");
                }
                ExpressionPackage rightExpressionPackage = (ExpressionPackage) i.next();
                if (rightExpressionPackage.getType() != DerivedMetricService.METRIC_EXPRESSION) {
                    throw new ServiceException("The operator \"^\" has not a valid expression after");
                }
                MetricExpression metricExpression = new Pow(leftExpressionPackage.getMetricExpression(), rightExpressionPackage.getMetricExpression());
                expressionPackage = new ExpressionPackage(metricExpression);
                newList.add(expressionPackage);

            } else {
                newList.add(expressionPackage);
            }
        }
        return newList;
    }

    LinkedList<ExpressionPackage> eliminateAddandSub(LinkedList<ExpressionPackage> list) throws ServiceException {
        LinkedList<ExpressionPackage> newList = new LinkedList<ExpressionPackage>();
        Iterator i = list.iterator();
        //eliminate the +- terminal
        while (i.hasNext()) {
            ExpressionPackage expressionPackage = (ExpressionPackage) i.next();
            if (expressionPackage.getType() == DerivedMetricService.ADD) {
                ExpressionPackage leftExpressionPackage = null;
                if (newList.size() <= 0) {
                    throw new ServiceException("The operator \"+\" has not a expression before");
                } else {
                    leftExpressionPackage = (ExpressionPackage) newList.removeLast();
                    if (leftExpressionPackage.getType() != DerivedMetricService.METRIC_EXPRESSION) {
                        throw new ServiceException("The operator \"+\" has not a valid expression before");
                    }
                }
                if (!i.hasNext()) {
                    throw new ServiceException("The operator \"+\" has not a expression after");
                }
                ExpressionPackage rightExpressionPackage = (ExpressionPackage) i.next();
                if (rightExpressionPackage.getType() != DerivedMetricService.METRIC_EXPRESSION) {
                    throw new ServiceException("The operator \"+\" has not a valid expression after");
                }
                MetricExpression metricExpression = new Add(leftExpressionPackage.getMetricExpression(), rightExpressionPackage.getMetricExpression());
                expressionPackage = new ExpressionPackage(metricExpression);
                newList.add(expressionPackage);


            } else {
                if (expressionPackage.getType() == DerivedMetricService.SUB) {
                    ExpressionPackage leftExpressionPackage = null;
                    if (newList.size() <= 0) {
                        throw new ServiceException("The operator \"-\" has not a expression before");
                    } else {
                        leftExpressionPackage = (ExpressionPackage) newList.removeLast();
                        if (leftExpressionPackage.getType() != DerivedMetricService.METRIC_EXPRESSION) {
                            throw new ServiceException("The operator \"-\" has not a valid expression before");
                        }
                    }
                    if (!i.hasNext()) {
                        throw new ServiceException("The operator \"-\" has not a expression after");
                    }
                    ExpressionPackage rightExpressionPackage = (ExpressionPackage) i.next();
                    if (rightExpressionPackage.getType() != DerivedMetricService.METRIC_EXPRESSION) {
                        throw new ServiceException("The operator \"+\" has not a valid expression after");
                    }
                    MetricExpression metricExpression = new Sub(leftExpressionPackage.getMetricExpression(), rightExpressionPackage.getMetricExpression());
                    expressionPackage = new ExpressionPackage(metricExpression);
                    newList.add(expressionPackage);

                } else {
                    newList.add(expressionPackage);
                }
            }
        }
        return newList;
    }

    LinkedList<ExpressionPackage> createExpressionPackages(String expression) throws ServiceException {
        char expressionArray[] = expression.toCharArray();
        ExpressionPackage expressionPackage;
        String subexpression = null;
        LinkedList<ExpressionPackage> list = new LinkedList<ExpressionPackage>();
        int i = 0;
        while (i < expressionArray.length) {
            int numberSymbol = getNumberSymbol(expressionArray[i]);
            if (numberSymbol != -1) {
                expressionPackage = new ExpressionPackage(numberSymbol);
                i++;
            } else {
                int j = i + 1;
                boolean flag = true;
                while (j < expressionArray.length && flag) {
                    if (getNumberSymbol(expressionArray[j]) != -1) {
                        flag = false;
                    } else {
                        j++;
                    }
                }
                subexpression = expression.substring(i, j);
                expressionPackage = getExpression(subexpression);
                i = j;
            }
            if (expressionPackage == null) {
                throw new ServiceException("Invalid " + subexpression + " expression");
            } else {
                list.add(expressionPackage);

            }
        }
        return list;
    }

    private int getNumberSymbol(char c) {
        int num = -1;
        if (c == '(') {
            num = 1;
        } else {
            if (c == ')') {
                num = 2;
            } else {
                if (c == '+') {
                    num = 3;
                } else {
                    if (c == '-') {
                        num = 4;
                    } else {
                        if (c == '*') {
                            num = 5;
                        } else {
                            if (c == '/') {
                                num = 6;
                            } else {
                                if (c == '^') {
                                    num = 7;
                                }
                            }
                        }
                    }
                }
            }

        }
        return num;
    }

    public ExpressionPackage getExpression(String subExpression) {

        ExpressionPackage expressionPackage = null;
        double doubleValue;
        try {
            doubleValue = Double.valueOf(subExpression);
            expressionPackage = new ExpressionPackage(DerivedMetricService.DOUBLE_VALUE, doubleValue);
        } catch (Exception e) {
            if (subExpression.equals("sqrt")) {
                expressionPackage = new ExpressionPackage(DerivedMetricService.SQRT);
            } else {
                try {
                    Metric metric = null;
                    metric = metricService.getMetric(subExpression);
                    if (metric != null) {
                        //System.out.println("Entrou na Metrica " + subExpression);
                        MetricManager metricManager = (MetricManager) MetricManagerFactory.getInstance().getMetricManager(metric);
                        MetricExpression metricExpression = new MetricManagerExpression(metricManager);
                        expressionPackage = new ExpressionPackage(metricExpression);
                    }
                } catch (Exception exception) {
                    return null;
                }
            }

        }
        /*if(!Double.isNaN(doubleValue)){
        expressionPackage=new ExpressionPackage(0,doubleValue);
        }
        else{
        if(subExpression.equals("sqrt")){
        expressionPackage=new ExpressionPackage(8);
        }
        }*/
        return expressionPackage;
    }

    private Token getTokenFromSubExpression(String subExpression) {
        Token token = null;
        double doubleValue;
        try {
            doubleValue = Double.valueOf(subExpression);
            token = new Token(doubleValue);
        } catch (Exception e) {
            if (subExpression.equals("sqrt")) {
                token = new Token(DerivedMetricService.SQRT);
            } else {
                try {
                    Metric metric = null;
                    metric = metricService.getByAcronym(subExpression);
                    if (metric != null) {
                        //System.out.println("Entrou na Metrica " + subExpression);
                        token = new Token(subExpression);
//                        MetricManager metricManager = (MetricManager) MetricManagerFactory.getInstance().getMetricManager(metric);
//                        MetricExpression metricExpression = new MetricManagerExpression(metricManager);
//                        expressionPackage = new ExpressionPackage(metricExpression);
                    }
                } catch (Exception exception) {
                    return null;
                }
            }

        }
        return token;
    }

    private String eliminateSpaces(String expression) {
        char expressionArray[] = expression.toCharArray();
        int i = 0;
        int spaces = 0;
        while (i < expressionArray.length) {
            if (expressionArray[i] == ' ') {
                spaces++;
            }
            i++;
        }
        i = 0;
        int j = 0;
        char newArray[] = new char[expressionArray.length - spaces];
        while (i < expressionArray.length) {
            if (expressionArray[i] != ' ') {
                newArray[j] = expressionArray[i];
                j++;
            }
            i++;
        }
        String newString = String.valueOf(newArray);
        return newString;
    }

    boolean bracketsAreMalformed(LinkedList<ExpressionPackage> list) {
        int bracket = 0;
        boolean allrigth = false;
        Iterator i = list.iterator();
        while (i.hasNext() && bracket >= 0) {
            ExpressionPackage expressionPackage = (ExpressionPackage) i.next();
            if (expressionPackage.getType() == 1) {
                bracket++;
            }
            if (expressionPackage.getType() == 2) {
                bracket--;
            }
        }
        if (bracket == 0) {
            allrigth = true;
        }
        return !allrigth;
    }

    boolean sqrtIsMalformed(LinkedList<ExpressionPackage> list) {
        int flag = 0;
        boolean allrigth = true;
        Iterator i = list.iterator();
        while (i.hasNext() && allrigth) {
            ExpressionPackage expressionPackage = (ExpressionPackage) i.next();
            if (flag == 1) {
                if (expressionPackage.getType() != 1) {
                    allrigth = false;
                } else {
                    flag = 0;
                }
            }
            if (expressionPackage.getType() == 8) {
                if (flag == 1) {
                    allrigth = false;
                } else {
                    flag = 1;
                }
            }
        }
        if (flag == 1) {
            allrigth = false;
        }
        return !allrigth;
    }
}
