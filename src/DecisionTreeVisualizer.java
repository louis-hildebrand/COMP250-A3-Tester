import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

public class DecisionTreeVisualizer
{
	private static final int HORIZ_SKIP = 10;
	private static final int VERT_SKIP = 30;
	private static final int NODE_WIDTH = 60;
	private static final int NODE_HEIGHT = 20;
	private static final Font NORMAL_FONT = new Font("Dialog", Font.PLAIN, 9);
	private static final Font HEADER_FONT = new Font("Dialog", Font.BOLD, 14);
	private static final Color LEAF_COLOR0 = Color.RED;
	private static final Color LEAF_COLOR1 = Color.GREEN;
	private static final Color LEAF_COLOR_UNDEFINED = Color.ORANGE;
	private static final int MAX_HEIGHT = 4;

	private JFrame window;

	public DecisionTreeVisualizer(DecisionTree dt, String header)
	{
		javax.swing.SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				createAndShowGUI(dt, header);
			}
		});
	}

	private void createAndShowGUI(DecisionTree dt, String header)
	{
		window = new JFrame("DecisionTree Visualizer");
		window.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		window.setSize(1300, 600);
		window.setLocationRelativeTo(null);

		JPanel contentPane = new JPanel();
		contentPane.setLayout(null);
		JScrollPane scrollPane = new JScrollPane(contentPane);
		window.add(scrollPane, BorderLayout.CENTER);

		// Draw tree
		if (dt == null)
		{
			JLabel nullLabel = new JLabel("[null]");
			nullLabel.setFont(NORMAL_FONT);
			nullLabel.setBounds(400, 250, (int)nullLabel.getPreferredSize().getWidth(),
					(int)nullLabel.getPreferredSize().getHeight());
			contentPane.add(nullLabel);
		}
		else
		{
			int h = getHeight(dt.rootDTNode);
			int maxBottomNodes = (int)Math.pow(2, h);
			int imageWidth = maxBottomNodes * (NODE_WIDTH + HORIZ_SKIP) + HORIZ_SKIP;
			int imageHeight = (1 + h) * (NODE_HEIGHT + VERT_SKIP) + VERT_SKIP;
			contentPane.setSize(imageWidth, imageHeight);
			scrollPane.setSize(imageWidth, imageHeight);
			drawTree(dt.rootDTNode, new Rectangle(0, 30, imageWidth, imageHeight), contentPane, 0);
		}

		// Header
		JLabel headerLabel = new JLabel(header);
		headerLabel.setFont(HEADER_FONT);
		headerLabel.setBounds(0, 0, (int)headerLabel.getPreferredSize().getWidth(),
				(int)headerLabel.getPreferredSize().getHeight());
		contentPane.add(headerLabel);

		window.setVisible(true);
	}

	private int getHeight(DecisionTree.DTNode root)
	{
		if (root.left == null && root.right == null)
			return 0;

		int height = 1 + Math.max(getHeight(root.left), getHeight(root.right));
		return Math.min(height, MAX_HEIGHT);
	}

	private void drawTree(DecisionTree.DTNode root, Rectangle canvas, JPanel contentPane, int depth)
	{
		if (depth > MAX_HEIGHT)
			return;
		if (root == null)
			return;

		JLabel rootLabel = makeLabel(root);
		contentPane.add(rootLabel);
		double centerX = canvas.getCenterX();
		rootLabel.setBounds((int)(centerX - NODE_WIDTH / 2), (int)canvas.getMinY(), NODE_WIDTH,
				NODE_HEIGHT);

		// TODO Children
		if (root.left != null)
		{
			int newX = (int)canvas.getX();
			int newY = (int)(canvas.getMinY() + NODE_HEIGHT + VERT_SKIP);
			int newWidth = (int)(canvas.getWidth() / 2);
			int newHeight = (int)(canvas.getHeight() - NODE_HEIGHT - VERT_SKIP);
			Rectangle leftCanvas = new Rectangle(newX, newY, newWidth, newHeight);
			drawTree(root.left, leftCanvas, contentPane, depth + 1);

			// Connect parent to child
			int parentX = (int)centerX;
			int parentY = (int)(canvas.getY() + NODE_HEIGHT);
			int leftChildX = (int)(leftCanvas.getCenterX());
			int leftChildY = parentY + VERT_SKIP;
			if (depth < MAX_HEIGHT)
			{
				Line line = new Line(parentX, parentY, leftChildX, leftChildY);
				contentPane.add(line);
			}
		}
		if (root.right != null)
		{
			int newX = (int)(canvas.getX() + canvas.getWidth() / 2);
			int newY = (int)(canvas.getMinY() + NODE_HEIGHT + VERT_SKIP);
			int newWidth = (int)(canvas.getWidth() / 2);
			int newHeight = (int)(canvas.getHeight() - NODE_HEIGHT - VERT_SKIP);
			Rectangle rightCanvas = new Rectangle(newX, newY, newWidth, newHeight);
			drawTree(root.right, rightCanvas, contentPane, depth + 1);

			// Connect parent to child
			int parentX = (int)centerX;
			int parentY = (int)(canvas.getY() + NODE_HEIGHT);
			int rightChildX = (int)(rightCanvas.getCenterX());
			int rightChildY = parentY + VERT_SKIP;
			if (depth < MAX_HEIGHT)
			{
				Line line = new Line(parentX, parentY, rightChildX, rightChildY);
				contentPane.add(line);
			}
		}
	}

	private JLabel makeLabel(DecisionTree.DTNode node)
	{
		JLabel nodeLabel = new JLabel();
		nodeLabel.setSize(NODE_WIDTH, NODE_HEIGHT);
		nodeLabel.setFont(NORMAL_FONT);
		nodeLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		nodeLabel.setHorizontalAlignment(SwingConstants.CENTER);

		if (node.leaf)
		{
			nodeLabel.setText(Integer.toString(node.label));
			nodeLabel.setOpaque(true);
			if (node.label == 0)
				nodeLabel.setBackground(LEAF_COLOR0);
			else if (node.label == 1)
				nodeLabel.setBackground(LEAF_COLOR1);
			else
				nodeLabel.setBackground(LEAF_COLOR_UNDEFINED);
		}
		else
		{
			nodeLabel.setText(String.format("x%d < %.2f", node.attribute, node.threshold));
		}

		return nodeLabel;
	}

	private class Line extends JPanel
	{
		private static final long serialVersionUID = -5451293947087728291L;

		int x0;
		int y0;
		int x1;
		int y1;

		private Line(int x0, int y0, int x1, int y1)
		{
			this.x0 = x0;
			this.y0 = y0;
			this.x1 = x1;
			this.y1 = y1;

			int x = Math.min(x0, x1);
			int y = Math.min(y0, y1);
			int width = Math.abs(x1 - x0);
			int height = Math.abs(y1 - y0);

			this.setBounds(x, y, width, height);
		}

		@Override
		protected void paintComponent(Graphics g)
		{
			super.paintComponent(g);

			int x = Math.min(x0, x1);
			int y = Math.min(y0, y1);

			g.drawLine(x0 - x, y0 - y, x1 - x, y1 - y);
		}
	}
}
