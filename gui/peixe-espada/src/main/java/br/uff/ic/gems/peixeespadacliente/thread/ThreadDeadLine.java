package br.uff.ic.gems.peixeespadacliente.thread;

import br.uff.ic.gems.peixeespadacliente.model.agent.LocalManagerAgent;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Heliomar
 */
public class ThreadDeadLine extends Thread {

    // 5 seconds
    public static int SLEEP_TIME = 5000;
    private Thread threadSupervisioned;
    private long deadLine;
    private LocalManagerAgent agentPeixeEspada;

    public ThreadDeadLine(Thread threadSupervisioned, long deadline, LocalManagerAgent agentPeixeEspada) {
        this.threadSupervisioned = threadSupervisioned;
        this.deadLine = deadline;
        this.agentPeixeEspada = agentPeixeEspada;
    }

    @Override
    public void run() {

        while (deadLine > 0 && !agentPeixeEspada.isFinalizeByError()) {
            try {
                this.sleep(SLEEP_TIME);
                deadLine -= SLEEP_TIME;
            } catch (InterruptedException ex) {
                Logger.getLogger(ThreadDeadLine.class.getName()).log(Level.SEVERE, null, ex);
                agentPeixeEspada.printError(ex);
            }
        }

        agentPeixeEspada.requestFinishWork();
        while (!agentPeixeEspada.isStopped()) {
            try {
                this.sleep(SLEEP_TIME);
            } catch (InterruptedException ex) {
                Logger.getLogger(ThreadDeadLine.class.getName()).log(Level.SEVERE, null, ex);
                agentPeixeEspada.printError(ex);
            }
        }
        threadSupervisioned.interrupt();
        this.interrupt();
    }
}
