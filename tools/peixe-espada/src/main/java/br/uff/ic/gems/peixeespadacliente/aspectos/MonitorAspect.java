package br.uff.ic.gems.peixeespadacliente.aspectos;

import br.uff.ic.gems.peixeespadacliente.utils.StatisticsCollector;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 *
 * @author João Felipe
 */
@Aspect
public class MonitorAspect {

    @Around("execution(@MonitorTime * *.*(..))")
    public Object aroundMonitorTime(final ProceedingJoinPoint pjp) throws Throwable {
        long time = System.currentTimeMillis();
        Object proceed = pjp.proceed();
        StatisticsCollector.getInstance().add(pjp.getSignature().getName(), System.currentTimeMillis() - time);
        return proceed;
    }

    @Around("execution(@SaveCSV * *.*(..))")
    public Object afterSaveCSV(final ProceedingJoinPoint pjp) throws Throwable {
        Object proceed = pjp.proceed();
        StatisticsCollector.getInstance().save();
        return proceed;
    }
}