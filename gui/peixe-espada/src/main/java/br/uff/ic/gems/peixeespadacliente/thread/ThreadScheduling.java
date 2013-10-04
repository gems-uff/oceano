package br.uff.ic.gems.peixeespadacliente.thread;

import br.uff.ic.gems.peixeespadacliente.model.agent.LocalManagerAgent;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Heliomar
 */
public class ThreadScheduling extends Thread {

    // 5 seconds
    public static int SLEEP_TIME = 5000;
    private LocalManagerAgent agentPeixeEspada;
    private long deadLine;

    public ThreadScheduling(LocalManagerAgent agent) {
        this.agentPeixeEspada = agent;
        this.deadLine = agent.getInitDate().getTime() - System.currentTimeMillis();
    }

    @Override
    public void run() {

        while (deadLine > 0) {
            try {
                sleep(SLEEP_TIME);
                deadLine -= SLEEP_TIME;
            } catch (InterruptedException ex) {
                Logger.getLogger(ThreadScheduling.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
            }
        }

        Thread threadSupervisioned = new Thread(agentPeixeEspada);

        threadSupervisioned.start();

        deadLine = agentPeixeEspada.getEndDate().getTime()-agentPeixeEspada.getInitDate().getTime();
        ThreadDeadLine td = new ThreadDeadLine(threadSupervisioned, deadLine, agentPeixeEspada);
        td.start();

    }
}
