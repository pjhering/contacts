package contacts.ui;

import contacts.entities.EMail;
import contacts.entities.Phone;
import java.awt.BorderLayout;
import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.NORTH;
import static java.awt.BorderLayout.SOUTH;
import static java.awt.BorderLayout.WEST;
import java.awt.GridLayout;
import static javax.swing.BorderFactory.createTitledBorder;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ContactPanel extends JPanel
{
    public final JTextField idField, firstNameField, lastNameField;
    public final JComboBox<String> titleField;
    public final JTextArea notesField;
    public final JList<Phone> phoneList;
    public final JList<EMail> emailList;
    
    private final JLabel idLabel, titleLabel, firstNameLabel, lastNameLabel;
    private final JScrollPane notesScroll, phoneScroll, emailScroll;
    private final JPanel labelPanel, fieldPanel, northPanel, southPanel;
    
    public ContactPanel()
    {
        idField = new JTextField(30);
        idField.setEditable(false);
        titleField = new JComboBox<>(new String[]{"Mr.", "Mrs.", "Ms.", "Miss", "Dr."});
        firstNameField = new JTextField(30);
        lastNameField = new JTextField(30);
        notesField = new JTextArea(15, 30);
        phoneList = new JList<>();
        emailList = new JList<>();
        
        idLabel = new JLabel("id");
        titleLabel = new JLabel("title");
        firstNameLabel = new JLabel("first name");
        lastNameLabel = new JLabel("last name");
        
        notesScroll = new JScrollPane(notesField);
        notesScroll.setBorder(createTitledBorder("notes"));
        phoneScroll = new JScrollPane(phoneList);
        phoneScroll.setBorder(createTitledBorder("phone numbers"));
        emailScroll = new JScrollPane(emailList);
        emailScroll.setBorder(createTitledBorder("email addresses"));
        
        labelPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        labelPanel.add(idLabel);
        labelPanel.add(titleLabel);
        labelPanel.add(firstNameLabel);
        labelPanel.add(lastNameLabel);
        
        fieldPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        fieldPanel.add(idField);
        fieldPanel.add(titleField);
        fieldPanel.add(firstNameField);
        fieldPanel.add(lastNameField);
        
        northPanel = new JPanel(new BorderLayout(5, 5));
        northPanel.add(labelPanel, WEST);
        northPanel.add(fieldPanel, CENTER);
        
        southPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        southPanel.add(phoneScroll);
        southPanel.add(emailScroll);
        
        setup();
    }
    
    private void setup()
    {
        setLayout(new BorderLayout(5, 5));
        add(northPanel, NORTH);
        add(notesScroll, CENTER);
        add(southPanel, SOUTH);
    }
}
