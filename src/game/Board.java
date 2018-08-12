package game;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.NumberFormatter;

public class Board extends JFrame{



	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel gamePanel;
	private JPanel gridPanel;
	private JPanel buttonPanel;
	private JButton rowTotalTestButton;
	private JButton columnTotalTestButton;
	private JButton boxTotalTestButton;
	private JButton columnGroupTestButton;
	private JButton rowGroupTestButton;
	private JButton solveButton;
	private JTextField box1;
	private JTextField box2;
	private JTextField box3;


	private int[][] gameState;
	private HashMap<JComponent,Coordinate> dict;
	private GridLayout gridLayout;
	private int gridHeight = 39;
	private int gridWidth = 44;
	private Font labelFont = new Font("Calibri",Font.BOLD,20);
	private Font inputFont = new Font("Calibri",Font.PLAIN,20);


	public Board() {
		this.setTitle("SUDOKU");
		this.setSize(500,500);
		this.setLayout(new BorderLayout());
		this.setResizable(false);
		this.setBackground(Color.WHITE);


		initialiseWithGameState(false);

		this.add(gamePanel,BorderLayout.CENTER);

		this.add(buttonPanel, BorderLayout.SOUTH);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(true);

	}

	public Board(int[][] grid) {
		JDialog dialog = new JDialog();
		
		this.gameState = grid;
		
		initialiseWithGameState(true);
		JPanel pan = new JPanel();
		pan.setLayout(new BorderLayout());
		pan.add(gamePanel,BorderLayout.CENTER);
		
		dialog.add(pan);
		dialog.setMinimumSize(new Dimension(500,500));
		
		//this.add(buttonPanel, BorderLayout.SOUTH);
	//	repaint();
		
		//dialog.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
		
		
	}
	
	private void createGamePanel() {

		gridLayout = new GridLayout(9,9,8,8);

		gridPanel = new JPanel();
		gridPanel.setLayout(gridLayout);

		gamePanel = new JPanel();

		gamePanel.setLayout(new FlowLayout());
		gamePanel.setBackground(Color.white);

		gamePanel.add(gridPanel);
		
		
		populateGamePanel();

	}
	private void createGamePanelDialog(int grid[][]) {
	
		gridLayout = new GridLayout(9,9,8,8);
		
		gridPanel = new JPanel();
		gridPanel.setLayout(gridLayout);

		gamePanel = new JPanel();

		gamePanel.setLayout(new FlowLayout());
		gamePanel.setBackground(Color.white);

		gamePanel.add(gridPanel);

		populateGamePanelDialog(grid);

	}
	
	private void clearBoard() {
		createGameState();
	}

	private void populateGamePanelDialog(int grid[][]) {
		for(int i=0;i<9;i++) {
			for(int j=0;j<9;j++) {
				if(gameState[i][j] != '\0'){ 
					JLabel label = new JLabel(gameState[i][j] + "",JLabel.CENTER);
					label.setMinimumSize(new Dimension(40,40));
					label.setHorizontalAlignment(SwingConstants.CENTER);
					label.setVerticalAlignment(SwingConstants.CENTER);
					label.setFont(labelFont);
					label.setBackground(Color.white);
					label.setForeground(Color.BLUE);
					gridPanel.add(label);
				}
				else {
					NumberFormat format = NumberFormat.getInstance();
					NumberFormatter formatter = new NumberFormatter(format);
					formatter.setValueClass(Integer.class);

					formatter.setMaximum(9);
					formatter.setAllowsInvalid(true);
					formatter.setCommitsOnValidEdit(false);

					JFormattedTextField field = new JFormattedTextField(formatter);		

					dict.put(field,new Coordinate(i,j));

					field.setMinimumSize(new Dimension(gridWidth,gridHeight));
					field.setPreferredSize(new Dimension(gridWidth,gridHeight));
					field.setMaximumSize(new Dimension(gridWidth,gridHeight));
					field.setHorizontalAlignment(JLabel.CENTER);
					field.setFont(inputFont);
					field.setBackground(Color.white);
					field.setBorder(BorderFactory.createEmptyBorder());
					field.addActionListener(e->{
						System.out.println(field.getText());
					});
					try {
						field.getDocument().addDocumentListener(new DocumentListener() {


							@Override
							public void changedUpdate(DocumentEvent arg0) {
								if(((Integer.parseInt(field.getText())) >= 0 && Integer.parseInt(field.getText()) <= 9 )) {
									//System.out.println(Integer.parseInt(field.getText()));
									int x = dict.get(field).getX();
									int y = dict.get(field).getY();
									gameState[x][y] = Integer.parseInt(field.getText());
									if(isSolved(gameState))System.out.println("Solved");;
									
								}
							}

							@Override
							public void insertUpdate(DocumentEvent arg0) {
								if(((Integer.parseInt(field.getText())) >= 0 && Integer.parseInt(field.getText()) <= 9 )) {
									//System.out.println(Integer.parseInt(field.getText()));
									int x = dict.get(field).getX();
									int y = dict.get(field).getY();
									gameState[x][y] = Integer.parseInt(field.getText());
									if(isSolved(gameState))System.out.println("Solved");;
								}

							}

							@Override
							public void removeUpdate(DocumentEvent arg0) {

							}

						});
					}catch(Exception ex) {

					}
					gridPanel.add(field);
				}
			}
		}
	}

