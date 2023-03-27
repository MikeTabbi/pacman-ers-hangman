import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;


public class Hangman extends JFrame implements ActionListener {
    // counts the number of incorrect guesses player has made
    private int incorrectGuesses;

    // store the challenge from the WordDB here
    private String[] wordChallenge;
    private final WordDB wordDB;
    private JLabel hangmanImage, categoryLabel, hiddenWordLabel, resultLabel, wordLabel, score;
    private JButton[] letterButtons;
    private JDialog resultDialog, setScore;
    private Font customFont;




    public Hangman() {
        super("Pacman-ers: Hangman");
        setSize(CommonConstants.FRAME_SIZE);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);
        getContentPane().setBackground(Color.BLACK);
        JLabel label1 = new JLabel("Test");

        // init vars
        wordDB = new WordDB();
        letterButtons = new JButton[26];
        wordChallenge = wordDB.loadChallenge();
        customFont = CustomTools.createFont(CommonConstants.FONT_PATH);
        createResultDialog();

        addGuiComponents();
    }

    private void addGuiComponents() {
        // hangman image
        hangmanImage = CustomTools.loadImage(CommonConstants.IMAGE_PATH);
        hangmanImage.setBounds(460, 0, hangmanImage.getPreferredSize().width, hangmanImage.getPreferredSize().height);

        // category display
        categoryLabel = new JLabel(wordChallenge[0]);
        categoryLabel.setFont(customFont.deriveFont(30f));
        categoryLabel.setHorizontalAlignment(SwingConstants.CENTER);
        categoryLabel.setOpaque(true);
        categoryLabel.setForeground(Color.WHITE);
        categoryLabel.setBackground(CommonConstants.PRIMARY_COLOR);
        categoryLabel.setBorder(BorderFactory.createLineBorder(CommonConstants.PRIMARY_COLOR));
        categoryLabel.setBounds(0, hangmanImage.getPreferredSize().height - 28, CommonConstants.FRAME_SIZE.width, categoryLabel.getPreferredSize().height);

        // hidden word
        hiddenWordLabel = new JLabel(CustomTools.hideWords(wordChallenge[1]));
        hiddenWordLabel.setFont(customFont.deriveFont(64f));
        hiddenWordLabel.setForeground(Color.WHITE);
        hiddenWordLabel.setHorizontalAlignment(SwingConstants.CENTER);
        hiddenWordLabel.setBounds(0, categoryLabel.getY() + categoryLabel.getPreferredSize().height + 50, CommonConstants.FRAME_SIZE.width, hiddenWordLabel.getPreferredSize().height);

        // letter buttons
        GridLayout gridLayout = new GridLayout(4, 7);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBounds(-5, hiddenWordLabel.getY() + hiddenWordLabel.getPreferredSize().height, CommonConstants.BUTTON_PANEL_SIZE.width, CommonConstants.BUTTON_PANEL_SIZE.height);
        buttonPanel.setLayout(gridLayout);

        // create the letter buttons
        for (char c = 'A'; c <= 'Z'; c++) {
            JButton button = new JButton(Character.toString(c));
            button.setBackground(CommonConstants.PRIMARY_COLOR);
            button.setFont(customFont.deriveFont(28f));
            // Changes the color of the letters
            button.setForeground(Color.BLACK);
            button.addActionListener(this);
            // using ASCII values to calculate the current index
            int currentIndex = c - 'A';

            letterButtons[currentIndex] = button;
            buttonPanel.add(letterButtons[currentIndex]);
        }


        // reset button
        JButton resetButton = new JButton("Reset");
        resetButton.setFont(customFont.deriveFont(22f));
        resetButton.setForeground(Color.BLACK);
        resetButton.setBackground(CommonConstants.SECONDARY_COLOR);
        resetButton.addActionListener(this);
        buttonPanel.add(resetButton);

        // quit button
        JButton quitButton = new JButton("Quit");
        quitButton.setFont(customFont.deriveFont(22f));
        quitButton.setForeground(Color.BLACK);
        quitButton.setBackground(CommonConstants.SECONDARY_COLOR);
        quitButton.addActionListener(this);
        buttonPanel.add(quitButton);

        getContentPane().add(categoryLabel);
        getContentPane().add(hangmanImage);
        getContentPane().add(hiddenWordLabel);
        getContentPane().add(buttonPanel);
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("Reset") || command.equals("Restart")) {
            resetGame();

            if (command.equals("Restart")) {
                resultDialog.setVisible(false);
            }
        } else if (command.equals("Quit")) {
            dispose();
            return;
        } else {
            // letter buttons

            // disable button
            JButton button = (JButton) e.getSource();
            button.setEnabled(false);

            // check if the word contains the user's guess,
            if (wordChallenge[1].contains(command)) {
                // indicate that the user got it right
                button.setBackground(Color.GREEN);
                // store the hidden word in a char array, so update the hidden text
                char[] hiddenWord = hiddenWordLabel.getText().toCharArray();

                for (int i = 0; i < wordChallenge[1].length(); i++) {
                    // update _ to correct letter
                    if (wordChallenge[1].charAt(i) == command.charAt(0)) {
                        hiddenWord[i] = command.charAt(0);
                    }
                }

                // update hiddenWordLabel
                hiddenWordLabel.setText(String.valueOf(hiddenWord));

                // the user guessed the word right
                if (!hiddenWordLabel.getText().contains("*")) {
                    resultLabel.setText("Correct!");
                    resultDialog.setVisible(true);
                }

            } else {
                // indicate that the user chose the wrong letter
                button.setBackground(Color.RED);

                // increase incorrect counter
                ++incorrectGuesses;

                // update hangman image
                CustomTools.updateImage(hangmanImage, "resources/" + (incorrectGuesses + 1) + ".png");

                // user failed to guess word right
                if (incorrectGuesses >= 6) {
                    // display result dialog with game over label
                    resultLabel.setText("Too Bad, Try Again?");
                    resultDialog.setVisible(true);
                }
            }
            wordLabel.setText("Word: " + wordChallenge[1]);
        }

    }


    private void createResultDialog() {
        resultDialog = new JDialog();
        resultDialog.setTitle("Result");
        resultDialog.setSize(CommonConstants.RESULT_DIALOG_SIZE);
        resultDialog.getContentPane().setBackground(CommonConstants.BACKGROUND_COLOR);
        resultDialog.setResizable(false);
        resultDialog.setLocationRelativeTo(this);
        resultDialog.setModal(true);
        resultDialog.setLayout(new GridLayout(4, 1));
        resultDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                resetGame();
            }
        });

        resultLabel = new JLabel();
        // Text for ending screen
        resultLabel.setForeground(Color.WHITE);
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);

        wordLabel = new JLabel();
        wordLabel.setForeground(Color.WHITE);
        wordLabel.setHorizontalAlignment(SwingConstants.CENTER);

        score = new JLabel();
        score.setForeground(Color.WHITE);
        score.setHorizontalAlignment(SwingConstants.CENTER);

        JButton restartButton = new JButton("Restart");
        restartButton.setForeground(Color.BLACK);
        restartButton.setBackground(CommonConstants.PRIMARY_COLOR);
        restartButton.addActionListener(this);

        resultDialog.add(resultLabel);
        resultDialog.add(wordLabel);
        resultDialog.add(restartButton);
        resultDialog.add(score);
    }

    private void resetGame() {
        // load new challenge
        wordChallenge = wordDB.loadChallenge();
        incorrectGuesses = 0;

        // load starting image
        CustomTools.updateImage(hangmanImage, CommonConstants.IMAGE_PATH);

        // update category
        categoryLabel.setText(wordChallenge[0]);

        // update hiddenWord
        String hiddenWord = CustomTools.hideWords(wordChallenge[1]);
        hiddenWordLabel.setText(hiddenWord);

        // enable all buttons again
        for (int i = 0; i < letterButtons.length; i++) {
            letterButtons[i].setEnabled(true);
            letterButtons[i].setBackground(CommonConstants.PRIMARY_COLOR);
        }
    }
}












