package order;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import customer.Customer;

public class orderView extends Application {
	private TextField numBox, dateBox, priceBox;
	private ComboBox<String> itemBox;
	private ComboBox<String> customerBox;
	
	public static void main(String[] args) {
		launch(args);
	}
	
		public void populateCustomers(ComboBox<Customer> comboBox) {
			SessionFactory factory = new Configuration().
	                configure("hibernate.cfg.xml").
	                addAnnotatedClass(Order.class).
	                buildSessionFactory();
			Session session = factory.getCurrentSession();
			
		    CriteriaBuilder cb = session.getCriteriaBuilder();
		    CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);
		    Root<Customer> root = cq.from(Customer.class);
		    cq.select(root);

		    List<Customer> customers = session.createQuery(cq).getResultList();
		    comboBox.getItems().addAll(customers);

		    session.close();
	}
	
	@Override
	public void start(Stage order) {
		
		// Title label
		order.setTitle("Order");
				
		// Labels
		Label numLabel = new Label("Number");
		Label dateLabel = new Label("Date");
		Label customerLabel = new Label("Customer");
		Label itemLabel = new Label("Item");
		Label priceLabel = new Label("Price ($)");
//        populateCustomers(customerBox);

		// Text fields
		numBox = new TextField();
		dateBox = new TextField();
		priceBox = new TextField();

		// Drop Down Menu's
        customerBox = new ComboBox<>();
        customerBox.getItems().addAll("Tyler", "Adam");
        customerBox.setPrefWidth(670);
        itemBox = new ComboBox<>();
        itemBox.getItems().addAll("Caesar Salad", "Greek Salad", "Cobb Salad");
        itemBox.setPrefWidth(300);

        Button search = new Button("Search");
        Button add = new Button("Add");
        Button update = new Button("Update");
        Button delete = new Button("Delete");

		// Combine each into their own VBox's
		VBox numVBox = new VBox(5, numLabel, numBox);
		numBox.setPadding(new Insets(0, 400, 0, 0));
		
		VBox dateBoxFinal = new VBox(5, dateLabel, dateBox);
		
		VBox customerInput = new VBox(5, customerLabel, customerBox);
		customerInput.setPadding(new Insets(10, 0, 20, 0));
		
		VBox itemInput = new VBox(5, itemLabel, itemBox);
		itemInput.setPadding(new Insets(0, 250, 0, 0));
		
		VBox priceInput = new VBox(5, priceLabel, priceBox);
		
		// Hbox for the buttons
		HBox butt = new HBox(15, search, add, update, delete);
		butt.setAlignment(Pos.BASELINE_CENTER);
		butt.setPadding(new Insets(100, 0, 0, 0));
		
		// Things on the same line
		HBox numdate = new HBox(10, numVBox, dateBoxFinal);
		HBox itemPrice = new HBox(10, itemInput, priceInput);
        
        // BUTTON ACTION EVENTS
        search.setOnAction(e -> {
        	searchOrder();
        });
        add.setOnAction(e -> {
        	addOrder();
        });
        update.setOnAction(e -> {
        	updateOrder();
        });
        delete.setOnAction(e -> {
        	delOrder();
        });
        
        // Combine all top objects together
        VBox top = new VBox(numdate, customerInput, itemPrice);
        top.setAlignment(Pos.TOP_CENTER);
        
		VBox root = new VBox(top, butt);
		root.setAlignment(Pos.CENTER);
		root.setPadding(new Insets(5, 15, 5, 15));
		
		Scene orderView = new Scene(root, 700, 400);
		order.setScene(orderView);
		order.show();
	}
	
	private void addOrder() {
		int number = Integer.parseInt(numBox.getText());
        String date = dateBox.getText();
	    double price = Double.parseDouble(priceBox.getText());
	    String item = itemBox.getValue();
	    
	 // Create a new Order object and set its properties
	    Order order = new Order();
	    order.setNumber(number);
	    order.setDate(date);
	    order.setPrice(price);
	    order.setItem(item);
	    
		SessionFactory factory = new Configuration().
                configure("hibernate.cfg.xml").
                addAnnotatedClass(Order.class).
                buildSessionFactory();

		Session session = factory.getCurrentSession();

		try {
			Order orderFinal = new Order(number, date, item, price, null);
			session.beginTransaction();
			
		    // Retrieve all customers from the database using Hibernate query
			Query<Order> query = session.createQuery("from Customer where name=:name", Order.class);
		    query.setParameter("name", order);
		    Order customer = query.getSingleResult();
		    
			session.save(orderFinal);
			session.getTransaction().commit();
		} 
		
		catch (Exception e) {
			e.printStackTrace();
		} 
		
		finally {
			factory.close();
		}

		// Reset the textfields
	    numBox.setText("");
	    dateBox.setText("");
	    priceBox.setText("");
	    customerBox.setValue(null);
	    itemBox.setValue(null);

		Stage popup = new Stage();
	    popup.initOwner(numBox.getScene().getWindow());
	    popup.setTitle("Success");
	    Label label = new Label("Order Successfully Included");
	    label.setAlignment(Pos.CENTER);
	    Scene scene = new Scene(label, 250, 100);
	    popup.setScene(scene);
	    popup.show();
	    
	}
