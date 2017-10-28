package net.shadowfacts.krypton.ekt.util

/**
 * @author shadowfacts
 */
class EKTException: RuntimeException {

	constructor(message: String): super(message)

	constructor(message: String, cause: Throwable): super(message, cause)

	constructor(cause: Throwable): super(cause)

}