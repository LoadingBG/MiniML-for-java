package miniml;

/**
 * An exception raised when a MiniML document 
 * couldn't update.
 * 
 * @see Document#update();
 */
@SuppressWarnings("serial")
final class UpdateException extends RuntimeException {
	/**
	 * Creates an exception with the given message.
	 * 
	 * @param message The message to be shown.
	 */
	UpdateException(String message) {
		super(message);
	}
}
