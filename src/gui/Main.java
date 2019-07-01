package gui;

import java.util.Optional;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Main GUI class
 * @author Francesco Raco
 *
 */
public class Main extends Application
{
	/**
	 * Buses path
	 */
	public static final String BUSESPATH = "buses/";
	
	/**
	 * Bus file format
	 */
	public static final String BUSFILEFORMAT = ".txt";
	
	/**
	 * Verdana font
	 */
    public static final String FONT = "fonts/Verdana.ttf";
	
	/**
	 * Access point of the GUI application
	 * @param args args
	 */
	public static void main(String[] args)
	{
        launch(args);
    }
	
	/**
	 * Choose action depending by choice of output which program has to return
	 * @param status Label which describes the status of execution
	 * @param txtField Text field containing the string name of the input file
	 * @param oc Choice of output which program has to return
	 * @param description Description of program operation
	 * @throws InterruptedException Interrupted Exception
	 */
	private void chooseAction(Label status, TextField txtField, OutputChoice oc, String description) throws InterruptedException
	{
		//Set status font
		status.setFont(Font.loadFont(ClassLoader.getSystemClassLoader().getResourceAsStream(FONT), 14));
		
		//Show status info
		status.setText("Sto lavorando...Attendere...");
		
		//Instantiate new Task overriding the method call():
		//This is useful to separate graphical and logical threads
		Task task = new Task<Void>()
		{
		    @Override public Void call()
		    {
		    	//Get file input name
		    	String input = txtField.getText();
		    
		    	//Alert header text
		    	final String HEADERTEXT = "Risposta:";
			
		    	//Calculate the result by OutputChoice and file name (without initial and ending white spaces),
		    	//then assign it to a final variable (OUTPUT) in order to make the string visible
		    	//inside the following lambda expression
		    	final String OUTPUT = QueryProcessor.getInstance().process(oc, BUSESPATH + input.trim() + BUSFILEFORMAT);
		    
		    	//Run the specified Runnable argument at some unspecified time in the future:
		    	//a lambda expression defines it with the specific implementation of the method run();
		    	//this is useful for showing the status label meanwhile the program works on the solution
		    	Platform.runLater(() ->
		    	{
		    		//Get scrolling alert (containing informations regarding server output) and show it
		    		getScrollingAlertAndShowIt(AlertType.INFORMATION, description, HEADERTEXT, OUTPUT);
		    	
		    		//clear the text field
		    		txtField.clear();
		    	
		    		//Reset status info
		    		status.setText("");
		    	});
		    	
		    	//Return null
		    	return null;
		    	
		    };
		};
		
		//Create new Thread object passing task as argument
		Thread thread = new Thread(task);
		
		//Mark this thread as either a daemon thread or a user thread
		thread.setDaemon(true);
        
		//Begin execution of the thread; method start() invokes run() method implemented before
		thread.start();
	}
	
	/**
	 * Get scrolling alert
	 * @param type Alert type
	 * @param title Alert title string
	 * @param headerText Header text
	 * @param content Alert content string
	 * @return Scrolling alert
	 */
	private Alert getScrollingAlert(AlertType type, String title, String headerText, String content)
	{
		//Create new Alert object by an alert type and set its title
		Alert alert = new Alert(type);
		alert.setTitle(title);
		
		//Set alert header text
		alert.setHeaderText(headerText);
		
		//Set width and height values related to DialogPane alert
		alert.getDialogPane().setPrefSize(600, 600);
		
		//Create a new TextArea object by content string
		TextArea area = new TextArea(content);
		
		//If a run of text exceeds the width of the TextArea,
		//then the text will wrap onto another line
		area.setWrapText(true);
		
		//TextInputControl can't be edited by the user
		area.setEditable(false);
		
		//Assign area to alert dialog pane
		alert.getDialogPane().setContent(area);
		
		//Dialog can be resized by the user
		alert.setResizable(true);
		
		//Return alert
		return alert;
	}
	
