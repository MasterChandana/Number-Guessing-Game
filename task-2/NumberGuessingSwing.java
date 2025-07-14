import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;
import javax.swing.border.*;

public class NumberGuessingSwing extends JFrame implements ActionListener {
    private JTextField inputField;
    private JButton guessButton, resetButton;
    private JLabel messageLabel, scoreLabel, attemptLabel, roundLabel, bestScoreLabel, welcomeLabel;
    private JPanel mainPanel;

    private int numberToGuess;
    private int attemptsLeft;
    private int score;
    private int round;
    private int bestScore = 0;
    private Color defaultBg;

    public NumberGuessingSwing() {
        setTitle("Number Guessing Game");
        setSize(420, 350);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        mainPanel = new GradientPanel();
        mainPanel.setLayout(null);
        defaultBg = mainPanel.getBackground();
        add(mainPanel);

        welcomeLabel = new JLabel("Welcome... Ready to test your luck?", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        welcomeLabel.setBounds(40, 10, 340, 30);
        mainPanel.add(welcomeLabel);

        roundLabel = new JLabel("Round: 1");
        roundLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        roundLabel.setBounds(40, 40, 100, 25);
        mainPanel.add(roundLabel);

        bestScoreLabel = new JLabel("Best: 0");
        bestScoreLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        bestScoreLabel.setBounds(320, 40, 80, 25);
        bestScoreLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        mainPanel.add(bestScoreLabel);

        JLabel title = new JLabel("Guess a number between 1 and 100", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 15));
        title.setBounds(60, 70, 300, 30);
        mainPanel.add(title);

        inputField = new JTextField();
        inputField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        inputField.setBounds(120, 110, 80, 35);
        inputField.setHorizontalAlignment(JTextField.CENTER);
        inputField.setBorder(new RoundedBorder(10));
        mainPanel.add(inputField);

        guessButton = new JButton("Guess");
        guessButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        guessButton.setBounds(220, 110, 90, 35);
        guessButton.setBackground(new Color(100, 181, 246));
        guessButton.setForeground(Color.WHITE);
        guessButton.setFocusPainted(false);
        guessButton.setBorder(new RoundedBorder(10));
        guessButton.addActionListener(this);
        mainPanel.add(guessButton);

        resetButton = new JButton("Play Again");
        resetButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        resetButton.setBounds(150, 155, 120, 32);
        resetButton.setBackground(new Color(76, 175, 80));
        resetButton.setForeground(Color.WHITE);
        resetButton.setFocusPainted(false);
        resetButton.setBorder(new DropShadowBorder(10));
        resetButton.setEnabled(false);
        resetButton.addActionListener(this);
        mainPanel.add(resetButton);

        messageLabel = new JLabel(" ", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        messageLabel.setBounds(40, 200, 340, 30);
        mainPanel.add(messageLabel);

        attemptLabel = new JLabel("Attempts Left: 10");
        attemptLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        attemptLabel.setBounds(60, 240, 140, 25);
        mainPanel.add(attemptLabel);

        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        scoreLabel.setBounds(220, 240, 120, 25);
        scoreLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        mainPanel.add(scoreLabel);

        startNewGame();
        setVisible(true);
    }

    private void startNewGame() {
        Random rand = new Random();
        numberToGuess = rand.nextInt(100) + 1;
        attemptsLeft = 10;
        round++;
        roundLabel.setText("Round: " + round);
        attemptLabel.setText("Attempts Left: " + attemptsLeft);
        inputField.setText("");
        inputField.setEnabled(true);
        guessButton.setEnabled(true);
        resetButton.setEnabled(false);
        messageLabel.setText("Round " + round + ": Start guessing.");
        mainPanel.setBackground(defaultBg);
    }

    private void endRound(boolean won) {
        guessButton.setEnabled(false);
        inputField.setEnabled(false);
        resetButton.setEnabled(true);
        if (won && score > bestScore) {
            bestScore = score;
            bestScoreLabel.setText("Best: " + bestScore);
        }
    }

    private void animateFeedback(Color color) {
        Color old = mainPanel.getBackground();
        mainPanel.setBackground(color);
        Timer timer = new Timer(350, evt -> mainPanel.setBackground(old));
        timer.setRepeats(false);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == guessButton) {
            try {
                int guess = Integer.parseInt(inputField.getText().trim());
                if (guess < 1 || guess > 100) {
                    messageLabel.setText("Enter a number 1-100.");
                    animateFeedback(new Color(255, 224, 178));
                    return;
                }
                attemptsLeft--;
                attemptLabel.setText("Attempts Left: " + attemptsLeft);

                if (guess == numberToGuess) {
                    int roundScore = (10 - attemptsLeft) * 10;
                    score += roundScore;
                    scoreLabel.setText("Score: " + score);
                    messageLabel.setText("Correct. You scored " + roundScore + " points. Next round?");
                    animateFeedback(new Color(200, 230, 201));
                    endRound(true);
                } else if (guess < numberToGuess) {
                    messageLabel.setText("Too Low. Try again.");
                    animateFeedback(new Color(255, 236, 179));
                } else {
                    messageLabel.setText("Too High. Try again.");
                    animateFeedback(new Color(255, 236, 179));
                }

                if (attemptsLeft == 0 && guess != numberToGuess) {
                    messageLabel.setText("Out of attempts. Number was: " + numberToGuess + ".");
                    animateFeedback(new Color(239, 154, 154));
                    endRound(false);
                }
            } catch (NumberFormatException ex) {
                messageLabel.setText("Please enter a valid number.");
                animateFeedback(new Color(255, 224, 178));
            }
        } else if (e.getSource() == resetButton) {
            startNewGame();
        }
    }

    private static class RoundedBorder extends AbstractBorder {
        protected int radius;

        RoundedBorder(int r) {
            radius = r;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.setColor(new Color(200, 200, 200));
            g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(4, 8, 4, 8);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = insets.right = 8;
            insets.top = insets.bottom = 4;
            return insets;
        }
    }

    private static class DropShadowBorder extends RoundedBorder {
        public DropShadowBorder(int r) {
            super(r);
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(new Color(0, 0, 0, 40));
            g2.fillRoundRect(x + 2, y + 4, width - 1, height - 1, radius, radius);
            g2.dispose();
            super.paintBorder(c, g, x, y, width, height);
        }
    }

    private static class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            int w = getWidth();
            int h = getHeight();
            Color color1 = new Color(186, 222, 255);
            Color color2 = new Color(255, 236, 239);
            GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, w, h);

            g2d.setColor(new Color(255, 255, 255, 40));
            g2d.fillOval(w - 120, 20, 100, 100);
            g2d.fillOval(30, h - 100, 120, 80);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(NumberGuessingSwing::new);
    }
}
