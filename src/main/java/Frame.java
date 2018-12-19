import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

public class Frame extends JFrame {
    JPanel contentPane;
    JPanel gamePane = new JPanel();
    BorderLayout borderLayout1 = new BorderLayout();

    JLabel status = new JLabel();

    JButton[][] buttons = new JButton[4][4];

    int[][] matrix = new int[4][4];

    /**
     * Construct the frame
     */
    public Frame() {
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
    private void jbInit() {
        contentPane = (JPanel) this.getContentPane();
        contentPane.setLayout(borderLayout1);
        this.setSize(new Dimension(206, 275));
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

        JMenuItem item3 = new JMenuItem("About...");

        item1.addActionListener(e -> newGame());
        item2.addActionListener(e -> {
            System.exit(0);//Выход из системы
        });
        //item3.addActionListener(e -> JOptionPane.


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
                matrix[i][j] = count;//задаем матрицу
                count++;
            }
        buttons[0][0].setText(" ");

        contentPane.add(gamePane, BorderLayout.CENTER);

        status.setBorder(BorderFactory.createEtchedBorder());

        contentPane.add(status, BorderLayout.SOUTH);

        newGame();
    }


    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            System.exit(0);
        }
    }


    private boolean canBeSolved(int[] invariants) {
        int sum = 0;
        for (int i = 0; i < 16; i++) {
            if (invariants[i] == 0) {
                sum += i / 4;
                continue;
            }

            for (int j = i + 1; j < 16; j++) {
                if (invariants[j] < invariants[i])
                    sum++;
            }
        }
        System.out.println(sum % 2 == 0);
        return sum % 2 == 0;
    }


    public void randomizeMatrix() {
        Random generator = new Random();
        int[] invariants = new int[16];

        do {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    matrix[i][j] = 0;
                    invariants[i * 4 + j] = 0;
                }
            }

            for (int i = 1; i < 16; i++) {
                int k, l;
                do {
                    k = generator.nextInt(4);
                    l = generator.nextInt(4);
                }
                while (matrix[k][l] != 0);
                matrix[k][l] = i;
                invariants[k * 4 + l] = i;
            }
        }
        while (!canBeSolved(invariants));
    }


    public void newGame() {

        randomizeMatrix();
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++) {

                if (matrix[i][j] != 0) buttons[i][j].setText("" + matrix[i][j]);
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


            if ((posj != 3) && (matrix[posi][posj + 1] == 0)) {
                matrix[posi][posj + 1] = matrix[posi][posj];
                matrix[posi][posj] = 0;

                buttons[posi][posj].setText("");
                buttons[posi][posj + 1].setText("" + matrix[posi][posj + 1]);
                status.setText("good turn");
            } else if ((posj != 0) && (matrix[posi][posj - 1] == 0)) {
                matrix[posi][posj - 1] = matrix[posi][posj];
                matrix[posi][posj] = 0;
                buttons[posi][posj].setText("");
                buttons[posi][posj - 1].setText("" + matrix[posi][posj - 1]);
                status.setText("good turn");
            } else if ((posi != 3) && (matrix[posi + 1][posj] == 0)) {
                matrix[posi + 1][posj] = matrix[posi][posj];
                matrix[posi][posj] = 0;
                buttons[posi][posj].setText("");
                buttons[posi + 1][posj].setText("" + matrix[posi + 1][posj]);
                status.setText("good turn");
            } else if ((posi != 0) && (matrix[posi - 1][posj] == 0)) {
                matrix[posi - 1][posj] = matrix[posi][posj];
                matrix[posi][posj] = 0;
                buttons[posi][posj].setText("");
                buttons[posi - 1][posj].setText("" + matrix[posi - 1][posj]);
                status.setText("good turn");
            } else status.setText("It's impossible");


            int count = 1;
            int error = 0;
            for (int i = 0; i < 4; i++)
                for (int j = 0; j < 4; j++) {
                    if (matrix[i][j] != count) error++;
                    count++;
                }
            if (error == 1) {
                status.setText("You win!!!");
                int result = JOptionPane.showConfirmDialog(null,
                        "You win!!!", "You win!!! New game?", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) newGame();
                else gamePane.setVisible(false);
            }
        }
    }
}

