package org.silentsoft.core.component.messagebox;

import org.controlsfx.dialog.Dialogs;
import org.controlsfx.control.action.Action;

public final class MessageBox {
	
	public static void showAbout(Object owner, String masthead, String message) {
		Dialogs.create()
		.owner(owner)
		.title("About")
		.masthead(masthead)
		.message(message)
		.showInformation();
	}
	
	public static Action showConfirm(Object owner, String message) {
		return Dialogs.create()
			   .owner(owner)
			   .title("Confirm")
			   .message(message)
			   .showConfirm();
	}
	
	public static void showErrorTypeVaildationFailure(Object owner, String message) {
		Dialogs.create()
        .owner(owner)
        .title("Error")
        .masthead("Vaildation failure")
        .message(message)
        .showError();
	}
	
	public static void showException(Object owner, Throwable exception) {
		Dialogs.create()
		.owner(owner)
		.title("Exception")
		.masthead("Work Failure")
		.message("See more information")
		.showException(exception);
	}
}
