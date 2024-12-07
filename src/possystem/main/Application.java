package possystem.main;

/**
 * Main class to start the application.
 */
public class Application {
    
    public static void main(String[] args) {
        // Set Nimbus look and feel (optional)
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(Application.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        System.out.println("Application is starting..."); // Add this to check if it prints to the console

        // Open the login form at the start of the application
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LoginForm().setVisible(true);  // Show the login form first
            }
        });
    }
}
