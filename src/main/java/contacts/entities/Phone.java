package contacts.entities;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.AUTO;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="PHONE")
public class Phone implements Serializable, Comparable<Phone>
{
    @Id
    @GeneratedValue(strategy=AUTO)
    @Column(name="ID", nullable=false, unique=true)
    private Long id;
    
    @Column(name="TEXTVALUE", nullable=false, length=100)
    private String textValue;
    
    public Phone()
    {
        this(null, null);
    }
    
    public Phone(String value)
    {
        this(null, value);
    }
    
    public Phone(Long id, String value)
    {
        this.id = id;
        this.textValue = value;
    }

    @Override
    public String toString()
    {
        return textValue;
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
        final Phone other = (Phone) obj;
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
        hash = 47 * hash + Objects.hashCode(this.id);
        return hash;
    }
    @Override
    public int compareTo(Phone o)
    {
        return textValue.compareTo(o.textValue);
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getTextValue()
    {
        return textValue;
    }

    public void setTextValue(String value)
    {
        this.textValue = value;
    }
}
