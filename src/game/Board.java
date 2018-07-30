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
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
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
	
	private int[][] gameState;
	private HashMap<JFormattedTextField,Coordinate> dict;
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


		initialise();

		this.add(gamePanel,BorderLayout.CENTER);

		this.add(buttonPanel, BorderLayout.SOUTH);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(true);

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
								}
							}

							@Override
							public void insertUpdate(DocumentEvent arg0) {
								if(((Integer.parseInt(field.getText())) >= 0 && Integer.parseInt(field.getText()) <= 9 )) {
									//System.out.println(Integer.parseInt(field.getText()));
									int x = dict.get(field).getX();
									int y = dict.get(field).getY();
									gameState[x][y] = Integer.parseInt(field.getText());
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

	private void initialise() {

		dict = new HashMap<JFormattedTextField, Coordinate>();

		createGameState();

		createGamePanel();

		createButtonPanel();

	}

	private void createButtonPanel() {
		buttonPanel = new JPanel();
		buttonPanel.setBackground(new Color(240,240,255));
		buttonPanel.setLayout(new FlowLayout());

		rowTotalTestButton = new JButton("Check Row 0 sum") ;
		rowTotalTestButton.addActionListener(e->{
			getRowTotal(0);
		});

		columnTotalTestButton = new JButton("Check Column 0 sum") ;
		columnTotalTestButton.addActionListener(e->{
			getColumnTotal(0);
		});

		boxTotalTestButton = new JButton("Check Box 0 sum") ;
		boxTotalTestButton.addActionListener(e->{
			//getBoxTotal(0);
			for(int i=0;i<9;i++) {
				for(int j=0;j<9;j++) {
					System.out.print(getBoxNo(i,j));
				}
				System.out.println();
			}
		});

		//		columnGroupTestButton = new JButton("Check Col 4-6 repeat for 2");
		//		columnGroupTestButton.addActionListener(e->{
		//			System.out.println(checkColumnGroup(2,1));
		//		});
		//		
		rowGroupTestButton= new JButton("Check Row 0-2 repeat for 1");
		rowGroupTestButton.addActionListener(e->{
			System.out.println(checkRowGroup(2,1));
		});
		
		
		solveButton = new JButton("Solve");
		solveButton.addActionListener(e->{
			solveSudoku(gameState);
		});
		
		

		//		buttonPanel.add(rowTotalTestButton);
		//		buttonPanel.add(columnTotalTestButton);
		buttonPanel.add(boxTotalTestButton);
		buttonPanel.add(solveButton);
		//buttonPanel.add(columnGroupTestButton);
		//		buttonPanel.add(rowGroupTestButton);
	}

	private int getBoxNo(int row,int col) {
		
		if(row<3) return (row/3 + (col/3));
		if(row<6) return (row/3 + (col/3)+3*1 - 1);
		if(row<9) return (row/3 + (col/3)+3*2 - 1);
		
		return 0;
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

	

	private boolean usedInBox(int[][] grid, int box,int value) {
//		int x = (box % 3) * 3;
//		int y = (box / 3) * 3;
//		
//		
//		
//		for(int i = x;i<x+3;i++) {
//			for(int j = y;j<y+3;j++) {
//				if(grid[i][j] == value) return true;
//			}
//		}
		
		return false;
	}
	
	private boolean usedInBox2(int[][] grid, int row,int col,int value) {
		int x = row/3;
		int y = col/3;
		
		for(int i=x*3;i<(x*3)+3;i++) {
			for(int j=y*3;j<(y*3);j++) {
				if(grid[i][j]== value) return true;
			}
		}
		
		return false;
	}


	private int getColumnTotal(int column) {
		int total=0;
		for(int i=0;i<9;i++) {
			total += gameState[i][column];
		}
		System.out.println(total);
		return total;
	}

	private int getRowTotal(int row) {
		int total=0;
		for(int i=0;i<9;i++) {
			total += gameState[row][i];
		}
		System.out.println(total);
		return total;
	}

	private int getBoxTotal(int box) {
		int x = (box % 3) * 3;
		int y = (box / 3) * 3;
		int sum = 0;
		for(int i = x;i<x+3;i++) {
			for(int j = y;j<y+3;j++) {
				sum += gameState[i][j];
			}
		}
		System.out.println("Sum = " + sum);
		return sum;
	}
	
	

	private boolean checkColumnGroup(int columnGroup,int value) {
		int total0 = 0;
		int total1 = 0;
		int total2 = 0;
		for(int i = 0;i<9;i++) {
			int temp = columnGroup* 3;
			for(int j=temp;j<temp+3;j++) {
				System.out.print(gameState[i][j] + " ");
				if(j % 3== 0) {
					if(gameState[i][j]==value) total0++;
				}
				else if(j % 3 == 1) {
					if(gameState[i][j]==value) total1++;
				}
				else if(j % 3 == 2) {
					if(gameState[i][j]==value) total2++;
				}
			}
			System.out.println();
		}
		System.out.println("Repeat = "+ (total0 + total1 + total2));
		System.out.println(total0);
		System.out.println(total1);
		System.out.println(total2);
		if (total0 == 1 & total1 == 1 & total2 == 1) return true;
		return false;
	}

	private boolean checkRowGroup(int rowGroup,int value) {
		int total0 = 0;
		int total1 = 0;
		int total2 = 0;
		for(int i = (rowGroup*3);i<(rowGroup*3) + 3;i++) {
			for(int j=0;j<9;j++) {
				System.out.print(gameState[i][j] + " ");
				if(i%3 == 0) {
					if(gameState[i][j]==value) total0++;
				}
				else if(i%3 == 1) {
					if(gameState[i][j]==value) total1++;
				}
				else if(i%3 == 2) {
					if(gameState[i][j]==value) total2++;
				}
			}
			System.out.println("");
		}
		System.out.println("Repeat = "+ (total0 + total1 + total2));
		System.out.println(total0);
		System.out.println(total1);
		System.out.println(total2);
		if (total0 == 1 & total1 == 1 & total2 == 1) return true;
		return false;
	}


	private boolean isSafe(int[][] grid,int row, int col, int num) {
		System.out.println("UsedInRow: " + usedInRow(grid, row, num));
		System.out.println("UsedInCol: " + usedInCol(grid, col, num));
		System.out.println("Box No: " + getBoxNo(row,col));
		System.out.println("UsedInBox: " + usedInBox(grid, getBoxNo(row,col), num));
		return !usedInRow(grid, row, num) &&
				!usedInCol(grid, col, num) &&
				!usedInBox2(grid, row,col,num);
	}

	private boolean solveSudoku(int grid[][]) {

		int row = 0, col = 0;

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
		
		gameState = new int[][] {
			{5,3,0,0,7,0,0,0,0},
			{6,0,0,1,9,5,0,0,0},
			{0,9,8,0,0,0,0,6,0},
			{8,0,0,0,6,0,0,0,3},
			{4,0,0,8,0,3,0,0,1},
			{7,0,0,0,2,0,0,0,6},
			{0,6,0,0,0,0,2,8,0},
			{2,8,7,4,1,9,6,3,5},
			{3,4,5,2,8,6,1,7,9}
		};
//		gameState[3][3] = 8;
//		gameState[1][2] = 2;
//		
	}
	
	private void generateRandomGameState() {
		
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

	@SuppressWarnings("unused")
	private void AC3(int[] domain){
		Queue<Integer> q = new LinkedList<Integer>();
		for(int i=0;i<9;i++	) {
			q.add(domain[i]);
		}
		while(!q.isEmpty()) {

		}
	}




}
