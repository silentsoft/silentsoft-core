package org.silentsoft.core.tray;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class TrayIconHandler {
	
	private static Logger LOGGER = LoggerFactory.getLogger(TrayIconHandler.class);
	
	private static TrayIcon trayIcon;
	
	private static PopupMenu getPopupMenu() {
		PopupMenu popupMenu = trayIcon.getPopupMenu();
		
		if (popupMenu == null) {
			popupMenu = new PopupMenu();
		}
		
		return popupMenu;
	}
	
	public static boolean isRegistered() {
		return (trayIcon != null && getPopupMenu() != null) ? true : false;
	}
	
	public static boolean isNotRegistered() {
		return !isRegistered();
	}
	
	public static boolean isExistsMenu(String menu) {
		if (isNotRegistered()) {
			return false;
		}
		
		for (int i=0, j=getPopupMenu().getItemCount(); i<j; i++) {
			if (getPopupMenu().getItem(i) instanceof Menu) {
				Menu item = (Menu) getPopupMenu().getItem(i);
				if (item.getLabel().equals(menu)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public static boolean isNotExistsMenu(String menu) {
		return !isExistsMenu(menu);
	}
	
	public static void registerTrayIcon(Image image, String toolTip) {
		if (SystemTray.isSupported()) {
			if (trayIcon != null) {
				trayIcon = null;
			}
			
			trayIcon = new TrayIcon(image, toolTip);
			trayIcon.setImageAutoSize(true);
			
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
	
	public static void addSeparator() {
		if (isNotRegistered()) {
			return;
		}
		
		getPopupMenu().addSeparator();
	}
	
	public static void addSeparator(String menu) {
		if (isNotRegistered()) {
			return;
		}
		
		for (int i=0, j=getPopupMenu().getItemCount(); i<j; i++) {
			if (getPopupMenu().getItem(i) instanceof Menu) {
				Menu item = (Menu) getPopupMenu().getItem(i);
				if (item.getLabel().equals(menu)) {
					item.addSeparator();
					
					getPopupMenu().insert(item, i);
					
					break;
				}
			}
		}
	}
	
	public static void addMenu(String menu) {
		if (isNotRegistered()) {
			return;
		}
		
		getPopupMenu().add(new Menu(menu));
	}
	
	public static void addItem(String label, ActionListener action) {
		if (isNotRegistered()) {
			return;
		}
		
		MenuItem menuItem = new MenuItem(label);
		menuItem.addActionListener(action);
		
		PopupMenu popupMenu = getPopupMenu();
		popupMenu.add(menuItem);
		
		trayIcon.setPopupMenu(popupMenu);
	}
	
	public static void addMenuItem(String menu, String label, ActionListener action) {
		if (isNotRegistered()) {
			return;
		}
		
		if (isNotExistsMenu(menu)) {
			addMenu(menu);
		}
		
		for (int i=0, j=getPopupMenu().getItemCount(); i<j; i++) {
			if (getPopupMenu().getItem(i) instanceof Menu) {
				Menu item = (Menu) getPopupMenu().getItem(i);
				if (item.getLabel().equals(menu)) {
					MenuItem menuItem = new MenuItem(label);
					menuItem.addActionListener(action);
					
					item.add(menuItem);
					
					getPopupMenu().insert(item, i);
					
					break;
				}
			}
		}
	}
}