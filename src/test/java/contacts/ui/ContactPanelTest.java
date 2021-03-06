package contacts.ui;

import static javax.swing.BorderFactory.createEmptyBorder;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;

public class ContactPanelTest {
    public static void main(String[] args)
    {
        JFrame frame = new JFrame("ContactPanelTest");
        ContactPanel panel = new ContactPanel();
        panel.setBorder(createEmptyBorder(5, 5, 5, 5));
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
