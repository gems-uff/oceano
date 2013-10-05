package br.uff.ic.gems.peixeespadacliente.exception;

/**
 *
 * @author Joao Felipe
 */
public class UnresovableConflictsException extends Throwable {

    public UnresovableConflictsException() {
    }

    public UnresovableConflictsException(Throwable t) {
        super(t);
    }

    public UnresovableConflictsException(String message) {
        super(message);
    }
}
