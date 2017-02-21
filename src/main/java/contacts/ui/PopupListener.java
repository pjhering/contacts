package contacts.ui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPopupMenu;

public class PopupListener implements MouseListener
{

    private final JPopupMenu popup;

    public PopupListener(JPopupMenu popup)
    {
        this.popup = popup;
    }
    
    @Override
    public void mouseClicked(MouseEvent e)
    {
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        maybeShowPopup(e);
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        maybeShowPopup(e);
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
    }

    private void maybeShowPopup(MouseEvent e) {
        if (e.isPopupTrigger()) {
            popup.show(e.getComponent(),
                       e.getX(), e.getY());
        }
    }
}
