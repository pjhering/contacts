package contacts;

import contacts.entities.Contact;
import contacts.entities.DAO;
import contacts.entities.EMail;
import contacts.entities.Hibernate;
import contacts.entities.Phone;
import contacts.ui.AppPanel;
import contacts.ui.ContactPanel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
    private final ContactPanel contact;

    private final Pattern validPhone = Pattern.compile("^[0-9]{3}-[0-9]{3}-[0-9]{4}$");
    private final Pattern validEmail = Pattern.compile("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");

    private Main()
    {
        panel = new AppPanel();
        contact = panel.contactPanel;
        panel.setBorder(createEmptyBorder(5, 5, 5, 5));
        contact.addPhoneButton.addActionListener(a1 -> addPhoneAction());
        contact.addEmailButton.addActionListener(a2 -> addEmailAction());
        panel.clearButton.addActionListener(a3 -> clearAction());
        panel.contactList.addListSelectionListener(a4 -> contactListChanged());
        panel.deleteButton.addActionListener(a5 -> deleteAction());
        panel.removeEmailItem.addActionListener(a6 -> removeEmailAction());
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
            contact.idField.setText(c.getId().toString());
            contact.titleField.setSelectedItem(c.getTitle());
            contact.firstNameField.setText(c.getFirstName());
            contact.lastNameField.setText(c.getLastName());
            contact.notesField.setText(c.getNotes());
            contact.phoneList.setModel(createListModel(c.getPhoneNumbers()));
            contact.emailList.setModel(createListModel(c.getEmailAddresses()));
            
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

    private void addPhoneAction()
    {
        Contact c = panel.contactList.getSelectedValue();
        boolean success = false;
        
        if(c == null)
        {
            message("A contact must be selected");
            return;
        }
        
        String text = contact.addPhoneField.getText();
        Matcher m = validPhone.matcher(text);
        
        if(m.matches())
        {
            try(DAO dao = new DAO())
            {
                Phone p = dao.createPhone(text, c);
                success = true;
                message("Added " + p);
                contact.addPhoneField.setText(null);
            }
            catch(Exception ex)
            {
                error(ex);
            }
        }
        else
        {
            message("Invalid phone number format");
            contact.addPhoneField.requestFocus();
        }
        
        if(success)
        {
            refreshContactsList();
            clearAllFields();
            setButtonsToInsert();
            panel.contactList.setSelectedValue(c, true);
        }
    }

    private void removePhoneAction()
    {
        Contact c = panel.contactList.getSelectedValue();
        boolean success = false;
        
        if(c == null)
        {
            message("A contact must be selected");
            return;
        }
        else
        {
            Phone p = contact.phoneList.getSelectedValue();
            
            if(p == null)
            {
                message("A phone number must be selected");
            }
            else
            {
                try(DAO dao = new DAO())
                {
                    c.getPhoneNumbers().remove(p);
                    dao.updateContact(c);
                    dao.deletePhone(p);
                    
                    success = true;
                }
                catch(Exception ex)
                {
                    error(ex);
                }
            }
        }
        
        if(success)
        {
            refreshContactsList();
            clearAllFields();
            setButtonsToInsert();
            panel.contactList.setSelectedValue(c, true);
        }
    }

    private void addEmailAction()
    {
        Contact c = panel.contactList.getSelectedValue();
        boolean success = false;
        
        if(c == null)
        {
            message("A contact must be selected");
            return;
        }
        
        String text = contact.addEmailField.getText();
        Matcher m = validEmail.matcher(text);
        
        if(m.matches())
        {
            try(DAO dao = new DAO())
            {
                EMail p = dao.createEMail(text, c);
                success = true;
                message("Added " + p);
                contact.addEmailField.setText(null);
            }
            catch(Exception ex)
            {
                error(ex);
            }
        }
        else
        {
            message("Invalid email address format");
            contact.addPhoneField.requestFocus();
        }
        
        if(success)
        {
            refreshContactsList();
            clearAllFields();
            setButtonsToInsert();
            panel.contactList.setSelectedValue(c, true);
        }
    }

    private void removeEmailAction()
    {
    }

    private void saveAction()
    {
        boolean success = false;
        String id = contact.idField.getText();
        
        try (DAO dao = new DAO())
        {
            if (id == null || id.length() == 0)
            {
                if(validate())
                {
                    Contact c = dao.createContact(
                            (String) contact.titleField.getSelectedItem(),
                            contact.firstNameField.getText(),
                            contact.lastNameField.getText(),
                            contact.notesField.getText());
                    message("Created " + c);
                    success = true;
                }
            }
            else
            {
                Contact c = panel.contactList.getSelectedValue();
                
                if(c != null && validate())
                {
                    c.setTitle((String) contact.titleField.getSelectedItem());
                    c.setFirstName(contact.firstNameField.getText());
                    c.setLastName(contact.lastNameField.getText());
                    c.setNotes(contact.notesField.getText());
                    
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
        String fname = contact.firstNameField.getText();
        if(fname != null && fname.length() > 100)
        {
            message("First name must be less than 100 characters");
            contact.firstNameField.requestFocus();
            return false;
        }
        
        String lname = contact.lastNameField.getText();
        if(lname == null || lname.trim().length() == 0)
        {
            message("Last name is required");
            contact.lastNameField.requestFocus();
            return false;
        }
        else if(lname.length() > 100)
        {
            message("Last name must be less than 100 characters");
            contact.lastNameField.requestFocus();
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
        contact.addPhoneButton.setEnabled(false);
        contact.addPhoneField.setEditable(false);
        contact.addEmailButton.setEnabled(false);
        contact.addEmailField.setEditable(false);
        panel.removeEmailItem.setEnabled(false);
        panel.removePhoneItem.setEnabled(false);
        panel.clearButton.setEnabled(true);
        panel.deleteButton.setEnabled(false);
        panel.saveButton.setEnabled(true);
        panel.saveButton.setText("save");
    }

    private void setButtonsToUpdate()
    {
        contact.addPhoneButton.setEnabled(true);
        contact.addPhoneField.setEditable(true);
        contact.addEmailButton.setEnabled(true);
        contact.addEmailField.setEditable(true);
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
