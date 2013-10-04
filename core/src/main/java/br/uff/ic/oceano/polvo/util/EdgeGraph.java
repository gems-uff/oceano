/*
 * Classe usada para mapear os nos do grafico
 */

package br.uff.ic.oceano.polvo.util;

/**
 *
 * @author Rafael
 */
public class EdgeGraph {
    private String source;
    private String target;
    private boolean flag;
    private long beginRev;
    private int metric;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public long getBeginRev() {
        return beginRev;
    }

    public void setBeginRev(long beginRev) {
        this.beginRev = beginRev;
    }

    public int getMetric() {
        return metric;
    }

    public void setMetric(int metric) {
        this.metric = metric;
    }

}
