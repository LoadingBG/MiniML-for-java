package miniml;

/**
 * An exception raised when a repeating ID is found 
 * while parsing a MiniML document.
 */
@SuppressWarnings("serial")
class RepeatingIdException extends RuntimeException {
	/**
	 * Creates an exception with the given message.
	 * 
	 * @param message The message to be shown.
	 */
	RepeatingIdException(String message) {
		super(message);
	}
}