//	
	public void delOrder() {
		String num = numBox.getText();
		
		SessionFactory factory = new Configuration().
                configure("hibernate.cfg.xml").
                addAnnotatedClass(Order.class).
                buildSessionFactory();

		Session session = factory.getCurrentSession();

		try {

			session.beginTransaction();
			Query query = session.createQuery("DELETE FROM Order WHERE name = :number");
		    query.setParameter("name", num);
		    
		    int rowsDeleted = query.executeUpdate();

			session.getTransaction().commit();


			} catch (Exception e) {
				e.printStackTrace();

			} finally {
				factory.close();
			}
		
		// Reset the textfields
	    numBox.setText("");
	    dateBox.setText("");
	    priceBox.setText("");
	    customerBox.setValue(null);
	    itemBox.setValue(null);
		
		Stage popup = new Stage();
	    popup.initOwner(numBox.getScene().getWindow());
	    popup.setTitle("Success");
	    
	    Label label = new Label("Order Successfully Deleted");
	    label.setAlignment(Pos.CENTER);
	    
	    Scene scene = new Scene(label, 250, 100);
	    popup.setScene(scene);
	    popup.show();
	}
	
	public void searchOrder() {
		int orderNumber = Integer.parseInt(numBox.getText());
		double price = Double.parseDouble(priceBox.getText());
	    String item = itemBox.getValue();
	    
		SessionFactory factory = new Configuration().
				configure("hibernate.cfg.xml").
				addAnnotatedClass(Order.class).
				buildSessionFactory();

		Session session = factory.getCurrentSession();
		session.beginTransaction();

	    Query<Order> query = session.createQuery("from Order where orderNumber = :orderNumber");
	    query.setParameter("orderNumber", orderNumber);

	    List<Order> orders = query.getResultList();
	    session.getTransaction().commit();

	    if (orders.isEmpty()) {
	        // show message "No Records Found" in a popup window
	    	Stage popup = new Stage();
		    popup.initOwner(numBox.getScene().getWindow());
		    popup.setTitle("Error");
		    
		    Label label = new Label("No Records Found");
		    label.setAlignment(Pos.CENTER);
		    
		    Scene scene = new Scene(label, 250, 100);
		    popup.setScene(scene);
		    popup.show();
	    	
	    } else {
	        // fill in the text fields with the corresponding order data
	        Order order = orders.get(0); // we assume there is only one order with this order number
	        numBox.setText(order.getDate().toString());
	        priceBox.setText(Double.toString(order.getPrice()));
	        itemBox.setValue(order.getItem());
	        customerBox.setValue(order.getCustomer().getName());
	    }


	}

		public void updateOrder() {
			int orderNumber = Integer.parseInt(numBox.getText());
	        String date = dateBox.getText();
			double price = Double.parseDouble(priceBox.getText());
		    String item = itemBox.getValue();
	        
	            Order orders = new Order();
	            orders.setNumber(orderNumber);
	            orders.setDate(date);
	            orders.setPrice(price);
	            orders.setItem(item);

	         // Save the updated customer data to the database
	            SessionFactory factory = new Configuration().
	                    configure("hibernate.cfg.xml").
	                    addAnnotatedClass(Order.class).
	                    buildSessionFactory();

	    		Session session = factory.getCurrentSession();

	    			session.beginTransaction();
	    	        Order order = session.get(Order.class, orderNumber);

	    			session.update(order);
	    			
					session.getTransaction().commit();
			            
					// Reset the textfields
				    numBox.setText("");
				    dateBox.setText("");
				    itemBox.getValue().isEmpty();
				    priceBox.setText("");
				    
					Stage popup = new Stage();
				    popup.initOwner(numBox.getScene().getWindow());
				    popup.setTitle("Order Updated");
				    
				    Label label = new Label("Order Data Successfully Updated");
				    label.setAlignment(Pos.CENTER);
				    
				    Scene scene = new Scene(label, 250, 100);
				    popup.setScene(scene);
				    popup.show();
	        }
	}