	private void populateGamePanel() {
		for(int i=0;i<9;i++) {
			for(int j=0;j<9;j++) {
				if(gameState[i][j] != '\0'){ 
					JLabel label = new JLabel(gameState[i][j] + "",JLabel.CENTER);
					label.setMinimumSize(new Dimension(40,40));
					label.setHorizontalAlignment(SwingConstants.CENTER);
					label.setVerticalAlignment(SwingConstants.CENTER);
					label.setFont(labelFont);
					label.setBackground(Color.white);
					label.setForeground(Color.BLUE);
					
					gridPanel.add(label);
				}
				else {
					NumberFormat format = NumberFormat.getInstance();
					NumberFormatter formatter = new NumberFormatter(format);
					formatter.setValueClass(Integer.class);

					formatter.setMaximum(9);
					formatter.setAllowsInvalid(true);
					formatter.setCommitsOnValidEdit(false);

					JFormattedTextField field = new JFormattedTextField(formatter);		

					dict.put(field,new Coordinate(i,j));

					field.setMinimumSize(new Dimension(gridWidth,gridHeight));
					field.setPreferredSize(new Dimension(gridWidth,gridHeight));
					field.setMaximumSize(new Dimension(gridWidth,gridHeight));
					field.setHorizontalAlignment(JLabel.CENTER);
					field.setFont(inputFont);
					field.setBackground(Color.white);
					field.setBorder(BorderFactory.createEmptyBorder());
					field.addActionListener(e->{
						System.out.println(field.getText());
					});
					try {
						field.getDocument().addDocumentListener(new DocumentListener() {


							@Override
							public void changedUpdate(DocumentEvent arg0) {
								if(((Integer.parseInt(field.getText())) >= 0 && Integer.parseInt(field.getText()) <= 9 )) {
									//System.out.println(Integer.parseInt(field.getText()));
									int x = dict.get(field).getX();
									int y = dict.get(field).getY();
									gameState[x][y] = Integer.parseInt(field.getText());
									if(isSolved(gameState))System.out.println("Solved");;
								}
							}

							@Override
							public void insertUpdate(DocumentEvent arg0) {
								if(((Integer.parseInt(field.getText())) >= 0 && Integer.parseInt(field.getText()) <= 9 )) {
									//System.out.println(Integer.parseInt(field.getText()));
									int x = dict.get(field).getX();
									int y = dict.get(field).getY();
									gameState[x][y] = Integer.parseInt(field.getText());
									if(isSolved(gameState))System.out.println("Solved");;
								}

							}

							@Override
							public void removeUpdate(DocumentEvent arg0) {

							}

						});
					}catch(Exception ex) {

					}
					gridPanel.add(field);
				}
			}
		}
	}

	private void initialiseWithGameState(boolean condition) {

		dict = new HashMap<JComponent, Coordinate>();
		if(!condition) {
			createGameState();
			createGamePanelDialog(gameState);

		}
		createGamePanel();

		createButtonPanel();
		
	}

