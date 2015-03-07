import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class OLAGraphDrawer extends JComponent {
	private int[] layout;
	private int[][] connectionMatrix;
	private int rows;
	private int cols;
	private int width;
	private int height; 
	private double spacing;
	private int offset = 50;
	
	public OLAGraphDrawer(int[] layout, int[][] connectionMatrix, int rows, int cols) {
		this.layout = layout;
		this.connectionMatrix = connectionMatrix;
		this.rows = rows;
		this.cols = cols;
		spacing = (double)500 / (Math.max(rows, cols) - 1);
		width = (int)( (cols - 1) * spacing) + offset*2;
		height = (int)( (rows- 1) * spacing) + offset*2;
		
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setPreferredSize(new Dimension(width, height));
		frame.getContentPane().add(this, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
	}
	
	public void setLayout(int[] layout) {
		this.layout = layout;
		this.update(this.getGraphics());
	}
	
	protected void paintComponent(Graphics g) {
		for(int row = 0; row < rows; row++) {
			for(int col = 0; col < cols; col++) {
				g.fillRect((int)(col*spacing) + offset, (int)(row*spacing) + offset, 5, 5);
			}
		}
		
		for(int pos1 = 0; pos1 < layout.length; pos1++) {
			for(int pos2 = pos1; pos2 < layout.length; pos2++) {
				g.setColor(new Color((int)(255*Math.random()), (int)(255*Math.random()),(int)(255*Math.random()), 100));
				int vertex1 = layout[pos1];
				int vertex2 = layout[pos2];
				int numConnections = connectionMatrix[vertex1][vertex2];
				int[] directionVector = OLAGraph.computeDirectionVector(pos1, pos2, cols);
				int[] pos1XY = OLAGraph.indexToCoordinates(pos1, cols);
				if(numConnections != 0) {
					//System.out.println("Drawing " + pos1XY[0] + "," + pos1XY[1] + "," + directionVector[0] + "," + directionVector[1]);
					g.fillRect((int)(pos1XY[0]*spacing )+ offset, (int)(pos1XY[1]*spacing) + offset, (int)(directionVector[0] * spacing), 5);
					g.fillRect((int)((pos1XY[0] + directionVector[0])*spacing )+ offset , (int)(pos1XY[1]*spacing) + offset, 5, (int)(directionVector[1] * spacing));
				}
			}
		}
	} 
}
