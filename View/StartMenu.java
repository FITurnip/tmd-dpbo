package View;

import Model.PlayerScore;
import ViewModel.StartMenuViewModel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartMenu extends JPanel implements GameOverListener {
    // menu properties
    private Image background;
    private JPanel menuPanel;
    private JTable scoreTable;
    private DefaultTableModel tableModel;
    private JFrame frame;
    private int frameWidth, frameHeight;
    private StartMenuViewModel startMenuViewModel;

    public StartMenu(JFrame frame, int frameWidth, int frameHeight) {
        // init frame
        this.frame = frame;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;

        // Load the background image
        this.background = new ImageIcon("src/assets/background.jpg").getImage();

        startMenuViewModel = new StartMenuViewModel();

        // Set layout for StartMenu panel
        setLayout(new GridBagLayout());

        // Create Menu panel
        menuPanel = new JPanel();
        menuPanel.setLayout(new BorderLayout());
        menuPanel.setOpaque(false); // Make the menu panel transparent

        // Create a header panel with label
        JPanel headerPanel = new JPanel();
        JLabel headerLabel = new JLabel("UP DOWN");
        headerPanel.setOpaque(false);
        headerLabel.setForeground(Color.WHITE); // Set header label color to white
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Set header label font size
        headerPanel.add(headerLabel);

        // Add header panel to the top (NORTH) of menuPanel
        menuPanel.add(headerPanel, BorderLayout.NORTH);

        // Create a panel for username input, buttons, and table
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setOpaque(false); // Make the content panel transparent

        // Create panel for username input
        JPanel usernamePanel = new JPanel();
        JLabel usernameLabel = new JLabel("Username: ");
        JTextField usernameField = new JTextField(20); // Adjust size as needed
        usernamePanel.setOpaque(false);

        // Define dark pencil colors for username input
        Color pencilDarkGray = new Color(105, 105, 105); // Dark gray shade

        usernameLabel.setForeground(Color.WHITE); // Set text color to white
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 18)); // Set label font size
        usernameField.setBackground(pencilDarkGray); // Set background color
        usernameField.setForeground(Color.WHITE); // Set text color to white
        usernameField.setFont(new Font("Arial", Font.PLAIN, 18)); // Set text field font size

        usernamePanel.add(usernameLabel);
        usernamePanel.add(usernameField);

        // Add username panel to contentPanel at the top
        contentPanel.add(usernamePanel, BorderLayout.NORTH);

        // Create a panel for buttons (Play and Quit)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false); // Make the button panel transparent

        // Define dark pencil colors for buttons
        Color pencilDarkRed = new Color(139, 0, 0); // Dark red shade

        JButton playButton = new JButton("Play");
        playButton.setBackground(pencilDarkGray);
        playButton.setForeground(Color.WHITE);
        playButton.setFont(new Font("Arial", Font.PLAIN, 18)); // Set button font size

        JButton quitButton = new JButton("Quit");
        quitButton.setBackground(pencilDarkRed);
        quitButton.setForeground(Color.WHITE);
        quitButton.setFont(new Font("Arial", Font.PLAIN, 18)); // Set button font size

        buttonPanel.add(playButton);
        buttonPanel.add(quitButton);

        // Add buttons panel to contentPanel at the bottom
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Create a table model with sample data
        String[] columnNames = {"Username", "Score", "Up", "Down"};

        tableModel = new DefaultTableModel(columnNames, 0);
        for (PlayerScore playerScore : startMenuViewModel.gettScore().getPlayerList()) {
            Object[] rowData = {
                    playerScore.getUsername(),
                    playerScore.getScore(),
                    playerScore.getUpCounter(),
                    playerScore.getDownCounter()
            };
            tableModel.addRow(rowData);
        }

        // Create the table
        scoreTable = new JTable(tableModel);
        scoreTable.setFillsViewportHeight(true); // Make table fill the entire height of its container
        scoreTable.setPreferredScrollableViewportSize(new Dimension(400, 100)); // Set table size

        // Set table header colors
        JTableHeader header = scoreTable.getTableHeader();
        header.setBackground(pencilDarkGray);
        header.setForeground(Color.WHITE); // Set header text color to white
        header.setFont(new Font("Arial", Font.BOLD, 16)); // Set header font size

        // Set table cell colors
        scoreTable.setBackground(pencilDarkGray);
        scoreTable.setForeground(Color.WHITE); // Set cell text color to white
        scoreTable.setGridColor(Color.GRAY); // Set grid color
        scoreTable.setFont(new Font("Arial", Font.PLAIN, 16)); // Set cell font size

        // Center align table cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < scoreTable.getColumnCount(); i++) {
            scoreTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(scoreTable);

        // Add scroll pane with table to contentPanel in the center
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        // Add content panel to menuPanel
        menuPanel.add(contentPanel, BorderLayout.CENTER);

        // Add ActionListener to Play button
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Switch to GameBase panel
                switchToGameBase(usernameField.getText());
            }
        });

        // Add ActionListener to Quit button
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Exit the application
            }
        });

        // Create constraints for the menuPanel to center it
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(50, 0, 0, 0); // Adjust top inset to give space to the header

        // Add Menu panel to the center of StartMenu using GridBagLayout constraints
        add(menuPanel, constraints);
    }

    private void switchToGameBase(String username) {
        // Add GameBase panel
        PlayerScore playerScore = startMenuViewModel.gettScore().getPlayer(username);
        if (playerScore == null) playerScore = new PlayerScore(username, 0, 0, 0);
        GameBase gameBase = new GameBase(playerScore, "src/", frameWidth, frameHeight);
        gameBase.setGameOverListener(StartMenu.this);
        frame.add(gameBase);

        // Refresh the frame
        frame.revalidate();
        frame.repaint();

        // Request focus for gameBase to receive key events
        gameBase.requestFocusInWindow();
    }

    @Override
    public void onGameOver() {
        System.out.println("GAME OVER");
        refreshScoreTable(); // Refresh the score table when game is over
    }

    private void refreshScoreTable() {
        startMenuViewModel.updateTScore();

        // Clear the existing table model
        tableModel.setRowCount(0);

        // Add updated scores to the table model
        for (PlayerScore playerScore : startMenuViewModel.gettScore().getPlayerList()) {
            Object[] rowData = {
                    playerScore.getUsername(),
                    playerScore.getScore(),
                    playerScore.getUpCounter(),
                    playerScore.getDownCounter()
            };
            tableModel.addRow(rowData);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the background image at (0, 0) relative to the JPanel
        g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
    }
}
