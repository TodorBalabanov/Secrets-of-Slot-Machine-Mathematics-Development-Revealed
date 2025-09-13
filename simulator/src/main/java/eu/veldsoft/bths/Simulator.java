package eu.veldsoft.bths;

import com.sun.star.script.provider.XScriptContext;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.frame.XModel;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.container.XIndexAccess;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.table.XCell;

public class Simulator {
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
