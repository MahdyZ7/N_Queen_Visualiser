import java.awt.*;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ChessBoard extends JPanel {
	public static final int delay = 1000;	// time of each frame in ms

	// grid variabiles
	private static int size;
	private int sq_size;
	private static final int panel_size = 350;

	// N Queen variables
	private static int position[];
	private int tail;
	private int safe;

	public ChessBoard() {
		size = 4;
		sq_size = panel_size/size;
		position = new int[size];
		tail = 0;
		safe = 1;
		setPreferredSize(new Dimension(panel_size, panel_size));
	}

	public ChessBoard(int x) {
		ChessBoard.size = x;
		sq_size = panel_size/size;
		position = new int[size];
		tail = 0;
		safe = 1;
		setPreferredSize(new Dimension(panel_size, panel_size));
	}
	
	public static boolean isQueenSafe(int queen, int row) {
		for (int i = 0; i < queen; i++) {
			int other_row = position[i];
			if (other_row == row ||
					other_row == row - (queen - i) ||
					other_row == row + (queen - i))
				return false;
		}
		return true;
	}

	public void printScreen()
	{
		repaint();
		try{
			Thread.sleep(delay);
		} catch (InterruptedException e) {
				e.printStackTrace();
		}
	}
	
	public void solve(int k) {
		tail = k ;
		safe = 0;
		if (k == size) {
			safe = 1;
			printScreen();
		} else {
			for (int i = 0; i < size; i++) {
				if (isQueenSafe(k, i)) {
					position[k] = i;
					safe = 0;
					printScreen();
					solve(k + 1);
					tail = k;
					printScreen();
				}
				else{
					safe = -1;
					position[k] = i;
					printScreen();
				}
			}
		}
		tail = k;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawChessBoard(g);
		drawQueens(g);
		if (safe == -1)
			highlightCell(g);
		else if (safe == 1)
			highlightGrid(g);
	}


	private void drawChessBoard(Graphics g) {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				int x = j * sq_size;
				int y = i * sq_size;
				if ((i + j) % 2 == 0) {
					g.setColor(Color.LIGHT_GRAY);
				} else {
					g.setColor(Color.BLACK);
				}
				g.fillRect(x, y, sq_size, sq_size);
			}
		}
	}

	private void drawQueens(Graphics g) {
		for (int i = 0; i <= tail && i < size; i++)
		{
			int y = i * sq_size + sq_size / 4;
			int x = position[i] * sq_size + sq_size / 4;
			g.setColor(Color.RED);
			g.fillOval(x, y, sq_size / 2, sq_size / 2);
		}
	}

	private void highlightCell(Graphics g) {
        int y = tail * sq_size;
        int x = position[tail] * sq_size;
        g.setColor(new Color(255, 0, 0, 80));
        g.fillRect(x, y, sq_size, sq_size);
    }

	private void highlightGrid(Graphics g) {
        g.setColor(new Color(0, 255, 0, 80));
        g.fillRect(0, 0, panel_size, panel_size);
    }
	
	public static void main(String[] args) {
		int size;

		try {
			size = Integer.parseInt(args[0]);
			if (size < 4)
				throw new ArrayIndexOutOfBoundsException();
		} catch (Exception e) {
			System.out.println("Please eneter a valid argument ( ex: java Draw.java 4)");
			return;
		}
		
		JFrame frame = new JFrame();
		ChessBoard board = new ChessBoard(size);
		frame.add(board);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		board.solve(0);
	}
}
