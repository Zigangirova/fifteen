import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Frame extends JFrame {
    private JPanel gamePane = new JPanel();
    private BorderLayout borderLayout1 = new BorderLayout();
    private JLabel status = new JLabel();
    private JButton[][] buttons = new JButton[4][4];
    private Board field = new Board();
    private Matrix<Integer> solverMatrix = new MatrixImpl(4, 4, 0);

    /**
     * Construct the frame
     */
    Frame() {
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Component initialization
     */
    private void jbInit() throws InterruptedException{
        JPanel contentPane = (JPanel) this.getContentPane();
        contentPane.setLayout(borderLayout1);
        this.setSize(new Dimension(213, 275));
        this.setTitle("Fifteen");

        this.setResizable(false);

        JMenuBar menuBar = new JMenuBar();

        JMenu menuGame = new JMenu("Game");
        JMenu menuHelp = new JMenu("Help");

        menuBar.add(menuGame);
        menuBar.add(menuHelp);

        this.setJMenuBar(menuBar);

        JMenuItem item1 = new JMenuItem("New game");
        JMenuItem item2 = new JMenuItem("Exit");

        JMenuItem item3 = new JMenuItem("Get solution");

        item1.addActionListener(e -> newGame());
        item2.addActionListener(e -> {
            System.exit(0);//Выход из системы
        });
        item3.addActionListener(e -> {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    solverMatrix.set(i, j, field.getMatrix()[i][j]);
                }
            }
            List<Integer> result = SolverKt.fifteenGameSolution(solverMatrix);
            System.out.println(result);
            status.setText("I know, what to do");
            int show = JOptionPane.showConfirmDialog(null,
                    "Do you want some?", "I have a solution, boi!", JOptionPane.YES_NO_OPTION);
            if (show == JOptionPane.YES_OPTION) {
                status.setText("I'm doing magic!!!");
                for (int move : result) {

                    field.makeMove(move);
                    recount();
                    /*try {
                        Thread.sleep(1000);
                    }
                    catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    finally {
                        System.out.println("rr");
                    }
                }
                recount();*/
                }
            }

        });


        menuGame.add(item1);
        menuGame.add(item2);
        menuHelp.add(item3);
        gamePane.setLayout(null);

        int count = 0;
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++) {
                buttons[i][j] = new JButton("" + count);
                buttons[i][j].addMouseListener(new mAdapter(i, j));
                buttons[i][j].setSize(50, 50);
                buttons[i][j].setLocation(50 * j, 50 * i);
                buttons[i][j].setCursor(new Cursor(Cursor.HAND_CURSOR));
                gamePane.add(buttons[i][j]);
                count++;
            }
        buttons[0][0].setText(" ");

        contentPane.add(gamePane, BorderLayout.CENTER);

        status.setBorder(BorderFactory.createEtchedBorder());

        contentPane.add(status, BorderLayout.SOUTH);

        newGame();
    }

    void recount() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (field.getMatrix()[i][j] != 0)
                    buttons[i][j].setText("" + field.getMatrix()[i][j]);
                else buttons[i][j].setText("");

            }
        }
    }


    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            System.exit(0);
        }
    }


    private void newGame() {

        field.randomizeMatrix();

        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++) {

                if (field.getMatrix()[i][j] != 0) buttons[i][j].setText("" + field.getMatrix()[i][j]);
                else buttons[i][j].setText("");
            }
        status.setText("new game started");
        gamePane.setVisible(true);
    }


    class mAdapter extends java.awt.event.MouseAdapter {
        int posi, posj;


        mAdapter(int posI, int posJ) {
            this.posi = posI;
            this.posj = posJ;
        }


        public void mouseReleased(MouseEvent e) {

            if (field.makeMove(posi, posj)) {
                recount();
                status.setText("nice turn!");
            } else status.setText("it's impossible");


            if (field.checkWin()) {
                status.setText("You win!!!");
                int result = JOptionPane.showConfirmDialog(null,
                        "You win!!!", "You win!!! New game?", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) newGame();
                else gamePane.setVisible(false);
            }
        }
    }
}

