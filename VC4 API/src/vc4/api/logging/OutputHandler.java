/**
 * 
 */
package vc4.api.logging;

/**
 * @author paul
 *
 */
public abstract class OutputHandler implements Handler{

	/* (non-Javadoc)
	 * @see vc4.api.logging.Handler#handleLog(vc4.api.logging.Logger, vc4.api.logging.Level, java.lang.Object)
	 */
	@Override
	public void handleLog(Logger logger, Level level, Object message) {
		String name = logger.getName();
		if(name == null) name = "";
		else name = "[" + name + "] ";
		String out = level.name() + " " + name + message;
		printText(level, out);
		
	}

	/* (non-Javadoc)
	 * @see vc4.api.logging.Handler#handleLog(vc4.api.logging.Logger, vc4.api.logging.Level, java.lang.Object, java.lang.Throwable)
	 */
	@Override
	public void handleLog(Logger logger, Level level, Object message, Throwable throwable) {
		if(message != null){
			handleLog(logger, level, message + ":");
		}
		handleLog(logger, level, throwable.toString() + " (Thread: " + Thread.currentThread().getName() + ")");
		StackTraceElement[] stack = throwable.getStackTrace();
		for(int dofor = 0; dofor < stack.length; ++dofor){
			printText(level, "\tat " + stack[dofor].toString());
		}
		Throwable cause = throwable.getCause();
		while(true){
			if(cause == null) break;
			printText(level, "Caused by: " + cause.toString());
			stack = cause.getStackTrace();
			for(int dofor = 0; dofor < stack.length; ++dofor){
				printText(level, "\tat " + stack[dofor].toString());
			}
			cause = cause.getCause();
		}
		stack = null;
	}
	
	protected abstract void printText(Level level, String text);
	
	/* (non-Javadoc)
	 * @see vc4.api.logging.Handler#close()
	 */
	@Override
	public void close() {
	}

}
