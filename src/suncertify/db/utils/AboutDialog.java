package suncertify.db.utils;

import javax.swing.JLabel;
import javax.swing.JDialog;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Frame;

/**
 * Represents the dialog that appears when a user selects the 'About' option.
 * The dialog contains a single label that displays text.
 * If the user clicks the mouse anywhere in the dialog, clicks out of the dialog
 * or presses a key, the dialog will close itself.
 * This is a personal preference with "About dialogs" that they close as soon
 * as any further user interaction occurs.
 *
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public class AboutDialog extends JDialog
{
    /**
     * The text to display on the label.
     */
    public static final String TEXT =
            "<html>  " +
            "<br>" +
            "<div align=\"center\"><b><u>Sun Certified Developer for the Java 2 Platform</u></b></div><br>" +
            "<div align=\"center\">Application Submission (Version 2.1.2)</div><br>" +
            "<div align=\"center\">Written by <b>Tony Morris</b> 2003</div><br>" +
            "<div align=\"center\"><u>tonymorr@au1.ibm.com</u></div><br>" +
            "</html>";

    private JLabel label;

    /**
     * Construct an <code>AboutDialog</code> with the given owner.
     * The dialog is set to be non-modal on the owner.
     *
     * @param owner The owner that created this <code>AboutDialog</code>.
     */
    public AboutDialog(Frame owner)
    {
        super(owner, "About", false);

        setup();
    }

    // initialize the about dialog
    private void setup()
    {
        instantiate();

        layoutComponents();

        addListeners();

        setComponentAttributes();
    }

    // instantiate all components that make up this dialog
    private void instantiate()
    {
        label = new JLabel();
    }

    // lay the components out on the dialog
    private void layoutComponents()
    {
        getContentPane().add(label);
    }

    // add event handlers to the dialog
    private void addListeners()
    {
        addMouseListener(new MouseAdapter()
        {
            public void mouseReleased(MouseEvent me)
            {
                mousePressed(me);
            }

            public void mousePressed(MouseEvent me)
            {
                AboutDialog.this.hide();
                AboutDialog.this.dispose();
            }
        });

        addKeyListener(new KeyAdapter()
        {
            public void keyPressed(KeyEvent ke)
            {
                AboutDialog.this.hide();
                AboutDialog.this.dispose();
            }
        });

        addFocusListener(new FocusAdapter()
        {
            public void focusLost(FocusEvent fe)
            {
                AboutDialog.this.hide();
                AboutDialog.this.dispose();
            }
        });
    }

    // set attributes of components that make up this dialog
    private void setComponentAttributes()
    {
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setText(TEXT);
    }
}
