package org.programmiersportgruppe.jirabrowser.misc;

import org.programmiersportgruppe.jirabrowser.BeanTableAdapter;

import javax.swing.*;
import javax.swing.plaf.UIResource;
import java.awt.datatransfer.Transferable;

/**
 * Created by fleipold on 24/06/2015.
 */
public class TableTransferHandler extends TransferHandler implements UIResource {

    private BeanTableAdapter<?> tableAdapter;

    public TableTransferHandler(BeanTableAdapter<?> tableAdapter) {

        this.tableAdapter = tableAdapter;
    }

    /**
     * Create a Transferable to use as the source for a data transfer.
     *
     * @param c The component holding the data to be transfered.  This
     *          argument is provided to enable sharing of TransferHandlers by
     *          multiple components.
     * @return The representation of the data to be transfered.
     */
    protected Transferable createTransferable(JComponent c) {
        if (c instanceof JTable) {
            JTable table = (JTable) c;
            int[] rows;
            int[] cols;

            if (!table.getRowSelectionAllowed() && !table.getColumnSelectionAllowed()) {
                return null;
            }

            if (!table.getRowSelectionAllowed()) {
                int rowCount = table.getRowCount();

                rows = new int[rowCount];
                for (int counter = 0; counter < rowCount; counter++) {
                    rows[counter] = counter;
                }
            } else {
                rows = table.getSelectedRows();
            }

            if (!table.getColumnSelectionAllowed()) {
                int colCount = table.getColumnCount();

                cols = new int[colCount];
                for (int counter = 0; counter < colCount; counter++) {
                    cols[counter] = counter;
                }
            } else {
                cols = table.getSelectedColumns();
            }

            if (rows == null || cols == null || rows.length == 0 || cols.length == 0) {
                return null;
            }

            StringBuffer plainBuf = new StringBuffer();
            StringBuffer htmlBuf = new StringBuffer();

            htmlBuf.append("<html>\n<body>\n");

            for (int row = 0; row < rows.length; row++) {
                htmlBuf.append("<li>\n");


                plainBuf.append(tableAdapter.getExternalString(rows[row])+"\n");
                htmlBuf.append(tableAdapter.getExternalHtmlRep(rows[row]));
                // we want a newline at the end of each line and not a tab

                plainBuf.deleteCharAt(plainBuf.length() - 1).append("\n");
                htmlBuf.append("</li>\n");
            }

            // remove the last newline
            plainBuf.deleteCharAt(plainBuf.length() - 1);
            htmlBuf.append("</body>\n</html>");

            return new BasicTransferable(plainBuf.toString(), htmlBuf.toString());
        }

        return null;
    }

    public int getSourceActions(JComponent c) {
        return COPY;
    }

}
