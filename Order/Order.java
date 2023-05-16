package order;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import customer.Customer;

@Entity
@Table(name="order")
public class Order {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="number")
	private int number;
	
	@Column(name="date")
	private String date;
	
	@Column(name="item")
	private String item;
	
	@Column(name="price")
	private double price;

//    @ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "customer_id")
//	private Customer customer;

	public Order() {
		// No args
	}
	
	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

//	public Customer getCustomer() {
//		return customer;
//	}

	public void setCustomer(Customer customer) {
//		this.customer = customer;
	}

	
	public Order(int number, String date, String item, double price, customer.Customer customer) {
		super();
		this.number = number;
		this.date = date;
		this.item = item;
		this.price = price;
//		this.customer = customer;
	}

	@Override
	public String toString() {
		return "Order [number=" + number + ", date=" + date + ", item=" + item + ", price=" + price + ", customer="
				+ /*customer*/ "]";
	}

	public Customer getCustomer() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
