package contacts;

import contacts.entities.Hibernate;

public class Main
{

    public static void main(String[] args)
    {
        Hibernate.setup();
        Hibernate.shutdown();
    }
}
