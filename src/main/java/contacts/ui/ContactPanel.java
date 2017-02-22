package contacts.ui;

import contacts.entities.EMail;
import contacts.entities.Phone;
import java.awt.BorderLayout;
import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.EAST;
import static java.awt.BorderLayout.NORTH;
import static java.awt.BorderLayout.SOUTH;
import static java.awt.BorderLayout.WEST;
import java.awt.GridLayout;
import static javax.swing.BorderFactory.createTitledBorder;
import javax.swing.JButton;
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
    public final JTextField addPhoneField;
    public final JButton addPhoneButton;
    public final JList<EMail> emailList;
    public final JTextField addEmailField;
    public final JButton addEmailButton;
    
    private final JLabel idLabel, titleLabel, firstNameLabel, lastNameLabel;
    private final JScrollPane notesScroll, phoneScroll, emailScroll;
    private final JPanel labelPanel, fieldPanel, northPanel, southPanel;
    private final JPanel phoneListPanel, addPhonePanel;
    private final JPanel emailListPanel, addEmailPanel;
    
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
        emailScroll = new JScrollPane(emailList);
        
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
        
        addPhoneField = new JTextField(15);
        addPhoneButton = new JButton("+");
        addPhonePanel = new JPanel(new BorderLayout(0, 0));
        addPhonePanel.add(addPhoneField, CENTER);
        addPhonePanel.add(addPhoneButton, EAST);
        phoneListPanel = new JPanel(new BorderLayout(0, 0));
        phoneListPanel.setBorder(createTitledBorder("phone numbers"));
        phoneListPanel.add(phoneScroll, CENTER);
        phoneListPanel.add(addPhonePanel, SOUTH);
        
        addEmailField = new JTextField(15);
        addEmailButton = new JButton("+");
        addEmailPanel = new JPanel(new BorderLayout(0, 0));
        addEmailPanel.add(addEmailField, CENTER);
        addEmailPanel.add(addEmailButton, EAST);
        emailListPanel = new JPanel(new BorderLayout(0, 0));
        emailListPanel.setBorder(createTitledBorder("email addresses"));
        emailListPanel.add(emailScroll, CENTER);
        emailListPanel.add(addEmailPanel, SOUTH);
        
        southPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        southPanel.add(phoneListPanel);
        southPanel.add(emailListPanel);
        
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
