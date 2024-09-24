import javax.swing.*;




public class App {
    public static void main(String[] args) throws Exception {
        int width=360;
        int height=640;
        JFrame frame=new JFrame("Flappy bird");
        //frame.setVisible(true);
        frame.setSize(width,height);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        bird Bird=new bird();
        frame.add(Bird);
        frame.pack();
        Bird.requestFocus();
        frame.setVisible(true);
    }
}
