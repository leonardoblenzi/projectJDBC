package application;

import java.util.Date;
import java.util.Locale;

import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Locale.setDefault(Locale.US);
		Department objeto = new Department(1, "Books");
		System.out.print(objeto);
		
		Seller seller = new Seller(11, "Leo", "leo@email.com", new Date(), 3000.0, objeto);
		
	}

}
