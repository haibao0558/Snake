import java.awt.*;
import java.awt.event.*;
import javax.swing.JOptionPane;


/**
 * 这个类代表贪吃蛇的活动场所
 * @author bjsxt
 * @version 1.0
 */
public class Yard extends Frame implements ActionListener{
	PaintThread paintThread = new PaintThread();
	private boolean gameOver = false; //游戏是否结束
	
	
	/**
	 * 行数
	 */
	    public static boolean printable = true;
        private static final long serialVersionUID = 1L;
        MenuBar jmb = null;
	    Menu jm1 = null, jm2 = null, jm3 = null;
	    MenuItem jmi1 = null, jmi2 = null, jmi3 = null, jmi4 = null, jmi5 = null;

	public static final int ROWS = 30;
	public static final int COLS = 30;
	public static final int BLOCK_SIZE = 25;
      
	private Font fontGameOver = new Font("楷体", Font.BOLD, 50);
    private Font fontscore = new Font("楷体", Font.BOLD, 20);
	
	private int score = 0;
	
	Snake s = new Snake(this);
	Egg e = new Egg();
	
	Image offScreenImage = null;
	
	public void launch() {
    printable = false;// 创建菜单及菜单选项
		jmb = new MenuBar();
		jm1 = new Menu("游戏");
		jm2 = new Menu("控制");
		jm3 = new Menu("帮助");
		jm1.setFont(new Font("TimesRoman", Font.BOLD, 15));// 设置菜单显示的字体
		jm2.setFont(new Font("TimesRoman", Font.BOLD, 15));// 设置菜单显示的字体
		jm3.setFont(new Font("TimesRoman", Font.BOLD, 15));// 设置菜单显示的字体

		jmi1 = new MenuItem("开始新游戏");
		jmi2 = new MenuItem("退出");
		jmi3 = new MenuItem("暂停");
		jmi4 = new MenuItem("继续");
		jmi5 = new MenuItem("关于我们");
		jmi1.setFont(new Font("TimesRoman", Font.BOLD, 15));
		jmi2.setFont(new Font("TimesRoman", Font.BOLD, 15));
		jmi3.setFont(new Font("TimesRoman", Font.BOLD, 15));
		jmi4.setFont(new Font("TimesRoman", Font.BOLD, 15));
		jmi5.setFont(new Font("TimesRoman", Font.BOLD, 15));
		
		jm1.add(jmi1);
		jm1.add(jmi2);
		jm2.add(jmi3);
		jm2.add(jmi4);
		jm3.add(jmi5);

		jmb.add(jm1);
		jmb.add(jm2);

		jmb.add(jm3);

		jmi1.addActionListener(this);
		jmi1.setActionCommand("NewGame");
		jmi2.addActionListener(this);
		jmi2.setActionCommand("Exit");
		jmi3.addActionListener(this);
		jmi3.setActionCommand("Stop");
		jmi4.addActionListener(this);
		jmi4.setActionCommand("Continue");
		jmi5.addActionListener(this);
		jmi5.setActionCommand("help");

		this.setMenuBar(jmb);// 菜单Bar放到JFrame上
		this.setVisible(true);

		this.setLocation(300, 65);
		this.setSize(COLS * BLOCK_SIZE, ROWS * BLOCK_SIZE);
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
			
		});
		this.setVisible(true);
		this.addKeyListener(new KeyMonitor());
		
		new Thread(paintThread).start();
	}
	
	public static void main(String[] args) {
		new Yard().launch();
	}
	public void stop() {
		gameOver = true;
	
	}
	
	@Override
	public void paint(Graphics g) {
		Color c = g.getColor();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, COLS * BLOCK_SIZE, ROWS * BLOCK_SIZE);
                this.setTitle("贪吃蛇——(方向键控制)   姓名：吕永贞      班级：12计科一班       学号：20121030146");
                g.setColor(Color.DARK_GRAY);//画出横线
		for(int i=1; i<ROWS; i++) {
			g.drawLine(0, BLOCK_SIZE * i, COLS * BLOCK_SIZE, BLOCK_SIZE * i);
		}
		for(int i=1; i<COLS; i++) {
			g.drawLine(BLOCK_SIZE * i, 0, BLOCK_SIZE * i, BLOCK_SIZE * ROWS);
		}
		
		g.setColor(Color.RED);
                g.setFont(fontscore);
		g.drawString("得分:" + score, 30,100);
		
		if(gameOver) {
			g.setFont(fontGameOver);
			g.drawString("游戏结束", 280, 350);
			
			paintThread.pause();
		}
		
		g.setColor(c);
		
		s.eat(e);
		e.draw(g);
		s.draw(g);
		
		
	}
	
	@Override
	public void update(Graphics g) {
		if(offScreenImage == null) {
			offScreenImage = this.createImage(COLS * BLOCK_SIZE, ROWS * BLOCK_SIZE);
		}
		Graphics gOff = offScreenImage.getGraphics();
		paint(gOff);
		g.drawImage(offScreenImage, 0, 0,  null);
	}
	
	private class PaintThread implements Runnable {
		private boolean running = true;
		private boolean pause = false;
		public void run() {
			while(running) {
				if(pause) continue; 
				else repaint();
				
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		public void pause() {
			this.pause = true;
		}
		
		public void reStart() {
			this.pause = false;
			s = new Snake(Yard.this);
			gameOver = false;
		}
		
		@SuppressWarnings("unused")
		public void gameOver() {
			running = false;
		}
		
	}
	
	private class KeyMonitor extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			if(key == KeyEvent.VK_F2) {
				paintThread.reStart();
			}
			s.keyPressed(e);
		}
		
	}
	
	/**
	 * 拿到所得的分数
	 * @return 分数
	 */
	
	public int getScore() {
		return score;
	}
	
	/**
	 * 设置所得的分数
	 * @param score 分数
	 */
	
	public void setScore(int score) {
		this.score = score;
	}
public void actionPerformed(ActionEvent e) {

		if (e.getActionCommand().equals("NewGame")) {
			printable = false;
			Object[] options = { "确定", "取消" };
			int response = JOptionPane.showOptionDialog(this, "您确认要开始新游戏！", "",
					JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null,
					options, options[0]);
			if (response == 0) {

				printable = true;
				this.dispose();
				new Yard().launch();
			} else {
				printable = true;
				new Thread(new PaintThread()).start(); // 线程启动
			}

		} else if (e.getActionCommand().endsWith("Stop")) {
			printable = false;
			 try {
			 Thread.sleep(200);
			
			 } catch (InterruptedException e1) {
			 
			e1.printStackTrace();
		 }
		} else if (e.getActionCommand().equals("Continue")) {

			if (!printable) {
				printable = true;
				new Thread(new PaintThread()).start(); // 线程启动
			}
		} else if (e.getActionCommand().equals("Exit")) {
			printable = false;
			Object[] options = { "确定", "取消" };
			int response = JOptionPane.showOptionDialog(this, "您确认要退出吗", "",
					JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null,
					options, options[0]);
			if (response == 0) {
				System.out.println("退出");
				System.exit(0);
			} else {
				printable = true;
				new Thread(new PaintThread()).start(); // 线程启动

			}

		} else if (e.getActionCommand().equals("help")) {
			printable = false;
			JOptionPane.showMessageDialog(null, "由于初学者许多功能都不完善，请大家见谅",
					"提示！", JOptionPane.INFORMATION_MESSAGE);
			       this.setVisible(true);
			       printable = true;
			new Thread(new PaintThread()).start(); // 线程启动
}
}
}