import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
public class bird extends JPanel implements ActionListener, KeyListener {
    int width = 360;
    int height = 640;
    Image bottompipe;
    Image toppipe;
    Image birdimg;
    Image background;
    int birdx = width / 8;
    int birdy = height / 2;
    int birdwidth = 34;
    class flappy {
        int x = birdx;
        int y = birdy;
        int bwidth = birdwidth;
        int bheight = birdwidth;
        public Image img;
        flappy(Image img) {
            this.img = img;
        }
    }
    // Pipe
    int pipex = width;
    int pipey = 0;
    int pipewidth = 64;
    int pipeheight = 512;
    class pipe {
        int x = pipex;
        int y = pipey;
        int width = pipewidth;
        int height = pipeheight;
        Image img;
        boolean passed;
        pipe(Image img, int x, int y) {
            this.img = img;
            this.x = x;
            this.y = y;
        }
    }
    // Game logic
    flappy Flappy;
    int velocityX = -4;  // Move pipe to left speed
    int velocityY = 0; // Move bird up and down
    int gravity = 1;
    ArrayList<pipe> pipes;
    Timer gameloop;
    Timer placepipeTimer;
    boolean gameover=false;
    double score=0;
    bird() {
        setPreferredSize(new Dimension(width, height));
        setFocusable(true);
        addKeyListener(this);
        // Load images
        background = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
        toppipe = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
        bottompipe = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();
        birdimg = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
        // Bird
        Flappy = new flappy(birdimg);
        pipes = new ArrayList<>();
        // Place pipe timer
        placepipeTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placepipe();
            }
        });
        placepipeTimer.start(); // Start placing pipes
        gameloop = new Timer(1000 / 60, this);
        gameloop.start();
    }
    public void placepipe() {
        // Top pipe
        int gap = 150;
        int pipeHeight = (int) (Math.random() * height / 2) + 50;
        pipe topPipe = new pipe(toppipe, width, pipeHeight - 512); // Position top pipe
        pipe bottomPipe = new pipe(bottompipe, width, pipeHeight + gap); // Position bottom pipe
        pipes.add(topPipe);
        pipes.add(bottomPipe);
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g) {
        g.drawImage(background, 0, 0, width, height, null);
        g.drawImage(Flappy.img, Flappy.x, Flappy.y, Flappy.bwidth, Flappy.bheight, null);
        // Draw pipes
        for (int i = 0; i < pipes.size(); i++) {
            pipe p = pipes.get(i);
            g.drawImage(p.img, p.x, p.y, p.width, p.height, null);
        }
        //score
        g.setColor(Color.white);
        g.setFont(new Font("Times new roman",Font.PLAIN,32));
        if(gameover){
            g.drawString("GAME-OVER  "+String.valueOf((int)score),10,35);
        }
        else{
            g.drawString(String.valueOf((int)score),10,35 );
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
        move();
        if(gameover){
            placepipeTimer.stop();
            gameloop.stop();
        }
    }
    public boolean collision(flappy a, pipe b){
        return a.x + a.bwidth > b.x && 
        a.x < b.x + b.width &&
        a.y < b.y + b.height && 
        a.y + a.bheight > b.y;
    }
    public void move() {
        velocityY += gravity;
        Flappy.y += velocityY;
        Flappy.y = Math.max(Flappy.y, 0); // Prevent bird from going off-screen (above)
        // Move pipes and remove off-screen ones
        for (int i = 0; i < pipes.size(); i++) {
            pipe p = pipes.get(i);
            p.x += velocityX;  // Move pipe to the left
            if(!p.passed &&Flappy.x>p.x+p.width){
                p.passed = true;
                score+=0.5;
            }
            if(collision(Flappy,p)){
                gameover = true;
            } 
            // Remove pipes that are off-screen
            if (p.x + p.width < 0) {
                pipes.remove(i);
                i--; // Adjust index after removing
            }
        }
        if(Flappy.y>height){
            gameover=true;
        }
    }
    public static void main(String[] args) {
        JFrame frame = new JFrame("Flappy Bird");
        bird gamePanel = new bird();
        frame.add(gamePanel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            velocityY = -9; // Make the bird jump
            if(gameover){
                //restart
                velocityY=0;
                pipes.clear();
                score=0;
                gameover=false;
                Flappy.y=birdy;
                gameloop.start();
                placepipeTimer.start();
            }
        }
    }
    @Override
    public void keyTyped(KeyEvent e) {
    }
    @Override
    public void keyReleased(KeyEvent e) {
    }
}