/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.core.tools.metrics.expression;

import br.uff.ic.oceano.core.tools.metrics.service.DerivedMetricService;
import br.uff.ic.oceano.core.exception.ServiceException;
import java.util.Stack;

/**
 *
 * @author wallace
 */
public class PushdownAutomaton {
    private Stack stack;
    private int state;
    public Token expressionReckonigze(Token token) throws ServiceException{
        Token aux=token;
        stack=new Stack();
        state=0;
        while(aux!=null){
            if(state==0){
                if((aux.getType()==DerivedMetricService.DOUBLE_VALUE) || (aux.getType()==DerivedMetricService.METRIC_MANAGER)){
                    state=1;
                }
                if(aux.getType()==DerivedMetricService.SUB){
                    state=3;
                    aux.setType(DerivedMetricService.UNARY_SUB);
                }
                if(aux.getType()==DerivedMetricService.SQRT){
                    state=4;

                }
                if(aux.getType()==DerivedMetricService.OPEN_BRACKETS){
                    stack.push(1);
                }
                if(aux.getType()==DerivedMetricService.CLOSE_BRACKETS){
                    if(stack.empty()){
                        throw new ServiceException("Malformed ) operator ");
                    }
                    else{
                        throw new ServiceException("Expression within the brackets has nothing ");
                    }
                }
                if(aux.getType()==DerivedMetricService.ADD){
                    throw new ServiceException("The operator \"+\" has not a expression before");
                }
                if(aux.getType()==DerivedMetricService.MULT){
                    throw new ServiceException("The operator \"*\" has not a expression before");
                }
                if(aux.getType()==DerivedMetricService.DIV){
                    throw new ServiceException("The operator \"/\" has not a expression before");
                }
                if(aux.getType()==DerivedMetricService.POW){
                    throw new ServiceException("The operator \"^\" has not a expression before");
                }
                
            }
            else{
                if(state==1){
                    if((aux.getType()==DerivedMetricService.ADD) || (aux.getType()==DerivedMetricService.SUB) || (aux.getType()==DerivedMetricService.MULT) || (aux.getType()==DerivedMetricService.DIV) || (aux.getType()==DerivedMetricService.POW)){
                        state=2;
                    }
                    if(aux.getType()==DerivedMetricService.CLOSE_BRACKETS){
                        if(stack.empty()){
                            throw new ServiceException("Malformed ) operator ");
                        }
                        else{
                            stack.pop();
                        }
                    }
                    if(aux.getType()==DerivedMetricService.DOUBLE_VALUE){
                        throw new ServiceException("Malformed double Value \""+aux.getDoubleValue()+"\" in Expression");
                    }
                    if(aux.getType()==DerivedMetricService.METRIC_MANAGER){
                        throw new ServiceException("Malformed metric Manager \""+aux.getMetricAcronym()+"\" in Expression");
                    }
                    if(aux.getType()==DerivedMetricService.OPEN_BRACKETS){
                        throw new ServiceException("Malformed ( operator ");
                    }
                    if(aux.getType()==DerivedMetricService.SQRT){
                        throw new ServiceException("Not allowed sqrt operator after a double value or a Metric ");
                    }
                }
                else{

                    if(state==2){
                        if((aux.getType()==DerivedMetricService.DOUBLE_VALUE) || (aux.getType()==DerivedMetricService.METRIC_MANAGER)){
                            state=1;
                        }
                        if(aux.getType()==DerivedMetricService.OPEN_BRACKETS){
                            state=0;
                            stack.push(1);
                        }
                        if(aux.getType()==DerivedMetricService.SUB){
                            state=3;
                            aux.setType(DerivedMetricService.UNARY_SUB);
                        }
                        if(aux.getType()==DerivedMetricService.SQRT){
                            state=4;
                        }
                        if(aux.getType()==DerivedMetricService.ADD){
                            throw new ServiceException("The operator \"+\" has not a valid expression before");
                        }
                        if(aux.getType()==DerivedMetricService.MULT){
                            throw new ServiceException("The operator \"*\" has not a valid expression before");
                        }
                        if(aux.getType()==DerivedMetricService.DIV){
                            throw new ServiceException("The operator \"/\" has not a valid expression before");
                        }
                        if(aux.getType()==DerivedMetricService.POW){
                            throw new ServiceException("The operator \"^\" has not a valid expression before");
                        }
                        if(aux.getType()==DerivedMetricService.CLOSE_BRACKETS){
                            throw new ServiceException("Malformed ) operator ");
                        }

                    }
                    else{
                        if(state==3){
                            if((aux.getType()==DerivedMetricService.DOUBLE_VALUE) || (aux.getType()==DerivedMetricService.METRIC_MANAGER)){
                                state=1;
                            }

                            if(aux.getType()==DerivedMetricService.SQRT){
                                state=4;

                            }
                            if(aux.getType()==DerivedMetricService.OPEN_BRACKETS){
                                stack.push(1);
                                state=0;
                            }
                            if(aux.getType()==DerivedMetricService.SUB){
                                throw new ServiceException("You not can put two \" -\" operators together ");
                            }
                            if(aux.getType()==DerivedMetricService.CLOSE_BRACKETS){
                                throw new ServiceException("You can not put a ) after a \"-\" operator ");
                            }
                            if(aux.getType()==DerivedMetricService.ADD){
                                throw new ServiceException("The operator \"+\" has not a expression before");
                            }
                            if(aux.getType()==DerivedMetricService.MULT){
                                throw new ServiceException("The operator \"*\" has not a expression before");
                            }
                            if(aux.getType()==DerivedMetricService.DIV){
                                throw new ServiceException("The operator \"/\" has not a expression before");
                            }
                            if(aux.getType()==DerivedMetricService.POW){
                                throw new ServiceException("The operator \"^\" has not a expression before");
                            }
                        }
                        else{
                            if(state==4){
                                if(aux.getType()==DerivedMetricService.OPEN_BRACKETS){
                                    state=0;
                                    stack.push(1);
                                }
                                else{
                                    throw new ServiceException("Invalid Expression after sqrt operator");
                                }
                            }
                        }
                    }
                }
            }
            aux=aux.getProx();
        }
        if(!stack.empty()){
            throw new ServiceException("Lack of ) operator");
        }
        if(state!=1){
            throw new ServiceException("Malformed Expression");
        }

        return token;
    }

}