	/**
	 * Show scrolling alert
	 * @param alert Alert
	 * @return Optional<ButtonType>
	 */
	private Optional<ButtonType> showScrollingAlert(Alert alert)
	{
		return alert.showAndWait();
	}
	
	/**
	 * Get scrolling alert and show it
	 * @param type Alert type
	 * @param title Alert title string
	 * @param headerText Header text
	 * @param content Alert content string
	 * @return Optional<ButtonType>
	 */
	private Optional<ButtonType> getScrollingAlertAndShowIt(AlertType type, String title, String headerText, String content)
	{
		Alert alert = getScrollingAlert(type, title, headerText, content);
		return showScrollingAlert(alert);
	}
	
	@Override
    public void start(Stage primaryStage)
	{
		//Instantiate text field containing the string name of the input file
		TextField text = new TextField();
		
		//Create label object which describes the status of execution
		//and set its style
		Label status = new Label("");
		status.setStyle("-fx-text-fill: red");
		
        //Create new toggle button related to solution, then set its text and action
		ToggleButton solutionButton = new ToggleButton();
        solutionButton.setText("Calcola Rotte");
        solutionButton.setOnAction(new EventHandler<ActionEvent>()
        {
 
            @Override
            public void handle(ActionEvent event)
            {
            	//Try execution and handle eventual exceptions
            	try
               {
            		//Invoke chooseAction specifying solution as output type which
            		//program has to return
            		chooseAction(status, text, OutputChoice.SOLUTION, "Soluzione");
               }
               catch (InterruptedException e)
               {
				
            	   e.printStackTrace();
               }
			}
        });
        
        //Create new toggle button related to the testing of R&R, then set its text and action
        ToggleButton testButton = new ToggleButton();
        testButton.setText("Testa R&R");
        testButton.setOnAction(new EventHandler<ActionEvent>()
        {
 
            @Override
            public void handle(ActionEvent event)
            {
            	//Try execution and handle eventual exceptions
            	try
            	{
            		//Invoke chooseAction specifying test as output type which
            		//program has to return
            		chooseAction(status, text, OutputChoice.TEST, "Testa Soluzione");
				}
            	catch (InterruptedException e)
            	{
					e.printStackTrace();
				}
            }
        });
        
        //Create a toggle group object
        ToggleGroup group = new ToggleGroup();
        
        //Add to group the solution and test button
        solutionButton.setToggleGroup(group);
        testButton.setToggleGroup(group);
        
        //Create a new flexible grid of rows and columns and set center-left alignment
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER_LEFT);
       
        //Create label by program title string
        Label title = new Label("   Rotte Autobus");
        
        //Set title font
        title.setFont(Font.loadFont(ClassLoader.getSystemClassLoader().getResourceAsStream(FONT), 16));
        
        //Create label by a text string which tells the user to write the name of a bus line
        //in the following text field
        Label fileInput = new Label("Linea:");
        
        //Set fileInput font
        fileInput.setFont(Font.loadFont(ClassLoader.getSystemClassLoader().getResourceAsStream(FONT), 14));
        
        //Add all labels and button created to gridPane specifying column and row index (last 2
        //colspan and rowspan arguments are optional)
        gridPane.add(title, 3, 0, 2, 1);
        gridPane.add(fileInput, 1, 1, 2, 2);
        gridPane.add(text, 3, 1, 2, 2);
        gridPane.add(solutionButton, 3, 3, 1, 1);
        gridPane.add(testButton, 4, 3, 1, 1);
        gridPane.add(status, 3, 4, 2, 2);
        
        //Set gridPane vertical and horizontal gap between nodes
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        
        //Create the container for all content, assign gridPane to it and
        //specifying width and height values
        Scene scene = new Scene(gridPane, 300, 150);

        //Set application title
        primaryStage.setTitle("Raco Maps");
        
        //Set scene as application scene 
        primaryStage.setScene(scene);
        
        //Set not resizable window
        primaryStage.setResizable(false);
        
        //Show application window
        primaryStage.show();
    }
}