/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.ostra.service.metric;

/**
 *  Configuration for a Request submitted to OstraMetricService1
 */
public class Configuration {
    private boolean retryingCompilation = false;
    private boolean retryingCheckout = true;
    private boolean usingUpdateRevision = false;
    private int revisionThreshold = 100;
    
    /**
     * @return the usingUpdateRevision
     */
    public boolean isUsingUpdateRevision() {
        return usingUpdateRevision;
    }

    /**
     * @param usingUpdateRevision the usingUpdateRevision to set
     */
    public void setUsingUpdateRevision(boolean usingUpdateRevision) {
        this.usingUpdateRevision = usingUpdateRevision;
    }

    /**
     * @return the revisionThreshold
     */
    public int getRevisionThreshold() {
        return revisionThreshold;
    }

    /**
     * @param revisionThreshold the revisionThreshold to set
     */
    public void setRevisionThreshold(int revisionThreshold) {
        this.revisionThreshold = revisionThreshold;
    }

    /**
     * @return the retryingCheckout
     */
    public boolean isRetryingCheckout() {
        return retryingCheckout;
    }

    /**
     * @param retryingCheckout the retryingCheckout to set
     */
    public void setRetryingCheckout(boolean retryingCheckout) {
        this.retryingCheckout = retryingCheckout;
    }

    /**
     * @return the retryingCompilation
     */
    public boolean isRetryingCompilation() {
        return retryingCompilation;
    }

    /**
     * @param retryingCompilation the retryingCompilation to set
     */
    public void setRetryingCompilation(boolean retryingCompilation) {
        this.retryingCompilation = retryingCompilation;
    }
}
