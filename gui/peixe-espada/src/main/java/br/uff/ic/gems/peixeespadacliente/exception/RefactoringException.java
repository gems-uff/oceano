package br.uff.ic.gems.peixeespadacliente.exception;

/**
 *
 * @author João Felipe
 */
public class RefactoringException extends Throwable {

    public RefactoringException() {
    }

    public RefactoringException(Throwable t) {
        super(t);
    }

    public RefactoringException(String message) {
        super(message);
    }
}
