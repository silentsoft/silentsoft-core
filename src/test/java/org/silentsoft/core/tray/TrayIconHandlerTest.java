package org.silentsoft.core.tray;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrayIconHandlerTest {

private static TrayIcon trayIcon;

	private static Logger LOGGER = LoggerFactory.getLogger(TrayIconHandlerTest.class);
	
	private static PopupMenu getPopupMenu() {
		PopupMenu popupMenu = trayIcon.getPopupMenu();
		
		if (popupMenu == null) {
			popupMenu = new PopupMenu();
		}
		
		return popupMenu;
	}
	
	public static void registerTrayIcon(Image image, String toolTip, ActionListener action) {
		if (SystemTray.isSupported()) {
			if (trayIcon != null) {
				trayIcon = null;
			}
			
			trayIcon = new TrayIcon(image);
			trayIcon.setImageAutoSize(true);
			
			if (toolTip != null) {
				trayIcon.setToolTip(toolTip);
			}
			
			if (action != null) {
				trayIcon.addActionListener(action);
			}
			
			try {
				for (TrayIcon registeredTrayIcon : SystemTray.getSystemTray().getTrayIcons()) {
					SystemTray.getSystemTray().remove(registeredTrayIcon);
				}
				
				SystemTray.getSystemTray().add(trayIcon);
			} catch (AWTException e) {
				LOGGER.error("I got catch an error during add system tray !", e);
			}
		} else {
			LOGGER.error("System tray is not supported !");
		}
	}
	
	@Test
	public void TrayTest() {
		
	}
}
