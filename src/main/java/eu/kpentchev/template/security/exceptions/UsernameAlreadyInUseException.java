package eu.kpentchev.template.security.exceptions;

public class UsernameAlreadyInUseException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = -2774644449467403236L;

	public UsernameAlreadyInUseException(String username) {
            super("The username '" + username + "' is already in use.");
    }
}