	private void createButtonPanel() {
		buttonPanel = new JPanel();
		buttonPanel.setBackground(new Color(240,240,255));
		buttonPanel.setLayout(new FlowLayout());



		boxTotalTestButton = new JButton("Check Box 0 sum") ;
		boxTotalTestButton.addActionListener(e->{
			int x = Integer.parseInt(box1.getText());
			int y = Integer.parseInt(box2.getText());



		});


		//		columnGroupTestButton = new JButton("Check Col 4-6 repeat for 2");
		//		columnGroupTestButton.addActionListener(e->{
		//			System.out.println(checkColumnGroup(2,1));
		//		});
		//		


		box1 = new JTextField();
		box2 = new JTextField();
		box3 = new JTextField();


		box1.setMinimumSize(new Dimension(100,100));
		box2.setMinimumSize(new Dimension(100,100));
		box1.setPreferredSize(new Dimension(30,30));
		box2.setPreferredSize(new Dimension(30,30));
		box3.setPreferredSize(new Dimension(30,30));

		solveButton = new JButton("Solve");
		solveButton.addActionListener(e->{
			if(solveSudoku(gameState)) {
				JOptionPane.showMessageDialog(this,"Solved");
				System.out.println("GameSolved = " + isSolved(gameState));
				JFrame solvedBoard = new Board(gameState);
				clearBoard();
				printGrid(gameState);
			}
			else {
				JOptionPane.showMessageDialog(this,"Not Solvable");
				createGameState();
			}
		});



		//		buttonPanel.add(rowTotalTestButton);
		//		buttonPanel.add(columnTotalTestButton);

		buttonPanel.add(box1);
		buttonPanel.add(box2);
		buttonPanel.add(box3);
		buttonPanel.add(boxTotalTestButton);
		buttonPanel.add(solveButton);

		//buttonPanel.add(columnGroupTestButton);
		//		buttonPanel.add(rowGroupTestButton);
	}


	private boolean usedInRow(int[][] grid, int row,int value) {
		for(int i=0;i<9;i++) {
			if(grid[row][i] == value) return true;
		}
		return false;
	}

	private boolean usedInCol(int[][] grid, int col,int value) {
		for(int i=0;i<9;i++) {
			if(grid[i][col] == value) return true;
		}
		return false;
	}



	private boolean usedInBox2(int[][] grid, int row,int col,int value) {
		int x = 3 * (row/3);
		int y = 3 * (col/3);

		for(int i=x;i<x+3;i++) {
			for(int j=y;j<y+3;j++) {
				if(grid[i][j]== value) return true;
			}

		}

		return false;
	}

	private int getBoxSum(int grid[][], int row, int col) {
		int x = 3 * (row/3);
		int y = 3 * (col/3);
		int sum=0;
		for(int i=x;i<x+3;i++) {
			for(int j=y;j<y+3;j++) {
				sum += grid[i][j];
			}

		}

		return sum;
	}

	private int getRowSum(int grid[][],int row) {
		int sum = 0;
		for(int i=0;i<9;i++) {
			sum += grid[row][i];
		}
		return sum;
	}

	private int getColumnSum(int grid[][],int col) {
		int sum = 0;
		for(int i=0;i<9;i++) {
			sum +=grid[i][col];
		}
		return sum;
	}


	private boolean isSafe(int[][] grid,int row, int col, int num) {
		return !usedInRow(grid, row, num) &&
				!usedInCol(grid, col, num) &&
				!usedInBox2(grid, row,col,num);
	}

	private boolean solveSudoku(int grid[][]) {

		int row = 0, col = 0;

		if(!validateBoard(grid)) {
			return false;
		}
		
		if(!containsUnassignedLocation(grid)) {
			return true;

		}
		Coordinate loc = getUnassignedLocation(grid);
		row = loc.getX();
		col = loc.getY();

		System.out.println(row + ", " + col);

		// Domain = {1,2,3,4,5,6,7,8,9}
		for(int num=1;num<=9;num++) {

			printGrid(grid);

			if(isSafe(grid,row,col,num))
			{

				grid[row][col] = num;

				if(solveSudoku(grid)) {

					printGrid(grid);

					return true;
				}

				grid[row][col] = 0;
			}
		}

		return false;
	}

	private void printGrid(int grid[][]) {
		for(int i=0;i<9;i++) {
			for(int j=0;j<9;j++) {
				System.out.print(grid[i][j] + " ");

			}
			System.out.println();;
		}

		System.out.println();;
	}
	private Coordinate getUnassignedLocation(int grid[][]) {
		Coordinate loc = new Coordinate(0,0);
		for(int i=0;i<9;i++) {
			for(int j=0;j<9;j++) {
				if(grid[i][j]<=0 || grid[i][j]>9) {
					loc.setX(i);
					loc.setY(j);
					return loc;
				}
			}
		}

		return null;
	}

	private boolean containsUnassignedLocation(int[][] grid) {
		for(int i=0;i<9;i++) {
			for(int j=0;j<9;j++) {
				if(grid[i][j]<=0 || grid[i][j]>9) {
					return true;
				}
			}
		}
		return false;
	}

