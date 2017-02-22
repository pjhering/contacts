package contacts.entities;

import java.util.List;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;

public class DAO implements AutoCloseable
{
    private final Session s;
    private final Transaction t;
    
    public DAO()
    {
        s = Hibernate.openSession();
        t = s.beginTransaction();
    }
    
    public Phone createPhone(String value)
    {
        Phone phone = new Phone(value);
        Long id = (Long) s.save(phone);
        phone.setId(id);
        
        return phone;
    }
    
    public EMail createEMail(String value)
    {
        EMail email = new EMail(value);
        Long id = (Long) s.save(email);
        email.setId(id);
        
        return email;
    }
    
    public Contact createContact(String title, String first, String last, String note)
    {
        Contact contact = new Contact(title, first, last, note);
        Long id = (Long) s.save(contact);
        contact.setId(id);
        
        return contact;
    }
    
    public Phone findPhoneById(Long id)
    {
        return s.load(Phone.class, id);
    }
    
    public EMail findEMailById(Long id)
    {
        return s.load(EMail.class, id);
    }
    
    public Contact findContactById(Long id)
    {
        return s.load(Contact.class, id);
    }
    
    public List<Phone> findAllPhones()
    {
        Query<Phone> q = s.createQuery("from Phone p", Phone.class);
        return q.list();
    }
    
    public List<EMail> findAllEMails()
    {
        Query<EMail> q = s.createQuery("from EMail e", EMail.class);
        return q.list();
    }
    
    public List<Contact> findAllContacts()
    {
        Query<Contact> q = s.createQuery("from Contact c", Contact.class);
        return q.list();
    }
    
    public void updatePhone(Phone value)
    {
        s.update(value);
    }
    
    public void updateEMail(EMail value)
    {
        s.update(value);
    }
    
    public void updateContact(Contact value)
    {
        s.update(value);
    }
    
    public void deletePhone(Phone value)
    {
        s.delete(value);
    }
    
    public void deleteEMail(EMail value)
    {
        s.delete(value);
    }
    
    public void deleteContact(Contact value)
    {
        s.delete(value);
    }
    
    @Override
    public void close() throws Exception
    {
        s.flush();
        TransactionStatus status = t.getStatus();
        
        switch(status)
        {
            case ACTIVE:
                t.commit();
                break;
                
            case FAILED_COMMIT:
                t.rollback();
        }
        
        s.close();
    }
}
