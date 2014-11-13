package suncertify.db.utils;

import java.awt.Toolkit;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.JTextField;

/**
 * Provides a class for allowing a user to enter a positive <code>long</code> into a <code>javax.swing.JTextField</code>.
 * The data that is entered is validated before is is allowed to be displayed.
 * Optionally, a PC beep may sound if the user enters an invalid character.
 * It is also possible to have a maximum value for input set - Any characters that are entered
 * that cause the maximum value to be exceeded will be treated as an invalid character entry.
 *
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public class PositiveLongTextField extends JTextField
{
    private static final long DEFAULT_UNLIMITED_VALUE = -1L;
    private Toolkit toolkit;
    private long limitValue;
    private boolean sound;

 	/**
     * Constructs an empty <tt>PositiveLongTextField</tt>.
	 * A default model is created, no initial value, the number of columns is set to 0 and the sound is on.
     */
    public PositiveLongTextField()
    {
        limitValue = DEFAULT_UNLIMITED_VALUE;
        toolkit = Toolkit.getDefaultToolkit();
        setSound(true);
    }

 	/**
     * Constructs an empty <tt>PositiveLongTextField</tt> with the specified number of columns.
	 * A default model is created and there is no initial value.
     *
     * @param columns The number of columns to use to calculate the preferred width.
	 * If columns is set to zero, the preferred width will be whatever naturally results from the component implementation.
     */
    public PositiveLongTextField(int columns)
    {
        super(columns);
        limitValue = DEFAULT_UNLIMITED_VALUE;
        toolkit = Toolkit.getDefaultToolkit();
        setSound(true);
    }

	/**
     * Constructs a <tt>PositiveLongTextField</tt> initialized with the specified value and columns.
	 * A default model is created.
     *
     * @param value The value to be displayed.
     * @param columns The number of columns to use to calculate the preferred width.
	 * If columns is set to zero, the preferred width will be whatever naturally results from the component implementation.
     */
    public PositiveLongTextField(long value, int columns)
    {
        super(columns);
        limitValue = DEFAULT_UNLIMITED_VALUE;
        toolkit = Toolkit.getDefaultToolkit();
        setValue(value);
        setSound(true);
    }

 	/**
     * Constructs a <tt>PositiveLongTextField</tt> that uses the given text storage model and the given number of columns.
	 * If the document is null, a default model is created.
     *
     * @param doc The text storage to use.  If this is <code>null</code>, a default will be provided by calling the <code>createDefaultModel</code> method.
     * @param value The initial value to display.
     * @param columns The number of columns to use to calculate the preferred width >= 0.
	 * If columns is set to zero, the preferred width will be whatever naturally results from the component implementation.
     * @throws java.lang.IllegalArgumentException if columns < 0
     */
    public PositiveLongTextField(Document doc, long value, int columns)
    {
        super(doc, "", columns);
        limitValue = DEFAULT_UNLIMITED_VALUE;
        toolkit = Toolkit.getDefaultToolkit();
        setValue(value);
        setSound(true);
    }

	/**
	 * Sets the underlying value that is represented by this <tt>PositiveLongTextField</tt>.
	 *
	 * @param value The underlying value to set this <tt>PositiveLongTextField</tt> to.
	 */
    public void setValue(long value)
    {
        setText(String.valueOf(value));
    }

 	/**
	 * Returns the underlying value that is represented by this <tt>PositiveLongTextField</tt>.
	 * If no value has been entered, zero is returned.
      *
	 * @return The underlying value that is represented by this <tt>PositiveLongTextField</tt>.
	 */
    public long getValue()
    {
        if(getText().length() == 0)
        {
            return 0L;
        }
        else
        {
            return Long.parseLong(getText());
        }
    }

	/**
	 * Sets the sound on or off for this <tt>PositiveLongTextField</tt>.
     * A <code>java.awt.Toolkit.beep()</code> will occur if a keystroke invalidates the input
     * as a <code>long</code> value or exceeds the minimum (0) or {@link #setLimitValue(long) maximum} value.
	 * The beep will only occur if the sound is set to "on" (<code>true</code).
     *
	 * @param sound Sound on or off for this <tt>PositiveLongTextField</tt>.
	 */
    public void setSound(boolean sound)
    {
        this.sound = sound;
    }

  	/**
	 * Returns <code>true</code> if there is sound when invalid data enetered into
     * this <tt>PositiveLongTextField</tt>, <code>false</code> otherwise.
	 *
	 * @return <code>true</code> if there is sound when invalid data entered into
     * this <tt>PositiveLongTextField</tt>, <code>false</code> otherwise.
	 */
    public boolean isSound()
    {
        return sound;
    }

	/**
	 * Sets the maximum limited value permitted by this <tt>PositiveLongTextField</tt>.
	 * Specifying a value less than zero, will result in having no limit.
	 *
	 * @param limitValue The maximum limited value permitted by this <tt>PositiveLongTextField</tt>.
	 */
    public void setLimitValue(long limitValue)
    {
        if(limitValue < 0L)
        {
            limitValue = DEFAULT_UNLIMITED_VALUE;
        }

        this.limitValue = limitValue;
    }

	/**
	 * Returns the maximum limited value permitted by this <tt>PositiveLongTextField</tt>.
	 * A return value of less than zero indicates having no limit.
	 *
	 * @return The maximum limited value permitted by this <tt>PositiveLongTextField</tt>.
	 */
    public long getLimitValue()
    {
        return limitValue;
    }

	/**
	 * Returns <code>true</code> if there is a maximum limited value permitted by this <tt>PositiveLongTextField</tt>, <code>false</code> otherwise.
	 *
	 * @return <code>true</code> if there is a maximum limited value permitted by this <tt>PositiveLongTextField</tt>, <code>false</code> otherwise.
	 */
    public boolean isUnlimitedValue()
    {
        return limitValue < 0;
    }

    /**
     * Returns a <code>Document</code> implementation that ensures user input is a valid positive <code>long</code> value.
     * The implementation class is a private member of this class.
     *
     * @return A <code>Document</code> implementation that ensures user input is a valid positive <code>long</code> value.
     */
    protected Document createDefaultModel()
    {
        return new NumDocument();
    }

    // a document that does the validating on user entries
    private class NumDocument extends PlainDocument
    {
        public void insertString(int offset, String str, AttributeSet a) throws BadLocationException
        {
            StringBuffer sb = new StringBuffer();

            for(int i = 0; i < str.length(); i++)
            {
                char c = str.charAt(i);

                if(Character.isDigit(c))
                {
                    sb.append(c);
                    continue;
                }

                if(isSound())
                {
                    toolkit.beep();
                }

                break;
            }

            try
            {
                StringBuffer text = new StringBuffer(PositiveLongTextField.this.getText());
                text.insert(offset, sb.toString());
                long value = Long.parseLong(text.toString());

                if(value < 0L)
                {
                    throw new NumberFormatException();
                }

                if(!isUnlimitedValue() && value > getLimitValue())
                {
                    if(isSound())
                    {
                        toolkit.beep();
                    }
                }
                else
                {
                    super.insertString(offset, sb.toString(), a);
                }
            }
            catch(NumberFormatException nfe)
            {
                toolkit.beep();
            }
        }
    }
}
