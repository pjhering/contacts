package contacts.entities;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public final class Hibernate
{
    private static Configuration config;
    private static ServiceRegistry registry;
    private static SessionFactory factory;
    
    public static Session openSession()
    {
        if(factory == null)
        {
            setup();
        }
        
        return factory.openSession();
    }

    public static void setup()
    {
        if(factory == null)
        {
            config = new Configuration()
                    .setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect")
                    .setProperty("hibernate.connection.driver_class", "org.h2.Driver")
                    .setProperty("hibernate.connection.url", "jdbc:h2:~/contacts/h2")
                    .setProperty("hibernate.connection.username", "contacts")
                    .setProperty("hibernate.connection.password", "contacts")
                    .setProperty("hibernate.show_sql", "true")
                    .setProperty("hibernate.format_sql", "true")
                    .setProperty("hibernate.hbm2ddl.auto", "update")
                    .addAnnotatedClass(Contact.class)
                    .addAnnotatedClass(EMail.class)
                    .addAnnotatedClass(Phone.class);
            registry = new StandardServiceRegistryBuilder()
                    .applySettings(config.getProperties())
                    .build();
            factory = config.buildSessionFactory(registry);
        }
    }
    
    public static void shutdown()
    {
        if(factory != null)
        {
            factory.close();
            factory = null;
            registry = null;
            config = null;
        }
    }
}
