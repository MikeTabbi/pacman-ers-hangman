import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

 class ButtonExample {
    public static void main(String[] args) {

        JFrame f=new JFrame("Hangman Menu");
        JButton b=new JButton("Start Game");
        b.setForeground(Color.BLACK);
        b.setBounds(700,400,95,30);

        JLabel hangmanImage = CustomTools.loadImage(CommonConstants.PACMAN_IMAGE);
        hangmanImage.setBounds(0, 0, hangmanImage.getPreferredSize().width, hangmanImage.getPreferredSize().height);


        b.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                new Hangman().setVisible(true);
            }
        });
        f.add(b);
        f.setSize(1450,840);
        f.setLayout(null);
        f.setVisible(true);
        f.getContentPane().add(hangmanImage);

    }
}

