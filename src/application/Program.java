package application;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Locale.setDefault(Locale.US);
	
		
		
		SellerDao sellerDao = DaoFactory.createSellerDao();
		
		System.out.println("=== Test 1: seller findById ===");
		
		Seller seller = sellerDao.findById(3);
		System.out.println(seller);
		
		System.out.println("=== Test 2: seller findByDepartment");
		Department dep = new Department(2, null);
		List<Seller> list = sellerDao.findByDepartment(dep);
		for(Seller obj : list) {
			System.out.println(obj);
		}
		
		System.out.println("=== Test 3: seller findAll");
		list = sellerDao.findAll();
		for(Seller obj : list) {
			System.out.println(obj);
		}
		
		System.out.println("=== Test 4: seller insert");
		Seller sellerTest = new Seller(null, "Leo", "leo@email.com", new Date(), 2500.0, dep);
		sellerDao.insert(sellerTest);
		System.out.println("Inserted! New id: " + sellerTest.getId() );
	}

}
