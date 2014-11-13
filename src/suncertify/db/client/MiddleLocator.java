package suncertify.db.client;

import java.awt.Toolkit;
import java.awt.Component;

/**
 * Provides the calculations involved in locating a <code>Component</code> in the centre of some bounds.
 * The bounds may be the current screen size, or within another <code>Component</code>.
 *
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public class MiddleLocator
{
    private int x;
    private int y;
    private int width;
    private int height;

    /**
     * IConstructs a <code>MiddleLocator</code> such that any <code>Component</code>s would be located in the center of the screen.
     */
    public MiddleLocator()
    {
        this(0, 0, (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(), (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight());
    }

    /**
     * Constructs a <code>MiddleLocator</code> such that any <code>Component</code>s would be located in the center of the given coordinates
     * and the bottom right of the screen.
     *
     * @param x The top left x bound to locate the <code>Component</code> in.
     * @param y The top left y bound to locate the <code>Component</code> in.
     */
    public MiddleLocator(int x, int y)
    {
        this(x, y, (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth() - x, (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight() - y);
    }

    /**
     * Constructs a <code>MiddleLocator</code> such that any <code>Component</code>s would be located in the center of the bounds of given
     * <code>Component</code> location.
     *
     * @param c The <code>Component</code> whose bounds are to be used to locate any other <code>Component</code>s.
     */
    public MiddleLocator(Component c)
    {
        this(c.getLocation().x, c.getLocation().y, c.getWidth(), c.getHeight());
    }

    /**
     * Constructs a <code>MiddleLocator</code> such that any <code>Component</code>s would be located in the center of the given
     * x,y coordinates and the x,y coordinates plus the width,height.
     *
     * @param x The top left x bound to locate the <code>Component</code> in.
     * @param y The top left y bound to locate the <code>Component</code> in.
     * @param width The additional width from the x coordinate in which to centrally locate <code>Component</code>s in.
     * @param height The additional downward height from the y coordinate in which to centrally locate <code>Component</code>s in.
     */
    public MiddleLocator(int x, int y, int width, int height)
    {
        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);
    }

    /**
     * Returns the x coordinate that is used to centrally locate <code>Component</code>s.
     *
     * @return The x coordinate that is used to centrally locate <code>Component</code>s
     */
    public int getX()
    {
        return x;
    }

    /**
     * Sets the x coordinate that is used to centrally locate <code>Component</code>s.
     *
     * @param x The new value of the x coordinate that is used to centrally locate <code>Component</code>s.
     */
    public void setX(int x)
    {
        this.x = x;
    }

    /**
     * Returns the y coordinate that is used to centrally locate <code>Component</code>s.
     *
     * @return The y coordinate that is used to centrally locate <code>Component</code>s.
     */
    public int getY()
    {
        return y;
    }

    /**
     * Sets the y coordinate that is used to centrally locate <code>Component</code>s.
     *
     * @param y The new value of the y coordinate that is used to centrally locate <code>Component</code>s.
     */
    public void setY(int y)
    {
        this.y = y;
    }

    /**
     * Returns the width value that is used to centrally locate <code>Component</code>s.
     *
     * @return The width value that is used to centrally locate <code>Component</code>s
     */
    public int getWidth()
    {
        return width;
    }

    /**
     * Sets the width value that is used to centrally locate <code>Component</code>s.
     *
     * @param width The new value of the width value that is used to centrally locate <code>Component</code>s.
     */
    public void setWidth(int width)
    {
        this.width = width;
    }

    /**
     * Returns the height value that is used to centrally locate <code>Component</code>s.
     *
     * @return The height value that is used to centrally locate <code>Component</code>s
     */
    public int getHeight()
    {
        return height;
    }

    /**
     * Sets the height value that is used to centrally locate <code>Component</code>s.
     *
     * @param height The new value of the height value that is used to centrally locate <code>Component</code>s.
     */
    public void setHeight(int height)
    {
        this.height = height;
    }

    /**
     * Locate the given component within the current set boundsof this <code>MiddleLocator</code>.
     *
     * @param c The <code>Component</code> to centrally locate within the bounds of this <code>MiddleLocator</code>.
     */
    public void locate(Component c)
    {
        c.setLocation(Math.max((width - c.getWidth()) / 2 + x, 0), Math.max((height - c.getHeight()) / 2 + y, 0));
    }
}
