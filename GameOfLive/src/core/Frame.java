package core;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import entity.EntityManager;

@SuppressWarnings("serial")
public class Frame extends JPanel implements Runnable, KeyListener, MouseListener, ComponentListener {

	private JFrame frame = null;
	private Thread thread = null;
	
	private String title;
	private final String threadTitle = "GameOfLive";

	private int frameWidth;
	private int frameHeight;
	
	private boolean paused = false;

	private long lastTime;
	private long thisTime;
	private double timeSinceLastFrame;

	private long lastUpdate;
	private long timeSinceLastUpdate;
	private int updateTime;
	
	private EntityManager entityManager = null;
	private int entityCountX;
	private int entityCountY;

	public Frame(int width, int height, String title, int tileCountX, int tileCountY, int updateTime) {

		setFrameWidth(width);
		setFrameHeight(height);
		setTitle(title);
		
		setTileCountX(tileCountX);
		setTileCountY(tileCountY);
		
		setUpdateTime(updateTime);
		
		this.setPreferredSize(new Dimension(getFrameWidth(), getFrameHeight()));

		frame = new JFrame();
		frame.setTitle(getTitle());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(this, BorderLayout.CENTER);
		frame.pack();
		frame.addKeyListener(this);
		this.addMouseListener(this);
		frame.addComponentListener(this);
		frame.setResizable(true);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		initObjects();

		startGame();
	}

	private void initObjects() {
		setEntityManager(new EntityManager(getFrameWidth(), getFrameHeight(),
				getTileCountX(), getTileCountX()));
	}

	private void updateObjects() {
		getEntityManager().update();
	}

	private void drawObjects(Graphics g) {
		getEntityManager().drawEntitys(g);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawObjects(g);
	}

	private void startGame() {
		setThread(new Thread(this, getThreadTitle()));
		getThread().start();
	}

	@Override
	public void run() {

		setPaused(true);
		lastTime = System.currentTimeMillis();
		lastUpdate = System.currentTimeMillis();

		while (true) {

			thisTime = System.currentTimeMillis();
			timeSinceLastFrame = (thisTime - lastTime);

			if (timeSinceLastFrame >= 16) {
				repaint();
				lastTime = thisTime;
			}

			thisTime = System.currentTimeMillis();
			timeSinceLastUpdate = thisTime - lastUpdate;
			
			if (!isPaused()) {
				if (timeSinceLastUpdate >= updateTime) {
					updateObjects();
					lastUpdate = System.currentTimeMillis();
				}
			} else {
					getEntityManager().updateMarkedEntities();
			}
		}
	}

	// ---> Getters and Setters <--- 
	
	private boolean isPaused() {
		return paused;
	}

	private void setPaused(boolean paused) {
		this.paused = paused;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	private void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private void setFrameWidth(int frameWidth) {
		this.frameWidth = frameWidth;
	}

	public int getFrameWidth() {
		return frameWidth;
	}

	private void setFrameHeight(int frameHeight) {
		this.frameHeight = frameHeight;
	}

	public int getFrameHeight() {
		return frameHeight;
	}

	public int getTileCountX() {
		return entityCountX;
	}

	private void setTileCountX(int tileCountX) {
		this.entityCountX = tileCountX;
	}

	public int getTileCountY() {
		return entityCountY;
	}

	private void setTileCountY(int tileCountY) {
		this.entityCountY = tileCountY;
	}

	public String getTitle() {
		return title;
	}

	private void setTitle(String title) {
		this.title = title;
	}

	public int getUpdateTime() {
		return updateTime;
	}

	private void setUpdateTime(int updateTime) {
		this.updateTime = updateTime;
	}
	
	public Thread getThread() {
		return thread;
	}

	public void setThread(Thread thread) {
		this.thread = thread;
	}
	
	public String getThreadTitle() {
		return threadTitle;
	}
	
	// ---> Listeners <---
	
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			setPaused(!isPaused());
		}
		if (e.getKeyCode() == KeyEvent.VK_DELETE) {
			getEntityManager().reset();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {

	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (isPaused()) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				getEntityManager().selectEntity(e.getPoint());
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void componentHidden(ComponentEvent e) {
	}

	@Override
	public void componentMoved(ComponentEvent e) {
	}

	@Override
	public void componentResized(ComponentEvent e) {
		getEntityManager().resize(getWidth(), getHeight());
	}

	@Override
	public void componentShown(ComponentEvent e) {
	}

}
