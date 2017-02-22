package contacts;

import contacts.entities.Contact;
import contacts.entities.DAO;
import contacts.entities.Hibernate;
import contacts.ui.AppPanel;
import contacts.ui.ContactPanel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collection;
import java.util.List;
import static javax.swing.BorderFactory.createEmptyBorder;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;
import static javax.swing.JOptionPane.YES_OPTION;
import javax.swing.ListModel;
import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

public class Main
{

    private final JFrame frame;
    private final AppPanel panel;

    private Main()
    {
        panel = new AppPanel();
        panel.setBorder(createEmptyBorder(5, 5, 5, 5));
        panel.contactPanel.addPhoneButton.addActionListener(a1 -> addPhoneAction());
        panel.contactPanel.addEmailButton.addActionListener(a2 -> addEmailAction());
        panel.clearButton.addActionListener(a3 -> clearAction());
        panel.contactList.addListSelectionListener(a4 -> contactListChanged());
        panel.deleteButton.addActionListener(a5 -> deleteAction());
        panel.removeEmailItem.addActionListener(a6 -> removeEmailAcion());
        panel.removePhoneItem.addActionListener(a7 -> removePhoneAction());
        panel.saveButton.addActionListener(a8 -> saveAction());

        frame = new JFrame("Contacts");
        frame.setContentPane(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowOpened(WindowEvent e)
            {
                doAppStart();
            }

            @Override
            public void windowClosing(WindowEvent e)
            {
                doAppStop();
            }
        });
        frame.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        frame.setVisible(true);
    }

    public static void main(String[] args)
    {
        Hibernate.setup();
        new Main();
    }

    private void doAppStart()
    {
        refreshContactsList();
        clearAllFields();
        setButtonsToInsert();
    }

    private void doAppStop()
    {
        Hibernate.shutdown();
        System.exit(0);
    }

    private void addEmailAction()
    {
    }

    private void addPhoneAction()
    {
    }

    private void clearAction()
    {
        refreshContactsList();
        clearAllFields();
        setButtonsToInsert();
    }

    private void contactListChanged()
    {
        Contact c = panel.contactList.getSelectedValue();
        
        if(c != null)
        {
            ContactPanel p = panel.contactPanel;
            p.idField.setText(c.getId().toString());
            p.titleField.setSelectedItem(c.getTitle());
            p.firstNameField.setText(c.getFirstName());
            p.lastNameField.setText(c.getLastName());
            p.notesField.setText(c.getNotes());
            p.phoneList.setModel(createListModel(c.getPhoneNumbers()));
            p.emailList.setModel(createListModel(c.getEmailAddresses()));
            
            setButtonsToUpdate();
        }
    }

    private void deleteAction()
    {
        Contact c = panel.contactList.getSelectedValue();
        boolean success = false;
        
        if(c != null && confirm("Really delete " + c + "?"))
        {
            try(DAO dao = new DAO())
            {
                dao.deleteContact(c);
                message(c + " deleted");
                success = true;
            }
            catch(Exception ex)
            {
                error(ex);
            }
        }
        
        if(success)
        {
            refreshContactsList();
            clearAllFields();
            setButtonsToInsert();
        }
    }

    private void removeEmailAcion()
    {
    }

    private void removePhoneAction()
    {
    }

    private void saveAction()
    {
        boolean success = false;
        String id = panel.contactPanel.idField.getText();
        
        try (DAO dao = new DAO())
        {
            if (id == null || id.length() == 0)
            {
                if(validate())
                {
                    Contact c = dao.createContact(
                            (String) panel.contactPanel.titleField.getSelectedItem(),
                            panel.contactPanel.firstNameField.getText(),
                            panel.contactPanel.lastNameField.getText(),
                            panel.contactPanel.notesField.getText());
                    message("Created " + c);
                    success = true;
                }
            }
            else
            {
                Contact c = panel.contactList.getSelectedValue();
                
                if(c != null && validate())
                {
                    c.setTitle((String) panel.contactPanel.titleField.getSelectedItem());
                    c.setFirstName(panel.contactPanel.firstNameField.getText());
                    c.setLastName(panel.contactPanel.lastNameField.getText());
                    c.setNotes(panel.contactPanel.notesField.getText());
                    
                    dao.updateContact(c);
                    message("Updated " + c);
                    success = true;
                }
            }
        }
        catch (Exception ex)
        {
            error(ex);
        }
        
        if(success)
        {
            refreshContactsList();
            clearAllFields();
            setButtonsToInsert();
        }
    }
    
    private boolean validate()
    {
        String fname = panel.contactPanel.firstNameField.getText();
        if(fname != null && fname.length() > 100)
        {
            message("First name must be less than 100 characters");
            panel.contactPanel.firstNameField.requestFocus();
            return false;
        }
        
        String lname = panel.contactPanel.lastNameField.getText();
        if(lname == null || lname.trim().length() == 0)
        {
            message("Last name is required");
            panel.contactPanel.lastNameField.requestFocus();
            return false;
        }
        else if(lname.length() > 100)
        {
            message("Last name must be less than 100 characters");
            panel.contactPanel.lastNameField.requestFocus();
            return false;
        }
        
        return true;
    }

    private void refreshContactsList()
    {
        try (DAO dao = new DAO())
        {
            List<Contact> list = dao.findAllContacts();
            ListModel<Contact> model = createListModel(list);
            this.panel.contactList.setModel(model);
        }
        catch (Exception ex)
        {
            error(ex);
        }
    }

    private void clearAllFields()
    {
        ContactPanel contact = panel.contactPanel;
        contact.emailList.setModel(new DefaultListModel<>());
        contact.firstNameField.setText(null);
        contact.idField.setText(null);
        contact.lastNameField.setText(null);
        contact.notesField.setText(null);
        contact.phoneList.setModel(new DefaultListModel<>());
        contact.titleField.setSelectedIndex(0);
    }

    private void setButtonsToInsert()
    {
        panel.contactPanel.addPhoneButton.setEnabled(false);
        panel.contactPanel.addPhoneField.setEditable(false);
        panel.contactPanel.addEmailButton.setEnabled(false);
        panel.contactPanel.addEmailField.setEditable(false);
        panel.removeEmailItem.setEnabled(false);
        panel.removePhoneItem.setEnabled(false);
        panel.clearButton.setEnabled(true);
        panel.deleteButton.setEnabled(false);
        panel.saveButton.setEnabled(true);
        panel.saveButton.setText("save");
    }

    private void setButtonsToUpdate()
    {
        panel.contactPanel.addPhoneButton.setEnabled(true);
        panel.contactPanel.addPhoneField.setEditable(true);
        panel.contactPanel.addEmailButton.setEnabled(true);
        panel.contactPanel.addEmailField.setEditable(true);
        panel.removeEmailItem.setEnabled(true);
        panel.removePhoneItem.setEnabled(true);
        panel.clearButton.setEnabled(true);
        panel.deleteButton.setEnabled(true);
        panel.saveButton.setEnabled(true);
        panel.saveButton.setText("update");
    }

    private boolean confirm(String question)
    {
        int value = JOptionPane.showConfirmDialog(frame, question, "CONFIRM", JOptionPane.YES_NO_OPTION);
        return value == YES_OPTION;
    }

    private void message(String message)
    {
        JOptionPane.showMessageDialog(frame, message, "MESSAGE", INFORMATION_MESSAGE);
    }

    private void error(Exception ex)
    {
        String message = ex.getClass().getSimpleName() + ": " + ex.getMessage();
        JOptionPane.showMessageDialog(frame, message, "ERROR", ERROR_MESSAGE);
    }

    private <T> ListModel<T> createListModel(Collection<T> list)
    {
        DefaultListModel<T> model = new DefaultListModel<>();
        list.forEach(t -> model.addElement(t));
        return model;
    }
}
