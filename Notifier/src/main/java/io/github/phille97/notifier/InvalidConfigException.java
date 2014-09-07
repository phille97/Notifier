package io.github.phille97.notifier;

@SuppressWarnings("serial")
public class InvalidConfigException extends Exception {
	
	public InvalidConfigException(String txt) {
		super(txt);
	}
}