	private void createGameState() {
		gameState = new int[9][9];
		//		for(int i=0;i<9;i++) {
		//			for(int j=0;j<9;j++) {
		//				if(Math.random()>1) {
		//					gameState[i][j] = (int)(Math.random() * 10);
		//				}
		//			}
		//		}
		//		
		//		gameState = new int[][] {
		//			{5,3,0,0,7,0,0,0,0},
		//			{6,0,0,1,9,5,0,0,0},
		//			{0,9,8,0,0,0,0,6,0},
		//			{8,0,0,0,6,0,0,0,3},
		//			{4,0,0,8,0,3,0,0,1},
		//			{7,0,0,0,2,0,0,0,6},
		//			{0,6,0,0,0,0,2,8,0},
		//			{2,8,7,4,1,9,6,3,5},
		//			{3,4,5,2,8,6,1,7,9}
		//		};
		//		
		//		gameState = new int[][] {
		//			{5,3,4,6,7,8,9,1,2},
		//			{6,7,2,1,9,5,3,4,8},
		//			{1,9,8,3,4,2,5,6,7},
		//			{8,5,9,7,6,1,4,2,3},
		//			{4,2,6,8,5,3,7,9,1},
		//			{7,1,3,9,2,4,8,5,6},
		//			{9,6,1,5,3,7,2,8,4},
		//			{2,8,7,4,1,9,6,3,5},
		//			{3,4,5,2,8,6,1,7,0}
		//		};
		//		int tempGrid[][] = new int[9][9];
		//		
		//		do {
		//		for(int i=0;i<9;i++) {
		//			for(int j=0;j<9;j++) {
		//				tempGrid[i][j] = gameState[i][j];
		//			}
		//		}
		//		
		//		generateRandomGameState(tempGrid);
		//		
		//		}while(!validateBoard(tempGrid));
		//		
		//		gameState = tempGrid;
		//		
	}

	private void generateRandomGameState(int grid[][]) {

		for(int i = 0;i<9;i++) {
			for(int j=0;j<9;j++) {
				if(Math.random() > 0.6) grid[i][j] = (int)(Math.random() * 9);
			}
		}

	}

	private boolean isSolved(int grid[][]) {
		for(int i=0;i<3;i++){
			for(int j=0;j<3;j++) {
				if(getBoxSum(grid,0,0)!=45) return false;
			}
		}
		for(int i=0;i<9;i++) {
			if(getRowSum(grid,i)!=45) return false;
		}
		for(int i=0;i<9;i++) {
			if(getColumnSum(grid,i)!=45) return false;
		}
		return true;
	}

	/*
	 * Check if 3 rows only have 3 of the same value. 
	 */

	public String toString() {
		String str="";
		for(int i=0;i<9;i++) {
			for(int j=0;j<9;j++) {
				if(gameState[i][j]!='\0') {
					str += gameState[i][j] + " "; 
				}
				else {
					str += "_ " ;
				}
			}
			str += "\n";
		}
		return str;
	}

	private boolean validateBoard(int grid[][]) {
		for(int i = 0;i<9;i++) {
			for(int j = 0;j<9;j++) {
				if(grid[i][j]!= 0) {
					if(existsInRow(grid,i,j,grid[i][j]) 
							|| existsInColumn(grid,i,j,grid[i][j])
							|| existsInBox(grid,i,j,grid[i][j])) return  false;
				}
			}
		}
		return true;
	}

	private boolean existsInRow(int[][] grid,int row,int col,int value) {
		for(int j=0;j<9;j++) {
			if(col != j) {
				if(grid[row][j] == value) return true; 
			}
		}
		return false;
	}

	private boolean existsInColumn(int[][] grid,int row,int col,int value) {
		for(int i=0;i<9;i++) {
			if(row != i) {
				if(grid[i][col] == value) return true; 
			}
		}
		return false;
	}

	private boolean existsInBox(int[][] grid,int row,int col, int value) {
		int x = 3 * (row/3);
		int y = 3 * (col/3);

		for(int i=x;i<x+3;i++) {
			for(int j=y;j<y+3;j++) {
				if(i!=row || col!=j) {
					if(grid[i][j]== value) return true;
				}
			}

		}


		return false;
	}

	public static void main(String[] args) {
		Board board = new Board();
		System.out.println(board);
	}

	public void paint(Graphics g) {
		super.paint(g);

		g.translate(gridPanel.getX() - 3,gridPanel.getY()+22);

		for(int j=0;j<9;j++) {
			for(int i=0;i<9;i++) {
				g.drawRect((52*i)+2, (47*j), 52, 47);
			}
		}

		for(int i=0;i<3;i++) {
			for(int j=0;j<3;j++) {
				Graphics2D g2 = (Graphics2D)g;
				double thickness = 3;
				Stroke oldStroke = g2.getStroke();
				g2.setStroke(new BasicStroke((float) thickness));
				g2.drawRect((52*i*3)+2, (47*j*3), 52*3, 47*3);
				g2.setStroke(oldStroke);

			}
		}
	}








}
