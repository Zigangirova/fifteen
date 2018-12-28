import javax.swing.UIManager;
import java.awt.*;

public class Game
{
    private boolean packFrame = false;

    private Game()
    {
        Frame frame = new Frame();

        if (packFrame)
        {
            frame.pack();
        }
        else
        {
            frame.validate();
        }
        //Center the window
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = frame.getSize();
        if (frameSize.height > screenSize.height)
        {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width)
        {
            frameSize.width = screenSize.width;
        }
        frame.setLocation((screenSize.width - frameSize.width) / 2,
                (screenSize.height - frameSize.height) / 2);
        frame.setVisible(true);
    }
    /**Main method*/
    public static void main(String[] args)
    {
        try
        {
            UIManager.setLookAndFeel(
                    UIManager.getCrossPlatformLookAndFeelClassName());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        new Game();
    }
}