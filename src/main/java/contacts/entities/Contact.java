package contacts.entities;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import static javax.persistence.CascadeType.ALL;
import javax.persistence.Column;
import javax.persistence.Entity;
import static javax.persistence.FetchType.EAGER;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.AUTO;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "CONTACT")
public class Contact implements Serializable, Comparable<Contact>
{

    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "ID", nullable = false, unique = true)
    private Long id;

    @Column(name = "TITLE", length = 10)
    private String title;

    @Column(name = "FIRSTNAME", length = 100)
    private String firstName;

    @Column(name = "LASTNAME", nullable = false, length = 100)
    private String lastName;

    @Column(name = "NOTES", length = 1000)
    private String notes;
    
    @OneToMany(fetch=EAGER, mappedBy="contact")
    private Set<Phone> phoneNumbers = new TreeSet<>(); 
    
    @OneToMany(fetch=EAGER, mappedBy="contact")
    private Set<EMail> emailAddresses = new TreeSet<>(); 
    

    public Contact()
    {
        this(null, null, null, null);
    }

    public Contact(String title, String first, String last, String notes)
    {
        this(null, title, first, last, notes);
    }
    
    public Contact(Long id, String title, String first, String last, String notes)
    {
        this.id = id;
        this.title = title;
        this.firstName = first;
        this.lastName = last;
        this.notes = notes;
    }

    @Override
    public String toString()
    {
        if(title != null)
        {
            if(firstName != null)
            {
                return lastName + ", " + title + " " + firstName;
            }
            else
            {
                return title + " " + lastName;
            }
        }
        else
        {
            if(firstName != null)
            {
                return lastName + ", " + firstName;
            }
            else
            {
                return lastName;
            }
        }
    }

    @Override
    public boolean equals(Object obj)
    {
        if(this == obj)
        {
            return true;
        }
        if(obj == null)
        {
            return false;
        }
        if(getClass() != obj.getClass())
        {
            return false;
        }
        final Contact other = (Contact) obj;
        if(!Objects.equals(this.id, other.id))
        {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public int compareTo(Contact o)
    {
        int value = lastName.compareTo(o.lastName);

        if(value == 0 && firstName != null)
        {
            return firstName.compareTo(o.firstName);
        }
        else
        {
            return value;
        }
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getNotes()
    {
        return notes;
    }

    public void setNotes(String notes)
    {
        this.notes = notes;
    }

    public Set<Phone> getPhoneNumbers()
    {
        return phoneNumbers;
    }

    public void setPhoneNumbers(Set<Phone> phoneNumbers)
    {
        this.phoneNumbers = phoneNumbers;
    }

    public Set<EMail> getEmailAddresses()
    {
        return emailAddresses;
    }

    public void setEmailAddresses(Set<EMail> emailAddresses)
    {
        this.emailAddresses = emailAddresses;
    }
}
