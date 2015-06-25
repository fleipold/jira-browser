package org.programmiersportgruppe.jirabrowser.misc;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class DoubleClickAdapter implements MouseListener {
    Action action;

    public DoubleClickAdapter(Action action) {
        this.action = action;
    }

    public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2){
            if (action.isEnabled())
                action.actionPerformed(new ActionEvent(e.getSource(),0,"doubleClick"));
        }
    }



    public void mouseEntered(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void mouseExited(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void mousePressed(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void mouseReleased(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

}
