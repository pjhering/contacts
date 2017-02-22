package contacts.ui;

import contacts.entities.Contact;
import contacts.ui.ContactPanel;
import java.awt.BorderLayout;
import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.SOUTH;
import static java.awt.BorderLayout.WEST;
import java.awt.FlowLayout;
import static java.awt.FlowLayout.RIGHT;
import java.awt.GridLayout;
import static javax.swing.BorderFactory.createTitledBorder;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import static javax.swing.SpringLayout.NORTH;

public class AppPanel extends JPanel
{
    public final ContactPanel contactPanel;
    public final JList<Contact> contactList;
    public final JButton clearButton, saveButton, deleteButton;
    public final JMenuItem removePhoneItem;
    public final JMenuItem removeEmailItem;
    
    private final JPopupMenu phoneMenu, emailMenu;
    private final JScrollPane contactScroll;
    private final JPanel buttonPanel, flowPanel;
    private final JPanel contentPanel, northPanel;
    
    public AppPanel()
    {
        contactPanel = new ContactPanel();
        contactList = new JList<>();
        clearButton = new JButton("clear");
        saveButton = new JButton("save");
        deleteButton = new JButton("delete");
        removePhoneItem = new JMenuItem("remove");
        removeEmailItem = new JMenuItem("remove");
        phoneMenu = new JPopupMenu();
        phoneMenu.add(removePhoneItem);
        contactPanel.phoneList.addMouseListener(new PopupListener(phoneMenu));
        emailMenu = new JPopupMenu();
        emailMenu.add(removeEmailItem);
        contactPanel.emailList.addMouseListener(new PopupListener(emailMenu));
        contactScroll = new JScrollPane(contactList);
        contactScroll.setBorder(createTitledBorder("contacts"));
        buttonPanel = new JPanel(new GridLayout(1, 3, 5, 5));
        buttonPanel.add(clearButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(deleteButton);
        flowPanel = new JPanel(new FlowLayout(RIGHT));
        flowPanel.add(buttonPanel);
        northPanel = new JPanel(new BorderLayout(5, 5));
        northPanel.add(contactPanel, CENTER);
        northPanel.add(flowPanel, SOUTH);
        contentPanel = new JPanel(new BorderLayout(5, 5));
        contentPanel.add(northPanel, NORTH);
        
        setup();
    }
    
    private void setup()
    {
        setLayout(new BorderLayout(5, 5));
        add(contactScroll, WEST);
        add(contentPanel, CENTER);
    }
}
