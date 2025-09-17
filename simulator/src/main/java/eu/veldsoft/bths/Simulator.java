package eu.veldsoft.bths;

import java.security.SecureRandom;

import com.sun.star.script.provider.XScriptContext;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.frame.XModel;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.container.XIndexAccess;
import com.sun.star.sheet.XSpreadsheet;

public class Simulator {
	private static final SecureRandom PRNG = new SecureRandom();

	private static int paytable[][] = {
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,},
		{0,0,1,1,1,1,1,1,1,1,1,1,0,0,0,1,0,0,},
		{0,0,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,},
		{0,0,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,},
	};

	private static int lines[][] = {
		{1,1,1,1,1,},
		{0,0,0,0,0,},
		{2,2,2,2,2,},
		{0,1,2,1,0,},
		{2,1,0,1,2,},
		{0,0,1,2,2,},
		{2,2,1,0,0,},
		{1,0,1,2,1,},
		{1,2,1,0,1,},
	};

	private static int baseGameReels[][] = {
		{1,3,4,5,6,7,8,9,10,11,12,},
		{1,3,4,5,6,7,8,9,10,11,12,15,16,},
		{1,3,4,5,6,7,8,9,10,11,12,15,16,},
		{1,3,4,5,6,7,8,9,10,11,12,15,16,},
		{1,3,4,5,6,7,8,9,10,11,12,},
	};

	private static int freeSpinsReels[][] = {
		{1,3,4,5,6,7,8,9,10,11,12,},
		{1,3,4,5,6,7,8,9,10,11,12,16,},
		{1,3,4,5,6,7,8,9,10,11,12,16,},
		{1,3,4,5,6,7,8,9,10,11,12,16,},
		{1,3,4,5,6,7,8,9,10,11,12,},
	};

	public static void simulate(XScriptContext ctx) {
		try {
			XModel model = ctx.getDocument();
			XSpreadsheetDocument document = UnoRuntime.queryInterface(XSpreadsheetDocument.class, model);
			XIndexAccess sheets = UnoRuntime.queryInterface(XIndexAccess.class, document.getSheets());
			XSpreadsheet first = UnoRuntime.queryInterface(XSpreadsheet.class, sheets.getByIndex(0));

			first.getCellByPosition(0, 0).setFormula("Simulation!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}